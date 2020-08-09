package edu.pdx.cs410j.qihao.phonebill_android;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;

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
                } catch (FileNotFoundException | ParserException | IllegalArgumentException e) {
                    error(e);
                }
            }
        });
    }

    private void search() throws FileNotFoundException, ParserException {
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

            alert(bill);
        } else {
            throw new IllegalArgumentException("No such customer: " + Customer);
        }
    }

    private void alert(PhoneBill bill) {
        new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle(bill.getCustomer())
                .setMessage(bill.getPhoneCalls().toString())
                .setNegativeButton("Continue", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(getApplicationContext(), "Please Click Something", Toast.LENGTH_LONG).show();
                    }
                })
                .show();
    }

    private void error(Exception e) {
        Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
    }

    private void error(String e) {
        Toast.makeText(this, e, Toast.LENGTH_LONG).show();
    }
}