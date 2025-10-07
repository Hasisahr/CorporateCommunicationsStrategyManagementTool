package hr.javafx.project.csmt.repository;

import hr.javafx.project.csmt.model.CompanyCredentials;
import hr.javafx.project.csmt.utils.LogUtils;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

/**
 * File-based repository implementation for managing {@link CompanyCredentials} entities.
 * Reads from and writes to a plain text file to store hashed company identifiers.
 * This repository complements database-backed versions by offering a lightweight storage mechanism.
 * It parses the {@code companies.txt} file, where each company is represented with two lines:
 * an ID and its corresponding identifier hash.
 * The main use of this repository class is to retrieve an ID of the company which is then used to
 * find the corresponding Company from the database with the same ID
 *
 */


public non-sealed class CompanyFileRepository extends AbstractRepository<CompanyCredentials> {

    private static final String BUSINESSES_FILENAME = "dat/companies.txt";
    private static final int NUMBER_OF_ROWS_PER_BUSINESS = 2;


    /**
     * Searches for a company credential entry by ID from the file-based storage.
     *
     * @param id the unique identifier of the company
     * @return the matching {@link CompanyCredentials} object
     */

    @Override
    public CompanyCredentials findById(Long id) {
        return  findAll().stream()
                .filter(b -> b.getId().equals(id))
                .toList()
                .getFirst();
    }

    /**
     * Loads and returns all company credential objects stored in the text file.
     *
     * @return a list of {@link CompanyCredentials} parsed from file
     */
    @Override
    public List<CompanyCredentials> findAll() {
        List<CompanyCredentials> companies = new ArrayList<>();
        try (Stream<String> stream = Files.lines(Path.of(BUSINESSES_FILENAME))) {
            List<String> rows = stream.toList();
            for (int i = 0; i < rows.size() / NUMBER_OF_ROWS_PER_BUSINESS; i++) {
                Long id = Long.parseLong(rows.get(i * NUMBER_OF_ROWS_PER_BUSINESS));
                String passwordHash = rows.get(i * NUMBER_OF_ROWS_PER_BUSINESS + 1);

                CompanyCredentials companyCredentials = new CompanyCredentials(id, passwordHash);
                companies.add(companyCredentials);
            }
        }catch (IOException e){
            LogUtils.error(e.getMessage());
        }
        return companies;
    }

    /**
     * Saves a new company credential to the text file.
     * Reads all existing company credentials, appends the new one, and rewrites the file.
     *
     * @param companyCredentials the credentials to add to the file
     */
    @Override
    public void save(CompanyCredentials companyCredentials) {
        List<CompanyCredentials> companies = findAll();
        companies.add(companyCredentials);
        try(PrintWriter writer = new PrintWriter(BUSINESSES_FILENAME)){
            for(CompanyCredentials b : companies) {
                writer.println(b.getId());
                writer.println(b.getIdentifierHash());
            }
        }
        catch (FileNotFoundException e) {
            LogUtils.error(e.getMessage());
        }

    }
}
