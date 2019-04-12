package com.example.jmor132.myapplication;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

public class TermDataManager {

    public static Uri insertTerm(Context context, String termName, String termStart, String termEnd){
        ContentValues values = new ContentValues();
        values.put(DBOpenHelper.TERM_NAME, termName);
        values.put(DBOpenHelper.TERM_START, termStart);
        values.put(DBOpenHelper.TERM_END, termEnd);
        Uri termUri = context.getContentResolver().insert(TermProvider.TERMS_URI,values);

        return termUri;

    }

    public static Term getTerm(Context context, long termID){
        Cursor cursor = context.getContentResolver().query(TermProvider.TERMS_URI, DBOpenHelper.TERMS_COLUMNS,
                                                            DBOpenHelper.TERMS_TABLE_ID + "=" + termID,
                                                            null, null);

        cursor.moveToFirst();
        String termName = cursor.getString(cursor.getColumnIndex(DBOpenHelper.TERM_NAME));
        String termStartDate = cursor.getString(cursor.getColumnIndex(DBOpenHelper.TERM_START));
        String termEndDate = cursor.getString(cursor.getColumnIndex(DBOpenHelper.TERM_END));
        int termActive = cursor.getInt(cursor.getColumnIndex(DBOpenHelper.TERM_ACTIVE));

        Term term = new Term();
        term.termID = termID;
        term.termName = termName;
        term.termStart = termStartDate;
        term.termEnd = termEndDate;
        term.active = termActive;

        return term;
    }
}
