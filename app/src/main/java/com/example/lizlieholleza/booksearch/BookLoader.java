package com.example.lizlieholleza.booksearch;

import android.app.DownloadManager;
import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.content.AsyncTaskLoader;

import java.util.List;

public class BookLoader extends AsyncTaskLoader<List<Book>>{
    private String url;

    public BookLoader(Context context, String url) {
        super(context);
        this.url = url;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public List<Book> loadInBackground() {
        if(url == null) {
            return null;
        }
        List<Book> result = QueryUtils.fetchBookData(url);
        return result;
    }
}
