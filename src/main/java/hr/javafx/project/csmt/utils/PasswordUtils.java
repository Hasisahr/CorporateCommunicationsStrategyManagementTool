package hr.javafx.project.csmt.utils;

import hr.javafx.project.csmt.exception.PasswordsNotMatchingException;
import hr.javafx.project.csmt.model.CompanyCredentials;
import hr.javafx.project.csmt.model.LoginUser;
import hr.javafx.project.csmt.repository.CompanyFileRepository;
import hr.javafx.project.csmt.repository.LoginFileRepository;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Optional;

/**
 * Utility class for password-related operations such as hashing, validation,
 * and credential lookup.
 * Provides static methods for secure password hashing using SHA-256,
 * verifying whether usernames or business identifiers exist,
 * and performing credential validation.
 * Used primarily during login and registration processes.
 * This class is non-instantiable and designed as a utility.
 *
 */


public class PasswordUtils {

    private PasswordUtils() {

    }

    /**
     * Hashes a plain text password using the SHA-256 algorithm.
     *
     * @param password the raw password string
     * @return the hashed password as a hexadecimal string, or null on failure
     */
    public static String hash(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(password.getBytes());
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                hexString.append(String.format("%02x", b));
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            LogUtils.error(e.getMessage());
        }
        return null;
    }

    /**
     * Checks if a given username already exists in the login file repository.
     *
     * @param username the username to verify
     * @return true if the username exists, false otherwise
     */
    public static Boolean checkIfUsernameExists(String username) {
        LoginFileRepository loginFileRepository = new LoginFileRepository();
        List<LoginUser> loginUsers = loginFileRepository.findAll();
        loginUsers = loginUsers.stream()
                .filter(e->e.getUsername().equals(username))
                .toList();
        return !loginUsers.isEmpty();
    }

    /**
     * Checks whether a given identifier already exists as a means to secure
     * that no two companies with the same identifier exist.
     * Internally hashes the password before comparing.
     *
     * @param password the raw company identifier to check
     * @return true if the hash matches an existing record, false otherwise
     */
    public static Boolean checkIfIdentifierExists(String password) {
        CompanyFileRepository companyFileRepository = new CompanyFileRepository();
        List<CompanyCredentials> companies = companyFileRepository.findAll();
        companies = companies.stream()
                .filter(c->c.getIdentifierHash().equals(hash(password)))
                .toList();
        return !companies.isEmpty();
    }

    /**
     * Validates a given identifier hash with the stored company credentials.
     *
     * @param passwordHash the hashed identifier
     * @return an Optional containing the matching {@link CompanyCredentials}, if any
     */
    public static Optional<CompanyCredentials> businessIdentifierValidator(String passwordHash){
        CompanyFileRepository companyFileRepository = new CompanyFileRepository();
        List<CompanyCredentials> companies = companyFileRepository.findAll().stream()
                .filter(b -> b.getIdentifierHash().equals(passwordHash))
                .toList();
        return companies.isEmpty() ? Optional.empty() : Optional.of(companies.getFirst());
    }

    /**
     * Validates username and password hash with existing login users to find the user trying to log in.
     *
     * @param username the username to validate
     * @param passwordHash the corresponding password hash
     * @return an Optional containing the matched {@link LoginUser}
     * @throws PasswordsNotMatchingException if no match is found
     */
    public static Optional<LoginUser> userPasswordValidator(String username, String passwordHash){
        LoginFileRepository loginFileRepository = new LoginFileRepository();
        List<LoginUser> loginUsers = loginFileRepository.findAll().stream()
                .filter(e->e.getPasswordHash().equals(passwordHash))
                .filter(e->e.getUsername().equals(username))
                .toList();
        if(loginUsers.isEmpty()){
            throw new PasswordsNotMatchingException("Passwords do not match");
        }
        return Optional.of(loginUsers.getFirst());
    }

}
