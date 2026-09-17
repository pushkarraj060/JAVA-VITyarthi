package model;

public class Claim {
    private int id;
    private int itemId;
    private int userId;
    private String reason;
    private String status;
    private String date;

    public Claim(int id, int itemId, int userId, String reason, String status, String date) {
        this.id = id;
        this.itemId = itemId;
        this.userId = userId;
        this.reason = reason;
        this.status = status;
        this.date = date;
    }

    public int getId() {
        return this.id;
    }

    public int getItemId() {
        return this.itemId;
    }

    public int getUserId() {
        return this.userId;
    }

    public String getReason() {
        return this.reason;
    }

    public String getStatus() {
        return this.status;
    }

    public String getDate() {
        return this.date;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String toFileString() {
        int var10000 = this.id;
        return var10000 + "|" + this.itemId + "|" + this.userId + "|" + this.clean(this.reason) + "|" + this.clean(this.status) + "|" + this.clean(this.date);
    }

    private String clean(String value) {
        return value == null ? "" : value.replace("|", "/");
    }

    public String toString() {
        return "Claim ID: " + this.id + " | Item ID: " + this.itemId + " | User ID: " + this.userId + " | Status: " + this.status + " | Date: " + this.date + "\nReason: " + this.reason;
    }
}
