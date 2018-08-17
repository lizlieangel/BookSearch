package com.example.lizlieholleza.booksearch;

import android.text.TextUtils;
import android.util.Log;
import android.widget.ListView;

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

import static com.example.lizlieholleza.booksearch.BookActivity.LOG_TAG;

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
            ArrayList<String> authors = new ArrayList<>();
            for(int i=0;i<bookArray.length();i++) {
                JSONObject currentBook = bookArray.getJSONObject(i);
                String bookTitle = currentBook.getString("title");
                JSONArray bookAuthors = currentBook.getJSONArray("authors");
                for(int j=0;j<bookAuthors.length();j++) {
                    authors.add(bookAuthors.getString(j));
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

    private static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Problem loading the url", e);
        }
        return url;
    }
    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";
        if(url == null) {
            return jsonResponse;
        }
        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try{
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000);
            urlConnection.setConnectTimeout(15000);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();
            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code");
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving the json results.", e);
        } finally {
            if(urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if(inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String line = bufferedReader.readLine();
            while (line != null) {
                output.append(line);
                line = bufferedReader.readLine();
            }
        }
        return output.toString();
    }

    public static List<Book> fetchBookData(String requestUrl) {
        URL url = createUrl(requestUrl);
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem making the http request.");
        }
        List<Book> books = extractFeatureFromJson(jsonResponse);
        return books;
    }
}
