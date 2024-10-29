package com.project.gamemarket.web.exception;

public class GameNotFoundException extends RuntimeException {
    private static final String GAME_NOT_FOUND_MESSAGE = "Game with name %s not found";

    public GameNotFoundException(String gameName) {
        super(String.format(GAME_NOT_FOUND_MESSAGE, gameName));
    }
}
