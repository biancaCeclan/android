package com.example.bianca.managebooks.model;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Book {
    private Long id;
    private String title;
    private String authorName;
    private int publicationYear;
    private int price;
    private String uuid;

    public Book(){
    }

    public Book(String title, String authorName, int publicationYear, int price) {
        this.title = title;
        this.authorName = authorName;
        this.publicationYear = publicationYear;
        this.price = price;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getUuid() {
        if (uuid == null) {
            uuid = UUID.randomUUID().toString();
        }
        return uuid;
    }

    public void setUuid() {
        if (uuid == null) {
            uuid = UUID.randomUUID().toString();
        }
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public Map<String, Object> bookToMap() {
        Map<String,Object> bookMap = new HashMap<>();
        bookMap.put("uuid", uuid);
        bookMap.put("title", title);
        bookMap.put("authorName", authorName);
        bookMap.put("publicationYear", publicationYear);
        bookMap.put("price", price);
        return bookMap;
    }

    @Override
    public String toString() {
        return "Book{" +
                "title='" + title + '\'' +
                ", authorName='" + authorName + '\'' +
                ", publicationYear=" + publicationYear +
                ", price=" + price +
                '}';
    }
}
