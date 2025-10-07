package hr.javafx.project.csmt.model;

import hr.javafx.project.csmt.enums.Role;
import hr.javafx.project.csmt.utils.LogUtils;

import java.io.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a single change entry that stores data including the name of the field changed, its initial and final values,
 * the role and name of the user who made the change, and the date it occurred.
 * This class supports concurrent access and management to a file where all changes are stored through synchronization.
 *
 */

public class Change implements Serializable {
    public static final String CHANGES_FILE_NAME = "dat/changes.dat";
    Boolean fileInUse = false;
    String name;
    String startingValue;
    String endingValue;
    Role changedByRole;
    String changedByName;
    LocalDateTime dateOfChange;

    public Change() {}

    public Change(String name, String startingValue, String endingValue, Role changedByRole, String changedByName) {
        this.name = name;
        this.startingValue = startingValue;
        this.endingValue = endingValue;
        this.changedByRole = changedByRole;
        this.changedByName = changedByName;
        this.dateOfChange = LocalDateTime.now();
    }


    public String getName() {
        return name;
    }

    public String getStartingValue() {
        return startingValue;
    }

    public String getEndingValue() {
        return endingValue;
    }

    public Role getChangedByRole() {
        return changedByRole;
    }

    public String getChangedByName() {
        return changedByName;
    }

    public LocalDateTime getDateOfChange() {
        return dateOfChange;
    }

    public void setFileInUse(Boolean fileInUse) {
        this.fileInUse = fileInUse;
    }

    /**
     * Loads and returns all recorded changes from the serialized data file.
     * Waits if the file is currently in use by another thread.
     *
     * @return a list of all {@code Change} instances from the file
     * @throws InterruptedException if the current thread is interrupted while waiting
     */

    public synchronized List<Change> findAll() throws InterruptedException {
        List<Change> changes;
        while(Boolean.TRUE.equals(fileInUse)) {
            wait();
        }

        setFileInUse(true);

        changes = new ArrayList<>();
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(CHANGES_FILE_NAME))) {
            Object obj;
            while ((obj = ois.readObject()) != null) {
                Change change = (Change) obj;
                changes.add(change);
            }
        }catch (IOException | ClassNotFoundException e) {
            LogUtils.error("Failed to load changes from file: " + e.getMessage());
        }
        finally {
            setFileInUse(false);
            notifyAll();
        }


        return changes;
    }

    /**
     * Saves the current {@code Change} instance to the file by appending
     * it to the existing list of saved changes.
     * Waits if the file is currently in use by another thread.
     *
     * @throws InterruptedException if the current thread is interrupted while waiting
     */

    public synchronized void save() throws InterruptedException {
        while(Boolean.TRUE.equals(fileInUse)) {
            wait();
        }

        setFileInUse(true);
        List<Change> changes = new ArrayList<>();
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(CHANGES_FILE_NAME))) {
            Object obj;
            while ((obj = ois.readObject()) != null) {
                Change change = (Change) obj;
                changes.add(change);
            }
        }catch (IOException | ClassNotFoundException e) {
            LogUtils.error("Failed to load changes from file: " + e.getMessage());
        }
        changes.add(this);
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(CHANGES_FILE_NAME))) {
            for (Change change : changes) {
                oos.writeObject(change);
            }
        } catch (IOException ex) {
            LogUtils.error(ex.getMessage());
        }
        finally{
            setFileInUse(false);
            notifyAll();
        }

    }

    @Override
    public String toString() {
        return "Change{" +
                "name='" + name + '\'' +
                ", startingValue='" + startingValue + '\'' +
                ", endingValue='" + endingValue + '\'' +
                ", changedByRole=" + changedByRole +
                ", changedByName='" + changedByName + '\'' +
                ", dateOfChange=" + dateOfChange +
                '}';
    }
}
