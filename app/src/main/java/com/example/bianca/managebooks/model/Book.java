package com.example.bianca.managebooks.model;

/**
 * Created by dana on 11/10/2016.
 */

public class Book {
    private String title;
    private String authorName;
    private int publicationYear;

    public Book(){
    }

    public Book(String title, String authorName, int publicationYear) {
        this.title = title;
        this.authorName = authorName;
        this.publicationYear = publicationYear;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public int getPublicationYear() {
        return publicationYear;
    }

    public void setPublicationYear(int publicationYear) {
        this.publicationYear = publicationYear;
    }

    @Override
    public String toString() {
        return "Book{" +
                "title='" + title + '\'' +
                ", authorName='" + authorName + '\'' +
                ", publicationYear=" + publicationYear +
                '}';
    }
}
