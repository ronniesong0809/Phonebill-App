package edu.pdx.cs410j.qihao.phonebill_android;

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
     * @param startTime  time of call began
     * @param endTime    time of call ended
     */
    //public PhoneCall(String caller, String callee, String startDate, String startTime, String startAmPm, String endDate, String endTime, String endAmPm) {
    public PhoneCall(String caller, String callee, String startTime, String endTime) {
        this.caller = caller;
        this.callee = callee;
        this.startTime = startTime;
        this.endTime = endTime;
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
     * compares objects for order
     * @param phoneCall PhoneCall object
     * @return index of position
     */
    @Override
    public int compareTo(PhoneCall phoneCall) {
        int result;
        if ((this.startTime.equals(phoneCall.startTime))) {
            result = this.caller.compareTo(phoneCall.caller);
        }else {
            result = this.startTime.compareTo(phoneCall.startTime);
        }
        return result;
    }
}
