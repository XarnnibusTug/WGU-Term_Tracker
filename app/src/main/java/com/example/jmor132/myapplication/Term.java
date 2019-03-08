package com.example.jmor132.myapplication;

import android.content.ContentValues;
import android.content.Context;


public class Term {

    public long termID;
    public String termName;
    public String termStart;
    public String termEnd;

    public void saveChanges(Context context){
        ContentValues values = new ContentValues();
        values.put(DBOpenHelper.TERM_NAME, termName);
        values.put(DBOpenHelper.TERM_START, termStart);
        values.put(DBOpenHelper.TERM_END, termEnd);
        context.getContentResolver().update(TermProvider.TERMS_URI, values, DBOpenHelper.TERMS_TABLE_ID + " = " + termID, null);

    }
}
