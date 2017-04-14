package com.wordpress.juniadev.newsapp;

/**
 * Represents one item on the news feed.
 */
public class News {

    private final String title;
    private final String section;
    private final String date;
    private final String url;

    public News(String title, String section, String date, String url) {
        this.title = title;
        this.section = section;
        this.date = date;
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public String getSection() {
        return section;
    }

    public String getDate() {
        return date;
    }

    public String getUrl() {
        return url;
    }
}
