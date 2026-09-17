package service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import model.Claim;

public class ClaimManager {
    private final String filePath;
    private final List<Claim> claims = new ArrayList();

    public ClaimManager(String filePath) {
        this.filePath = filePath;
        this.loadClaims();
    }

    private void loadClaims() {
        this.claims.clear();
        File file = new File(this.filePath);
        if (file.exists()) {
            String line;
            try (BufferedReader br = new BufferedReader(new FileReader(file))) {
                while((line = br.readLine()) != null) {
                    if (!line.trim().isEmpty()) {
                        String[] p = line.split("\\|", -1);
                        if (p.length >= 6) {
                            this.claims.add(new Claim(Integer.parseInt(p[0]), Integer.parseInt(p[1]), Integer.parseInt(p[2]), p[3], p[4], p[5]));
                        }
                    }
                }
            } catch (Exception e) {
                System.out.println("Could not load claims: " + e.getMessage());
            }

        }
    }

    public Claim addClaim(int itemId, int userId, String reason, String date) {
        Claim claim = new Claim(this.getNextId(), itemId, userId, reason, "PENDING", date);
        this.claims.add(claim);
        this.saveClaims();
        return claim;
    }

    public List<Claim> getAllClaims() {
        return new ArrayList(this.claims);
    }

    public List<Claim> getClaimsByUser(int userId) {
        List<Claim> result = new ArrayList();

        for(Claim claim : this.claims) {
            if (claim.getUserId() == userId) {
                result.add(claim);
            }
        }

        return result;
    }

    public Claim findById(int id) {
        for(Claim claim : this.claims) {
            if (claim.getId() == id) {
                return claim;
            }
        }

        return null;
    }

    public boolean updateStatus(int claimId, String status) {
        Claim claim = this.findById(claimId);
        if (claim == null) {
            return false;
        } else {
            claim.setStatus(status);
            this.saveClaims();
            return true;
        }
    }

    private int getNextId() {
        int max = 0;

        for(Claim claim : this.claims) {
            if (claim.getId() > max) {
                max = claim.getId();
            }
        }

        return max + 1;
    }

    private void saveClaims() {
        try (PrintWriter out = new PrintWriter(new FileWriter(this.filePath))) {
            for(Claim claim : this.claims) {
                out.println(claim.toFileString());
            }
        } catch (IOException e) {
            System.out.println("Could not save claims: " + e.getMessage());
        }

    }
}
