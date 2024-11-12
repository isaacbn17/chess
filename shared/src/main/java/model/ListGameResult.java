package model;

import java.util.ArrayList;

public record ListGameResult (ArrayList<GameSimplified> games){

    @Override
    public String toString() {
        return "ListGameResult{" +
                "games=" + games +
                '}';
    }
}
