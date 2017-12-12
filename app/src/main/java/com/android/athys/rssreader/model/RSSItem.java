package com.android.athys.rssreader.model;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;

/**
 * Created by Athys on 01/12/2017.
 */

public class RSSItem implements Parcelable {
    private String title;
    private String description;
    private URL link;
    private Date pubDate;
    private String channel;

    public void setTitle(String title) {
        this.title = title;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public void setLink(URL link) {
        this.link = link;
    }
    public void setPubDate(Date pubDate) {
        this.pubDate = pubDate;
    }
    public void setChannel(String channel) {
        this.channel = channel;
    }

    public String getTitle() {
        return title;
    }
    public String getDescription() {
        return description;
    }
    public URL getLink() {
        return link;
    }
    public Date getPubDate() {
        return pubDate;
    }
    public String getChannel() {
        return channel;
    }

    //=======================================================================================
    public RSSItem () {}

    public RSSItem(Parcel parcel) {
        setTitle(parcel.readString());
        setDescription(parcel.readString());
        try {
            setLink(new URL(parcel.readString()));
        }
        catch (MalformedURLException except) {
            Log.e("RSSItem", "Exception while reading parcel link", except);
        }
        long tmpDate = parcel.readLong();
        setPubDate(tmpDate == -1 ? null : new Date(tmpDate));
        setChannel(parcel.readString());
    }

    //=======================================================================================
    @Override
    public int describeContents() {
        return 0;
    }

    //=======================================================================================
    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeString(getTitle());
        parcel.writeString(getDescription());
        parcel.writeString(getLink().toString());
        parcel.writeLong(getPubDate() != null ? getPubDate().getTime() : -1);
        parcel.writeString(getChannel());
    }

    //=======================================================================================
    public static final Parcelable.Creator<RSSItem> CREATOR = new Parcelable.Creator<RSSItem>() {
        public RSSItem createFromParcel(Parcel parcel) {
            return new RSSItem(parcel);
        }

        public RSSItem[] newArray(int size) {
            return new RSSItem[size];
        }
    };

}
