package com.example.jmor132.myapplication;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

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
    private TextView startDateView;
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
        startDateView = findViewById(R.id.courseStartDate);
        endDateView = findViewById(R.id.courseEndDate);
        statusView = findViewById(R.id.courseStatus);


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


    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_course_viewer, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();
        switch(id){
            case R.id.action_enable_notifications:
                return enableNotifications();
            case R.id.action_disable_notifications:
                return disableNotifications();
            case R.id.action_drop_course:
                return dropCourse();
            case R.id.action_start_course:
                return startCourse();
            case R.id.action_mark_course_completed:
                return courseCompleted();
                default:
                    return super.onOptionsItemSelected(item);
        }
    }

    private void generateCourse(){
        course = CourseDataManager.getCourse(this, courseID);
        courseNameView.setText(course.courseName);
        startDateView.setText(course.courseStart);
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

    private boolean enableNotifications(){
        Toast.makeText(this, getString(R.string.notificaitons_enabled), Toast.LENGTH_SHORT).show();
        return true;
    }

    private boolean disableNotifications(){
        course.notifications = 0;
        course.saveChange(this);
        Toast.makeText(this, getString (R.string.notificaitons_disabled), Toast.LENGTH_LONG).show();
        return true;
    }

    private boolean startCourse(){
        course.status = CourseStatus.IN_PROGRESS;
        course.saveChange(this);
        setLabel();
        Toast.makeText(this, getString (R.string.course_started), Toast.LENGTH_LONG).show();
        return true;
    }
    private boolean dropCourse(){
        course.status = CourseStatus.DROPPED;
        course.saveChange(this);
        Toast.makeText(this, getString(R.string.course_dropped_message), Toast.LENGTH_SHORT).show();
        setLabel();
        return true;
    }

    private boolean courseCompleted(){
        course.status = CourseStatus.COMPLETED;
        course.saveChange(this);
        setLabel();
        Toast.makeText(this, (R.string.course_completed_message), Toast.LENGTH_LONG).show();
        return true;
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        if(resultCode == RESULT_OK){
            generateCourse();
        }
    }
}
