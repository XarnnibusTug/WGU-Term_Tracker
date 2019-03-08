package com.example.jmor132.myapplication;

import android.content.ContentValues;
import android.content.Context;

public class CourseNote {
    public long courseNoteID;
    public long courseID;
    public String text;


    public void saveChanges(Context context){
        ContentValues values = new ContentValues();
        values.put(DBOpenHelper.COURSE_NOTE_COURSE_ID, courseID);
        values.put(DBOpenHelper.COURSE_NOTE_TEXT, text);
        context.getContentResolver().update(CourseNotesProvider.COURSE_NOTES_URI, values, DBOpenHelper.COURSE_NOTES_TABLE_ID +
                                            " = " + courseNoteID, null);

    }
}
