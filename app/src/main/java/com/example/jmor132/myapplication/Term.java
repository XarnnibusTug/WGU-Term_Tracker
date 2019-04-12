package com.example.jmor132.myapplication;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;


public class Term {

    public long termID;
    public String termName;
    public String termStart;
    public String termEnd;
    public int active;

    public void saveChanges(Context context){
        ContentValues values = new ContentValues();
        values.put(DBOpenHelper.TERM_NAME, termName);
        values.put(DBOpenHelper.TERM_START, termStart);
        values.put(DBOpenHelper.TERM_END, termEnd);
        values.put(DBOpenHelper.TERM_ACTIVE, active);
        context.getContentResolver().update(TermProvider.TERMS_URI, values, DBOpenHelper.TERMS_TABLE_ID + " = " + termID, null);

    }

    public long getClassCount(Context context){
        Cursor cursor = context.getContentResolver().query(CourseProvider.COURSES_URI, DBOpenHelper.COURSES_COLUMNS,
                                            DBOpenHelper.COURSE_TERM_ID + "=" + this.termID, null, null);
        int numRows = cursor.getCount();
        return numRows;
    }

    public void activateTerm(Context context){
        this.active = 1;
        saveChanges(context);
    }

    public void deactivateTerm(Context context){
        this.active = 0;
        saveChanges(context);
    }
}
