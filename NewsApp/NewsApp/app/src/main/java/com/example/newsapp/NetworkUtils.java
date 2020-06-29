package com.example.newsapp;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class NetworkUtils {

    public static final String LOG_TAG = NetworkUtils.class.getSimpleName();
    private static final String RESPONSE_KEY = "response";
    private static final String RESULTS_KEY = "results";
    private static final String SECTION_KEY = "sectionName";
    private static final String DATE_KEY = "webPublicationDate";
    private static final String TITLE_KEY = "webTitle";
    private static final String URL_KEY = "webUrl";
    private static final String AUTHOR_KEY = "webTitle";
    public static final String TAGS_KEY = "tags";

    private NetworkUtils(){

    }

    public static List<NewsItem> fetchNewsData(String requestURL, Context context) {
        URL url = createURL(requestURL, context);
        String jsonResponse = null;
        try {
            jsonResponse = makeHTTPRequest(url, context);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error in input stream", e);
        }

        List<NewsItem> news = extractNewsData(jsonResponse, context);

        return news;
    }

    public static URL createURL(String stringURL, final Context context) {
        URL url = null;
        try {
            url = new URL(stringURL);
        } catch (MalformedURLException e) {
            Handler handler = new Handler(Looper.getMainLooper());
            handler.post(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(context, "Something went wrong, please contact the developer", Toast.LENGTH_SHORT)
                            .show();
                }
            });
            Log.e(LOG_TAG, "Error Creating URL", e);
        }
        return url;
    }
    public static String makeHTTPRequest(URL url, Context context) throws IOException {
        String jsonResponse = null;
        if (url == null) {
            return jsonResponse;
        }
        final Context mContext = context;
        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;

        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000);
            urlConnection.setConnectTimeout(15000);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();
            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromInputStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Handler handler = new Handler(Looper.getMainLooper());
            handler.post(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(mContext, "Something went wrong, please contact the developer", Toast
                            .LENGTH_SHORT)
                            .show();
                }
            });

            Log.e(LOG_TAG, "Problem retrieving the Guardian API JSON results", e);
        } finally {

            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            // Close stream
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return jsonResponse;
    }
    public static String readFromInputStream(InputStream inputStream) throws IOException {
        StringBuilder streamOutput = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset
                    .forName("UTF-8"));
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String line = bufferedReader.readLine();
            while (line != null) {
                streamOutput.append(line);
                line = bufferedReader.readLine();
            }
        }
        return streamOutput.toString();
    }

    public static List<NewsItem> extractNewsData(String jsonResponse, Context context) {
        final Context mContext = context;
        if (TextUtils.isEmpty(jsonResponse)) {
            return null;
        }

        List<NewsItem> news = new ArrayList<NewsItem>();

        try {
            JSONObject baseJSONObject = new JSONObject(jsonResponse);
            JSONObject responseJSONObject = baseJSONObject.getJSONObject(RESPONSE_KEY);
            JSONArray newsResults = responseJSONObject.getJSONArray(RESULTS_KEY);
            String section;
            String publicationDate;
            String title;
            String webUrl;
            String author = "";

            for (int i = 0; i < newsResults.length(); i++) {
                JSONObject newsArticle = newsResults.getJSONObject(i);
                if (newsArticle.has(SECTION_KEY)) {
                    section = newsArticle.getString(SECTION_KEY);
                } else section = null;

                if (newsArticle.has(DATE_KEY)) {
                    publicationDate = newsArticle.getString(DATE_KEY);
                } else publicationDate = null;
                if (newsArticle.has(TITLE_KEY)) {
                    title = newsArticle.getString(TITLE_KEY);
                } else title = null;
                if (newsArticle.has(URL_KEY)) {
                    webUrl = newsArticle.getString(URL_KEY);
                } else webUrl = null;
                if (newsArticle.has(TAGS_KEY)){
                    JSONArray tags = newsArticle.getJSONArray(TAGS_KEY);
                    JSONObject getdata = tags.getJSONObject(0);
                    if (getdata.has(AUTHOR_KEY)){
                        author = getdata.getString(AUTHOR_KEY);
                    }

                }
                NewsItem newsList = new NewsItem(title, section, webUrl, publicationDate, author);
                news.add(newsList);
            }

        } catch (JSONException e) {
            Handler handler = new Handler(Looper.getMainLooper());
            handler.post(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(mContext, "Something went wrong, please contact the developer", Toast
                            .LENGTH_SHORT)
                            .show();
                }
            });
        }
        return news;
    }
}
