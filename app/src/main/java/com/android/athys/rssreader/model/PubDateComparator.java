package com.android.athys.rssreader.model;

import java.util.Comparator;

/**
 * Created by Marius on 03/12/2017.
 */

public class PubDateComparator implements Comparator<RSSItem> {
    public int compare(RSSItem item1, RSSItem item2) {
        return item1.getPubDate().compareTo(item2.getPubDate());
    }
}
