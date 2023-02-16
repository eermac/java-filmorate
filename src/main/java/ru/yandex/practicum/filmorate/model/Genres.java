package ru.yandex.practicum.filmorate.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Genres {
    private int id;
    private String name;
    public Genres(){

    }
    public Genres(Integer id) {
        this.id = id;
    }

    public Genres(Integer id, String name) {
        this.id = id;
        this.name = name;
    }
}
