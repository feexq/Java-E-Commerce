package com.project.gamemarket.common;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum GameType {

    HADES("Hades"),
    HADES_TWO("Hades II"),
    CURSE_OF_THE_DEAD_GODS("Curse of the Dead Goods"),
    WITCHER_THREE("Witcher 3");

    private final String name;

    public static GameType fromName(String name) {
        for (GameType gameType : values()) {
            if (gameType.name.equalsIgnoreCase(name)) {
                return gameType;
            }
        }
        throw new IllegalArgumentException(String.format("Game type '%s' not found", name));
    }

}
