package service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import model.User;

public class AuthenticationService {
    private final String filePath;
    private final List<User> users = new ArrayList();

    public AuthenticationService(String filePath) {
        this.filePath = filePath;
        this.loadUsers();
        this.createDefaultAdmin();
    }

    private void loadUsers() {
        this.users.clear();
        File file = new File(this.filePath);
        if (file.exists()) {
            String line;
            try (BufferedReader br = new BufferedReader(new FileReader(file))) {
                while((line = br.readLine()) != null) {
                    if (!line.trim().isEmpty()) {
                        String[] p = line.split("\\|", -1);
                        if (p.length >= 7) {
                            this.users.add(new User(Integer.parseInt(p[0]), p[1], p[2], p[3], p[4], p[5], p[6]));
                        }
                    }
                }
            } catch (Exception e) {
                System.out.println("Could not load users: " + e.getMessage());
            }

        }
    }

    private void createDefaultAdmin() {
        if (this.findByEmail("admin@campus.com") == null) {
            int id = this.getNextId();
            this.users.add(new User(id, "System Admin", "ADMIN001", "admin@campus.com", "admin123", "0000000000", "ADMIN"));
            this.saveUsers();
        }

    }

    public User register(String name, String studentId, String email, String password, String phone) {
        if (this.findByEmail(email) == null && this.findByStudentId(studentId) == null) {
            User user = new User(this.getNextId(), name, studentId, email, password, phone, "STUDENT");
            this.users.add(user);
            this.saveUsers();
            return user;
        } else {
            return null;
        }
    }

    public User login(String email, String password) {
        for(User user : this.users) {
            if (user.getEmail().equalsIgnoreCase(email) && user.getPassword().equals(password)) {
                return user;
            }
        }

        return null;
    }

    public User findById(int id) {
        for(User user : this.users) {
            if (user.getId() == id) {
                return user;
            }
        }

        return null;
    }

    public User findByEmail(String email) {
        for(User user : this.users) {
            if (user.getEmail().equalsIgnoreCase(email)) {
                return user;
            }
        }

        return null;
    }

    public User findByStudentId(String studentId) {
        for(User user : this.users) {
            if (user.getStudentId().equalsIgnoreCase(studentId)) {
                return user;
            }
        }

        return null;
    }

    public List<User> getAllUsers() {
        return new ArrayList(this.users);
    }

    private int getNextId() {
        int max = 0;

        for(User user : this.users) {
            if (user.getId() > max) {
                max = user.getId();
            }
        }

        return max + 1;
    }

    private void saveUsers() {
        try (PrintWriter out = new PrintWriter(new FileWriter(this.filePath))) {
            for(User user : this.users) {
                out.println(user.toFileString());
            }
        } catch (IOException e) {
            System.out.println("Could not save users: " + e.getMessage());
        }

    }
}
