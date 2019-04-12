package com.example.jmor132.myapplication;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

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

    public void openCurrentTerm(View view){
        Cursor cursor = getContentResolver().query(TermProvider.TERMS_URI, null,
                                            DBOpenHelper.TERM_ACTIVE + "=1", null,null);
        while(cursor.moveToNext()) {
            Intent intent = new Intent(this, TermViewActivity.class );
            long id = cursor.getLong(cursor.getColumnIndex(DBOpenHelper.TERMS_TABLE_ID));
            Uri uri = Uri.parse(TermProvider.TERMS_URI + "/" + id);
            intent.putExtra(TermProvider.TERM_CONTENT_TYPE, uri);
            startActivityForResult(intent, TERM_LIST_ACTIVITY_CODE);
            return;
        }
        Toast.makeText(this, getString(R.string.no_term_set), Toast.LENGTH_LONG).show();
    }
}
