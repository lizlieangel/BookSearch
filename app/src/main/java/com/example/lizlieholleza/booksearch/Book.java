package com.example.lizlieholleza.booksearch;

public class Book {
    private String bookUrl;
    private String title;
    private String author;
    private String imageUrl;

    public Book(String bUrl, String title, String author, String imgUrl) {
        this.bookUrl = bUrl;
        this.title = title;
        this.author = author;
        this.imageUrl = imgUrl;
    }

    public String getBookUrl() {
        return bookUrl;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public String getImageUrl() {
        return imageUrl;
    }
}
