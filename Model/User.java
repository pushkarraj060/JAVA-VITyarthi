package model;

public class User {
    private int id;
    private String name;
    private String studentId;
    private String email;
    private String password;
    private String phone;
    private String role;

    public User(int id, String name, String studentId, String email, String password, String phone, String role) {
        this.id = id;
        this.name = name;
        this.studentId = studentId;
        this.email = email;
        this.password = password;
        this.phone = phone;
        this.role = role;
    }

    public int getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public String getStudentId() {
        return this.studentId;
    }

    public String getEmail() {
        return this.email;
    }

    public String getPassword() {
        return this.password;
    }

    public String getPhone() {
        return this.phone;
    }

    public String getRole() {
        return this.role;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String toFileString() {
        int var10000 = this.id;
        return var10000 + "|" + this.clean(this.name) + "|" + this.clean(this.studentId) + "|" + this.clean(this.email) + "|" + this.clean(this.password) + "|" + this.clean(this.phone) + "|" + this.clean(this.role);
    }

    private String clean(String value) {
        return value == null ? "" : value.replace("|", "/");
    }

    public String toString() {
        return "ID: " + this.id + " | Name: " + this.name + " | Student ID: " + this.studentId + " | Email: " + this.email + " | Phone: " + this.phone + " | Role: " + this.role;
    }
}
