package edu.pdx.cs410j.qihao.phonebill_android;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.NoSuchElementException;

import edu.pdx.cs410J.ParserException;

public class SearchPhoneCalls extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_phone_calls);

        final EditText startDate_et = findViewById(R.id.startDate_sc_editText);
        startDate_et.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                datePick(startDate_et);
            }
        });

        final EditText endDate_et = findViewById(R.id.endDate_sc_editText);
        endDate_et.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                datePick(endDate_et);
            }
        });

        final EditText startTime_et = findViewById(R.id.startTime_sc_editText);
        startTime_et.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                timePick(startTime_et);
            }
        });

        final EditText endTime_et = findViewById(R.id.endTime_sc_editText);
        endTime_et.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                timePick(endTime_et);
            }
        });

        Button searchPhoneCalls = findViewById(R.id.search_phone_calls_button);
        searchPhoneCalls.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    EditText customer = findViewById(R.id.customer_sc_editText);
                    String Customer = customer.getText().toString();

                    EditText date1 = findViewById(R.id.startDate_sc_editText);
                    String startDate = date1.getText().toString();
                    EditText time1 = findViewById(R.id.startTime_sc_editText);
                    String startTime = time1.getText().toString();

                    EditText date2 = findViewById(R.id.endDate_sc_editText);
                    String endDate = date2.getText().toString();
                    EditText time2 = findViewById(R.id.endTime_sc_editText);
                    String endTime = time2.getText().toString();

                    if (Customer.equals("")) {
                        throw new IllegalArgumentException("customer can't be empty.");
                    } else if (startDate.equals("")) {
                        throw new IllegalArgumentException("date1 can't be empty.");
                    } else if (startTime.equals("")) {
                        throw new IllegalArgumentException("time1 can't be empty.");
                    } else if (endDate.equals("")) {
                        throw new IllegalArgumentException("date2 can't be empty.");
                    } else if (endTime.equals("")) {
                        throw new IllegalArgumentException("time2 can't be empty.");
                    }

                    search(Customer, startDate + " " + startTime, endDate + " " + endTime);
                } catch (NoSuchElementException | ParserException | IllegalArgumentException | IOException e) {
                    error(e);
                }
            }
        });
    }

    private void datePick(final EditText editText) {
        final Calendar calendar = Calendar.getInstance();
        int startYear = calendar.get(Calendar.YEAR);
        int startMonth = calendar.get(Calendar.MONTH);
        int startDay = calendar.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog dialog = new DatePickerDialog(SearchPhoneCalls.this, new DatePickerDialog.OnDateSetListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.DAY_OF_MONTH, day);

                @SuppressLint("SimpleDateFormat")
                SimpleDateFormat format = new SimpleDateFormat("MM/dd/YYYY");
                String date = format.format(calendar.getTime());
                editText.setText(date);
            }
        }, startYear, startMonth, startDay);
        dialog.show();
    }

    private void timePick(final EditText editText) {
        final Calendar calendar = Calendar.getInstance();
        int startHour = calendar.get(Calendar.HOUR_OF_DAY);
        int startMinute = calendar.get(Calendar.MINUTE);
        TimePickerDialog dialog = new TimePickerDialog(SearchPhoneCalls.this, new TimePickerDialog.OnTimeSetListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onTimeSet(TimePicker timePicker, int hour, int minute) {
                calendar.set(Calendar.HOUR_OF_DAY, hour);
                calendar.set(Calendar.MINUTE, minute);

                @SuppressLint("SimpleDateFormat")
                SimpleDateFormat format = new SimpleDateFormat("hh:mm a");
                String time = format.format(calendar.getTime());
                editText.setText(time);
            }
        }, startHour, startMinute, false);
        dialog.show();
    }

    /**
     * standard out PhoneBill in pretty format if between two times
     * @param date1 start date
     * @param date2 end date
     * @throws IOException throws IO exception
     */
    @SuppressLint("SimpleDateFormat")
    private void search(String Customer, String date1, String date2) throws IOException, ParserException, IllegalArgumentException {
        File dir = getDataDir();
        File file = new File(dir, Customer + ".txt");
        if (file.exists()) {
            TextParser parser = new TextParser(new FileReader(file));
            PhoneBill bill = parser.parse();

            setContentView(R.layout.activity_print_result);
            FloatingActionButton goBackHome = findViewById(R.id.goBackHome);
            goBackHome.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    finish();
                }
            });

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
            callArray.add(1, "There " + (found == 1 ? "is " : "are ") + found + " phone call" + (found == 1 ? "" : "s") + " found between " + date1 + " - " + date2 + ".");

            adapter.notifyDataSetChanged();
            listView.setAdapter(adapter);
            if (found==0) {
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
    private boolean betweenTwoTimes(PhoneCall call, String _date1, String _date2) throws IllegalArgumentException {
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