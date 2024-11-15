package model;

public record GameID(int gameID) {
    @Override
    public String toString() {
        return String.valueOf(gameID);
    }
}
