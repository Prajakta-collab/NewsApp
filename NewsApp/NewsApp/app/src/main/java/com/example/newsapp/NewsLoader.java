package com.example.newsapp;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.loader.content.AsyncTaskLoader;

import java.util.List;

public class NewsLoader extends AsyncTaskLoader<List<NewsItem>> {

    private String queryUrl;

    public NewsLoader(@NonNull Context context, String url) {
        super(context);
        queryUrl=url;
    }

    @Nullable
    @Override
    public List<NewsItem> loadInBackground() {
        if (queryUrl == null) {
            return null;
        }
        List<NewsItem> news = NetworkUtils.fetchNewsData(queryUrl, getContext());
        return news;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }
}
