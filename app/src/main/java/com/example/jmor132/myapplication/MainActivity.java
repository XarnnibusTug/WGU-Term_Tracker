package com.example.jmor132.myapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    private static final int TERM_LIST_ACTIVITY_CODE = 2222;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }


    //Open Term Screen
    public void openTermsList(View view){
        Intent intent = new Intent(this, TermsListActivity.class);
        startActivityForResult(intent, TERM_LIST_ACTIVITY_CODE);
    }
}
