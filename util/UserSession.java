package util;

import model.MsUser;

public class UserSession {
    private static UserSession instance;
    private MsUser currentUser;

    private UserSession() {}

    public static UserSession getInstance() {
        if (instance == null) {
            instance = new UserSession();
        }
        return instance;
    }

    public MsUser getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(MsUser currentUser) {
        this.currentUser = currentUser;
    }
    
    public void logout() {
        this.currentUser = null;
    }
}