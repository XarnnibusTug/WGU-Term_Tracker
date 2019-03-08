package com.example.jmor132.myapplication;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.drm.DrmStore;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class TermEditor extends AppCompatActivity implements View.OnClickListener {

    private static final int MAIN_ACTIVITY_CODE = 1;
    private String action;
    private String termFilter;
    private Term term;

    private EditText termNameField;
    private EditText termStartField;
    private EditText termEndField;

    private DatePickerDialog termStartDateDialog;
    private DatePickerDialog termEndDateDialog;
    private SimpleDateFormat dateFormat;

    private TermProvider database;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_term_editor);

        database = new TermProvider();

        termNameField = findViewById(R.id.termNameEditText);
        termStartField =findViewById(R.id.termStartDateEditText);
        termStartField.setInputType(InputType.TYPE_NULL);
        termEndField = findViewById(R.id.termEndDateEditText);
        termEndField.setInputType(InputType.TYPE_NULL);

        dateFormat = new SimpleDateFormat("MM-dd-yyyy", Locale.US);

        Intent intent   = getIntent();
        Uri uri = intent.getParcelableExtra(TermProvider.TERM_CONTENT_TYPE);

        if(uri == null){
            action = intent.ACTION_INSERT;
            setTitle(getString(R.string.add_new_term));
        }
        else {
            action = Intent.ACTION_EDIT;
            setTitle(getString(R.string.edit_term_title));
            long termId = Long.parseLong(uri.getLastPathSegment());
            term = TermDataManager.getTerm(this, termId);
            generateTerm(term);
        }
        setupDatePickers();
    }

    private void setupDatePickers(){

        termStartField.setOnClickListener(this);
        termEndField.setOnClickListener(this);

        final Calendar calendar = Calendar.getInstance();

        termStartDateDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                Calendar newDate = calendar.getInstance();
                newDate.set(year, month, dayOfMonth);
                termStartField.setText(dateFormat.format(newDate.getTime()));
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));

        termEndDateDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, month, dayOfMonth);
                termEndField.setText(dateFormat.format(newDate.getTime()));
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));

        termStartField.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    termStartDateDialog.show();
                }
            }
        });
    }



    private void generateTerm(Term term){
        termNameField.setText(term.termName);
        termStartField.setText(term.termStart);
        termEndField.setText(term.termEnd);
    }

    private void getTermInfo(){
        term.termName = termNameField.getText().toString().trim();
        term.termStart = termStartField.getText().toString().trim();
        term.termEnd = termEndField.getText().toString().trim();
    }

    public void saveTerm(View view){
        if(action == Intent.ACTION_INSERT){
            term = new Term();
            getTermInfo();
            TermDataManager.insertTerm(this,term.termName, term.termStart, term.termEnd);
            Toast.makeText(this, getString(R.string.term_saved), Toast.LENGTH_LONG).show();
            setResult(RESULT_OK);
        }
        else if(action == Intent.ACTION_EDIT){
            getTermInfo();
            term.saveChanges(this);
            Toast.makeText(this, getString(R.string.term_updated), Toast.LENGTH_LONG).show();
            setResult(RESULT_OK);
        }
        finish();
    }

    @Override
    public void onClick(View view) {
        if(view == termStartField){
            termStartDateDialog.show();
        }
        if (view == termEndField){
            termEndDateDialog.show();
        }
    }
}
