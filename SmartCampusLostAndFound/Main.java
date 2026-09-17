package SmartCampusLostAndFound;

import java.io.File;
import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
import model.Claim;
import model.Item;
import model.User;
import service.AuthenticationService;
import service.ClaimManager;
import service.ItemManager;
import service.MatchingService;

public class Main {
    private static final Scanner sc;
    private static final String DATA_DIR = "SmartCampusLostAndFound";
    private static final String USERS_FILE;
    private static final String ITEMS_FILE;
    private static final String CLAIMS_FILE;
    private static AuthenticationService auth;
    private static ItemManager itemManager;
    private static ClaimManager claimManager;
    private static MatchingService matchingService;

    public static void main(String[] args) {
        createDataDirectory();
        auth = new AuthenticationService(USERS_FILE);
        itemManager = new ItemManager(ITEMS_FILE);
        claimManager = new ClaimManager(CLAIMS_FILE);
        matchingService = new MatchingService();

        while(true) {
            printHeader("SMART CAMPUS LOST & FOUND");
            System.out.println("1. Register");
            System.out.println("2. Login");
            System.out.println("3. Exit");
            int choice = readInt("Enter choice: ");
            if (choice == 1) {
                register();
            } else if (choice == 2) {
                User user = login();
                if (user != null) {
                    if (user.getRole().equalsIgnoreCase("ADMIN")) {
                        adminMenu(user);
                    } else {
                        studentMenu(user);
                    }
                }
            } else {
                if (choice == 3) {
                    System.out.println("\nThank you for using Smart Campus Lost & Found!");
                    sc.close();
                    return;
                }

                System.out.println("Invalid choice.");
            }
        }
    }

    private static void createDataDirectory() {
        File dir = new File("SmartCampusLostAndFound");
        if (!dir.exists()) {
            dir.mkdirs();
        }

    }

    private static void register() {
        printHeader("STUDENT REGISTRATION");
        String name = readNonEmpty("Name: ");
        String studentId = readNonEmpty("Student ID: ");
        String email = readNonEmpty("Email: ");
        String password = readNonEmpty("Password: ");
        String phone = readNonEmpty("Phone: ");
        if (!email.contains("@")) {
            System.out.println("Invalid email.");
        } else {
            User user = auth.register(name, studentId, email, password, phone);
            if (user == null) {
                System.out.println("Registration failed. Email or Student ID already exists.");
            } else {
                System.out.println("Registration successful!");
                System.out.println("Your User ID is: " + user.getId());
            }

        }
    }

    private static User login() {
        printHeader("LOGIN");
        String email = readNonEmpty("Email: ");
        String password = readNonEmpty("Password: ");
        User user = auth.login(email, password);
        if (user == null) {
            System.out.println("Invalid email or password.");
        } else {
            System.out.println("Login successful. Welcome, " + user.getName() + "!");
        }

        return user;
    }

    private static void studentMenu(User user) {
        while(true) {
            printHeader("STUDENT DASHBOARD");
            System.out.println("Welcome, " + user.getName());
            System.out.println("1. Report Lost Item");
            System.out.println("2. Report Found Item");
            System.out.println("3. Search Items");
            System.out.println("4. View My Reports");
            System.out.println("5. Find Possible Matches");
            System.out.println("6. Submit Claim");
            System.out.println("7. View My Claims");
            System.out.println("8. Logout");
            int choice = readInt("Enter choice: ");
            switch (choice) {
                case 1:
                    reportItem(user, "LOST");
                    break;
                case 2:
                    reportItem(user, "FOUND");
                    break;
                case 3:
                    searchItems();
                    break;
                case 4:
                    viewMyReports(user);
                    break;
                case 5:
                    findMatches(user);
                    break;
                case 6:
                    submitClaim(user);
                    break;
                case 7:
                    viewMyClaims(user);
                    break;
                case 8:
                    return;
                default:
                    System.out.println("Invalid choice.");
            }
        }
    }

    private static void adminMenu(User admin) {
        while(true) {
            printHeader("ADMIN DASHBOARD");
            System.out.println("1. View All Users");
            System.out.println("2. View All Items");
            System.out.println("3. View Lost Items");
            System.out.println("4. View Found Items");
            System.out.println("5. View All Claims");
            System.out.println("6. Approve Claim");
            System.out.println("7. Reject Claim");
            System.out.println("8. Mark Item as Returned");
            System.out.println("9. Delete Item");
            System.out.println("10. View Statistics");
            System.out.println("11. Logout");
            int choice = readInt("Enter choice: ");
            switch (choice) {
                case 1:
                    viewAllUsers();
                    break;
                case 2:
                    printItems(itemManager.getAllItems());
                    break;
                case 3:
                    printItems(itemManager.getLostItems());
                    break;
                case 4:
                    printItems(itemManager.getFoundItems());
                    break;
                case 5:
                    viewAllClaims();
                    break;
                case 6:
                    processClaim(true);
                    break;
                case 7:
                    processClaim(false);
                    break;
                case 8:
                    markReturned();
                    break;
                case 9:
                    deleteItem(admin);
                    break;
                case 10:
                    statistics();
                    break;
                case 11:
                    return;
                default:
                    System.out.println("Invalid choice.");
            }
        }
    }

    private static void reportItem(User user, String type) {
        printHeader("REPORT " + type + " ITEM");
        String name = readNonEmpty("Item name: ");
        String category = readNonEmpty("Category: ");
        String color = readNonEmpty("Color: ");
        String location = readNonEmpty("Location: ");
        String date = readNonEmpty("Date (DD-MM-YYYY): ");
        String description = readNonEmpty("Description: ");
        Item item = itemManager.addItem(user.getId(), type, name, category, color, location, date, description);
        System.out.println("\nItem reported successfully!");
        System.out.println("Item ID: " + item.getId());
        List<String> matches = matchingService.findMatches(item, itemManager.getAllItems());
        if (!matches.isEmpty()) {
            System.out.println("\n*** POSSIBLE MATCHES FOUND ***");

            for(String match : matches) {
                System.out.println(match);
            }
        }

    }

    private static void searchItems() {
        printHeader("SEARCH ITEMS");
        String keyword = readNonEmpty("Enter keyword: ");
        List<Item> results = itemManager.search(keyword);
        if (results.isEmpty()) {
            System.out.println("No matching items found.");
        } else {
            printItems(results);
        }

    }

    private static void viewMyReports(User user) {
        printHeader("MY REPORTS");
        List<Item> items = itemManager.getItemsByUser(user.getId());
        printItems(items);
    }

    private static void findMatches(User user) {
        printHeader("FIND POSSIBLE MATCHES");
        List<Item> myItems = itemManager.getItemsByUser(user.getId());
        if (myItems.isEmpty()) {
            System.out.println("You have no reports.");
        } else {
            for(Item item : myItems) {
                PrintStream var10000 = System.out;
                int var10001 = item.getId();
                var10000.println("\nYour Item #" + var10001 + " - " + item.getName());
                List<String> matches = matchingService.findMatches(item, itemManager.getAllItems());
                if (matches.isEmpty()) {
                    System.out.println("No possible matches found.");
                } else {
                    for(String match : matches) {
                        System.out.println(match);
                    }
                }
            }

        }
    }

    private static void submitClaim(User user) {
        printHeader("SUBMIT CLAIM");
        int itemId = readInt("Enter found item ID: ");
        Item item = itemManager.findById(itemId);
        if (item == null) {
            System.out.println("Item not found.");
        } else if (!item.getType().equalsIgnoreCase("FOUND")) {
            System.out.println("You can only claim a FOUND item.");
        } else if (item.getReportedBy() == user.getId()) {
            System.out.println("You cannot claim your own report.");
        } else {
            String reason = readNonEmpty("Why do you believe this item belongs to you? ");
            Claim claim = claimManager.addClaim(itemId, user.getId(), reason, currentDate());
            System.out.println("Claim submitted successfully!");
            System.out.println("Claim ID: " + claim.getId());
            System.out.println("Status: PENDING");
        }
    }

    private static void viewMyClaims(User user) {
        printHeader("MY CLAIMS");
        List<Claim> claims = claimManager.getClaimsByUser(user.getId());
        if (claims.isEmpty()) {
            System.out.println("You have not submitted any claims.");
        } else {
            for(Claim claim : claims) {
                System.out.println(claim);
                System.out.println("----------------------------------------");
            }

        }
    }

    private static void viewAllUsers() {
        printHeader("ALL USERS");

        for(User user : auth.getAllUsers()) {
            System.out.println(user);
        }

    }

    private static void viewAllClaims() {
        printHeader("ALL CLAIMS");
        List<Claim> claims = claimManager.getAllClaims();
        if (claims.isEmpty()) {
            System.out.println("No claims found.");
        } else {
            for(Claim claim : claims) {
                System.out.println(claim);
                System.out.println("----------------------------------------");
            }

        }
    }

    private static void processClaim(boolean approve) {
        printHeader(approve ? "APPROVE CLAIM" : "REJECT CLAIM");
        int claimId = readInt("Enter Claim ID: ");
        Claim claim = claimManager.findById(claimId);
        if (claim == null) {
            System.out.println("Claim not found.");
        } else if (!claim.getStatus().equalsIgnoreCase("PENDING")) {
            System.out.println("This claim has already been processed.");
        } else {
            String status = approve ? "APPROVED" : "REJECTED";
            claimManager.updateStatus(claimId, status);
            if (approve) {
                itemManager.markReturned(claim.getItemId());
            }

            System.out.println("Claim " + status + " successfully.");
        }
    }

    private static void markReturned() {
        printHeader("MARK ITEM AS RETURNED");
        int itemId = readInt("Enter Item ID: ");
        if (itemManager.markReturned(itemId)) {
            System.out.println("Item marked as RETURNED.");
        } else {
            System.out.println("Item not found.");
        }

    }

    private static void deleteItem(User admin) {
        printHeader("DELETE ITEM");
        int itemId = readInt("Enter Item ID: ");
        if (itemManager.deleteItem(itemId, admin.getId(), true)) {
            System.out.println("Item deleted.");
        } else {
            System.out.println("Item not found.");
        }

    }

    private static void statistics() {
        printHeader("SYSTEM STATISTICS");
        int users = auth.getAllUsers().size();
        int totalItems = itemManager.getAllItems().size();
        int lost = itemManager.getLostItems().size();
        int found = itemManager.getFoundItems().size();
        int returned = 0;

        for(Item item : itemManager.getAllItems()) {
            if (item.getStatus().equalsIgnoreCase("RETURNED")) {
                ++returned;
            }
        }

        int pending = 0;
        int approved = 0;

        for(Claim claim : claimManager.getAllClaims()) {
            if (claim.getStatus().equalsIgnoreCase("PENDING")) {
                ++pending;
            }

            if (claim.getStatus().equalsIgnoreCase("APPROVED")) {
                ++approved;
            }
        }

        System.out.println("Total Users       : " + users);
        System.out.println("Total Items       : " + totalItems);
        System.out.println("Active Lost       : " + lost);
        System.out.println("Active Found      : " + found);
        System.out.println("Items Returned    : " + returned);
        System.out.println("Pending Claims    : " + pending);
        System.out.println("Approved Claims   : " + approved);
    }

    private static void printItems(List<Item> items) {
        if (items.isEmpty()) {
            System.out.println("No items found.");
        } else {
            for(Item item : items) {
                System.out.println(item);
            }

        }
    }

    private static String currentDate() {
        return (new SimpleDateFormat("dd-MM-yyyy")).format(new Date());
    }

    private static int readInt(String message) {
        while(true) {
            try {
                System.out.print(message);
                return Integer.parseInt(sc.nextLine().trim());
            } catch (Exception var2) {
                System.out.println("Please enter a valid number.");
            }
        }
    }

    private static String readNonEmpty(String message) {
        while(true) {
            System.out.print(message);
            String value = sc.nextLine().trim();
            if (!value.isEmpty()) {
                return value;
            }

            System.out.println("This field cannot be empty.");
        }
    }

    private static void printHeader(String title) {
        System.out.println("\n==============================================");
        System.out.println("       " + title);
        System.out.println("==============================================");
    }

    static {
        sc = new Scanner(System.in);
        USERS_FILE = "SmartCampusLostAndFound" + File.separator + "users.txt";
        ITEMS_FILE = "SmartCampusLostAndFound" + File.separator + "items.txt";
        CLAIMS_FILE = "SmartCampusLostAndFound" + File.separator + "claims.txt";
    }
}
