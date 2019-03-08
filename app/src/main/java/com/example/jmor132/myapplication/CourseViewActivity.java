package com.example.jmor132.myapplication;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;

import org.w3c.dom.Text;

public class CourseViewActivity extends AppCompatActivity {

    private static final int COURSE_NOTE_LIST_ACTIVITY_CODE = 11111;
    private static final int ASSESSMENT_LIST_ADCTIVITY_CODE = 22222;
    private static final int COURSE_EDITOR_ACTIVITY_CODE = 33333;

    private Menu menu;
    private Uri courseUri;
    private long courseID;
    private Course course;

    private TextView courseNameView;
    private TextView startDateViewName;
    private TextView endDateView;
    private TextView statusView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_view);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        courseUri = intent.getParcelableExtra(CourseProvider.COURSE_CONTENT_TYPE);
        courseID = Long.parseLong(courseUri.getLastPathSegment());
        course = CourseDataManager.getCourse(this, courseID);

        courseNameView = findViewById(R.id.courseNameView);
        startDateViewName = findViewById(R.id.courseStartDate);
        endDateView = findViewById(R.id.courseEndDate);
        statusView = findViewById(R.id.courseStatus);



        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        generateCourse();
        setLabel();

    }

    private void setLabel(){
        statusView = findViewById(R.id.courseStatus);
        String status = "";
        switch(course.status.toString()){
            case "PLANNED":
                status = "Planned To Take";
                break;
            case "IN_PROGRESS" :
                status = "In Progress";
                break;
            case "COMPLETED":
                status = "Completed";
                break;
            case "DROPPED":
                status = "Dropped";
                break;
        }
        statusView.setText("Status:" + status);
    }

    private void generateCourse(){
        course = CourseDataManager.getCourse(this, courseID);
        courseNameView.setText(course.courseName);
        startDateViewName.setText(course.courseStart);
        endDateView.setText(course.courseEnd);
    }


    public void editCourse(View view){
        Intent intent = new Intent(this, CourseEditor.class);
        Uri uri = Uri.parse(CourseProvider.COURSES_URI + "/" + course.courseID);
        intent.putExtra(CourseProvider.COURSE_CONTENT_TYPE, uri);
        startActivityForResult(intent, COURSE_EDITOR_ACTIVITY_CODE);
    }

    public void openCourseNotes(View view){
        Intent intent = new Intent(CourseViewActivity.this, CourseNoteListActivity.class );
        Uri uri = Uri.parse(CourseProvider.COURSES_URI + "/" + courseID);
        intent.putExtra(CourseProvider.COURSE_CONTENT_TYPE , uri);
        startActivityForResult(intent, COURSE_NOTE_LIST_ACTIVITY_CODE);
    }
}
