package eecs3311;

public abstract class UserFactory {
    public abstract User createUser(String type, String email, String password, String organizationID);
}

