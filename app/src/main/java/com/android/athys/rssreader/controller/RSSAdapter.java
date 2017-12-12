package com.android.athys.rssreader.controller;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.athys.rssreader.R;
import com.android.athys.rssreader.model.PubDateComparator;
import com.android.athys.rssreader.model.RSSItem;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Athys on 30/11/2017.
 */

public class RSSAdapter extends RecyclerView.Adapter<RSSAdapter.ArticleViewHolder> implements XMLAsyncTask.ItemListConsumer{

    public interface URLLoader {
        void load(RSSItem item);
    }

    private List<RSSItem> mItems = new ArrayList<>();
    private final URLLoader mURLLoader;

    public RSSAdapter(URLLoader urlLoader) {
        mURLLoader = urlLoader;
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    @Override
    public ArticleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.article_list_item, parent, false);
        return new ArticleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ArticleViewHolder holder, int position) {
        holder.setItem(mItems.get(position));
    }

    @Override
    public void addItemList(List<RSSItem> items) {
        mItems.addAll(items);
        notifyDataSetChanged();
    }

    public void emptyItemList() {
        mItems.clear();
        notifyDataSetChanged();
    }

    public void sortItemListByDate() {
        Collections.sort(mItems, new PubDateComparator());
        Collections.reverse(mItems);
        notifyDataSetChanged();
    }

//=================================================================================
    protected class ArticleViewHolder extends RecyclerView.ViewHolder {
        private final TextView mTitle;
        private final TextView mChannel;
        private final TextView mPubDate;
        private RSSItem mCurrentItem;

        ArticleViewHolder(final View itemView) {
            super(itemView);
            mTitle = itemView.findViewById(R.id.article_title);
            mChannel = itemView.findViewById(R.id.article_channel);
            mPubDate = itemView.findViewById(R.id.article_date);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mURLLoader.load(mCurrentItem);
                }
            });
        }

        void setItem(RSSItem item){
            mCurrentItem = item;
            mTitle.setText(mCurrentItem.getTitle());
            mChannel.setText(mCurrentItem.getChannel());
            mPubDate.setText(DateFormat.getDateTimeInstance().format(mCurrentItem.getPubDate()));
        }
    }
}
