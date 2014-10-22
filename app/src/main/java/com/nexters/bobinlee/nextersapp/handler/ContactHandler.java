package com.nexters.bobinlee.nextersapp.handler;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.nexters.bobinlee.nextersapp.adapter.ContactAdapter;
import com.nexters.bobinlee.nextersapp.model.Contact;

/**
 * Created by BoBinLee on 2014-08-10.
 */
public class ContactHandler extends Handler {
    public final static int CONTACT = 1;
    public final static int ADD = 2;

    private ContactAdapter mContactAdapter;
    private Context mContext;

    public ContactHandler(Context context, ContactAdapter contactAdapter){
        mContactAdapter = contactAdapter;
        mContext = context;
    }

    @Override
    public void handleMessage(Message msg) {
        super.handleMessage(msg);
        Bundle bundle = msg.getData();
        int menu = bundle.getInt("menu");

        switch (menu) {
            case CONTACT:
                Gson gson = new Gson();
                String json = bundle.getString("contact");
                Contact contact = gson.fromJson(json, Contact.class);

//                Log.d("ContactHandler", "ContactHandler : " + contact.rank);
                mContactAdapter.add(contact);
                break;
            case ADD :
                Toast.makeText(mContext, "추가 완료", Toast.LENGTH_SHORT)
                        .show();
                break;
        }
    }
}
