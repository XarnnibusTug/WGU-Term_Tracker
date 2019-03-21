package com.example.jmor132.myapplication;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;


public class AssessmentProvider extends ContentProvider {

    //AUTHORITY and PATH
    private static final String AUTHORITY = "com.example.jmor123.myapplication.AssessmentProvider";
    private static final String ASSESSMENT_PATH = "assessment";

    //Course Path URI
    public static final Uri ASSESSMENT_URI = Uri.parse("content://" + AUTHORITY + "/" + ASSESSMENT_PATH);

    //Constants
    private static final int ASSESSMENT = 7;
    private static final int ASSESSMENT_ID = 8;

    private static final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    static {
        uriMatcher.addURI(AUTHORITY, ASSESSMENT_PATH, ASSESSMENT);
        uriMatcher.addURI(AUTHORITY,ASSESSMENT_PATH + "/#", ASSESSMENT_ID);
    }

    private SQLiteDatabase database;

    @Override
    public boolean onCreate() {
        DBOpenHelper helper = new DBOpenHelper(getContext());
        database = helper.getWritableDatabase();
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,  String[] selectionArgs, String sortOrder) {
        switch(uriMatcher.match(uri)){
            case ASSESSMENT:
                return database.query(DBOpenHelper.TABLE_ASSESSMENTS, DBOpenHelper.ASSESSMENT_COLUMNS, selection, null, null, null, DBOpenHelper.ASSESSMENT_TABLE_ID + " ASC");
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
        switch(uriMatcher.match(uri)) {
            case ASSESSMENT:
                id = database.insert(DBOpenHelper.TABLE_ASSESSMENTS, null, values);
                return uri.parse(ASSESSMENT_PATH + "/" + id);
            default:
                throw new IllegalArgumentException("Unsupported URI: " + uri);
        }
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        switch(uriMatcher.match(uri)){
            case ASSESSMENT:
                return database.delete(DBOpenHelper.TABLE_ASSESSMENTS, selection, selectionArgs);
                default:
                    throw new IllegalArgumentException("Unsupported URI: " + uri);
        }
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;
    }
}
