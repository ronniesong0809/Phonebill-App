package edu.pdx.cs410j.qihao.phonebill_android;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Calendar;

import edu.pdx.cs410J.ParserException;

public class AddPhoneCall extends AppCompatActivity {
    EditText customer;
    EditText caller;
    EditText callee;
    EditText start;
    EditText end;

    PhoneCall call;
    PhoneBill bill;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_phone_call);

        final EditText startTime = findViewById(R.id.startTime);
        startTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                datePick(startTime);
            }
        });

        final EditText endTime = findViewById(R.id.endTime);
        endTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                datePick(endTime);
            }
        });

        Button addCall = findViewById(R.id.add_phone_call_button);
        addCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    addCall();
                } catch (ParserException | IOException e) {
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
        DatePickerDialog dialog = new DatePickerDialog(AddPhoneCall.this, new DatePickerDialog.OnDateSetListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                editText.setText(day + "/" + month + "/" + year);
            }
        }, startYear, startMonth, startDay);
        dialog.show();
    }

    private void addCall() throws IOException, ParserException {
        Intent intent = new Intent(AddPhoneCall.this, MainActivity.class);

        customer = findViewById(R.id.customer);
        caller = findViewById(R.id.caller);
        callee = findViewById(R.id.callee);
        start = findViewById(R.id.startTime);
        end = findViewById(R.id.endTime);

        String Customer = customer.getText().toString();
        String Caller = caller.getText().toString();
        String Callee = callee.getText().toString();
        String Start = start.getText().toString();
        String End = end.getText().toString();

        File dir = getDataDir();
        File file = new File(dir, Customer + ".txt");

        if (file.exists()) {
            TextParser parser = new TextParser(new FileReader(file));
            bill = parser.parse();
            if (bill.getCustomer().equals(Customer)){
                call = new PhoneCall(Caller, Callee, Start, End);
                bill.addPhoneCall(call);
            } else {
                throw new IllegalArgumentException("The customer name given [" + Customer + "] is different than the one found in the stored file [" + bill.getCustomer() + "]");
            }
        } else {
            bill = new PhoneBill(Customer);
            call = new PhoneCall(Caller, Callee, Start, End);
            bill.addPhoneCall(call);
        }
        TextDumper dumper = new TextDumper(new PrintWriter(file));;
        dumper.dump(bill);

        intent.putExtra("PhoneCall", call);
        intent.putExtra("PhoneBill", bill);

        setResult(RESULT_OK, intent);
        finish();
    }

    private void error(Exception e) {
        Toast.makeText(this, "x" + e.getMessage(), Toast.LENGTH_LONG).show();
    }
}