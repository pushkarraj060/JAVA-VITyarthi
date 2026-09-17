package service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import model.Item;

public class ItemManager {
    private final String filePath;
    private final List<Item> items = new ArrayList();

    public ItemManager(String filePath) {
        this.filePath = filePath;
        this.loadItems();
    }

    private void loadItems() {
        this.items.clear();
        File file = new File(this.filePath);
        if (file.exists()) {
            String line;
            try (BufferedReader br = new BufferedReader(new FileReader(file))) {
                while((line = br.readLine()) != null) {
                    if (!line.trim().isEmpty()) {
                        String[] p = line.split("\\|", -1);
                        if (p.length >= 10) {
                            this.items.add(new Item(Integer.parseInt(p[0]), Integer.parseInt(p[1]), p[2], p[3], p[4], p[5], p[6], p[7], p[8], p[9]));
                        }
                    }
                }
            } catch (Exception e) {
                System.out.println("Could not load items: " + e.getMessage());
            }

        }
    }

    public Item addItem(int reportedBy, String type, String name, String category, String color, String location, String date, String description) {
        type = type.toUpperCase();
        Item item = new Item(this.getNextId(), reportedBy, type, name, category, color, location, date, description, "ACTIVE");
        this.items.add(item);
        this.saveItems();
        return item;
    }

    public boolean deleteItem(int itemId, int userId, boolean admin) {
        for(int i = 0; i < this.items.size(); ++i) {
            Item item = (Item)this.items.get(i);
            if (item.getId() == itemId && (admin || item.getReportedBy() == userId)) {
                this.items.remove(i);
                this.saveItems();
                return true;
            }
        }

        return false;
    }

    public Item findById(int id) {
        for(Item item : this.items) {
            if (item.getId() == id) {
                return item;
            }
        }

        return null;
    }

    public List<Item> getAllItems() {
        return new ArrayList(this.items);
    }

    public List<Item> getItemsByUser(int userId) {
        List<Item> result = new ArrayList();

        for(Item item : this.items) {
            if (item.getReportedBy() == userId) {
                result.add(item);
            }
        }

        return result;
    }

    public List<Item> search(String keyword) {
        List<Item> result = new ArrayList();
        keyword = keyword.toLowerCase();

        for(Item item : this.items) {
            if (item.getName().toLowerCase().contains(keyword) || item.getCategory().toLowerCase().contains(keyword) || item.getColor().toLowerCase().contains(keyword) || item.getLocation().toLowerCase().contains(keyword) || item.getDescription().toLowerCase().contains(keyword)) {
                result.add(item);
            }
        }

        return result;
    }

    public List<Item> getLostItems() {
        return this.getByType("LOST");
    }

    public List<Item> getFoundItems() {
        return this.getByType("FOUND");
    }

    private List<Item> getByType(String type) {
        List<Item> result = new ArrayList();

        for(Item item : this.items) {
            if (item.getType().equalsIgnoreCase(type) && item.getStatus().equalsIgnoreCase("ACTIVE")) {
                result.add(item);
            }
        }

        return result;
    }

    public boolean markReturned(int itemId) {
        Item item = this.findById(itemId);
        if (item == null) {
            return false;
        } else {
            item.setStatus("RETURNED");
            this.saveItems();
            return true;
        }
    }

    public void saveItems() {
        try (PrintWriter out = new PrintWriter(new FileWriter(this.filePath))) {
            for(Item item : this.items) {
                out.println(item.toFileString());
            }
        } catch (IOException e) {
            System.out.println("Could not save items: " + e.getMessage());
        }

    }

    private int getNextId() {
        int max = 0;

        for(Item item : this.items) {
            if (item.getId() > max) {
                max = item.getId();
            }
        }

        return max + 1;
    }
}
