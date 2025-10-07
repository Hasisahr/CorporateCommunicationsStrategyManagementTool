package hr.javafx.project.csmt.controller.login;


import hr.javafx.project.csmt.enums.Role;
import hr.javafx.project.csmt.exception.PasswordsNotMatchingException;
import hr.javafx.project.csmt.model.Employee;
import hr.javafx.project.csmt.model.LoginUser;
import hr.javafx.project.csmt.model.ProjectManager;
import hr.javafx.project.csmt.model.TeamMember;
import hr.javafx.project.csmt.repository.EmployeeDatabaseRepository;
import hr.javafx.project.csmt.utils.LogUtils;
import hr.javafx.project.csmt.utils.PasswordUtils;
import hr.javafx.project.csmt.utils.ShowScreenUtils;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;


import java.util.Optional;

/**
 * Main JavaFX controller responsible for handling user login and navigation from the login screen.
 * Validates user credentials, determines their role, and forwards them to the appropriate main screen.
 * Also provides methods for navigating to registration and changes views.
 *
 */


public class LoginScreenController {
    @FXML
    TextField usernameField;
    @FXML
    PasswordField passwordField;

    /**
     * Handles user authentication by validating entered credentials.
     * If the login is successful, the user is redirected to either the
     * project manager or team member interface depending on their role.
     * Displays an error alert if credentials are invalid.
     */
    public void login() {
        String username = usernameField.getText();
        String password = passwordField.getText();

        if(!username.isEmpty() && !password.isEmpty()) {
            password = PasswordUtils.hash(password);
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            try {
                Optional<LoginUser> loginUser = PasswordUtils.userPasswordValidator(username, password);

                if (loginUser.isPresent()) {
                    ShowScreenUtils showScreenUtils = new ShowScreenUtils();
                    EmployeeDatabaseRepository employeeDatabaseRepository = new EmployeeDatabaseRepository();
                    Long userId = loginUser.get().getId();
                    Employee employee = employeeDatabaseRepository.findById(userId);
                    if (employee != null) {
                        if (employee.getRole().equals(Role.PROJECT_MANAGER)) {
                            showScreenUtils.showProjectManagerScreen((ProjectManager) employee);
                        } else {
                            showScreenUtils.showTeamMemberScreen((TeamMember) employee);
                        }
                    }
                    LogUtils.info("User logged in:" + username);
                }
            }
            catch(PasswordsNotMatchingException e) {
                alert.setTitle("Login failed");
                alert.setHeaderText("Login failed");
                alert.setContentText("Username or password is incorrect!");
                alert.showAndWait();
            }
        }
    }

    /**
     * Navigates to the screen showing the list of changes made.
     */
    public void showChangesScreen(){
        ShowScreenUtils showScreenUtils = new ShowScreenUtils();
        showScreenUtils.showScreen("changes.fxml", 400, 600);
    }

    /**
     * Navigates to the screen for registering a new company.
     */
    public void showRegisterBusinessScreen(){
        ShowScreenUtils showScreenUtils = new ShowScreenUtils();
        showScreenUtils.showScreen("registerCompanyScreen.fxml", 400, 600);
    }

    /**
     * Navigates to the screen for registering a new user (employee).
     */
    public void showRegisterUserScreen(){
        ShowScreenUtils showScreenUtils = new ShowScreenUtils();
        showScreenUtils.showScreen("accordionRegisterUser.fxml", 400, 600);
    }
}