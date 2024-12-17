package com.project.gamemarket.common;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CategoryType {

    ACTION_ROGUELIKE("Action Roguelike"),
    RPG("RPG"),
    HACK_AND_SLASH("Hack and Slash"),
    MYTHOLOGY("Mythology"),
    ACTION("Action"),
    ADVENTURE("Adventure");

    private final String name;

    public static CategoryType fromName(String name) {
        for (CategoryType genre : values()) {
            if (genre.name.equalsIgnoreCase(name)) {
                return genre;
            }
        }
        throw new IllegalArgumentException(String.format("Category type '%s' not found", name));
    }
}
