package com.example.newsapp;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class NewsAdapter extends ArrayAdapter<NewsItem> {
    public NewsAdapter(@NonNull Activity context, ArrayList<NewsItem> newsItems) {
        super(context, 0,newsItems);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listitemview=convertView;
        if (listitemview==null){
            listitemview= LayoutInflater.from(getContext()).inflate(R.layout.news_item,parent,false);

        }

        final NewsItem newsItem=getItem(position);
        TextView title=listitemview.findViewById(R.id.title);
        TextView sectname=listitemview.findViewById(R.id.sectname);
        TextView date=listitemview.findViewById(R.id.date);
        TextView author = listitemview.findViewById(R.id.author);
        date.setText(convertDateFormat(newsItem.getPublishDate()));
        title.setText(newsItem.getTitle());
        sectname.setText(newsItem.getSectionName());
        author.setText(newsItem.getAuthor());

        listitemview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse(newsItem.getWebUrl())
                );
                getContext().startActivity(i);

            }
        });

        return listitemview;
    }
    public String convertDateFormat(String input) {
        input = input.substring(0, input.length() - 1);
        String olddateFormat = "yyyy-MM-dd'T'HH:mm:ss";
        String newdateFormat = "dd/MM/yyyy";
        SimpleDateFormat inputdate = new SimpleDateFormat(olddateFormat);
        SimpleDateFormat outputdate = new SimpleDateFormat(newdateFormat);
        Date date = null;
        String output = "";
        try {
            date = inputdate.parse(input);
            output = outputdate.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return output;
    }


}
