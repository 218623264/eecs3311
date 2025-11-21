package factory;

import model.User;

public abstract class UserFactory {
    public abstract User createUser(String type, String email, String password, String organizationID);
}