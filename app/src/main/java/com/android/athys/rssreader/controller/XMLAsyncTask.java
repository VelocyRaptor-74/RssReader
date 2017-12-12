package com.android.athys.rssreader.controller;

import android.os.AsyncTask;
import android.util.Log;
import com.android.athys.rssreader.model.RSSItem;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import java.io.InputStream;
import java.io.InterruptedIOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.xml.parsers.DocumentBuilderFactory;


/**
 * Created by Athys on 30/11/2017.
 */

public class XMLAsyncTask extends AsyncTask<String, Void, List<RSSItem>> {

    interface ItemListConsumer {
       void addItemList(List<RSSItem> items);
    }

    private ItemListConsumer mItemListConsumer;

    public XMLAsyncTask(ItemListConsumer itemListConsumer) {
        mItemListConsumer = itemListConsumer;
    }

    @Override
    protected List<RSSItem> doInBackground(String... params) {

        //Log.i("XMLAsyncTask", "doInBackground ..."  + params[0]);
        try {
            URL url = new URL(params[0]);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            InputStream stream = connection.getInputStream();
            try {
                return parseRSSFeed(DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(stream));
            }
            catch (Exception except) {
                Log.e("XMLAsyncTask", "Exception while building document", except);
            }
            finally {
                stream.close();
            }
        }
        catch (InterruptedIOException except) {
            Log.e("XMLAsyncTask", "Interrupted");
        }
        catch (Exception except) {
            Log.e("XMLAsyncTask", "Exception while streaming URL", except);
        }

        return null;
    }

    @Override
    protected void onPostExecute(List<RSSItem> items) {
        mItemListConsumer.addItemList(items);
    }

    private List<RSSItem> parseRSSFeed(Document document)
    {
        // Get channel title
        Element element = (Element) document.getElementsByTagName("channel").item(0);
        String channel = element.getElementsByTagName("title").item(0).getTextContent();

        int itemCount = document.getElementsByTagName("item").getLength();
        RSSItem item;
        List<RSSItem> items = new ArrayList<>();
        DateFormat formatter = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss zzz", Locale.US);

        // Create an instance of RSSItem for each item of the document and add it to the list returned
        for(int i=0; i<itemCount; i++){
            element = (Element) document.getElementsByTagName("item").item(i);
            item = new RSSItem();
            item.setTitle(element.getElementsByTagName("title").item(0).getTextContent());
            item.setDescription(element.getElementsByTagName("description").item(0).getTextContent());
            item.setChannel(channel);
            try {
                item.setLink(new URL(element.getElementsByTagName("link").item(0).getTextContent()));
            }
            catch (Exception except) {
                Log.e("XMLAsyncTask", "Exception while parsing item link", except);
            }
            try {
                item.setPubDate(formatter.parse(element.getElementsByTagName("pubDate").item(0).getTextContent()));
            }
            catch (Exception except) {
                Log.e("XMLAsyncTask", "Exception while parsing item pubDate", except);
            }

            items.add(item);
        }
   //     try { Thread.sleep(3000); }
   //     catch (Exception except) {Log.e("XMLAsyncTask", "Exception while parsing RSSFeed", except); }

        return items;
    }
}
