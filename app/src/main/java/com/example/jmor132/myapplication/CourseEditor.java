package com.example.jmor132.myapplication;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class CourseEditor extends AppCompatActivity implements View.OnClickListener {

    private String action;
    private Uri courseUri;
    private Uri termUri;
    private Course course;

    private EditText editCourseName;
    private EditText editCourseStart;
    private EditText editCourseEnd;
    private EditText editCourseMentor;
    private EditText editCoursePhone;
    private EditText editCourseEmail;

    private DatePickerDialog courseStartDateDialog;
    private DatePickerDialog courseEndDateDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_editor);

        editCourseName = findViewById(R.id.editCourseName);
        editCourseStart = findViewById(R.id.editCourseStart);
        editCourseEnd = findViewById(R.id.editCourseEnd);
        editCourseMentor = findViewById(R.id.editCourseMentor);
        editCoursePhone = findViewById(R.id.editMentorNumber);
        editCourseEmail = findViewById(R.id.editCourseMentorEmail);

        Intent intent = getIntent();
        courseUri = intent.getParcelableExtra(CourseProvider.COURSE_CONTENT_TYPE);
        termUri = intent.getParcelableExtra(TermProvider.TERM_CONTENT_TYPE);


        if(courseUri == null){
            action = Intent.ACTION_INSERT;
            setTitle(getString(R.string.add_new_course));
        }
        else {
            action = Intent.ACTION_EDIT;
            setTitle(getString(R.string.edit_course_title));
            long classID = Long.parseLong(courseUri.getLastPathSegment());
            course  = CourseDataManager.getCourse(this, classID);
            fillCourseForm(course);
        }
        setupDatePickers();
    }


    private void fillCourseForm(Course course){
        editCourseName.setText(course.courseName);
        editCourseStart.setText(course.courseStart);
        editCourseEnd.setText(course.courseEnd);
        editCourseMentor.setText(course.mentorName);
        editCoursePhone.setText(course.mentorPhone);
        editCourseEmail.setText(course.mentorEmail);
    }

    private void setupDatePickers(){
        editCourseStart.setOnClickListener(this);
        editCourseEnd.setOnClickListener(this);

        Calendar calendar = Calendar.getInstance();
        courseStartDateDialog= new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, month, dayOfMonth);
                editCourseStart.setText(DateUtil.dateFormat.format(newDate.getTime()));
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));

        courseEndDateDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, month, dayOfMonth);
                editCourseEnd.setText(DateUtil.dateFormat.format(newDate.getTime()));
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));

        editCourseStart.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    courseStartDateDialog.show();
                }
            }
        });

        editCourseEnd.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    courseEndDateDialog.show();
                }
            }
        });
    }

    public void saveCourse(View view){
        if(action == Intent.ACTION_INSERT){
            long termID = Long.parseLong(termUri.getLastPathSegment());
            CourseDataManager.insertCourse(this, termID,
                    editCourseName.getText().toString().trim(),
                    editCourseStart.getText().toString().trim(),
                    editCourseEnd.getText().toString().trim(),
                    editCourseMentor.getText().toString().trim(),
                    editCoursePhone.getText().toString().trim(),
                    editCourseEmail.getText().toString().trim(),
                    CourseStatus.PLANNED);
        }
        else if(action == Intent.ACTION_EDIT){
            course.courseName = editCourseName.getText().toString().trim();
            course.courseStart = editCourseStart.getText().toString().trim();
            course.courseEnd = editCourseEnd.getText().toString().trim();
            course.mentorName = editCourseMentor.getText().toString().trim();
            course.mentorPhone = editCoursePhone.getText().toString().trim();
            course.mentorEmail = editCourseEmail.getText().toString().trim();
            course.saveChange(this);
            setResult(RESULT_OK);
        }
        finish();
    }

    @Override
    public void onClick(View view) {
        if(view == editCourseStart){
            courseStartDateDialog.show();
        }
        if(view == editCourseEnd){
            courseEndDateDialog.show();
        }
    }
}
