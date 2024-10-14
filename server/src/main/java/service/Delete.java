package service;

import dataaccess.DataAccessException;
import dataaccess.MemoryUserDAO;
import dataaccess.UserDAO;
import model.UserData;

import java.util.ArrayList;

public class Delete {

    public void clearApp(ArrayList<UserData> users) throws DataAccessException {
        UserDAO userDAO = new MemoryUserDAO();
        userDAO.clear(users);
    }
}
