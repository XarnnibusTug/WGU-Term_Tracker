package com.example.jmor132.myapplication;

import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.widget.TextView;

public class AssessmentViewActivity extends AppCompatActivity {

    private static final int ASSESSMENT_EDITOR_ACTIVITY_CODE = 11111;

    private long assessmentID;
    private Assessment assessment;
    private TextView assessmentName;
    private TextView assessmentDescription;
    private TextView assessmesntDate;
    private Menu menu;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assessment_view);

        generateAssessment();
    }

    private void generateAssessment(){
        Uri assessmentUri = getIntent().getParcelableExtra(AssessmentProvider.ASSESSMENT_CONTENT_TYPE);
        assessmentID = Long.parseLong(assessmentUri.getLastPathSegment());
        assessment = AssessmentDataManager.getAssessment(this, assessmentID);
        
    }
}
