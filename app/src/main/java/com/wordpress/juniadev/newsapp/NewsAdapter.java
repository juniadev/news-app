package com.wordpress.juniadev.newsapp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

public class NewsAdapter extends ArrayAdapter<News> {

    public NewsAdapter(@NonNull Context context, @NonNull List<News> objects) {
        super(context, 0, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItemView = convertView;
        if(listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.news_list_item, parent, false);
        }

        News news = getItem(position);
        setTextViewData(listItemView, R.id.title, news.getTitle());
        setTextViewData(listItemView, R.id.section, news.getSection());
        setTextViewData(listItemView, R.id.date, formatDate(news.getDate()));
        return listItemView;
    }

    @NonNull
    private void setTextViewData(View listItemView, int viewId, String data) {
        TextView textView = (TextView) listItemView.findViewById(viewId);
        textView.setText(data);
    }

    /**
     * Convert date to display on the app.
     * Source: http://stackoverflow.com/a/6637487
     * @param newsDate Date in the format 2017-03-29T14:13:57Z
     * @return Date in the format mm/dd/yyyy
     */
    private String formatDate(String newsDate) {
        final DateFormat fromFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        fromFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
        final DateFormat toFormat = new SimpleDateFormat("MM/dd/yyyy");
        try {
            final Date originalDate = fromFormat.parse(newsDate);
            return toFormat.format(originalDate);
        } catch (ParseException e) {
            return "";
        }
    }
}
