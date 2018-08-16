package com.example.lizlieholleza.booksearch;

public class Book {
    private String bookUrl;
    private String title;
    private String[] authors;
    private String imageUrl;

    public Book(String bUrl, String title, String[] authors, String imgUrl) {
        this.bookUrl = bUrl;
        this.title = title;
        this.authors = authors;
        this.imageUrl = imgUrl;
    }

    public String getBookUrl() {
        return bookUrl;
    }

    public String getTitle() {
        return title;
    }

    public String[] getAuthor() {
        return authors;
    }

    public String getImageUrl() {
        return imageUrl;
    }
}
