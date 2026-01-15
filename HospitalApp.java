package application;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import java.util.ArrayList;
import java.util.List;

// --- OOP: ABSTRACTION ---
interface Assignable {
    String getAssignmentDetails();
}

// --- OOP: INHERITANCE (Base Class) ---
abstract class Person {
    private String name;
    private int age;
    private String gender;

    public Person(String name, int age, String gender) {
        this.name = name;
        this.age = age;
        this.gender = gender;
    }

    public String getName() { return name; }
    public int getAge() { return age; }
    public String getGender() { return gender; }
}

// --- OOP: INHERITANCE (Sub-Classes) ---
class Doctor extends Person implements Assignable {
    private String specialization;
    private String role; 

    public Doctor(String name, int age, String gender, String specialization, String role) {
        super(name, age, gender);
        this.specialization = specialization;
        this.role = role;
    }

    public String getSpecialization() { return specialization; }
    public String getRole() { return role; }

    @Override
    public String getAssignmentDetails() {
        return "Dr. " + getName() + " [" + role + "] - " + specialization;
    }
}

class Staff extends Person {
    private String shift; 

    public Staff(String name, int age, String gender, String shift) {
        super(name, age, gender);
        this.shift = shift;
    }

    public String getShift() { return shift; }
}

class Patient extends Person {
    private String illness;
    private String assignedWard;

    public Patient(String name, int age, String gender, String illness, String assignedWard) {
        super(name, age, gender);
        this.illness = illness;
        this.assignedWard = assignedWard;
    }

    public String getIllness() { return illness; }
    public String getAssignedWard() { return assignedWard; }
}

class Ward {
    private String name;
    private int limit;
    private int currentOccupancy;

    public Ward(String name, int limit) {
        this.name = name;
        this.limit = limit;
        this.currentOccupancy = 0;
    }

    public String getName() { return name; }
    public int getLimit() { return limit; }
    public int getCurrentOccupancy() { return currentOccupancy; }
    public void incrementOccupancy() { this.currentOccupancy++; }
}

// --- MAIN APPLICATION ---
public class HospitalApp extends Application {

    private Stage primaryStage;
    private final String BG_COLOR = "-fx-background-color: #FFFFE0;"; // Light Yellow
    private Image logoImage;

    // In-Memory Data Storage
    private List<Doctor> doctors = new ArrayList<>();
    private List<Patient> patients = new ArrayList<>();
    private List<Staff> staffMembers = new ArrayList<>();
    private List<Ward> wards = new ArrayList<>();

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        
        // Icon
        try {
            logoImage = new Image(getClass().getResourceAsStream("logo.png"));
            primaryStage.getIcons().add(logoImage);
        } catch (Exception e) {
            System.out.println("Logo not found. Continuing without images.");
        }

        initializeDefaultWards();
        showLoginScreen();
    }

    private void initializeDefaultWards() {
        wards.add(new Ward("General Ward", 10));
        wards.add(new Ward("Emergency Ward", 5));
        wards.add(new Ward("Cardiology Ward", 5));
    }

    // --- LOGIN SCREEN ---
    private void showLoginScreen() {
        VBox layout = new VBox(15);
        layout.setStyle(BG_COLOR);
        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(30));

        if (logoImage != null) {
            ImageView logoView = new ImageView(logoImage);
            logoView.setFitWidth(200);
            logoView.setPreserveRatio(true);
            layout.getChildren().add(logoView);
        }

        Label title = new Label("IQRA Hospital Login");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 26));

        TextField userField = new TextField();
        userField.setPromptText("Admin Username");
        userField.setMaxWidth(250);

        PasswordField passField = new PasswordField();
        passField.setPromptText("Password");
        passField.setMaxWidth(250);

        Button loginBtn = new Button("Login");
        loginBtn.setMinWidth(100);
        loginBtn.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-weight: bold;");
        loginBtn.setOnAction(e -> {
            if (userField.getText().equalsIgnoreCase("admin") && passField.getText().equals("admin")) {
                showMainMenu();
            } else {
                showAlert("Access Denied", "Invalid Admin Credentials.");
            }
        });

        layout.getChildren().addAll(title, userField, passField, loginBtn);
        primaryStage.setScene(new Scene(layout, 900, 750));
        primaryStage.setTitle("IQRA HMS - Secure Login");
        primaryStage.show();
    }

    // --- MAIN DASHBOARD ---
    private void showMainMenu() {
        BorderPane root = new BorderPane();
        root.setStyle(BG_COLOR);

        VBox header = new VBox(10);
        header.setPadding(new Insets(20));
        header.setAlignment(Pos.CENTER);
        
        HBox topBar = new HBox(15);
        topBar.setAlignment(Pos.TOP_RIGHT);
        Button recordsBtn = new Button("📊 Records");
        Button logoutBtn = new Button("Logout");
        logoutBtn.setStyle("-fx-background-color: #f44336; -fx-text-fill: white;");
        logoutBtn.setOnAction(e -> showLoginScreen());
        recordsBtn.setOnAction(e -> showRecordsPopup());
        topBar.getChildren().addAll(recordsBtn, logoutBtn);

        Label title = new Label("IQRA Hospital Management System");
        title.setFont(Font.font("Verdana", FontWeight.BOLD, 30));

        header.getChildren().addAll(topBar, title);

        if (logoImage != null) {
            ImageView mainLogo = new ImageView(logoImage);
            mainLogo.setFitWidth(120);
            mainLogo.setPreserveRatio(true);
            header.getChildren().add(mainLogo);
        }

        root.setTop(header);

        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(40); grid.setVgap(40);

        Button mDocs = createMenuButton("Manage Doctors");
        Button mWards = createMenuButton("Manage Wards");
        Button mStaff = createMenuButton("Manage Staff");
        Button mPatients = createMenuButton("Manage Patients");

        mDocs.setOnAction(e -> showManageDoctors());
        mWards.setOnAction(e -> showManageWards());
        mStaff.setOnAction(e -> showManageStaff());
        mPatients.setOnAction(e -> showManagePatients());

        grid.add(mDocs, 0, 0); grid.add(mWards, 1, 0);
        grid.add(mStaff, 0, 1); grid.add(mPatients, 1, 1);

        root.setCenter(grid);
        primaryStage.setScene(new Scene(root, 900, 750));
    }

    private Button createMenuButton(String text) {
        Button btn = new Button(text);
        btn.setPrefSize(250, 130);
        btn.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        btn.setStyle("-fx-background-radius: 15; -fx-cursor: hand;");
        return btn;
    }

    // --- DOCTOR MANAGEMENT ---
    private void showManageDoctors() {
        VBox layout = new VBox(10);
        layout.setPadding(new Insets(20));
        layout.setStyle(BG_COLOR);

        TextField nameIn = new TextField(); nameIn.setPromptText("Doctor Name");
        TextField ageIn = new TextField(); ageIn.setPromptText("Age");
        ComboBox<String> genIn = new ComboBox<>(FXCollections.observableArrayList("Male", "Female", "Other"));
        genIn.setPromptText("Gender");
        ComboBox<String> specIn = new ComboBox<>(FXCollections.observableArrayList("General", "Emergency", "Cardiology", "Orthopedic"));
        specIn.setPromptText("Specialization");
        ComboBox<String> roleIn = new ComboBox<>(FXCollections.observableArrayList("Senior Doctor", "Assistant Doctor"));
        roleIn.setPromptText("Role");

        ListView<String> list = new ListView<>();
        refreshDoctorList(list);

        Button add = new Button("Register Doctor");
        add.setOnAction(e -> {
            try {
                doctors.add(new Doctor(nameIn.getText(), Integer.parseInt(ageIn.getText()), genIn.getValue(), specIn.getValue(), roleIn.getValue()));
                refreshDoctorList(list);
            } catch (Exception ex) { showAlert("Error", "Invalid inputs. Check all fields."); }
        });

        Button back = new Button("Back"); back.setOnAction(e -> showMainMenu());
        layout.getChildren().addAll(new Label("Add Doctor:"), nameIn, ageIn, genIn, specIn, roleIn, add, new Label("Registered Doctors:"), list, back);
        primaryStage.setScene(new Scene(layout, 900, 750));
    }

    private void refreshDoctorList(ListView<String> lv) {
        List<String> items = new ArrayList<>();
        for(Doctor d : doctors) items.add(d.getAssignmentDetails() + " | Age: " + d.getAge());
        lv.setItems(FXCollections.observableArrayList(items));
    }

    // --- WARD MANAGEMENT ---
    private void showManageWards() {
        VBox layout = new VBox(10);
        layout.setPadding(new Insets(20));
        layout.setStyle(BG_COLOR);

        ComboBox<String> nameIn = new ComboBox<>(FXCollections.observableArrayList("General Ward 2", "Orthopedic Ward", "Emergency Ward 2", "Cardiology Ward 2"));
        nameIn.setPromptText("Ward Selection");
        TextField limitIn = new TextField(); limitIn.setPromptText("Patient Limit");

        ListView<String> list = new ListView<>();
        refreshWardList(list);

        Button add = new Button("Add Ward");
        add.setOnAction(e -> {
            try {
                wards.add(new Ward(nameIn.getValue(), Integer.parseInt(limitIn.getText())));
                refreshWardList(list);
            } catch (Exception ex) { showAlert("Error", "Enter numeric capacity."); }
        });

        Button back = new Button("Back"); back.setOnAction(e -> showMainMenu());
        layout.getChildren().addAll(new Label("New Ward:"), nameIn, limitIn, add, new Label("Available Wards:"), list, back);
        primaryStage.setScene(new Scene(layout, 900, 750));
    }

    private void refreshWardList(ListView<String> lv) {
        List<String> items = new ArrayList<>();
        for (Ward w : wards) items.add(w.getName() + " [Capacity: " + w.getLimit() + " | Occupied: " + w.getCurrentOccupancy() + "]");
        lv.setItems(FXCollections.observableArrayList(items));
    }

    // --- STAFF MANAGEMENT ---
    private void showManageStaff() {
        VBox layout = new VBox(10);
        layout.setPadding(new Insets(20));
        layout.setStyle(BG_COLOR);

        TextField nameIn = new TextField(); nameIn.setPromptText("Staff Name");
        ComboBox<String> shiftIn = new ComboBox<>(FXCollections.observableArrayList("Morning", "Evening", "Night"));
        shiftIn.setPromptText("Shift Selection");

        ListView<String> list = new ListView<>();
        refreshStaffList(list);

        Button add = new Button("Assign Staff");
        add.setOnAction(e -> {
            staffMembers.add(new Staff(nameIn.getText(), 30, "N/A", shiftIn.getValue()));
            refreshStaffList(list);
        });

        Button back = new Button("Back"); back.setOnAction(e -> showMainMenu());
        layout.getChildren().addAll(new Label("Staff Form:"), nameIn, shiftIn, add, new Label("Current Staff:"), list, back);
        primaryStage.setScene(new Scene(layout, 900, 750));
    }

    private void refreshStaffList(ListView<String> lv) {
        List<String> items = new ArrayList<>();
        for(Staff s : staffMembers) items.add(s.getName() + " | Shift: " + s.getShift());
        lv.setItems(FXCollections.observableArrayList(items));
    }

    // --- PATIENT MANAGEMENT ---
    private void showManagePatients() {
        VBox layout = new VBox(10);
        layout.setPadding(new Insets(20));
        layout.setStyle(BG_COLOR);

        TextField nameIn = new TextField(); nameIn.setPromptText("Patient Name");
        TextField illnessIn = new TextField(); illnessIn.setPromptText("Diagnosis");
        ComboBox<String> wardIn = new ComboBox<>();
        for(Ward w : wards) wardIn.getItems().add(w.getName());
        wardIn.setPromptText("Admit To Ward");

        ListView<String> list = new ListView<>();
        refreshPatientList(list);

        Button admit = new Button("Process Admission");
        admit.setOnAction(e -> {
            String wName = wardIn.getValue();
            Ward target = wards.stream().filter(w -> w.getName().equals(wName)).findFirst().orElse(null);

            if(target != null) {
                if(target.getCurrentOccupancy() >= target.getLimit()) {
                    showAlert("Ward Full", "This ward has reached its maximum capacity.");
                } else if(validatePatientAdmission(wName)) {
                    patients.add(new Patient(nameIn.getText(), 25, "Male", illnessIn.getText(), wName));
                    target.incrementOccupancy();
                    refreshPatientList(list);
                }
            }
        });

        Button back = new Button("Back"); back.setOnAction(e -> showMainMenu());
        layout.getChildren().addAll(new Label("Admission Form:"), nameIn, illnessIn, wardIn, admit, new Label("Admitted Patients:"), list, back);
        primaryStage.setScene(new Scene(layout, 900, 750));
    }

    private void refreshPatientList(ListView<String> lv) {
        List<String> items = new ArrayList<>();
        for(Patient p : patients) items.add(p.getName() + " | Illness: " + p.getIllness() + " | Ward: " + p.getAssignedWard());
        lv.setItems(FXCollections.observableArrayList(items));
    }

    private boolean validatePatientAdmission(String wardName) {
        boolean hasSenior = doctors.stream().anyMatch(d -> d.getRole().equals("Senior Doctor"));
        boolean hasAssistant = doctors.stream().anyMatch(d -> d.getRole().equals("Assistant Doctor") && wardName.contains(d.getSpecialization()));
        boolean hasStaff = !staffMembers.isEmpty();

        if (!hasSenior) { showAlert("Protocol Error", "Hospital must have at least 1 Senior Doctor."); return false; }
        if (!hasAssistant) { showAlert("Protocol Error", "Ward specialized field requires 1 Assistant Doctor."); return false; }
        if (!hasStaff) { showAlert("Protocol Error", "Ward requires staff presence."); return false; }
        
        return true;
    }

    private void showRecordsPopup() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("System Data");
        alert.setHeaderText("Live IQRA Hospital Summary");
        alert.setContentText("Total Doctors: " + doctors.size() + 
                           "\nTotal Patients: " + patients.size() + 
                           "\nTotal Staff: " + staffMembers.size() + 
                           "\nTotal Wards: " + wards.size());
        alert.showAndWait();
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}