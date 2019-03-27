package com.example.jmor132.myapplication;

import android.content.Intent;
import android.net.Uri;
import android.os.ResultReceiver;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class AssessmentViewActivity extends AppCompatActivity {

    private static final int ASSESSMENT_EDITOR_ACTIVITY_CODE = 11111;

    private long assessmentID;
    private Assessment assessment;
    private TextView assessmentName;
    private TextView assessmentDescription;
    private TextView assessmentDate;
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
        assessmentName = findViewById(R.id.assessmentTitle);
        assessmentDescription = findViewById(R.id.assessmentDescription);
        assessmentDate = findViewById(R.id.assessmentDateTime);
        assessmentName.setText(assessment.assessmentCode + ": " + assessmentName);
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

    public void deleteAssessment(View view){
        AssessmentDataManager.deleteAssessment(AssessmentViewActivity.this, assessmentID);
        setResult(RESULT_OK);
        finish();
        Toast.makeText(AssessmentViewActivity.this, getString(R.string.assessment_deleted), Toast.LENGTH_LONG).show();

    }


}
