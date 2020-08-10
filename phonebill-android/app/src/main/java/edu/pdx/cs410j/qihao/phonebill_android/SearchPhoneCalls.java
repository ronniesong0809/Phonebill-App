package edu.pdx.cs410j.qihao.phonebill_android;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.NoSuchElementException;

import edu.pdx.cs410J.ParserException;

public class SearchPhoneCalls extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_phone_calls);



        Button searchPhoneCalls = findViewById(R.id.search_phone_calls_button);
        searchPhoneCalls.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    EditText date1 = findViewById(R.id.startTime_sc_editText);
                    String start = date1.getText().toString();
                    EditText date2 = findViewById(R.id.endTime_sc_editText);
                    String end = date2.getText().toString();
                    search(start, end);
                } catch (NoSuchElementException | ParserException | IllegalArgumentException | IOException e) {
                    error(e);
                }
            }
        });
    }

    @SuppressLint("SimpleDateFormat")
    private void search(String date1, String date2) throws IOException, ParserException {
        EditText customer = findViewById(R.id.customer_sc_editText);
        String Customer = customer.getText().toString();
        if (Customer.equals("")) {
            throw new IllegalArgumentException("customer can't be empty.");
        }

        File dir = getDataDir();
        File file = new File(dir, Customer + ".txt");
        if (file.exists()) {
            TextParser parser = new TextParser(new FileReader(file));
            PhoneBill bill = parser.parse();

            setContentView(R.layout.activity_print_result);
            ListView listView = findViewById(R.id.result_list);
            ArrayList<String> callArray = new ArrayList<>();

            ArrayAdapter<String> adapter = new ArrayAdapter<>(SearchPhoneCalls.this, R.layout.list_item, callArray);

            callArray.add(String.format("%-15s %s", "", bill.getCustomer() + "'s Phone Bill"));
            int found = 0;

            for (PhoneCall call: bill.getPhoneCalls()) {
                long duration = call.getEndTime().getTime() - call.getStartTime().getTime();

                if (betweenTwoTimes(call, date1, date2)) {
                    String builder = "\n-----------" + call.getStartTime() + "----------\n";
                    builder += String.format("%-15s %s\n", "Caller Number:", call.getCaller());
                    builder += String.format("%-15s %s\n", "Callee Number:", call.getCallee());
                    builder += String.format("%-15s %s\n", "Start Time:", reformat(call.getStartTime()));
                    builder += String.format("%-15s %s\n", "End Time:", reformat(call.getEndTime()));
                    builder += String.format("%-15s %s\n", "Time Zone:", new SimpleDateFormat().getTimeZone().toZoneId());
                    builder += String.format("%-15s %s\n", "Call Duration:", duration / 1000 / 60 + " minutes (" + duration / 1000 + " seconds)");

                    callArray.add(builder);
                    found++;
                }
            }
            if (found!=0) {
                callArray.add(1, "There " + (found == 1 ? "is " : "are ") + found + " phone call" + (found == 1 ? "" : "s") + " found between " + date1 + " - " + date2 + ".");

                adapter.notifyDataSetChanged();
                listView.setAdapter(adapter);
            } else {
                throw new NoSuchElementException("There is no phone call found between " + date1 + " - " + date2 + ".");
            }
        } else {
            throw new IllegalArgumentException("No such customer: " + Customer);
        }
    }

    /**
     * check if a phone call is between two times
     * @param call PhoneCall
     * @param _date1 start time
     * @param _date2 end time
     * @return return true or false
     */
    private boolean betweenTwoTimes(PhoneCall call, String _date1, String _date2) {
        Date date1 = parseDate(_date1);
        Date date2 = parseDate(_date2);
        validateStartTimeBeforeEndTime(date1, date2);
        return call.getStartTime().after(date1) && call.getStartTime().before(date2);
    }

    /**
     * reformat to EEEEE, MMMM d, yyyy - hh:mm a
     * @param time time as Date object
     * @return String of new format
     */
    @SuppressLint("SimpleDateFormat")
    private String reformat(Date time) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEE, MMM dd, yyyy hh:mm a");
        return simpleDateFormat.format(time);
    }

    /**
     * parse string of date to Date format
     * @param date date string that contains date, time and ampm
     * @return return date object
     */
    @SuppressLint("SimpleDateFormat")
    private Date parseDate(String date) {
        Date newDate;
        SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy hh:mm a");
        format.setLenient(false);
        try {
            newDate = format.parse(date);
        } catch (ParseException e) {
            throw new IllegalArgumentException("Unparseable date: " + date);
        }
        return newDate;
    }

    /**
     * Check if the phone call’s end time is before its starts time
     * @param startTime start time of the phoneCall
     * @param endTime end time of the phoneCall
     */
    private void validateStartTimeBeforeEndTime(Date startTime, Date endTime) {
        if (startTime.after(endTime)){
            throw new IllegalArgumentException("[" + startTime + "] is after the [" + endTime + "], make sure your phone call’s start time is before its end time!");
        }
    }

    private void error(Exception e) {
        Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
    }
}