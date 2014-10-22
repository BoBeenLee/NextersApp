package com.nexters.bobinlee.nextersapp.activity;

import android.app.ActionBar;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ListView;

import com.nexters.bobinlee.nextersapp.R;
import com.nexters.bobinlee.nextersapp.adapter.ContactAdapter;
import com.nexters.bobinlee.nextersapp.handler.ContactHandler;
import com.nexters.bobinlee.nextersapp.model.Contact;
import com.nexters.bobinlee.nextersapp.thread.ContactThread;

import java.util.ArrayList;


public class MainActivity extends AbstractAsyncActivity implements View.OnClickListener {
    private ActionBar actionBarMain;

    private Button btnAddContact;
    private Button btnChkAll;
    private Button btnUnChkAll;

    // test
//    private Button btnTest;

    private ListView lvContacts;
    private WebView wvGoogleDoc;

    private GoogleDocBridge googleDocBridge;
    private ContactAdapter contactAdapter;

    private Handler contactHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initActionBar();
        initResource();
        initEvent();
    }

    public void initActionBar(){
        actionBarMain = getActionBar();
        getActionBar().setDisplayShowTitleEnabled(false);
        getActionBar().setDisplayShowHomeEnabled(false);
        getActionBar().setDisplayShowCustomEnabled(true);

        getActionBar().setCustomView(R.layout.action_main);
    }

    public void initResource() {
        View actionView = actionBarMain.getCustomView();

        btnAddContact = (Button) actionView.findViewById(R.id.add_contact);
        btnChkAll = (Button) actionView.findViewById(R.id.chk_all);
        btnUnChkAll = (Button) actionView.findViewById(R.id.un_chk_all);
//        btnTest = (Button) findViewById(R.id.btn_test);

        lvContacts = (ListView) findViewById(R.id.lv_contacts);
        wvGoogleDoc = (WebView) findViewById(R.id.wv_google_doc);
//        lvContacts.
        // settings
        contactAdapter = new ContactAdapter(this);
        lvContacts.setAdapter(contactAdapter);
        contactHandler = new ContactHandler(this, contactAdapter);

        if (wvGoogleDoc != null) {
            //wvGoogleDoc.loadUrl("file:///android_asset/google_map.html");
//            Log.d("google null", "google null assad");
            wvGoogleDoc.loadUrl("file:///android_asset/google_spreadsheet.html");
            wvGoogleDoc.getSettings().setJavaScriptEnabled(true);
        }
        googleDocBridge = new GoogleDocBridge(wvGoogleDoc, contactHandler);


        wvGoogleDoc.addJavascriptInterface(googleDocBridge, "GoogleDocBridge");
        wvGoogleDoc.setWebViewClient(new WebViewClient() {
            public void onPageFinished(WebView view, String url) {
                googleDocBridge.sendContacts();
            }
        });
    }

    public void initEvent() {
        btnAddContact.setOnClickListener(this);
        btnChkAll.setOnClickListener(this);
        btnUnChkAll.setOnClickListener(this);
//        btnTest.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.add_contact:
                ArrayList<Contact> contacts = new ArrayList<Contact>();

                for(Contact contact : contactAdapter.getList()){
                    if(contact.selected)
                        contacts.add(contact);
                }
                ContactThread contactThread = new ContactThread(MainActivity.this, contactHandler, contacts);
                contactThread.start();
                break;
            case R.id.chk_all:
                contactAdapter.toggleAllContact(true);
                break;
            case R.id.un_chk_all:
                contactAdapter.toggleAllContact(false);
                break;
//            case R.id.btn_test:
//                contactAdapter.add(test());
//                break;
        }
    }

    public Contact test() {
        Contact contact = new Contact();
        contact.name = "이보빈";
        contact.email = "adsf@naver.com";
        contact.phone = "010-4197-7512";
        return contact;
    }

    private static class GoogleDocBridge {
        private WebView mWebView;
        private Handler mHandler;

        private GoogleDocBridge(WebView webView, Handler handler) {
            this.mWebView = webView;
            this.mHandler = handler;
        }

        @JavascriptInterface
        public void addContact(final String json) {
            Message msg = new Message();
            Bundle bundle = new Bundle();

//            Log.d("addContact", "addContact : " + json);
            bundle.putInt("menu", ContactHandler.CONTACT);
            bundle.putString("contact", json);
            msg.setData(bundle);

            mHandler.sendMessage(msg);
        }

        @JavascriptInterface
        public void test(final String str){
            Log.d("test", "test:" + str);
        }

        public void sendTest(){
            mWebView.loadUrl("javascript:test()");
        }

        public void sendContacts() {
            mWebView.loadUrl("javascript:sendContacts()");
        }
    }
}
