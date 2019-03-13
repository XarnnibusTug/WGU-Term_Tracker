package com.example.jmor132.myapplication;

import android.app.LoaderManager;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.CursorAdapter;

public class CourseNoteListActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>{

    private static final int COURSE_NOTE_EDIT_ACTIVITY_CODE = 11111;
    private static final int COURSE_NOTE_VIEW_ACTIVITY_CODE = 22222;

    private long courseID;
    private Uri courseUri;
    private CursorAdapter cursorAdapater;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_note_list);

        courseUri = getIntent().getParcelableExtra(CourseProvider.COURSE_CONTENT_TYPE);
        courseID = Long.parseLong(courseUri.getLastPathSegment());

    }


    private void generateCourseNoteList(){
        String[] from = {DBOpenHelper.COURSE_NOTE_TEXT};
        int[] to = {R.id.courseNoteText};
    }

    @Override
    public Loader onCreateLoader(int id, Bundle args) {
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

    }


    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}
