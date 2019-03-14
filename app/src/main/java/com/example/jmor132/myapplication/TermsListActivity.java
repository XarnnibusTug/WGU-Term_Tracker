package com.example.jmor132.myapplication;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

public class TermsListActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {


    public static final int TERM_EDITOR_ACTIVITY_CODE = 11111;
    public static final int TERM_VIEWER_ACTIVITY_CODE = 22222;

    private CursorAdapter cursorAdapter;
    private TermProvider database;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terms_list);

        generateTermsList();
        getLoaderManager().initLoader(0, null,  this);


    }

    public void generateTermsList(){

        String[] from = {DBOpenHelper.TERM_NAME, DBOpenHelper.TERM_START, DBOpenHelper.TERM_END};
        int[] to ={R.id.termName, R.id.termStart, R.id.termEnd};

        cursorAdapter = new SimpleCursorAdapter(this, R.layout.term_list_item, null, from , to, 0);
        database = new TermProvider();

        ListView termList = findViewById(android.R.id.list);
        termList.setAdapter(cursorAdapter);

        termList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(TermsListActivity.this, TermViewActivity.class);
                Uri uri = Uri.parse(TermProvider.TERMS_URI + "/" + id);
                intent.putExtra(TermProvider.TERM_CONTENT_TYPE, uri);
                startActivityForResult(intent, TERM_VIEWER_ACTIVITY_CODE);
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK){
            restartLoader();
        }
    }

    public void  newTermEditor(View view){
        Intent intent = new Intent(this, TermEditor.class);
        startActivityForResult(intent, TERM_EDITOR_ACTIVITY_CODE);

    }

    private void restartLoader(){
        getLoaderManager().restartLoader(0,null,this);
    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(this, TermProvider.TERMS_URI, null,null,null,null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        cursorAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        cursorAdapter.swapCursor(null);
    }
}
