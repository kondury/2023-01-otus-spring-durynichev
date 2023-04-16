package com.github.kondury.library.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Document("genres")
public class Genre {

    public static final String UNKNOWN_GENRE = "<UNKNOWN GENRE>";

    @Id
    private String id;
    private String name;

    public Genre(String name) {
        this.name = name;
    }
}