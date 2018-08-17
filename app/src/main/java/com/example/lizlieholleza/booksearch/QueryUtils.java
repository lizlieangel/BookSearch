package com.example.lizlieholleza.booksearch;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class QueryUtils {
    private QueryUtils(){}

    private static List<Book> extractFeatureFromJson(String bookJson) {
        if(TextUtils.isEmpty(bookJson)) {
            return null;
        }
        List<Book> books = new ArrayList<>();
        try {
            JSONObject baseJsonResponse = new JSONObject(bookJson);
            JSONArray bookArray = baseJsonResponse.getJSONArray("items");
            String[] authors = new String[0];
            for(int i=0;i<bookArray.length();i++) {
                JSONObject currentBook = bookArray.getJSONObject(i);
                String bookTitle = currentBook.getString("title");
                JSONArray bookAuthors = currentBook.getJSONArray("authors");
                for(int j=0;j<bookAuthors.length();j++) {
                    authors = new String[bookAuthors.length()];
                    authors[j] = bookAuthors.getString(j);
                }
                String bookUrl = currentBook.getString("webReaderLink");
                JSONObject imageLink = currentBook.getJSONObject("imageLinks");
                String imageUrl = imageLink.getString("thumbnail");
                Book book = new Book(bookUrl, bookTitle, authors, imageUrl);
                books.add(book);
            }
        } catch (JSONException e) {
            Log.e("QueryUtils", "Problem parsing the book JSON results");
        }
        return books;
    }
}
