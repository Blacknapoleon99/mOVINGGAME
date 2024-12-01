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
    private AngelDemon[][] creatures;
    private List<AngelDemon> capturedCreatures;
    private String playerName;
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

        this.password = encryptPassword(password);
    }

    // For security, decrypt the password before using it
    public String getDecryptedPassword() {

        return decryptPassword(password);
    }

    private String encryptPassword(String password) {

        return password;
    }

    private String decryptPassword(String encryptedPassword) {

        return encryptedPassword;
    }
}
