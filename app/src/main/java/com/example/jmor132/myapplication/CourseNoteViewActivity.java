package com.example.jmor132.myapplication;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.ShareActionProvider;
import android.widget.TextView;
import android.widget.Toast;

public class CourseNoteViewActivity extends AppCompatActivity {

    private static final int COURSE_NOTE_EDITOR_CODE = 11111;


    private long courseNoteID;
    private Uri courseNoteUri;
    private TextView courseNoteText;
    String courseNote;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_note_view);

        courseNoteText = findViewById(R.id.courseNoteText);
        courseNoteUri = getIntent().getParcelableExtra(CourseNotesProvider.COURSE_NOTE_CONTENT_TYPE);

        if(courseNoteUri != null){
            courseNoteID = Long.parseLong(courseNoteUri.getLastPathSegment());
            setTitle(getString(R.string.course_note_title));
            generateNote();
        }
    }

    private void generateNote(){
        CourseNote courseNote = CourseNoteDataManager.getCourseNote(this, courseNoteID);
        courseNoteText.setText(courseNote.text);
        courseNoteText.setMovementMethod(new ScrollingMovementMethod());
    }

    public void editNote(View view){
        Intent intent = new Intent(this, CourseNoteEdit.class);
        intent.putExtra(CourseNotesProvider.COURSE_NOTE_CONTENT_TYPE, courseNoteUri);
        startActivityForResult(intent, COURSE_NOTE_EDITOR_CODE);

    }

    public void deleteNote(View view){
        CourseNoteDataManager.deleteCourseNote(CourseNoteViewActivity.this, courseNoteID);
        setResult(RESULT_OK);
        finish();
        Toast.makeText(CourseNoteViewActivity.this, getString(R.string.courseNote_deleted), Toast.LENGTH_LONG).show();
    }

    public void shareNote(View view){
        courseNote = courseNoteText.getText().toString();

        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_SUBJECT, courseNote);
        sendIntent.setType("text/plain");
        startActivity(sendIntent);
    }
}
