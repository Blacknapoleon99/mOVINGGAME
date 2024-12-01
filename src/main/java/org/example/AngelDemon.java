package org.example;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
public class AngelDemon {
    private String name;
    private String type;
    private Element element;
    private Stats stats;
    private static ScheduledExecutorService healthUpdateService;
    private static AngelDemon[][] creatures; 
    static {
        // Initialize the executor service for health updates
        healthUpdateService = Executors.newScheduledThreadPool(1);
    }
    public AngelDemon(String name, String type, Element element, Stats stats) {
        this.name = name;
        this.type = type;
        this.element = element;
        this.stats = stats;
    }
    public static void startGameLoop() {
        // Start updating health at a fixed rate (e.g., every 16ms for 60 FPS)
        healthUpdateService.scheduleAtFixedRate(() -> {
            if (creatures != null) {
                for (int x = 0; x < creatures.length; x++) {
                    for (int y = 0; y < creatures[x].length; y++) {
                        if (creatures[x][y] != null) {
                            creatures[x][y].updateHealth();
                        }
                    }
                }
            }
        }, 0, 16, TimeUnit.MILLISECONDS);
    }
    public static void stopGameLoop() {
        // Stop the health update loop when the game ends
        if (healthUpdateService != null && !healthUpdateService.isShutdown()) {
            healthUpdateService.shutdown();
        }
    }
    public void updateHealth() {
        // Update the health of this creature
        stats.decreaseHealth(1);
        if (stats.getHealth() <= 0) {
            System.out.println(name + " has been defeated!");
        }
    }
    public boolean isAngel() {
        return "Angel".equals(type);
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
    public static class Stats {
        private int health;
        public Stats(int health) {
            this.health = health;
        }
        public int getHealth() {
            return health;
        }
        public void decreaseHealth(int amount) {
            health = Math.max(0, health - amount);
        }
    }
}


