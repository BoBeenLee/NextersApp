package com.nexters.bobinlee.nextersapp.thread;

import android.content.ContentProviderOperation;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.ContactsContract;
import android.util.Log;

import com.nexters.bobinlee.nextersapp.activity.AbstractAsyncActivity;
import com.nexters.bobinlee.nextersapp.handler.ContactHandler;
import com.nexters.bobinlee.nextersapp.model.Contact;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by BoBinLee on 2014-08-11.
 */
public class ContactThread extends Thread {
    private AtomicBoolean isRunning = new AtomicBoolean(false);
    private Handler mHandler;
    private ArrayList<Contact> mContacts;
    private AbstractAsyncActivity mAbstractAsyncActivity;

    public ContactThread(AbstractAsyncActivity abstractAsyncActivity, Handler handler, ArrayList<Contact> contacts) {
        mHandler = handler;
        mContacts = contacts;
        mAbstractAsyncActivity = abstractAsyncActivity;
    }

    @Override
    public synchronized void start() {
        this.isRunning.set(true);
        mAbstractAsyncActivity.showLoadingProgressDialog();
        super.start();
    }

    @Override
    public void run() {
        for(int i=0; isRunning.get() && i < mContacts.size(); i++) {
            addAsContactAutomatic(mAbstractAsyncActivity, mContacts.get(i));
        }
        Message msg = new Message();
        Bundle bundle = new Bundle();
        bundle.putInt("menu", ContactHandler.ADD);
        msg.setData(bundle);

        mAbstractAsyncActivity.dismissProgressDialog();
        mHandler.sendMessage(msg);
    }

    @Override
    public void interrupt() {
        this.isRunning.set(false);
        super.interrupt();

    }

    @Override
    public boolean isInterrupted() {
        return this.isRunning.get() == true ? false : true;
    }

    /**
     * Automatically add a contact into someone's contacts list
     *
     * @param context Activity context
     * @param contact {@link com.nexters.bobinlee.nextersapp.model.Contact} to add to contacts list
     */
    public void addAsContactAutomatic(final Context context, final Contact contact) {
        String displayName = contact.name;
        String mobileNumber = contact.phone;
        String email = contact.email;

        ArrayList<ContentProviderOperation> ops = new ArrayList<ContentProviderOperation>();

        ops.add(ContentProviderOperation.newInsert(ContactsContract.RawContacts.CONTENT_URI)
                .withValue(ContactsContract.RawContacts.ACCOUNT_TYPE, null)
                .withValue(ContactsContract.RawContacts.ACCOUNT_NAME, null).build());
        // Names
        if (displayName != null) {
            ops.add(ContentProviderOperation
                    .newInsert(ContactsContract.Data.CONTENT_URI)
                    .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                    .withValue(ContactsContract.Data.MIMETYPE,
                            ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE)
                    .withValue(ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME,
                            displayName).build());
        }

        // Mobile Number
        if (mobileNumber != null) {
            ops.add(ContentProviderOperation
                    .newInsert(ContactsContract.Data.CONTENT_URI)
                    .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                    .withValue(ContactsContract.Data.MIMETYPE,
                            ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
                    .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, mobileNumber)
                    .withValue(ContactsContract.CommonDataKinds.Phone.TYPE,
                            ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE).build());
        }

        // Email
        if (email != null) {
            ops.add(ContentProviderOperation
                    .newInsert(ContactsContract.Data.CONTENT_URI)
                    .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                    .withValue(ContactsContract.Data.MIMETYPE,
                            ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE)
                    .withValue(ContactsContract.CommonDataKinds.Email.DATA, email)
                    .withValue(ContactsContract.CommonDataKinds.Email.TYPE,
                            ContactsContract.CommonDataKinds.Email.TYPE_WORK).build());
        }

        // Asking the Contact provider to create a new contact
        try {
            context.getContentResolver().applyBatch(ContactsContract.AUTHORITY, ops);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
