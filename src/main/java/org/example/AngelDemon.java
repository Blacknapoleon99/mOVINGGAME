package org.example;

import java.util.Timer;
import java.util.TimerTask;

public class AngelDemon {
    private String name;
    private String type;
    private Element element;
    private Stats stats;
    private Timer healthUpdateTimer;

    private void initGame() {
        healthUpdateTimer = new Timer();
        healthUpdateTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                updateEnemyHealth();
            }
        }, 0, 1000);
    }

    private void updateEnemyHealth() {
        for (int x = 0; x < creatures.length; x++) {
            for (int y = 0; y < creatures[x].length; y++) {
                if (creatures[x][y] != null) {
                    creatures[x][y].updateHealth();
                }
            }
        }
        // Update the health of the enemy
    }

    private void updateGealth*(int amount( ))

    public AngelDemon(String name, String type, Element element, Stats stats) {
        this.name = name;
        this.type = type;
        this.element = element;
        this.stats = stats;
    }

    public boolean isAngel() {
        return type.equals("Angel");
    }

    public Element getElement() {
        return element;
    }

    public Stats getStats() {
        return stats;
    }

    @Override
    public String toString() {
        return name + " (" + element.name() + ")";
    }

    public enum Element {
        FIRE, WATER, EARTH, WIND, LIGHT, SHADOW, PSYCHIC, METAL, ICE, THUNDER;

        public double getMultiplierAgainst(Element other) {
            switch (this) {
                case FIRE:
                    return (other == WATER) ? 0.5 : 1.0;
                case WATER:
                    return (other == FIRE) ? 1.5 : 1.0;
                case LIGHT:
                    return (other == SHADOW) ? 1.5 : (other == PSYCHIC) ? 0.5 : 1.0;
                case SHADOW:
                    return (other == LIGHT) ? 0.5 : 1.0;
                default:
                    return 1.0;
            }
        }
    }
}
