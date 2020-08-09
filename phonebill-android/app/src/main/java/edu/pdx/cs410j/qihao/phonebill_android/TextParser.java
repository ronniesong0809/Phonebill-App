package edu.pdx.cs410j.qihao.phonebill_android;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;

import edu.pdx.cs410J.ParserException;
import edu.pdx.cs410J.PhoneBillParser;

public class TextParser implements PhoneBillParser<PhoneBill> {
    private final Reader reader;

    /**
     * Creates a new <code>PhoneBillTextParser</code>
     * @param reader Reader object
     */
    public TextParser(Reader reader) {
        this.reader = reader;
    }

    /**
     * parse PhoneBill from file
     * @return AbstractPhoneBill object
     * @throws ParserException throws parser exception
     */
    @Override
    public PhoneBill parse() throws ParserException {
        BufferedReader br = new BufferedReader(this.reader);
        PhoneBill bill;
        try {
            String customer = br.readLine();
            bill = new PhoneBill(customer);

            while (br.ready()) {
                String caller = br.readLine();
                String callee = br.readLine();
                String _start = br.readLine();
                String _end = br.readLine();
                if (caller == null) {
                    break;
                }
                String[] start = _start.split(" ");
                String[] end = _end.split(" ");
                String startDate = start[0];
                String startTime = start[1];
                String startAmPm = start[2];
                String endDate = end[0];
                String endTime = end[1];
                String endAmPm = end[2];

                bill.addPhoneCall(new PhoneCall(caller, callee, startDate, startTime + " " + startAmPm, endDate, endTime + " " + endAmPm));
            }
            return bill;

        } catch (IOException e) {
            throw new ParserException("While parsing", e);
        }
    }
}