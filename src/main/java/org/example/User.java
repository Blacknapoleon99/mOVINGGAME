package org.example;

import java.io.Serializable;
import javax.swing.*;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.awt.event.ActionListener;

public class User implements Serializable {
    private String username;
    private String hashedPassword;
    private GameState gameState;

    public User(String username, String hashedPassword, GameState gameState) {
        this.username = username;
        this.hashedPassword = hashedPassword;
        this.gameState = gameState;
    }

    public String getUsername() {
        return username;
    }

    public String getHashedPassword() {
        return hashedPassword;
    }

    public GameState getGameState() {
        return gameState;
    }
}
