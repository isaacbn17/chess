package model;

import chess.ChessGame;

public record GameData(int gameID, String whiteUserName, String blackUsername,
                       String gameName, ChessGame game) {
}
