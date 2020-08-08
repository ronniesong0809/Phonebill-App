package edu.pdx.cs410j.qihao.phonebill_android;

import java.io.IOException;
import java.io.PrintWriter;

import edu.pdx.cs410J.PhoneBillDumper;

/**
 * This class is represents a <code>TextDumper</code>.
 */
public class TextDumper implements PhoneBillDumper<PhoneBill> {
    private final PrintWriter writer;

    /**
     * Creates a new <code>PhoneBillTextDumper</code>
     *
     * @param writer PrintWriter object
     */
    TextDumper(PrintWriter writer) {
        this.writer = writer;

    }

    /**
     * dump PhoneBill to via Rest
     *
     * @param bill PhoneBill object
     * @throws IOException throws IO exception
     */
    @Override
    public void dump(PhoneBill bill) throws IOException {
        this.writer.println(bill.getCustomer());

        for (PhoneCall call : bill.getPhoneCalls()) {
            this.writer.println(call.getCaller());
            this.writer.println(call.getCallee());
            this.writer.println(call.getStartTimeString());
            this.writer.println(call.getEndTimeString());
        }
        this.writer.flush();
        this.writer.close();
    }
}
