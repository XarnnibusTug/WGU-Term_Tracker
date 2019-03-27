package com.example.jmor132.myapplication;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

import java.util.Calendar;
import java.util.TimeZone;

import javax.security.auth.Destroyable;

public class AssessmentEdit extends AppCompatActivity implements View.OnClickListener{

    private Assessment assessment;
    private long courseID;
    private EditText assessmentCodeEdit;
    private EditText assessmentNameEdit;
    private EditText assessmentDescriptionEdit;
    private EditText assessmentDateTimeEdit;
    private DatePickerDialog assessmentDateDialog;
    private TimePickerDialog assessmentTimeDialog;
    private String action;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assessment_edit);

        assessmentCodeEdit = findViewById(R.id.EditAssessmentCode);
        assessmentNameEdit = findViewById(R.id.EditAssessmentName);
        assessmentDescriptionEdit = findViewById(R.id.EditAssessmentDescription);
        assessmentDateTimeEdit = findViewById(R.id.EditAssessmentDateTime);

        Uri assessmentUri = getIntent().getParcelableExtra(AssessmentProvider.ASSESSMENT_CONTENT_TYPE);
        if(assessmentUri == null){
            setTitle(getString(R.string.new_assessment));
            action = Intent.ACTION_INSERT;
            Uri courseUri = getIntent().getParcelableExtra(CourseProvider.COURSE_CONTENT_TYPE);
            courseID = Long.parseLong(courseUri.getLastPathSegment());
            assessment = new Assessment();
        }
        else {
            setTitle(getString(R.string.edit_assessment));
            action = Intent.ACTION_EDIT;
            Long assessmentID = Long.parseLong(assessmentUri.getLastPathSegment());
            assessment = AssessmentDataManager.getAssessment(this, assessmentID);
            courseID = assessment.courseID;
            generateAssessment();
        }
        setupDateAndTimePickers();
    }

    private void generateAssessment(){
        if(assessment != null){
            assessmentCodeEdit.setText(assessment.assessmentCode);
            assessmentNameEdit.setText(assessment.assessmentName);
            assessmentDescriptionEdit.setText(assessment.assessmentDescription);
            assessmentDateTimeEdit.setText(assessment.dateTime);

        }
    }

    private void setupDateAndTimePickers() {
        assessmentDateTimeEdit.setOnClickListener(this);

        Calendar calendar = Calendar.getInstance();
        assessmentDateDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                Calendar calendar2 = Calendar.getInstance();
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, month, dayOfMonth);
                assessmentDateTimeEdit.setText(DateUtil.dateFormat.format(newDate.getTime()));
                assessmentTimeDialog = new TimePickerDialog(AssessmentEdit.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        String AM_PM;
                        if (hourOfDay < 12) {
                            AM_PM = "AM";
                        }
                        else {
                            AM_PM = "PM";
                        }
                        if (hourOfDay > 12) {
                            hourOfDay = hourOfDay - 12;
                        }
                        if (hourOfDay == 0) {
                            hourOfDay = 12;
                        }
                        String minuteString = Integer.toString(minute);
                        if (minute < 10) {
                            minuteString = "0" + minuteString;
                        }
                        String datetime = assessmentDateTimeEdit.getText().toString() + " " + hourOfDay + ":" + minuteString
                                + " " + AM_PM + " " + TimeZone.getDefault().getDisplayName(false, TimeZone.SHORT);
                        assessmentDateTimeEdit.setText(datetime);
                    }
                }, calendar2.get(Calendar.HOUR_OF_DAY), calendar2.get(Calendar.MINUTE), false);
                assessmentTimeDialog.show();
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));

        assessmentDateTimeEdit.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    assessmentDateDialog.show();
                }
            }
        });
    }

    public void saveAssessment(View view){
        assessment.assessmentCode = assessmentCodeEdit.getText().toString().trim();
        assessment.assessmentName = assessmentNameEdit.getText().toString().trim();
        assessment.assessmentDescription = assessmentDescriptionEdit.getText().toString().trim();
        assessment.dateTime = assessmentDateTimeEdit.getText().toString().trim();

        switch (action){
            case Intent.ACTION_INSERT:
                AssessmentDataManager.insertAssessment(this, courseID, assessment.assessmentCode, assessment.assessmentName, assessment.assessmentDescription,
                                                        assessment.dateTime);
                setResult(RESULT_OK);
                finish();
                break;
            case Intent.ACTION_EDIT:
                assessment.saveChanges(this);
                setResult(RESULT_OK);
                finish();
                break;
                default:
                    throw new UnsupportedOperationException();
        }
    }



    @Override
    public void onClick(View view) {
        if(view == assessmentDateTimeEdit){
            assessmentDateDialog.show();
        }
    }
}
