package com.example.jmor132.myapplication;

import android.app.LoaderManager;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

public class CourseListActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final int COURSE_VIEWER_ACTIVITY_CODE = 11111;
    private static final int COURSE_EDITIOR_ACTIVITY_CODE = 22222;

    private long termID;
    private Uri termUri;
    private Term term;

    private MySimpleCursorAdapter cursorAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_list);

        Intent intent = getIntent();
        termUri = intent.getParcelableExtra(TermProvider.TERM_CONTENT_TYPE);
        loadTermData();
        generateClassList();
        getLoaderManager().initLoader(0,null,this);
    }

    private void loadTermData(){
        if(termUri == null){
            setResult(RESULT_CANCELED);
            finish();
        }
        else{
            termID = Long.parseLong(termUri.getLastPathSegment());
            term = TermDataManager.getTerm(this, termID);
            setTitle(getString(R.string.courses));
        }
    }

    public void  newCourseEdit(View view){
        Intent intent = new Intent(this, CourseEditor.class);
        intent.putExtra(TermProvider.TERM_CONTENT_TYPE, termUri);
        startActivityForResult(intent, COURSE_EDITIOR_ACTIVITY_CODE);

    }

    private void generateClassList(){
        String[] from ={DBOpenHelper.COURSE_NAME, DBOpenHelper.COURSE_START, DBOpenHelper.COURSE_END, DBOpenHelper.COURSE_STATUS};
        int[] to = {R.id.textViewCourseName, R.id.tvCourseStartDate, R.id.tvCourseEndDate, R.id.tvCourseStatus};

        cursorAdapter = new MySimpleCursorAdapter(this, R.layout.course_list_item, null, from, to);
        CourseProvider database = new CourseProvider();

        ListView list = findViewById(android.R.id.list);
        list.setAdapter(cursorAdapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(CourseListActivity.this, CourseViewActivity.class);
                Uri uri = Uri.parse(CourseProvider.COURSES_URI + "/" + id);
                intent.putExtra(CourseProvider.COURSE_CONTENT_TYPE, uri);
                startActivityForResult(intent, COURSE_VIEWER_ACTIVITY_CODE);
            }
        });
    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(this, CourseProvider.COURSES_URI, DBOpenHelper.COURSES_COLUMNS, DBOpenHelper.COURSE_TERM_ID + " = " + this.termID, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        cursorAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        cursorAdapter.swapCursor(null);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        loadTermData();
        restartLoader();
    }

    private void restartLoader(){
        getLoaderManager().restartLoader(0,null,this);
    }

    // Custom Cursor adapter to display Course Progress status.
    public class MySimpleCursorAdapter extends android.support.v4.widget.SimpleCursorAdapter {

        public MySimpleCursorAdapter(Context context, int layout, Cursor cursor, String[] from, int[] to){
            super(context, layout, cursor, from, to);
        }

        @Override
        public void setViewText(TextView view, String text){
            if(view.getId() == R.id.tvCourseStatus){
                String status = "";
                switch(text){
                    case "PLANNED":
                        status = "Plan To take";
                        break;
                    case "IN_PROGRESS":
                        status = "In Progress";
                        break;
                    case "COMPLETED":
                        status = "Completed";
                        break;
                    case "DROPPED":
                        status = "Dropped";
                        break;
                }
                view.setText("Status: " + status);
            }
            else{
                view.setText(text);
            }

        }

    }
}
