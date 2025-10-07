package hr.javafx.project.csmt.repository;

import hr.javafx.project.csmt.model.LoginUser;
import hr.javafx.project.csmt.utils.LogUtils;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * File-based repository implementation for managing {@link LoginUser} entities.
 * Reads and writes serialized user login data from a binary file.
 * Used to authenticate users based on username and password hash.
 * Data is stored in the {@code users2.dat} file as serialized Java objects.
 * This repository class is mainly used to retrieve the user ID during login stages
 * which is then used to fetch the corresponding user from the database based on the ID.
 *
 */

public non-sealed class LoginFileRepository extends AbstractRepository<LoginUser> {

    private static final String USERS_FILENAME = "dat/users2.dat";

    /**
     * Retrieves a login user by their unique identifier from the local file.
     *
     * @param id the ID of the user to retrieve
     * @return the matching {@link LoginUser}, or throws if not found
     */
    @Override
    public LoginUser findById(Long id) {
        return findAll().stream()
                .filter(e-> e.getId().equals(id))
                .toList()
                .getFirst();
    }

    /**
     * Loads all login users from the binary file using deserialization.
     *
     * @return a list of all {@link LoginUser} objects stored in the file
     */
    public List<LoginUser> findAll(){
        List<LoginUser> loginUsers = new ArrayList<>();
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(USERS_FILENAME))) {
            Object obj;
            while ((obj = ois.readObject()) != null) {
                LoginUser loginUser = (LoginUser) obj;
                loginUsers.add(loginUser);
            }
        }catch (IOException | ClassNotFoundException e) {
            LogUtils.error("Failed to load employees from file: " + e.getMessage());
        }
        return loginUsers;
    }

    /**
     * Reads all current login users from the file, appends a new {@link LoginUser} and rewrites the entire file.
     *
     * @param lu the login user to save
     */
    @Override
    public void save(LoginUser lu) {
        List<LoginUser> loginUsers = findAll();
        loginUsers.add(lu);
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(USERS_FILENAME))) {
            for (LoginUser loginUser : loginUsers) {
                oos.writeObject(loginUser);
            }
        } catch (IOException ex) {
            LogUtils.error(ex.getMessage());
        }
    }
}
