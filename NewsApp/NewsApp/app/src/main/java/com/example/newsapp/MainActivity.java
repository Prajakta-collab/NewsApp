package com.example.newsapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<NewsItem>>{

    ListView listView;
    TextView loadingtxt;
    ProgressBar progressBar;
    private static final int NEWS_LOADER = 1;
    private static final String QUERY_URL = "https://content.guardianapis.com/search?";
    private static NewsAdapter newsAdapter;
    NetworkInfo networkInfo;
    public static final String TAG = MainActivity.class.getSimpleName();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listView=findViewById(R.id.listview);
        loadingtxt=findViewById(R.id.loadingtxt);
        progressBar=findViewById(R.id.progress);

        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        networkInfo = connMgr.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()) {
            loadingtxt.setText(getString(R.string.message_fetching));
            LoaderManager.getInstance(this).initLoader(NEWS_LOADER,null,this);
            newsAdapter = new NewsAdapter(this, new ArrayList<NewsItem>());
            listView.setAdapter(newsAdapter);

        } else {
            progressBar.setVisibility(View.GONE);
            loadingtxt.setText(getString(R.string.message_no_internet));
        }
    }

    @NonNull
    @Override
    public Loader<List<NewsItem>> onCreateLoader(int id, @Nullable Bundle args) {
        Uri baseIri = Uri.parse(QUERY_URL);
        Uri.Builder uriBuilder = baseIri.buildUpon();
        uriBuilder.appendQueryParameter("api-key", "test");
        uriBuilder.appendQueryParameter("show-tags","contributor");
        Log.v(TAG, "Uri: " + uriBuilder);
        return new NewsLoader(this, uriBuilder.toString());
    }

    @Override
    public void onLoadFinished(@NonNull Loader<List<NewsItem>> loader, List<NewsItem> data) {
        if (data != null && !data.isEmpty()) {
            newsAdapter.addAll(data);
            progressBar.setVisibility(View.GONE);
            loadingtxt.setText("");

        } else {
            loadingtxt.setText(getString(R.string.message_no_news));
        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader<List<NewsItem>> loader) {
        newsAdapter.clear();
    }

}
