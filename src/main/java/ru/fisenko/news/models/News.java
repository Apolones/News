package ru.fisenko.news.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;

import java.util.Date;

@Entity
@Table(name = "news")
public class News {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Size(min = 2, max = 50, message = "Header should be between 2 and 50 characters")
    @Column(name = "header", length = 50)
    private String header;

    @Column(name = "data")
    private Date data;

    @Size(min = 2, max = 1000, message = "Body should be between 2 and 1000 characters")
    @Column(name = "body", length = 1000)
    private String body;

    @Column(name = "image_path")
    private String image_path;

    public News(String header, Date data, String body, String image_path) {
        this.header = header;
        this.data = data;
        this.body = body;
        this.image_path = image_path;
    }

    public News() {
    }

    public Long getId() {
        return id;
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getImage_path() {
        return image_path;
    }

    public void setImage_path(String image_path) {
        this.image_path = image_path;
    }
}
