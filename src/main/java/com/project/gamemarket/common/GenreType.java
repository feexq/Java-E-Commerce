package com.project.gamemarket.common;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum GenreType {
    ACTION_ROGUELIKE("Action Roguelike"),
    RPG("RPG"),
    HACK_AND_SLASH("Hack and Slash"),
    MYTHOLOGY("Mythology"),
    ACTION("Action"),
    ADVENTURE("Adventure");

    private final String name;

    public static GenreType fromName(String name) {
        for (GenreType genre : values()) {
            if (genre.name.equalsIgnoreCase(name)) {
                return genre;
            }
        }
        throw new IllegalArgumentException(String.format("Genre type '%s' not found", name));
    }
}
