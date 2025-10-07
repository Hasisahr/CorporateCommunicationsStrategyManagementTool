package hr.javafx.project.csmt.controller.login;

import hr.javafx.project.csmt.model.Company;
import hr.javafx.project.csmt.model.CompanyCredentials;
import hr.javafx.project.csmt.repository.CompanyDatabaseRepository;
import hr.javafx.project.csmt.repository.CompanyFileRepository;
import hr.javafx.project.csmt.utils.AlertUtils;
import hr.javafx.project.csmt.utils.Database;
import hr.javafx.project.csmt.utils.LogUtils;
import hr.javafx.project.csmt.utils.PasswordUtils;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

/**
 * JavaFX controller for handling company registration within the application.
 * Validates user input for business name and identifier, ensures it is unique,
 * and creates entries in both the database and file-based repositories.
 * Displays appropriate alerts on success or failure.
 *
 */
public class RegisterCompanyScreenController {


    @FXML
    TextField businessName;

    @FXML
    PasswordField businessIdentifier;

    @FXML
    PasswordField businessIdentifierRepeated;

    /**
     * Handles form validation and registration of a new company.
     * Ensures all fields are filled and identifiers are unique.
     * Uses validators from {@link PasswordUtils} to ensure every company has a unique identifier.
     * On success, the company and its credentials are saved, and UI fields are cleared.
     * Shows relevant alerts for both successful and failed submissions.
     */
    public void registerCompany(){
        StringBuilder errors = new StringBuilder();

        String name = businessName.getText();
        if(name.isEmpty()){
            errors.append("Please enter a business name.\n");
        }
        String identifier = businessIdentifier.getText();
        if(identifier.isEmpty()){
            errors.append("Please enter a business identifier.\n");
        }
        String identifierRepeated = businessIdentifierRepeated.getText();
        if(identifierRepeated.isEmpty()){
            errors.append("Please repeat your business identifier.\n");
        }
        if(!identifier.equals(identifierRepeated)){
            errors.append("Identifiers are not matching.\n");
        }
        if(Boolean.TRUE.equals(PasswordUtils.checkIfIdentifierExists(identifier))){
            errors.append("Please use a different identifier.\n");
        }

        if(errors.isEmpty()){
            identifier = PasswordUtils.hash(identifier);
            Database database = new Database();
            CompanyDatabaseRepository companyDatabaseRepository = new CompanyDatabaseRepository();
            CompanyFileRepository companyFileRepository = new CompanyFileRepository();

            Long id = database.getCompanyIdFromDatabase();

            Company company = new Company(id, name);

            CompanyCredentials companyCredentials = new CompanyCredentials(id, identifier);
            companyDatabaseRepository.save(company);
            companyFileRepository.save(companyCredentials);

            AlertUtils.showSuccessAlert(company.getName());

            businessName.clear();
            businessIdentifier.clear();
            businessIdentifierRepeated.clear();
            LogUtils.info("New company created" + company.getName());
        }
        else{
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Failed to register the business.");
            alert.setContentText(errors.toString());
            alert.showAndWait();
        }

    }

}
