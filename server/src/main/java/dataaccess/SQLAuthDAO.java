package dataaccess;

import model.AuthData;

import java.sql.SQLException;
import java.util.Map;
import java.util.UUID;

import static java.sql.Statement.RETURN_GENERATED_KEYS;

public class SQLAuthDAO implements AuthDAO {
    @Override
    public void clear() throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            try (var  ps = conn.prepareStatement("TRUNCATE auth")) {
                ps.executeUpdate();
            }
        } catch (SQLException ex) {
            throw new DataAccessException(ex.getMessage());
        }
    }
    public String generateToken() {
        return UUID.randomUUID().toString();
    }

    @Override
    public AuthData addAuthData(String username) throws DataAccessException {
        String authToken = generateToken();
        AuthData authData = new AuthData(authToken, username);
        try (var conn = DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement(
                    "INSERT INTO auth (username, authToken) VALUES (?, ?)", RETURN_GENERATED_KEYS)) {
                preparedStatement.setString(1, username);
                preparedStatement.setString(2, authToken);

                preparedStatement.executeUpdate();
            }
        } catch (SQLException ex) {
            throw new DataAccessException(ex.getMessage());
        }
        return authData;
    }

    @Override
    public Map<String, AuthData> getAuthData() {
        return null;
    }

}
