package com.example.jmor132.myapplication;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;


public class CourseNotesProvider  extends ContentProvider {

    //AUTHORITY and PATH
    private static final String AUTHORITY = "com.example.jmor123.myapplication.CourseNotesProvider";
    private static final String COURSE_NOTE_PATH = "courseNotes";

    //Path
    public static final Uri COURSE_NOTES_URI = Uri.parse("content://" + AUTHORITY + "/" + COURSE_NOTE_PATH);

    private static final int COURSE_NOTES = 5;
    private static final int COURSE_NOTES_ID = 6;

    private static final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    static {
        uriMatcher.addURI(AUTHORITY, COURSE_NOTE_PATH, COURSE_NOTES);
        uriMatcher.addURI(AUTHORITY,COURSE_NOTE_PATH + "/#", COURSE_NOTES_ID);
    }

    public static final String COURSE_NOTE_CONTENT_TYPE = "courseNote";

    private SQLiteDatabase database;


    @Override
    public boolean onCreate() {
        DBOpenHelper helper = new DBOpenHelper(getContext());
        database = helper.getReadableDatabase();
        return true;
    }



    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        switch(uriMatcher.match(uri)) {
            case COURSE_NOTES:
                return database.query(DBOpenHelper.TABLE_COURSE_NOTES, DBOpenHelper.COURSE_NOTES_COLUMNS, selection,
                        null, null, null, DBOpenHelper.COURSE_NOTES_TABLE_ID + " ASC");
            default:
                throw new IllegalArgumentException("Unsupported URI: " + uri);
        }
    }


    @Override
    public String getType(Uri uri) {
        return null;
    }


    @Override
    public Uri insert(Uri uri, ContentValues values) {
        long id;
        switch(uriMatcher.match(uri)){
            case COURSE_NOTES:
                id = database.insert(DBOpenHelper.TABLE_COURSE_NOTES, null, values);
                return uri.parse(COURSE_NOTE_PATH + "/" + id);
            default:
                throw new IllegalArgumentException("Unsupported URI: " + uri);
        }

    }

    @Override
    public int delete( Uri uri, String selection, String[] selectionArgs) {
        switch (uriMatcher.match(uri)){
            case COURSE_NOTES:
                return database.delete(DBOpenHelper.TABLE_COURSE_NOTES, selection, selectionArgs);
                default:
                    throw new IllegalArgumentException("Unsupported URI: " + uri);
        }
    }

    @Override
    public int update(Uri uri, ContentValues values,  String selection,  String[] selectionArgs) {
        switch (uriMatcher.match(uri)) {
            case COURSE_NOTES:
                return database.update(DBOpenHelper.TABLE_COURSE_NOTES, values, selection, selectionArgs);
            default:
                throw new IllegalArgumentException("Unsupported URI: " + uri);
        }
    }
}
