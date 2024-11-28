package org.example;

import java.io.Serializable;
import java.util.List;

public class GameState implements Serializable {
    private int playerHP;
    private int playerXP;
    private int playerLevel;
    private int soulSpheres;
    private int divineSpheres;
    private int playerX;
    private int playerY;
    private AngelDemon[][] creatures; // Assuming this is a 2D grid of creatures, can be null if no creature at a position.
    private List<AngelDemon> capturedCreatures;
    private String playerName; // Added to save the player's name
    private String password; // Encrypted password for authentication

    // Constructors, getters, setters and any other methods you might need

    public GameState() {
        // Default constructor
    }

    public int getPlayerHP() {
        return playerHP;
    }

    public void setPlayerHP(int playerHP) {
        this.playerHP = playerHP;
    }

    // ... Add getters and setters for all the other fields ...

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public String getPassword() {
        return password;
    }

    // For security, encrypt the password before setting it
    public void setPassword(String password) {
        // Simple encryption can be added here; for real-world applications, use stronger methods
        this.password = encryptPassword(password);
    }

    // For security, decrypt the password before using it
    public String getDecryptedPassword() {
        // Decrypt password before returning; for real-world applications, use stronger methods
        return decryptPassword(password);
    }

    private String encryptPassword(String password) {
        // Placeholder encryption; this is not a real encryption method and should be replaced with a more secure one.
        return password;
    }

    private String decryptPassword(String encryptedPassword) {
        // Placeholder decryption; this is not a real decryption method and should be replaced with a more secure one.
        return encryptedPassword;
    }
}
