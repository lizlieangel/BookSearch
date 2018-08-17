package com.example.lizlieholleza.booksearch;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

public class BookAdapter extends ArrayAdapter<Book> {
    private String authorNames;

    public BookAdapter(Context context, List<Book> books) {
        super(context, 0, books);
    }

    private String listAuthors(ArrayList<String> authors) {
        for(int i=0; i < authors.size(); i++) {
            authorNames += authors.get(0) + " ";
        }
        return  authorNames;
    }

    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItemView = convertView;
        if(listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.book_list_item, parent, false);
        }
        Book currentBook = getItem(position);
        ImageView bookImage = (ImageView) listItemView.findViewById(R.id.book_image);
        if(currentBook != null) {
            new DownloadImageTask(bookImage).execute(currentBook.getImageUrl());
        }
        TextView bookTitle = (TextView) listItemView.findViewById(R.id.book_title);
        bookTitle.setText(currentBook.getTitle());
        TextView bookAuthor = (TextView) listItemView.findViewById(R.id.book_author);
        String authors = listAuthors(currentBook.getAuthor());
        bookAuthor.setText(authors);
        return listItemView;
    }

    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        private ImageView bmImage;

        public DownloadImageTask(ImageView image) {
            this.bmImage =  image;
        }

        @Override
        protected Bitmap doInBackground(String... urls) {
            String urlDisplay = urls[0];
            Bitmap bmap = null;
            try {
                InputStream in = new java.net.URL(urlDisplay).openStream();
                bmap = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("error", e.getMessage());
                e.printStackTrace();
            }
            return bmap;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            bmImage.setImageBitmap(bitmap);
        }
    }
}
