package edu.pdx.cs410j.qihao.phonebill_android;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import edu.pdx.cs410J.ParserException;

public class SearchCustomer extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_customer);

        Button searchCustomer = findViewById(R.id.search_customer_button);
        searchCustomer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    search();
                } catch (ParserException | IllegalArgumentException | IOException e) {
                    error(e);
                }
            }
        });
    }

    @SuppressLint("SimpleDateFormat")
    private void search() throws IOException, ParserException {
        EditText customer = findViewById(R.id.customer);
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
            FloatingActionButton goBackHome = findViewById(R.id.goBackHome);
            goBackHome.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    finish();
                }
            });

            ListView listView = findViewById(R.id.result_list);
            ArrayList<String> callArray = new ArrayList<>();

            ArrayAdapter<String> adapter = new ArrayAdapter<>(SearchCustomer.this, R.layout.list_item, callArray);

            callArray.add(String.format("%-15s %s", "", bill.getCustomer() + "'s Phone Bill"));
            for (PhoneCall call: bill.getPhoneCalls()) {
                long duration = call.getEndTime().getTime() - call.getStartTime().getTime();

                String builder = "\n-----------" + call.getStartTime() + "----------\n";
                builder += String.format("%-15s %s\n", "Caller Number:", call.getCaller());
                builder += String.format("%-15s %s\n", "Callee Number:", call.getCallee());
                builder += String.format("%-15s %s\n", "Start Time:", reformat(call.getStartTime()));
                builder += String.format("%-15s %s\n", "End Time:", reformat(call.getEndTime()));
                builder += String.format("%-15s %s\n", "Time Zone:", new SimpleDateFormat().getTimeZone().toZoneId());
                builder += String.format("%-15s %s\n", "Call Duration:", duration / 1000 / 60 + " minutes (" + duration / 1000 + " seconds)");

                callArray.add(builder);
            }
            adapter.notifyDataSetChanged();

            listView.setAdapter(adapter);

        } else {
            throw new IllegalArgumentException("No such customer: " + Customer);
        }
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

    private void error(Exception e) {
        Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
    }
}