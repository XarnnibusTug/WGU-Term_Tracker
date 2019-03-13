package com.example.jmor132.myapplication;

import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import java.net.Inet4Address;

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
        cursorAdapater = new SimpleCursorAdapter(this, R.layout.course_note_list, null, from, to, 0);
        CourseNotesProvider database = new CourseNotesProvider();

        ListView list = findViewById(R.id.courseNoteList);
        list.setAdapter(cursorAdapater);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent =  new Intent(CourseNoteListActivity.this, CourseNoteViewActivity.class);
                Uri uri = Uri.parse(CourseNotesProvider.COURSE_NOTES_URI + "/" + id);
                intent.putExtra(CourseNotesProvider.COURSE_NOTE_CONTENT_TYPE, uri);
                startActivityForResult(intent, COURSE_NOTE_VIEW_ACTIVITY_CODE);
            }
        });
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(this, CourseNotesProvider.COURSE_NOTES_URI, DBOpenHelper.COURSE_NOTES_COLUMNS,
                                 DBOpenHelper.COURSE_NOTE_COURSE_ID + " = " + this.courseID, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        cursorAdapater.swapCursor(data);
    }


    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        cursorAdapater.swapCursor(null);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        restartLoader();
    }

    private void restartLoader(){
        getLoaderManager().restartLoader(0,null,this);
    }

    public void newNote(View view){
        Intent intent = new Intent(CourseNoteListActivity.this, CourseNoteEdit.class);
        intent.putExtra(CourseProvider.COURSE_CONTENT_TYPE, courseUri);
        startActivityForResult(intent,COURSE_NOTE_EDIT_ACTIVITY_CODE);
    }
}
