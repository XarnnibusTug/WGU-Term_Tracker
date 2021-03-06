package com.example.jmor132.myapplication;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class CourseNoteEdit extends AppCompatActivity {

    private long courseID;
    private Uri courseUri;
    private long courseNoteID;
    private Uri courseNoteUri;
    private CourseNote courseNote;
    private EditText noteText;
    private String action;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_note_edit);
        noteText = findViewById(R.id.courseNoteEdit);
        courseNoteUri = getIntent().getParcelableExtra(CourseNotesProvider.COURSE_NOTE_CONTENT_TYPE);


        if(courseNoteUri == null){
            setTitle(getString(R.string.add_new_note));
            courseUri = getIntent().getParcelableExtra(CourseProvider.COURSE_CONTENT_TYPE);
            courseID = Long.parseLong(courseUri.getLastPathSegment());
            action = Intent.ACTION_INSERT;
        }
        else {
            setTitle(getString(R.string.edit_course_note));
            courseNoteID = Long.parseLong(courseNoteUri.getLastPathSegment());
            courseNote = CourseNoteDataManager.getCourseNote(this, courseNoteID);
            courseID = courseNote.courseID;
            noteText.setText(courseNote.text);
            action = Intent.ACTION_EDIT;
            Toast.makeText(CourseNoteEdit.this, getString(R.string.courseNote_edit), Toast.LENGTH_LONG).show();
        }
    }

    public void saveCourseNote(View view){
        if(action == Intent.ACTION_INSERT){
            CourseNoteDataManager.insertCourseNote(this, courseID, noteText.getText().toString().trim());
            setResult(RESULT_OK);
            finish();
        }
        if(action == Intent.ACTION_EDIT){
            courseNote.text = noteText.getText().toString().trim();
            courseNote.saveChanges(this);
            setResult(RESULT_OK);
            finish();
        }
    }
}
