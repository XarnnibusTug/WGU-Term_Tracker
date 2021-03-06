package com.example.jmor132.myapplication;

import android.annotation.TargetApi;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
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

import java.util.Date;

public class AssessmentViewActivity extends AppCompatActivity {
    private static final int ASSESSMENT_EDITOR_ACTIVITY_CODE = 11111;

    private long assessmentID;
    private Assessment assessment;
    private TextView assessmentCode;
    private TextView assessmentName;
    private TextView assessmentDescription;
    private TextView assessmentDate;
    private Menu menu;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assessment_view);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        generateAssessment();
    }

    private void generateAssessment(){
        Uri assessmentUri = getIntent().getParcelableExtra(AssessmentProvider.ASSESSMENT_CONTENT_TYPE);
        assessmentID = Long.parseLong(assessmentUri.getLastPathSegment());
        assessment = AssessmentDataManager.getAssessment(this, assessmentID);
        assessmentCode = findViewById(R.id.assessmentCodeView);
        assessmentCode.setText(assessment.assessmentCode);
        assessmentName = findViewById(R.id.assessmentNameView);
        assessmentDescription = findViewById(R.id.assessmentDescription);
        assessmentDate = findViewById(R.id.assessmentDateTime);
        assessmentName.setText(assessment.assessmentName);
        assessmentDescription.setText(assessment.assessmentDescription);
        assessmentDate.setText(assessment.dateTime);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK){
            generateAssessment();
        }
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_assessment_viewer, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();
        switch(id){
            case R.id.action_enable_notifications:
                return enableNotifications();
            case R.id.action_disable_notifications:
                return disableNotficiations();
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    @TargetApi(Build.VERSION_CODES.N)
    private boolean enableNotifications(){
        long now = DateUtil.todayLong();

        AlarmHandler.assessmentAlarm(getApplicationContext(), (int) assessmentID, System.currentTimeMillis() + 1000, "Assessment is Today!",
                                    assessment.assessmentName + "Starts" + assessment.dateTime);
        if(now <= DateUtil.getDateTimestamp(assessment.dateTime)){
            AlarmHandler.assessmentAlarm(getApplicationContext(),(int) assessmentID, DateUtil.getDateTimestamp(assessment.dateTime),  "Assessment Is today" ,
                                                                    assessment.assessmentName + "Starts" + assessment.dateTime);
        }
        if (now <= DateUtil.getDateTimestamp(assessment.dateTime)- 3 * 24 * 60 * 60 * 1000){
            AlarmHandler.assessmentAlarm(getApplicationContext(), (int) assessmentID, DateUtil.getDateTimestamp(assessment.dateTime), "Assessment is in 3 days!" ,
                                                                            assessment.assessmentName + "Starts" + assessment.dateTime);
        }
        if (now <= DateUtil.getDateTimestamp(assessment.dateTime) - 21 * 24 * 60 * 60 * 1000){
            AlarmHandler.assessmentAlarm(getApplicationContext(), (int) assessmentID, DateUtil.getDateTimestamp(assessment.dateTime), "Assessment is in 3 weeks",
                                                                                    assessment.assessmentName + "Starts" + assessment.dateTime);
        }
        assessment.notifications = 1;
        assessment.saveChanges(this);
        return true;
    }

    private boolean disableNotficiations(){
        assessment.notifications = 0;
        assessment.saveChanges(this);
        return true;
    }


    public void deleteAssessment(View view){
        AssessmentDataManager.deleteAssessment(AssessmentViewActivity.this, assessmentID);
        setResult(RESULT_OK);
        finish();
        Toast.makeText(AssessmentViewActivity.this, getString(R.string.assessment_deleted), Toast.LENGTH_LONG).show();

    }

    public void editAssessment(View view){
        Intent intent = new Intent(AssessmentViewActivity.this, AssessmentEdit.class);
        Uri uri = Uri.parse(AssessmentProvider.ASSESSMENT_URI + "/" + assessmentID);
        intent.putExtra(AssessmentProvider.ASSESSMENT_CONTENT_TYPE, uri);
        startActivityForResult(intent, ASSESSMENT_EDITOR_ACTIVITY_CODE);

    }

}
