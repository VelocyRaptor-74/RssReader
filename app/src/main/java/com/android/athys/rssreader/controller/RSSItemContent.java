package com.android.athys.rssreader.controller;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;

import com.android.athys.rssreader.R;
import com.android.athys.rssreader.model.RSSItem;

public class RSSItemContent extends AppCompatActivity {

    public static final String ITEM = "Item";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        RSSItem  rssItem = getIntent().getParcelableExtra(ITEM);
        getSupportFragmentManager()
                .beginTransaction()
                .add(android.R.id.content, RSSItemFragment.newInstance(rssItem))
                .commit()
        ;
    }

    //=======================================================================================
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.item_menu, menu);
        return true;
    }
}
