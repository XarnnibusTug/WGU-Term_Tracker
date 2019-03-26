package com.example.jmor132.myapplication;

import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

public class AssessmentListActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final int ASSESSMENT_VIEWER_ACTIVITY_CODE = 11111;
    private static final int ASSESSMENT_EDITOR_ACTIVITY_CODE = 22222;

    private CursorAdapter cursorAdapter;
    private long courseID;
    private Uri courseUri;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assessment_list);

        courseUri = getIntent().getParcelableExtra(CourseProvider.COURSE_CONTENT_TYPE);
        courseID = Long.parseLong((courseUri.getLastPathSegment()));
        generateAssessments();
        getLoaderManager().initLoader(0,null,this);
    }

    protected void generateAssessments(){
        String[] from ={DBOpenHelper.ASSESSMENT_CODE, DBOpenHelper.ASSESSMENT_NAME, DBOpenHelper.ASSESSMENT_DATETIME};
        int[] to = {R.id.codeTextView, R.id.nameTextView, R.id.dateTimeView };
        cursorAdapter = new SimpleCursorAdapter(this, R.layout.assessment_list, null, from, to, 0);
        AssessmentProvider database = new AssessmentProvider();

        ListView lists = findViewById(R.id.assessmentListView);
        lists.setAdapter(cursorAdapter);
        lists.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(AssessmentListActivity.this, AssessmentViewActivity.class);
                Uri uri = Uri.parse(AssessmentProvider.ASSESSMENT_URI + "/" + id);
                intent.putExtra(AssessmentProvider.ASSESSMENT_CONTENT_TYPE, uri);
                startActivityForResult(intent, ASSESSMENT_VIEWER_ACTIVITY_CODE);
            }
        });
    }


    public void addAssessment(View view){
        Intent intent = new Intent(AssessmentListActivity.this, AssessmentEdit.class);
        intent.putExtra(CourseProvider.COURSE_CONTENT_TYPE, courseUri);
        startActivityForResult(intent, ASSESSMENT_EDITOR_ACTIVITY_CODE);
    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(this, AssessmentProvider.ASSESSMENT_URI, DBOpenHelper.ASSESSMENT_COLUMNS,
                                DBOpenHelper.ASSESSMENT_COURSE_ID + " = " + this.courseID, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        cursorAdapter.swapCursor(data);
    }


    @Override
    public void onLoaderReset(Loader loader) {
        cursorAdapter.swapCursor(null);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        restartLoader();
    }

    private void restartLoader(){
        getLoaderManager().restartLoader(0,null,this);
    }
}
