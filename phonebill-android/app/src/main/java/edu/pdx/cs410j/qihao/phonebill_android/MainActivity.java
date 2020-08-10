package edu.pdx.cs410j.qihao.phonebill_android;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.StatusBarManager;
import android.content.Intent;
import android.os.Bundle;
import android.service.notification.StatusBarNotification;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

/**
 * This class is represents a <code>MainActivity</code>.
 */
public class MainActivity extends AppCompatActivity {
    public static final int ADD_PHONE_CALL_RESULT = 43;

    /**
     * initialize MainActivity activity
     * @param savedInstanceState containing the activity's previously frozen state
     */
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
                startActivityForResult(intent, ADD_PHONE_CALL_RESULT);
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

        ImageView readMe = findViewById(R.id.read_me);
        readMe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ReadMe();
            }
        });
    }

    /**
     * get result from another activity
     * @param requestCode request code
     * @param resultCode result code
     * @param data intent data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ADD_PHONE_CALL_RESULT && resultCode == RESULT_OK) {
            if (data != null) {
                if (data.hasExtra("PhoneCall")) {
                    PhoneCall result = (PhoneCall) data.getSerializableExtra("PhoneCall");
                    Toast.makeText(this, "New Call:\n" + result + "\nhas been successfully added!", Toast.LENGTH_LONG).show();
                }

                if (data.hasExtra("PhoneBill")) {
                    PhoneBill result = (PhoneBill) data.getSerializableExtra("PhoneBill");
                    Toast.makeText(this, "Current PhoneBill: " + result, Toast.LENGTH_LONG).show();

                }
            } else {
                Toast.makeText(this, "Nothing change", Toast.LENGTH_LONG).show();
            }
        }
    }

    /**
     * README alert dialog
     */
    public void ReadMe() {
        String message = "Copyright (c) 2020 Ronnie Song\n\n";
        message += "This is a simple Android Phone Bill that allows its users to creating a phone bill, entering calls, pretty printing a phone bill and its calls, searching for calls, etc.";

        AlertDialog dialog = new AlertDialog.Builder(this)
                .setIcon(R.drawable.baseline_help_outline_24)
                .setTitle("README")
                .setMessage(message)
                .setNegativeButton("Cancel", null)
                .create();
        dialog.show();
    }
}