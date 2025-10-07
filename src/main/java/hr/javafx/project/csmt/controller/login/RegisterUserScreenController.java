package hr.javafx.project.csmt.controller.login;

import hr.javafx.project.csmt.enums.Role;
import hr.javafx.project.csmt.model.*;
import hr.javafx.project.csmt.repository.CompanyDatabaseRepository;
import hr.javafx.project.csmt.repository.EmployeeDatabaseRepository;
import hr.javafx.project.csmt.repository.LoginFileRepository;
import hr.javafx.project.csmt.utils.Database;
import hr.javafx.project.csmt.utils.LogUtils;
import hr.javafx.project.csmt.utils.PasswordUtils;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.util.Optional;


/**
 * Controller for registering a new user (team member or project manager) in the application.
 * Validates input fields, checks for existing usernames, verifies passwords,
 * and ensures that the business identifier is correct and associated with a company.
 * On successful validation, saves the new user to login file repository and employee database repository,
 * displays a success alert, and redirects to the login screen.
 *
 */

public class RegisterUserScreenController {
    @FXML
    public TextField nameTextField;
    @FXML
    public TextField usernameTextField;
    @FXML
    public PasswordField passwordTextField;
    @FXML
    public PasswordField passwordConfirmTextField;
    @FXML
    public TextField businessIdentifierTextField;
    @FXML
    public ComboBox<Role> roleComboBox;

    /**
     * Initializes the role dropdown with available user roles from {@link Role}.
     */
    public void initialize() {
        roleComboBox.getItems().addAll(Role.values());
    }



    /**
     * Validates and registers a new employee based on the entered form data.
     * Ensures fields are filled and passwords match.
     * Checks if the username is already taken using {@link PasswordUtils}.
     * Hashes password and business identifier for validation using {@link PasswordUtils}.
     * Registers the employee in both the database and login file
     * On failure, displays an error alert with the collected validation issues.
     *
     */
    public void registerUser(){
        StringBuilder errors = new StringBuilder();
        String name = nameTextField.getText();

        String username = usernameTextField.getText();

        String password = passwordTextField.getText();

        String confirmPassword = passwordConfirmTextField.getText();

        String businessIdentifier = businessIdentifierTextField.getText();

        Role role = roleComboBox.getSelectionModel().getSelectedItem();

        if(name.isEmpty() || username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty() || businessIdentifier.isEmpty() || role == null){
            errors.append("Please fill out all fields.\n");
        }
        if(Boolean.TRUE.equals(PasswordUtils.checkIfUsernameExists(username))){
            errors.append("Username already exists.\n");
        }

        if(!password.equals(confirmPassword)){
            errors.append("Passwords do not match\n");
        }
        else {
            password = PasswordUtils.hash(password);
        }


        businessIdentifier = PasswordUtils.hash(businessIdentifier);
        CompanyDatabaseRepository companyDatabaseRepository = new CompanyDatabaseRepository();
        Optional<CompanyCredentials> companyCredentials = PasswordUtils.businessIdentifierValidator(businessIdentifier);
        Company company = null;
        if(companyCredentials.isPresent()) {
            company = companyDatabaseRepository.findById(companyCredentials.get().getId());
        }
        if(company==null){
            errors.append("Business not found\n");
        }
        if(errors.isEmpty() && role != null && company != null){

            EmployeeDatabaseRepository employeeDatabaseRepository = new EmployeeDatabaseRepository();
            Database database = new Database();

            Long id = database.getEmployeeIdFromDatabase();

            if(role.equals(Role.PROJECT_MANAGER)){
                ProjectManager.Builder projectManagerBuilder = new ProjectManager.Builder();
                ProjectManager projectManager = (ProjectManager) projectManagerBuilder.withId(id)
                        .withName(name)
                        .withRole(role)
                        .withEmployer(company)
                        .build();
                employeeDatabaseRepository.save(projectManager);
            }
            else{
                TeamMember.Builder teamMemberBuilder = new TeamMember.Builder();
                TeamMember teamMember = teamMemberBuilder.withId(id)
                        .withName(name)
                        .withRole(role)
                        .withEmployer(company)
                        .withTasks()
                        .build();
                employeeDatabaseRepository.save(teamMember);
            }

            LoginUser loginUser = new LoginUser(id, username, password);

            LoginFileRepository loginFileRepository = new LoginFileRepository();
            loginFileRepository.save(loginUser);


            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Success");
            alert.setHeaderText("Registration successful");
            alert.showAndWait();
            if(alert.getResult() == ButtonType.OK){
                BackToLoginButtonController backToLoginButtonController = new BackToLoginButtonController();
                backToLoginButtonController.backToLoginScreen();
            }
            LogUtils.info("New user registered:" + username);
        }
        else{
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Registration failed");
            alert.setContentText(errors.toString());
            alert.showAndWait();
            alert.setContentText(errors.toString());
        }
    }
}
