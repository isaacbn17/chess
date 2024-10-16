package dataaccess;

import java.util.ArrayList;
import model.AuthData;

public interface AuthDAO {
  public void clear();
  public AuthData addAuthToken(String username);
}
