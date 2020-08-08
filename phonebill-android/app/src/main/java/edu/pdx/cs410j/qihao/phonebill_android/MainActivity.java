package edu.pdx.cs410j.qihao.phonebill_android;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button launchSearchCustomer = findViewById(R.id.search_customer);
        launchSearchCustomer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, SearchCustomer.class);
                startActivity(intent);
            }
        });

        Button launchAddPhoneCall = findViewById(R.id.add_phone_call);
        launchAddPhoneCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AddPhoneCall.class);
                startActivity(intent);
            }
        });

        Button launchSearchPhoneCalls = findViewById(R.id.search_phone_calls);
        launchSearchPhoneCalls.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, SearchPhoneCalls.class);
                startActivity(intent);
            }
        });
    }
}