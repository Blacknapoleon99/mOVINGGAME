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
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.JOptionPane;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.DefaultListModel;


public class AngelDemonGame {
    

    private GameState gameState;
    private JFrame frame, mainMenuFrame, loadMenuFrame;
    private JPanel infoPanel;
    private JLabel playerHPLabel, creatureHPLabel, playerLevelLabel, playerXPLabel, soulSphereLabel, divineSphereLabel;
    private JButton[][] grid;
    private AngelDemon[][] creatures;
    private int playerX, playerY, creaturesRemaining, playerHP = 100, playerXP = 0, playerLevel = 1, soulSpheres = 5, divineSpheres = 0;
    private final int XP_PER_LEVEL = 100;
    private List<AngelDemon> capturedCreatures = new ArrayList<>();
    private String[] playerAbilities = {"Strike", "Heal", "Power Attack", "Soul Sucker"};
    private int[] abilityDamage = {10, -20, 20, 40};
    private boolean isSoulSuckerCharged = false;
    private String playerName;




    String imagePath = "C:\\GitHub\\mOVINGGAME\\src\\main\\resources\\Images\\player.png";
    ImageIcon playerIcon = new ImageIcon(imagePath);
    JLabel playerLabel = new JLabel(playerIcon);

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new AngelDemonGame());
    }

    static class AngelDemon implements Serializable {
        private String type;
        private int HP, maxHP, level;
        private boolean isAngel;

        public AngelDemon(String type, int HP, int level, boolean isAngel) {
            this.type = type;
            this.HP = HP;
            this.maxHP = HP;
            this.level = level;
            this.isAngel = isAngel;
        }

        public boolean isAngel() {
            return isAngel;
        }

        public int getHP() {
            return HP;
        }

        public void takeDamage(int damage) {
            this.HP -= damage;
            if (this.HP < 0) {
                this.HP = 0;
            }
        }

        public int getLevel() {
            return level;
        }

        public int attack() {
            return level * (isAngel ? 5 : 8);
        }

        @Override
        public String toString() {
            return type;
        }

        public static AngelDemon randomCreature(int playerLevel) {
            String[] angelTypes = {"Seraphim", "Cherubim", "Thrones", "Dominions", "Virtues", "Powers", "Principalities", "Archangels", "Angels"};
            String[] demonTypes = {"Lucifer", "Beelzebub", "Satan", "Abaddon", "Mammon", "Belphegor", "Asmodeus", "Leviathan"};
            boolean isAngel = new Random().nextBoolean();
            String type = isAngel ? angelTypes[new Random().nextInt(angelTypes.length)] : demonTypes[new Random().nextInt(demonTypes.length)];
            int HP = 50 + new Random().nextInt(50) + playerLevel * 5;
            int level = playerLevel + new Random().nextInt(3) - 1;
            return new AngelDemon(type, HP, level, isAngel);
        }
    }

    private void initGame() {
        frame = new JFrame("Catch Angels & Demons");
        frame.setSize(1100, 1100);
        frame.setLayout(new BorderLayout());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        infoPanel = new JPanel();
        playerHPLabel = new JLabel("Player HP: " + playerHP);
        creatureHPLabel = new JLabel("Creature HP: - ");
        playerLevelLabel = new JLabel("Player Level: " + playerLevel);
        playerXPLabel = new JLabel("XP: " + playerXP + "/" + XP_PER_LEVEL);
        soulSphereLabel = new JLabel("SoulSpheres: " + soulSpheres);
        divineSphereLabel = new JLabel("DivineSpheres: " + divineSpheres);
        creatures = new AngelDemon[30][30];
        JButton menuButton = new JButton("Captured Creatures");
        menuButton.addActionListener(e -> showCapturedCreatures());
        JButton saveButton1 = new JButton("Save Slot 1");
        JButton saveButton2 = new JButton("Save Slot 2");
        JButton saveButton3 = new JButton("Save Slot 3");
        saveButton1.addActionListener(e -> saveGame(1));
        saveButton2.addActionListener(e -> saveGame(2));
        saveButton3.addActionListener(e -> saveGame(3));
        JPanel panel = new JPanel();
        panel.add(playerLabel);
        frame.add(panel); // Assuming 'frame' is an instance of your JFrame.
        frame.revalidate();
        frame.repaint();
        infoPanel.add(playerHPLabel);
        infoPanel.add(creatureHPLabel);
        infoPanel.add(playerLevelLabel);
        infoPanel.add(playerXPLabel);
        infoPanel.add(soulSphereLabel);
        infoPanel.add(divineSphereLabel);
        infoPanel.add(menuButton);
        infoPanel.add(saveButton1);
        infoPanel.add(saveButton2);
        infoPanel.add(saveButton3);
        frame.add(infoPanel, BorderLayout.NORTH);
        JPanel gamePanel = new JPanel();
        gamePanel.setLayout(new GridLayout(30, 30));
        grid = new JButton[30][30];
        ActionListener buttonListener = e -> {
            JButton button = (JButton) e.getSource();
            int x = (int) button.getClientProperty("x");
            int y = (int) button.getClientProperty("y");
            if (Math.abs(playerX - x) <= 1 && Math.abs(playerY - y) <= 1) {
                playerX = x;
                playerY = y;
                if (creatures[x][y] != null) {
                    int creatureHP = creatures[x][y].getHP();
                    creatureHPLabel.setText("Creature HP: " + creatureHP + " (" + creatures[x][y] + ")");
                    startBattle(creatures[x][y]);
                    if (creatureHP <= 0 && new Random().nextDouble() < 0.005) {
                        divineSpheres++;
                        updateLabels();
                    }
                    if (creatureHP <= 0) {
                        creatures[x][y] = null;
                        creaturesRemaining--;
                        creatureHPLabel.setText("Creature HP: - ");
                        if (creaturesRemaining == 0) {
                            JOptionPane.showMessageDialog(frame, "You caught all creatures! Game Over.");
                            System.exit(0);
                        }
                    }
                    if (playerHP <= 0) {
                        JOptionPane.showMessageDialog(frame, "You're defeated! Game Over.");
                        System.exit(0);
                    }
                }
                refreshGrid();
            }
        };

        Random rand = new Random();
        for (int x = 0; x < 30; x++) {
            for (int y = 0; y < 30; y++) {
                JButton button = new JButton();
                button.putClientProperty("x", x);
                button.putClientProperty("y", y);
                button.addActionListener(buttonListener);
                grid[x][y] = button;
                if (rand.nextInt(15) == 0) {
                    creatures[x][y] = AngelDemon.randomCreature(playerLevel);
                    creaturesRemaining++;
                }
                gamePanel.add(button);
            }
        }
        frame.add(gamePanel, BorderLayout.CENTER);
        refreshGrid();
    }



    public AngelDemonGame() {
        showMainMenu();
    }



    private void startNewGame() {
        mainMenuFrame.dispose();
        initGame();
        frame.setVisible(true);
    }

    private void showMainMenu() {
        mainMenuFrame = new JFrame("Main Menu");
        mainMenuFrame.setSize(300, 300);
        JButton newGameButton = new JButton("New Game");
        JButton loadGameButton = new JButton("Load Game");
        JButton exitButton = new JButton("Exit");
        newGameButton.addActionListener(e -> startNewGame());
        loadGameButton.addActionListener(e -> showLoadMenu());
        exitButton.addActionListener(e -> System.exit(0));
        mainMenuFrame.setLayout(new GridLayout(3, 1));
        mainMenuFrame.add(newGameButton);
        mainMenuFrame.add(loadGameButton);
        mainMenuFrame.add(exitButton);
        mainMenuFrame.setVisible(true);

    }

    private void showLoadMenu() {
        loadMenuFrame = new JFrame("Load Game");
        loadMenuFrame.setSize(300, 300);
        JButton slot1Button = new JButton("Slot 1");
        JButton slot2Button = new JButton("Slot 2");
        JButton slot3Button = new JButton("Slot 3");
        slot1Button.addActionListener(e -> loadGame(1));
        slot2Button.addActionListener(e -> loadGame(2));
        slot3Button.addActionListener(e -> loadGame(3));
        loadMenuFrame.setLayout(new GridLayout(3, 1));
        loadMenuFrame.add(slot1Button);
        loadMenuFrame.add(slot2Button);
        loadMenuFrame.add(slot3Button);
        loadMenuFrame.setVisible(true);
    }

    private void loadGame(int slot) {
        loadMenuFrame.dispose();
        File saveFile = new File("save" + slot + ".dat");
        if (!saveFile.exists()) {
            try {
                saveFile.createNewFile();
                JOptionPane.showMessageDialog(frame, "Save slot " + slot + " is empty. Starting a new game.");
                startNewGame();
                return;
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(frame, "Failed to create save file. Please check permissions.");
                ex.printStackTrace();
                return;
            }
        }
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(saveFile))) {
            GameState gs = (GameState) ois.readObject();
            playerHP = gs.playerHP;
            playerXP = gs.playerXP;
            playerLevel = gs.playerLevel;
            soulSpheres = gs.soulSpheres;
            divineSpheres = gs.divineSpheres;
            playerX = gs.playerX;
            playerY = gs.playerY;
            creatures = gs.creatures;
            capturedCreatures = gs.capturedCreatures;
            initGame();
            refreshGrid();
            frame.setVisible(true);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(frame, "Failed to load game from slot " + slot + ". Error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void saveGame(int slot) {
        GameState gs = new GameState();
        gs.playerHP = playerHP;
        gs.playerXP = playerXP;
        gs.playerLevel = playerLevel;
        gs.soulSpheres = soulSpheres;
        gs.divineSpheres = divineSpheres;
        gs.playerX = playerX;
        gs.playerY = playerY;
        gs.creatures = creatures;
        gs.capturedCreatures = capturedCreatures;
        File saveFile = new File("save" + slot + ".dat");
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(saveFile))) {
            oos.writeObject(gs);
            JOptionPane.showMessageDialog(frame, "Game saved to slot " + slot + " successfully!");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(frame, "Failed to save game to slot " + slot + ". Error: " + e.getMessage());
            e.printStackTrace();
        }
    }



    private void refreshGrid() {
        for (int x = 0; x < 30; x++) {
            for (int y = 0; y < 30; y++) {
                if (x == playerX && y == playerY) {
                    grid[x][y].setText("P");
                    grid[x][y].setBackground(Color.GREEN);
                } else if (creatures[x][y] != null) {
                    grid[x][y].setText(creatures[x][y].toString().charAt(0) + "");
                    grid[x][y].setBackground((creatures[x][y].isAngel()) ? Color.WHITE : Color.RED);
                } else {
                    grid[x][y].setText("");
                    grid[x][y].setBackground(null);
                }
            }
        }
    }

    private void updateLabels() {
        playerHPLabel.setText("Player HP: " + playerHP);
        playerLevelLabel.setText("Player Level: " + playerLevel);
        playerXPLabel.setText("XP: " + playerXP + "/" + XP_PER_LEVEL);
        soulSphereLabel.setText("SoulSpheres: " + soulSpheres);
        divineSphereLabel.setText("DivineSpheres: " + divineSpheres);
    }

    private void startBattle(AngelDemon creature) {
        JFrame battleFrame = new JFrame("Battle");
        battleFrame.setSize(400, 200);
        battleFrame.setLayout(new GridLayout(3, 1));
        JLabel battleInfo = new JLabel("You encountered a " + creature + "! Choose your ability:");
        battleFrame.add(battleInfo);
        JPanel abilitiesPanel = new JPanel();
        for (int i = 0; i < playerAbilities.length; i++) {
            JButton abilityButton = new JButton(playerAbilities[i]);
            int finalI = i;
            abilityButton.addActionListener(e -> {
                int damage = abilityDamage[finalI];
                if (damage < 0) {
                    playerHP += Math.abs(damage);
                    playerHP = Math.min(playerHP, 100);
                } else {
                    creature.takeDamage(damage);
                }
                if (creature.getHP() <= 0) {
                    creatures[playerX][playerY] = null;
                    creaturesRemaining--;
                    updateLabels();
                    battleFrame.dispose();
                    return;
                }
                for (Component comp : abilitiesPanel.getComponents()) {
                    comp.setEnabled(false);
                }
                Timer creatureTurnTimer = new Timer(1000, evt -> {
                    int creatureDamage = creature.attack();
                    playerHP -= creatureDamage;
                    updateLabels();
                    if (playerHP <= 0) {
                        JOptionPane.showMessageDialog(frame, "You're defeated! Game Over.");
                        System.exit(0);
                    }
                    for (Component comp : abilitiesPanel.getComponents()) {
                        comp.setEnabled(true);
                    }
                    ((Timer) evt.getSource()).stop();
                });
                creatureTurnTimer.setRepeats(false);
                creatureTurnTimer.start();
            });
            abilitiesPanel.add(abilityButton);
        }
        JButton captureButton = new JButton("Use SoulSphere");
        captureButton.addActionListener(e -> {
            if (soulSpheres <= 0) {
                JOptionPane.showMessageDialog(battleFrame, "You have no SoulSpheres left!");
                return;
            }
            double catchProbability = (100.0 - creature.getHP()) / 100.0 * (5.0 / (creature.getLevel() - playerLevel + 5));
            if (new Random().nextDouble() <= catchProbability) {
                creatures[playerX][playerY] = null;
                creaturesRemaining--;
                capturedCreatures.add(creature);
                soulSpheres--;
                playerXP += 20;
                checkForLevelUp();
                updateLabels();
                battleFrame.dispose();
            } else {
                JOptionPane.showMessageDialog(battleFrame, "Failed to capture " + creature + "! Try weakening it more.");
            }
        });
        JButton useDivineSphereButton = new JButton("Use DivineSphere");
        useDivineSphereButton.addActionListener(e -> {
            if (divineSpheres <= 0) {
                JOptionPane.showMessageDialog(battleFrame, "You have no DivineSpheres left!");
                return;
            }
            creatures[playerX][playerY] = null;
            creaturesRemaining--;
            capturedCreatures.add(creature);
            divineSpheres--;
            playerXP += 20;
            checkForLevelUp();
            updateLabels();
            battleFrame.dispose();
        });
        abilitiesPanel.add(captureButton);
        abilitiesPanel.add(useDivineSphereButton);
        battleFrame.add(abilitiesPanel);
        battleFrame.setLocationRelativeTo(frame);
        battleFrame.setModalExclusionType(Dialog.ModalExclusionType.APPLICATION_EXCLUDE);
        battleFrame.setVisible(true);
    }

    private void checkForLevelUp() {
        if (playerXP >= XP_PER_LEVEL) {
            playerLevel++;
            playerXP -= XP_PER_LEVEL;
            playerHP = 100;
            updateLabels();
        }
    }

    private void showCapturedCreatures() {
        JFrame capturedFrame = new JFrame("Captured Creatures");
        capturedFrame.setSize(400, 400);
        capturedFrame.setLayout(new BorderLayout());
        DefaultListModel<AngelDemon> listModel = new DefaultListModel<>();
        for (AngelDemon ad : capturedCreatures) {
            listModel.addElement(ad);
        }
        JList<AngelDemon> capturedList = new JList<>(listModel);
        JScrollPane scrollPane = new JScrollPane(capturedList);
        capturedFrame.add(scrollPane, BorderLayout.CENTER);
        JButton closeButton = new JButton("Close");
        closeButton.addActionListener(e -> capturedFrame.dispose());
        capturedFrame.add(closeButton, BorderLayout.SOUTH);
        capturedFrame.setLocationRelativeTo(frame);
        capturedFrame.setVisible(true);
    }



    static class GameState implements Serializable {
        int playerHP, playerXP, playerLevel, soulSpheres, divineSpheres, playerX, playerY;
        AngelDemon[][] creatures;
        List<AngelDemon> capturedCreatures;
    }
}



