package edu.pdx.cs410j.qihao.phonebill_android;

import android.annotation.SuppressLint;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import edu.pdx.cs410J.AbstractPhoneCall;

/**
 * This class is represents a <code>PhoneCall</code>.
 */
public class PhoneCall extends AbstractPhoneCall implements Comparable<PhoneCall>{
    private final String caller;
    private final String callee;
    private final String startTime;
    private final String endTime;

    /**
     * Creates a new <code>PhoneCall</code>
     * @param caller     Phone number of caller
     * @param callee     Phone number of person who was called
     * @param startDate  date of call began
     * @param startTime  time of call began
     * @param endDate    date of call ended
     * @param endTime    time of call ended
     */
    public PhoneCall(String caller, String callee, String startDate, String startTime, String endDate, String endTime) {
        this.caller = validatePhoneNumber(caller);
        this.callee = validatePhoneNumber(callee);
        this.startTime = validateDate(startDate) + " " + validateTime(startTime);
        this.endTime = validateDate(endDate) + " " + validateTime(endTime);
        validateStartTimeBeforeEndTime(this.startTime, this.endTime);
    }

    private void validateStartTimeBeforeEndTime(String _startTime, String _endTime) {
        Date startTime = parseDate(_startTime);
        Date endTime = parseDate(_endTime);
        if (startTime.after(endTime)){
            throw new IllegalArgumentException("[" + _startTime + "] is after the [" + _endTime + "], make sure your phone call’s start time is before its end time!");
        }
    }

    /**
     * Validate the format of <code>number</code>.
     * @param number Format: XXX-XXX-XXXX
     * @return return phone number if valid, else throw exception.
     */
    private String validatePhoneNumber(String number) {
        if (number.equals("")) {
            throw new IllegalArgumentException("number can't be empty.");
        }
        boolean valid = number.matches("^\\d{3}-\\d{3}-\\d{4}$");
        if(valid) {
            return number;
        } else {
            throw new IllegalArgumentException("[" + number + "] is an incorrect Phone Number format.\nUsage: XXX-XXX-XXXX");
        }
    }

    /**
     * Validate the format of <code>date</code>.
     * @param date Format: MM/DD/YYYY
     * @return return date if valid, else throw exception.
     */
    private String validateDate(String date) {
        if (date.equals("")) {
            throw new IllegalArgumentException("date can't be empty.");
        }
        boolean valid = date.matches("^(0?[1-9]|1[012])/(0?[1-9]|1\\d|2\\d|3[01])/(\\d{4})$");
        if(valid) {
            return date;
        } else {
            throw new IllegalArgumentException("[" + date + "] is an incorrect Date format.\nUsage: MM/DD/YYYY");
        }
    }

    /**
     * Validate the format of <code>time</code>.
     * @param time Format: HH:MM
     * @return return time if valid, else throw exception.
     */
    private String validateTime(String time) {
        if (time.equals("")) {
            throw new IllegalArgumentException("time can't be empty.");
        }
        boolean valid = time.matches("^(0?\\d|1[0-2]):(0?\\d|1\\d|2\\d|3\\d|4\\d|5\\d) (AM|PM|am|pm)$");
        if(valid) {
            return time;
        } else {
            throw new IllegalArgumentException("[" + time + "] is an incorrect Time format.\nUsage: HH:MM in the 12 hour time format");
        }
    }

    /**
     * parse string of date to Date format
     * @param date date string that contains date, time and ampm
     * @return return date object
     */
    private Date parseDate(String date) {
        Date newDate;
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy hh:mm a");
        try {
            newDate = format.parse(date);
        } catch (ParseException e) {
            throw new IllegalArgumentException("Unparseable date: " + date);
        }
        return newDate;
    }

    /**
     * Returns a <code>String</code> of <code>caller</code>.
     * @return number of caller
     */
    @Override
    public String getCaller() {
        return this.caller;
    }

    /**
     * Returns a <code>String</code> of <code>callee</code>.
     * @return number of callee
     */
    @Override
    public String getCallee() {
        return this.callee;
    }

    /**
     * Returns a <code>Date</code> of <code>startTime</code>.
     * @return return start date and time in date object
     */
    @Override
    public String getStartTimeString() {
        return this.startTime;
    }

    /**
     * Returns a <code>Date</code> of <code>endTime</code>.
     * @return return end date and time in date object
     */
    @Override
    public String getEndTimeString() {
        return this.endTime;
    }

    /**
     * Returns a <code>Date</code> of <code>startTime</code>.
     * @return return start date and time in date object
     */
    @Override
    public Date getStartTime() {
        return parseDate(this.startTime);
    }

    /**
     * Returns a <code>Date</code> of <code>endTime</code>.
     * @return return end date and time in date object
     */
    @Override
    public Date getEndTime() {
        return parseDate(this.endTime);
    }

    /**
     * compares objects for order
     * @param phoneCall PhoneCall object
     * @return index of position
     */
    @Override
    public int compareTo(PhoneCall phoneCall) {
        int result;
        if (this.startTime.equals(phoneCall.startTime)) {
            result = this.caller.compareTo(phoneCall.caller);
        }else {
            result = this.startTime.compareTo(phoneCall.startTime);
        }
        return result;
    }
}
