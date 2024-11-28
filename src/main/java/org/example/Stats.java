package org.example;
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

public class Stats {
    private int attack;
    private int defense;
    private int speed;

    public Stats(int attack, int defense, int speed) {
        this.attack = attack;
        this.defense = defense;
        this.speed = speed;
    }

    public int getAttack() {
        return attack;
    }

    public int getDefense() {
        return defense;
    }

    public int getSpeed() {
        return speed;
    }
}
// Path: src/main/java/org/example/Stats.java