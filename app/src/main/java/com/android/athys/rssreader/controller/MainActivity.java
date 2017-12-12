package com.android.athys.rssreader.controller;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;

import com.android.athys.rssreader.R;
import com.android.athys.rssreader.model.RSSItem;

public class MainActivity extends AppCompatActivity implements RSSAdapter.URLLoader, MainFragment.OnListRefreshListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // To ovoid overlaying after rotate
        if (savedInstanceState != null) return;

        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.listFragment, MainFragment.newInstance())
                .commit()
        ;
    }

    //=======================================================================================
    @Override
    public void load(RSSItem item) {
        if (findViewById(R.id.rssItemFragment) != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.rssItemFragment, RSSItemFragment.newInstance(item))
                    .addToBackStack(null)
                    .commit();
        }
        else {
            Intent intent = new Intent(this, RSSItemContent.class);
            intent.putExtra(RSSItemContent.ITEM, item);
            startActivity(intent);
        }
    }

    //=======================================================================================
    public void onBackPressed() {
        if(getSupportFragmentManager().getBackStackEntryCount() > 1)
            getSupportFragmentManager().popBackStack();
        // To keep the first one on screen
        //else
          //  super.onBackPressed();
    }

    //=======================================================================================
    public void onListRefresh() {
        // Empty the back stack
        while(getSupportFragmentManager().getBackStackEntryCount() > 0){
            getSupportFragmentManager().popBackStackImmediate();
        }
    }

    //=======================================================================================
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.item_list_menu, menu);
        return true;
    }
}
