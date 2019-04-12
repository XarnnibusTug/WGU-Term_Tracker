package com.example.jmor132.myapplication;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CursorAdapter;
import android.widget.TextClock;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class TermViewActivity extends AppCompatActivity {

    private static final int TERM_EDIT_ACTVIITY_CODE = 11111;
    private static final int COURSE_LIST_ACTIVITY_CODE = 22222;

    private Uri termUri;
    private Term term;

    private CursorAdapter cursorAdapter;

    private TextView termTitle;
    private TextView tvTermStart;
    private TextView tvTermEnd;


    private long termID;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_term_view);


        Intent intent = getIntent();
        termUri = intent.getParcelableExtra(TermProvider.TERM_CONTENT_TYPE);

        fillForm();
        generateTermData();

    }

    private void fillForm(){
        termTitle = findViewById(R.id.TermViewTitle);
        tvTermStart = findViewById(R.id.TermViewStartDate);
        tvTermEnd = findViewById(R.id.TermViewEndDate);

    }

    private void generateTermData() {
        if (termUri == null) {
            setResult(RESULT_CANCELED);
            finish();
        } else {
            termID = Long.parseLong(termUri.getLastPathSegment());
            term = TermDataManager.getTerm(this, termID);

            setTitle(getString(R.string.view_term));
            termTitle.setText(term.termName);
            tvTermStart.setText(term.termStart);
            tvTermEnd.setText(term.termEnd);
        }
    }

    public void editTermButton(View view){
        Intent intent = new Intent(this, TermEditor.class);
        Uri uri = Uri.parse(TermProvider.TERMS_URI + "/" + term.termID);
        intent.putExtra(TermProvider.TERM_CONTENT_TYPE, uri);
        startActivityForResult(intent, TERM_EDIT_ACTVIITY_CODE);

    }

    public void deleteTermButton(View view){
        long classCount = term.getClassCount(TermViewActivity.this);
        if(classCount == 0) {
            getContentResolver().delete(TermProvider.TERMS_URI, DBOpenHelper.TERMS_TABLE_ID + " = " + termID, null);
            Toast.makeText(TermViewActivity.this, getString(R.string.term_deleted), Toast.LENGTH_LONG).show();
            setResult(RESULT_OK);
            finish();
        }
        else {
            Toast.makeText(TermViewActivity.this, getString(R.string.courseAttached), Toast.LENGTH_LONG).show();
        }

    }

    public void markTermActive(View view){
        Cursor cursor = getContentResolver().query(TermProvider.TERMS_URI, null,null,null,null);
        ArrayList<Term> termList = new ArrayList<>();
        while(cursor.moveToNext()){
            termList.add(TermDataManager.getTerm(this, cursor.getLong(cursor.getColumnIndex(DBOpenHelper.TERMS_TABLE_ID))));
        }
        for(Term term : termList){
            term.deactivateTerm(this);
        }
        this.term.activateTerm(this);
        Toast.makeText(TermViewActivity.this, getString(R.string.term_marked_active), Toast.LENGTH_LONG).show();

    }

    public void openCourses(View view){
        Intent intent = new Intent(this, CourseListActivity.class);
        intent.putExtra(TermProvider.TERM_CONTENT_TYPE, termUri);
        startActivityForResult(intent, COURSE_LIST_ACTIVITY_CODE);
    }
}
