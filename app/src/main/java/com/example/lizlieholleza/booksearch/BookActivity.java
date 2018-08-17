package com.example.lizlieholleza.booksearch;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class BookActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Book>>{
    private static final int LOADER_ID = 1;
    private TextView emptyStateView;
    private View loadingIndicator;
    private BookAdapter adapter;
    public static final String LOG_TAG = BookActivity.class.getName();
    private String requestUrl = "https://www.googleapis.com/books/v1/volumes?q=";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book);

        ConnectivityManager cm = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();

        emptyStateView = findViewById(R.id.empty_view);

        if(isConnected) {
            LoaderManager loaderManager = getLoaderManager();
            loaderManager.initLoader(LOADER_ID, null, this);
        } else {
            loadingIndicator = findViewById(R.id.loading_indicator);
            loadingIndicator.setVisibility(View.GONE);
            emptyStateView.setText(R.string.no_connection);
        }

        ListView bookListView = (ListView) findViewById(R.id.list);
        adapter = new BookAdapter(this, new ArrayList<Book>());
        bookListView.setEmptyView(emptyStateView);
        bookListView.setAdapter(adapter);
        bookListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Book currentBook = adapter.getItem(i);
                Uri bookUri = Uri.parse(currentBook.getBookUrl());
                Intent bookPage = new Intent(Intent.ACTION_VIEW, bookUri);
                startActivity(bookPage);
            }
        });
        Button searchButton = (Button) findViewById(R.id.search_button);
        final EditText searchText = (EditText) findViewById(R.id.search_bar);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String text = searchText.getText().toString();
                requestUrl += text;
            }
        });
    }

    @Override
    public Loader<List<Book>> onCreateLoader(int i, Bundle bundle) {
        return new BookLoader(this,requestUrl);
    }

    @Override
    public void onLoadFinished(Loader<List<Book>> loader, List<Book> books) {
        loadingIndicator = findViewById(R.id.loading_indicator);
        loadingIndicator.setVisibility(View.GONE);
        emptyStateView.setText(R.string.no_book);
        adapter.clear();
        if(books != null && !books.isEmpty()) {
            adapter.addAll(books);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<Book>> loader) {
        adapter.clear();
    }

    private class BookAsyncTask extends AsyncTask<String,Void,List<Book>> {
        @Override
        protected List<Book> doInBackground(String... urls) {
            if(urls.length < 1 || urls[0] == null) {
                return null;
            }
            List<Book> result = QueryUtils.fetchBookData(urls[0]);
            return result;
        }

        @Override
        protected void onPostExecute(List<Book> books) {
            adapter.clear();
            if(books != null && books.isEmpty()) {
                adapter.addAll(books);
            }
        }
    }
}
