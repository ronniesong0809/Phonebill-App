package edu.pdx.cs410j.qihao.phonebill_android;

import java.util.Collection;
import java.util.SortedSet;
import java.util.TreeSet;

import edu.pdx.cs410J.AbstractPhoneBill;

/**
 * This class is represents a <code>PhoneBill</code>.
 */
public class PhoneBill extends AbstractPhoneBill<PhoneCall> {
    private final String customer;
    private final SortedSet<PhoneCall> calls = new TreeSet<PhoneCall>();

    /**
     * Creates a new <code>Student</code>
     * @param customer name of customer
     */
    public PhoneBill(String customer) {
        this.customer = customer;
    }

    /**
     * Returns a <code>String</code> of <code>customer</code>.
     * @return name of customer
     */
    @Override
    public String getCustomer() {
        return this.customer;
    }

    /**
     * add a <code>PhoneCall</code> into <code>PhoneBill</code> Collection.
     * @param call PhoneCall object
     */
    @Override
    public void addPhoneCall(PhoneCall call) {
        this.calls.add(call);
    }

    /**
     * Returns a <code>Collection</code> of <code>PhoneCall</code>.
     * @return return PhoneCalls
     */
    @Override
    public Collection<PhoneCall> getPhoneCalls() {
        return this.calls;
    }
}
