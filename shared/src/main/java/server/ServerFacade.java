package server;

import com.google.gson.Gson;
import exception.ResponseException;
import model.*;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;


public class ServerFacade {
    private final String serverURL;

    public ServerFacade(String url) {
        serverURL = url;
    }

    public RegisterResult createUser(UserData userData) throws Exception {
        return this.makeRequest("POST", "/user", userData, RegisterResult.class, null);
    }
    public RegisterResult loginUser(LoginRequest loginRequest) throws Exception {
        return this.makeRequest("POST",  "/session", loginRequest, RegisterResult.class, null);
    }
    public void logoutUser(String authToken) throws Exception {
        this.makeRequest("DELETE", "/session", null, null, authToken);
    }
    public GameID createGame(GameName gameName, String authToken) throws Exception {
        return this.makeRequest("POST", "/game", gameName, GameID.class, authToken);
    }
    public ArrayList<GameSimplified> listGames(String authToken) throws Exception {
        return this.makeRequest("GET", "/game", null, ListGameResult.class, authToken).games();
    }

    private <T> T makeRequest(String method, String path, Object request, Class<T> responseClass, String authToken) throws Exception {
        URL url = (new URI(serverURL + path)).toURL();
        HttpURLConnection http = (HttpURLConnection) url.openConnection();
        http.setRequestMethod(method);
        http.setDoOutput(true);
        if (authToken != null) {
            http.setRequestProperty("authorization", authToken);
        }

        writeBody(request, http);
        http.connect();
        throwIfNotSuccessful(http);
        return readBody(http, responseClass);
    }
    private static void writeBody(Object request, HttpURLConnection http) throws IOException {
        if (request != null) {
            http.addRequestProperty("Content-Type", "application/json");
            String reqData = new Gson().toJson(request);
            try (OutputStream reqBody = http.getOutputStream()) {
                reqBody.write(reqData.getBytes());
            }
        }
    }
    private void throwIfNotSuccessful(HttpURLConnection http) throws ResponseException, IOException {
        var status = http.getResponseCode();
        switch (status) {
            case 401 -> throw new ResponseException("Error: unauthorized.");
            case 403 -> throw new ResponseException("Error: username already taken.");
            case 400 -> throw new ResponseException("Error: bad request.");
            case 500 -> throw new ResponseException("Error");
        }
    }
    private static <T> T readBody(HttpURLConnection http, Class<T> responseClass) throws IOException {
        T response = null;
        if (http.getContentLength() < 0) {
            try (InputStream respBody = http.getInputStream()) {
                InputStreamReader reader = new InputStreamReader(respBody);
                if (responseClass != null) {
                    response = new Gson().fromJson(reader, responseClass);
                }
            }
        }
        return response;
    }


}
