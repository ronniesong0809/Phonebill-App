package edu.pdx.cs410j.qihao.phonebill_android;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringReader;

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

                Intent intent = new Intent(AddPhoneCall.this, MainActivity.class);

                /*String tests = "1/1/1111 1:1 AM";
                String[] test = tests.split(" ");
                String test1 = test[0];
                String test2 = test[1];


                customer = findViewById(R.id.customer);
                caller = findViewById(R.id.caller);
                callee = findViewById(R.id.callee);
                start = findViewById(R.id.startTime);
                end = findViewById(R.id.end);

                String Customer = customer.getText().toString();
                String Caller = caller.getText().toString();
                String Callee = callee.getText().toString();
                String StartString = start.getText().toString();
                String EndString = end.getText().toString();

                String[] Start = StartString.split(" ");
                String[] End = EndString.split(" ");

                call = new PhoneCall(Caller, Callee, Start[0], Start[1], Start[2], End[0], End[1], End[2]);
                bill = new PhoneBill(Customer);*/


                call = new PhoneCall("111-111-1111", "222-222-2222", "1/1/1111", "1:1", "AM", "2/2/2222", "2:2", "PM");
                bill = new PhoneBill("Ronnie");
                bill.addPhoneCall(call);

                intent.putExtra("PhoneCall", call);
                intent.putExtra("PhoneBill", bill);

                File dir = getDataDir();
                File file = new File(dir, "Ronnie.txt");

                TextDumper dumper;
                try {
                    dumper = new TextDumper(new PrintWriter(file));
                    dumper.dump(bill);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }


                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }
}