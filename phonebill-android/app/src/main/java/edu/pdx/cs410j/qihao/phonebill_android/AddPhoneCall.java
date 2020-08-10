package edu.pdx.cs410j.qihao.phonebill_android;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

import com.google.android.material.snackbar.Snackbar;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import edu.pdx.cs410J.ParserException;

/**
 * This class is represents a <code>AddPhoneCall</code>.
 */
public class AddPhoneCall extends AppCompatActivity {
    EditText customer;
    EditText caller;
    EditText callee;
    EditText startDate;
    EditText startTime;
    EditText endDate;
    EditText endTime;

    PhoneCall call;
    PhoneBill bill;

    /**
     * initialize AddPhoneCall activity
     * @param savedInstanceState containing the activity's previously frozen state
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_phone_call);

        EditText caller_et = findViewById(R.id.caller);
        //caller_et.addTextChangedListener(new PhoneNumberFormattingTextWatcher());
        caller_et.addTextChangedListener(NumberWatcher(caller_et));

        EditText callee_et = findViewById(R.id.callee);
        //callee_et.addTextChangedListener(new PhoneNumberFormattingTextWatcher());
        callee_et.addTextChangedListener(NumberWatcher(callee_et));

        final EditText startDate_et = findViewById(R.id.startDate_editText);
        startDate_et.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                datePick(startDate_et);
            }
        });

        final EditText endDate_et = findViewById(R.id.endDate_editText);
        endDate_et.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                datePick(endDate_et);
            }
        });

        final EditText startTime_et = findViewById(R.id.startTime_editText);
        startTime_et.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                timePick(startTime_et);
            }
        });

        final EditText endTime_et = findViewById(R.id.endTime_editText);
        endTime_et.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                timePick(endTime_et);
            }
        });

        Button addCall = findViewById(R.id.add_phone_call_button);
        addCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    addCall();

                    Intent intent = new Intent(AddPhoneCall.this, MainActivity.class);
                    intent.putExtra("PhoneCall", call);
                    intent.putExtra("PhoneBill", bill);

                    setResult(RESULT_OK, intent);
                    finish();
                } catch (ParserException | IOException | IllegalArgumentException e) {
                    error(e);
                }
            }
        });
    }

    /**
     * custom phone number pattern TextWatcher that auto append dash
     * @param caller_et caller EditText
     * @return TextWatcher
     */
    private TextWatcher NumberWatcher(final EditText caller_et) {
        final String[] lastChar = {" "};

        return new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                int digits = caller_et.getText().toString().length();
                if (digits > 1)
                    lastChar[0] = caller_et.getText().toString().substring(digits-1);
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                int digits = caller_et.getText().toString().length();
                if (!lastChar[0].equals("-")) {
                    if (digits == 3 || digits == 7) {
                        caller_et.append("-");
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        };
    }

    /**
     * DatePicker to pick date
     * @param editText Date EditText
     */
    private void datePick(final EditText editText) {
        final Calendar calendar = Calendar.getInstance();
        int startYear = calendar.get(Calendar.YEAR);
        int startMonth = calendar.get(Calendar.MONTH);
        int startDay = calendar.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog dialog = new DatePickerDialog(AddPhoneCall.this, new DatePickerDialog.OnDateSetListener() {
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

    /**
     * TimePicker to pick time
     * @param editText Time EditText
     */
    private void timePick(final EditText editText) {
        final Calendar calendar = Calendar.getInstance();
        int startHour = calendar.get(Calendar.HOUR_OF_DAY);
        int startMinute = calendar.get(Calendar.MINUTE);
        TimePickerDialog dialog = new TimePickerDialog(AddPhoneCall.this, new TimePickerDialog.OnTimeSetListener() {
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
     * add Phone Call to bill then dump to internal storage
     * @throws IOException throws IO exception
     * @throws ParserException throws ParserException exception
     * @throws IllegalArgumentException throws IllegalArgument exception
     */
    private void addCall() throws IOException, ParserException, IllegalArgumentException {
        customer = findViewById(R.id.customer);
        caller = findViewById(R.id.caller);
        callee = findViewById(R.id.callee);
        startDate = findViewById(R.id.startDate_editText);
        startTime = findViewById(R.id.startTime_editText);
        endDate = findViewById(R.id.endDate_editText);
        endTime = findViewById(R.id.endTime_editText);

        String Customer = customer.getText().toString();
        String Caller = caller.getText().toString();
        String Callee = callee.getText().toString();
        String StartDate = startDate.getText().toString();
        String StartTime = startTime.getText().toString();
        String EndDate = endDate.getText().toString();
        String EndTime = endTime.getText().toString();

        File dir = getDataDir();
        File file = new File(dir, Customer + ".txt");

        if (file.exists()) {
            TextParser parser = new TextParser(new FileReader(file));
            bill = parser.parse();
            if (bill.getCustomer().equals(Customer)){
                call = new PhoneCall(Caller, Callee, StartDate, StartTime, EndDate, EndTime);
                bill.addPhoneCall(call);
            } else {
                throw new IllegalArgumentException("The customer name given [" + Customer + "] is different than the one found in the stored file [" + bill.getCustomer() + "]");
            }
        } else {
            bill = new PhoneBill(Customer);
            call = new PhoneCall(Caller, Callee, StartDate, StartTime, EndDate, EndTime);
            bill.addPhoneCall(call);
        }
        TextDumper dumper = new TextDumper(new PrintWriter(file));
        dumper.dump(bill);
    }

    /**
     * show error in Snackbar
     * @param e exception
     */
    private void error(Exception e) {
        String text = e.getMessage();
        if (text == null) {
            text = "Unexpected error occurred!";
        }
        //Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
        Snackbar.make(findViewById(android.R.id.content), text, Snackbar.LENGTH_LONG).show();
    }
}