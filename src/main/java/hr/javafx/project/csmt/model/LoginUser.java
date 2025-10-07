package hr.javafx.project.csmt.model;

/**
 * Represents a login user entity with authentication credentials.
 * This class extends {@link Entity} and includes a username and
 * a hashed password for authentication.
 * This class is mainly used as a temporary user during login stages.
 * Its ID is then used to retrieve the corresponding user from the database.
 */


public class LoginUser extends Entity {
    String username;
    String passwordHash;

    public LoginUser(Long id, String username, String passwordHash) {
        super(id);
        this.username = username;
        this.passwordHash = passwordHash;
    }

    public String getUsername() {
        return username;
    }

    public String getPasswordHash() {
        return passwordHash;
    }
}
