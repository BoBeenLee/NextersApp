package com.nexters.bobinlee.nextersapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import com.nexters.bobinlee.nextersapp.R;

public class StartActivity extends ActionBarActivity implements View.OnClickListener {
    private Button btnMoveMain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_start);

        initResource();
        initEvent();
    }

    public void initResource(){
        btnMoveMain = (Button) findViewById(R.id.move_main);
    }

    public void initEvent(){
        btnMoveMain.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.move_main :
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                finish();
                break;
        }
    }
}
