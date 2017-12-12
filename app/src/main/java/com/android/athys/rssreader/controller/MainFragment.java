package com.android.athys.rssreader.controller;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.android.athys.rssreader.R;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MainFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MainFragment extends Fragment {

    public interface OnListRefreshListener {
        void onListRefresh();
    }


    private RSSAdapter mAdapter;
    private List<XMLAsyncTask> mTasks;
    private List<String> mRSSUrls;
    private int mTaskNotifyCount;
    private Menu mMenu;
    private OnListRefreshListener mOnListRefreshListener;

    public MainFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     * @return A new instance of fragment MainFragment.
     */
    public static MainFragment newInstance() {
        return new MainFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_main, container, false);
    }

    @Override
    public void onViewCreated(final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        final RecyclerView recyclerView = view.findViewById(R.id.newsRSSList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mAdapter = new RSSAdapter((RSSAdapter.URLLoader) getActivity());
        recyclerView.setAdapter(mAdapter);

        mRSSUrls = new ArrayList<>();
        mRSSUrls.add("http://www.lemonde.fr/rss/une.xml");
        mRSSUrls.add("http://www.lemonde.fr/m-actu/rss_full.xml");
        mRSSUrls.add("http://www.lemonde.fr/afrique/rss_full.xml");
        mRSSUrls.add("http://www.lemonde.fr/culture/rss_full.xml");

        mTasks = new ArrayList<>();
        executeTasks();

        // Hide the progress bar and display the menu items when all task have ended
        mAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onChanged() {
                if (++mTaskNotifyCount == mTasks.size()) {
                    view.findViewById(R.id.progress).setVisibility(View.GONE);
                    mMenu.setGroupEnabled(R.id.rss_item_list_group, true);
                    mMenu.setGroupVisible(R.id.rss_item_list_group, true);
                }
            }
        });
    }

    //=======================================================================================
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mOnListRefreshListener = (OnListRefreshListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement OnListRefreshListener");
        }

    }

    //=======================================================================================
    @Override
    public void onDestroy() {
        super.onDestroy();
        for (XMLAsyncTask task : mTasks) {
            if (task != null)
                task.cancel(true);
        }
    }

    //=======================================================================================
    private void executeTasks() {
        mTasks.clear();
        mTaskNotifyCount = 0;
        for (String rSSUrl : mRSSUrls) {
            XMLAsyncTask task = new XMLAsyncTask(mAdapter);
            mTasks.add(task);
            task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, rSSUrl);
        }
    }

    //=======================================================================================
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        mMenu = menu;
    }

    //=======================================================================================
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_sort:
            /* Sort by descending date */
                mAdapter.sortItemListByDate();
                return true;

            case R.id.action_refresh:
            /* Reload list */
               mOnListRefreshListener.onListRefresh();
                getActivity().setTitle(R.string.app_name);
                getView().findViewById(R.id.progress).setVisibility(View.VISIBLE);
                mMenu.setGroupEnabled(R.id.rss_item_list_group, false);
                mMenu.setGroupVisible(R.id.rss_item_list_group, false);
                mAdapter.emptyItemList();
                executeTasks();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
