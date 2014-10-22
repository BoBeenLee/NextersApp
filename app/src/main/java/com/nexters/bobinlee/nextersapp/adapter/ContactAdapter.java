package com.nexters.bobinlee.nextersapp.adapter;

import android.content.Context;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.nexters.bobinlee.nextersapp.R;
import com.nexters.bobinlee.nextersapp.model.Contact;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Created by BoBinLee on 2014-08-10.
 */
public class ContactAdapter extends BaseAdapter {
    private ArrayList<Contact> mContacts;
    private LayoutInflater mInflater = null;
    private Context mContext = null;


    public ContactAdapter(Context context) {
        super();
        mContacts = new ArrayList<Contact>();
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void add(Contact contact) {
        mContacts.add(contact);
//        Collections.sort(mContacts, new RankAscCompare());
        notifyDataSetChanged();
//        notifyDataSetInvalidated();
    }

    private static class RankAscCompare implements Comparator<Contact> {
        /**
         * 오름차순(ASC)
         */
        @Override
        public int compare(Contact arg0, Contact arg1) {
            if(arg0.rank > arg1.rank)
                return 1;
            else if(arg0.rank == arg1.rank)
                return 0;
            return -1;
        }
    }

    public ArrayList<Contact> getList() {
        return mContacts;
    }

    public void toggleAllContact(boolean isCheck) {
//        Log.d("toggleAllContact", "toggleAllContact : " + mContacts.size() + " - " + mCbContacts.size());
        for (int i = 0; i < mContacts.size(); i++) {
            Contact contact = mContacts.get(i);
            contact.selected = isCheck;
        }
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mContacts.size();
    }

    @Override
    public Object getItem(int position) {
        return mContacts.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        CheckBox selectedContact;

//        Log.d("ContactAdapter", "ContactAdapter: " + position);

        if (view == null || position != Long.parseLong(view.getTag().toString())) {
            TextView name, email, rank, phone;
            final Contact contact;

            view = mInflater.inflate(R.layout.list_item, null);
            name = (TextView) view.findViewById(R.id.contact_name);
            email = (TextView) view.findViewById(R.id.contact_email);
            rank = (TextView) view.findViewById(R.id.contact_rank);
            phone = (TextView) view.findViewById(R.id.phone_number);
            contact = mContacts.get(position);
            selectedContact = (CheckBox) view.findViewById(R.id.selected_contact);

            view.setTag(position);

            name.setText(contact.name);
            email.setText(Html.fromHtml("<a href=\"mailto:" + contact.email + "\">" + contact.email + "</a>"));
            email.setMovementMethod(LinkMovementMethod.getInstance());
            rank.setText("" + contact.rank);
            phone.setText(contact.phone);
            selectedContact.setOnCheckedChangeListener(new CheckBox.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    contact.selected = isChecked;
                }
            });
        }
        selectedContact = (CheckBox) view.findViewById(R.id.selected_contact);
        selectedContact.setChecked(mContacts.get(position).selected);
        return view;
    }
}
