package com.example.lizlieholleza.booksearch;

import android.content.Context;
import android.content.AsyncTaskLoader;
import java.util.List;

public class BookLoader extends AsyncTaskLoader<List<Book>>{
    private String url;

    public BookLoader(Context context, String url) {
        super(context);
        this.url = url;
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
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
