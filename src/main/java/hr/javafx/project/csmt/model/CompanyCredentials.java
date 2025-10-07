package hr.javafx.project.csmt.model;

/**
 * Represents a company's authentication credentials.
 * This entity has a hashed identifier used for verifying
 * or authenticating a company during the employee registration process.
 * This class is mainly used in {@link hr.javafx.project.csmt.repository.CompanyFileRepository}.
 */

public class CompanyCredentials extends Entity {
    String identifierHash;

    public CompanyCredentials(Long id, String identifierHash) {
        super(id);
        this.identifierHash = identifierHash;
    }
    public String getIdentifierHash() {
        return identifierHash;
    }
}
