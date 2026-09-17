package model;

public class Item {
    private int id;
    private int reportedBy;
    private String type;
    private String name;
    private String category;
    private String color;
    private String location;
    private String date;
    private String description;
    private String status;

    public Item(int id, int reportedBy, String type, String name, String category, String color, String location, String date, String description, String status) {
        this.id = id;
        this.reportedBy = reportedBy;
        this.type = type;
        this.name = name;
        this.category = category;
        this.color = color;
        this.location = location;
        this.date = date;
        this.description = description;
        this.status = status;
    }

    public int getId() {
        return this.id;
    }

    public int getReportedBy() {
        return this.reportedBy;
    }

    public String getType() {
        return this.type;
    }

    public String getName() {
        return this.name;
    }

    public String getCategory() {
        return this.category;
    }

    public String getColor() {
        return this.color;
    }

    public String getLocation() {
        return this.location;
    }

    public String getDate() {
        return this.date;
    }

    public String getDescription() {
        return this.description;
    }

    public String getStatus() {
        return this.status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String toFileString() {
        int var10000 = this.id;
        return var10000 + "|" + this.reportedBy + "|" + this.clean(this.type) + "|" + this.clean(this.name) + "|" + this.clean(this.category) + "|" + this.clean(this.color) + "|" + this.clean(this.location) + "|" + this.clean(this.date) + "|" + this.clean(this.description) + "|" + this.clean(this.status);
    }

    private String clean(String value) {
        return value == null ? "" : value.replace("|", "/");
    }

    public String toString() {
        return "\nItem ID: " + this.id + "\nType: " + this.type + "\nItem: " + this.name + "\nCategory: " + this.category + "\nColor: " + this.color + "\nLocation: " + this.location + "\nDate: " + this.date + "\nDescription: " + this.description + "\nStatus: " + this.status + "\nReported By User ID: " + this.reportedBy + "\n----------------------------------------";
    }
}
