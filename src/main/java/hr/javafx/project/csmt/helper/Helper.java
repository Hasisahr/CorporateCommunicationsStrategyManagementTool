package hr.javafx.project.csmt.helper;

import hr.javafx.project.csmt.model.Company;
import hr.javafx.project.csmt.repository.CompanyDatabaseRepository;
import hr.javafx.project.csmt.utils.LogUtils;

public class Helper {
    public static void main(String[] args) {
        CompanyDatabaseRepository repo = new CompanyDatabaseRepository();
        Company company = repo.findAll().stream()
                .filter(c -> c.getId().equals(3l))
                .toList()
                .getFirst();
        LogUtils.info(company.toString());
    }
}
