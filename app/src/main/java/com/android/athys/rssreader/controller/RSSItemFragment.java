package com.android.athys.rssreader.controller;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.android.athys.rssreader.R;
import com.android.athys.rssreader.model.RSSItem;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RSSItemFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RSSItemFragment extends Fragment {

    private static final String ARG_LINK = "link";
    private static final String ARG_TITLE = "title";

    // Empty public constructor required
    public RSSItemFragment() { }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param "RSSItem"
     * @return A new instance of fragment RSSItemFragment.
     */
    public static RSSItemFragment newInstance(RSSItem item) {
        RSSItemFragment fragment = new RSSItemFragment();
        Bundle args = new Bundle();
        args.putString(ARG_LINK, item.getLink().toString());
        args.putString(ARG_TITLE, item.getTitle());
        fragment.setArguments(args);
        return fragment;
    }

    //=======================================================================================
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        getActivity().setTitle(getArguments().getString(ARG_TITLE));
    }

    //=======================================================================================
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        WebView webView = new WebView(getActivity());
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return false;
            }
        });
        webView.loadUrl(getArguments().getString(ARG_LINK));
        return webView;
    }

    //=======================================================================================
    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        menu.setGroupEnabled(R.id.rss_item_list_group, true);
        menu.setGroupVisible(R.id.rss_item_list_group, true);
        menu.setGroupEnabled(R.id.rss_item_group, true);
        menu.setGroupVisible(R.id.rss_item_group, true);
    }

    //=======================================================================================
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_share:
            /* Share the item link  */
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_TEXT, getArguments().getString(ARG_LINK));
                startActivity(Intent.createChooser(intent, "Share!"));
                return true;

            case R.id.action_open:
            /* Open the item link  */
                intent = new Intent(Intent.ACTION_VIEW, Uri.parse(getArguments().getString(ARG_LINK)));
                startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }
}
