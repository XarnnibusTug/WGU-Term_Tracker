package com.example.jmor132.myapplication;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

public class CourseNoteDataManager {

    public static Uri insertCourseNote(Context context, long courseID, String text){
        ContentValues values = new ContentValues();
        values.put(DBOpenHelper.COURSE_NOTE_COURSE_ID, courseID);
        values.put(DBOpenHelper.COURSE_NOTE_TEXT, text);
        Uri courseNoteUri = context.getContentResolver().insert(CourseNotesProvider.COURSE_NOTES_URI, values);
        return courseNoteUri;
    }

    public static CourseNote getCourseNote(Context context, long courseNoteID){
        Cursor cursor = context.getContentResolver().query(CourseNotesProvider.COURSE_NOTES_URI, DBOpenHelper.COURSE_NOTES_COLUMNS,
                                                            DBOpenHelper.COURSE_NOTES_TABLE_ID + " = " + courseNoteID, null, null);

        cursor.moveToFirst();
        long courseID = cursor.getLong(cursor.getColumnIndex(DBOpenHelper.COURSE_NOTE_COURSE_ID));
        String text = cursor.getString(cursor.getColumnIndex(DBOpenHelper.COURSE_NOTE_TEXT));

        CourseNote courseNote = new CourseNote();
        courseNote.courseNoteID = courseNoteID;
        courseNote.courseID = courseID;
        courseNote.text = text;
        return courseNote;

    }

    public static boolean deleteCourseNote(Context context, long courseNoteID){
        context.getContentResolver().delete(CourseNotesProvider.COURSE_NOTES_URI, DBOpenHelper.COURSE_NOTES_TABLE_ID + "=" + courseNoteID, null);
        return true;
    }

}
