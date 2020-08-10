package edu.pdx.cs410j.qihao.phonebill_android;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class PrintResult extends AppCompatActivity {

    private static final int SEARCH_CUSTOMER_RESULT = 32;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_print_result);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SEARCH_CUSTOMER_RESULT && resultCode == RESULT_OK) {
            if (data != null) {
                if (data.hasExtra("PhoneBill")) {
                    PhoneBill result = (PhoneBill) data.getSerializableExtra("PhoneBill");
                    ListView listView = findViewById(R.id.result_list);

                    ArrayAdapter<PhoneCall> adapter = new ArrayAdapter<>(this, R.layout.activity_print_result);
                    for (PhoneCall call: result.getPhoneCalls()) {
                        adapter.add(call);
                    }
                    listView.setAdapter(adapter);

                    Toast.makeText(this, "PhoneBill: " + result, Toast.LENGTH_LONG).show();
                }
            } else {
                Toast.makeText(this, "Nothing found", Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(this, "request code error", Toast.LENGTH_LONG).show();
        }
    }
}