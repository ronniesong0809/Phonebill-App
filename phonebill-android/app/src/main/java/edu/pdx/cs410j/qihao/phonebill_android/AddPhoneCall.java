package edu.pdx.cs410j.qihao.phonebill_android;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.ActivityChooserView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringReader;

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