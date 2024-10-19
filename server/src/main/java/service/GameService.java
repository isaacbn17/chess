package service;

import dataaccess.*;
import model.AuthData;
import model.LoginRequest;
import model.RegisterRequest;
import model.UserData;

import java.util.Objects;

public class GameService {
    GameDAO gameDAO;
    AuthDAO authDAO;

    public GameService(GameDAO gameDAO, AuthDAO authDAO) {
        this.gameDAO = gameDAO;
        this.authDAO = authDAO;
    }

    public void listGames() {

    }
}
