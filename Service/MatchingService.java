package service;

import java.util.ArrayList;
import java.util.List;
import model.Item;

public class MatchingService {
    public List<String> findMatches(Item target, List<Item> allItems) {
        List<String> results = new ArrayList();

        for(Item other : allItems) {
            if (other.getId() != target.getId() && !other.getStatus().equalsIgnoreCase("RETURNED")) {
                boolean opposite = target.getType().equalsIgnoreCase("LOST") && other.getType().equalsIgnoreCase("FOUND") || target.getType().equalsIgnoreCase("FOUND") && other.getType().equalsIgnoreCase("LOST");
                if (opposite) {
                    int score = this.calculateScore(target, other);
                    if (score >= 50) {
                        results.add("Possible Match: Item #" + other.getId() + " | Score: " + score + "%\n" + other.toString());
                    }
                }
            }
        }

        return results;
    }

    public int calculateScore(Item a, Item b) {
        int score = 0;
        if (this.same(a.getName(), b.getName())) {
            score += 30;
        } else if (this.containsSimilarWord(a.getName(), b.getName())) {
            score += 15;
        }

        if (this.same(a.getCategory(), b.getCategory())) {
            score += 20;
        }

        if (this.same(a.getColor(), b.getColor())) {
            score += 10;
        }

        if (this.same(a.getLocation(), b.getLocation())) {
            score += 20;
        }

        if (this.same(a.getDate(), b.getDate())) {
            score += 20;
        }

        return score;
    }

    private boolean same(String a, String b) {
        return a != null && b != null && a.trim().equalsIgnoreCase(b.trim());
    }

    private boolean containsSimilarWord(String a, String b) {
        String[] words = a.toLowerCase().split("\\s+");
        String lowerB = b.toLowerCase();

        for(String word : words) {
            if (word.length() >= 3 && lowerB.contains(word)) {
                return true;
            }
        }

        return false;
    }
}
