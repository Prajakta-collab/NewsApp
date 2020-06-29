package com.example.newsapp;

public class NewsItem {
    private String Title;
    private String SectionName;
    private String WebUrl;
    private String PublishDate;
    private String Author;

    public String getTitle() {
        return Title;
    }

    public String getSectionName() {
        return SectionName;
    }

    public String getWebUrl() {
        return WebUrl;
    }

    public String getPublishDate() {
        return PublishDate;
    }

    public String getAuthor() {
        return Author;
    }

    public NewsItem(String title, String sectionName, String webUrl, String publishDate, String mAuthor) {
        Title = title;
        SectionName = sectionName;
        WebUrl = webUrl;
        PublishDate = publishDate;
        Author = mAuthor;
    }

}
