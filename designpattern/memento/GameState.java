package memento;

// Originator — the player's live game state, creates and restores mementos
public class GameState {

    private final String playerName;
    private int    health;
    private int    level;
    private int    score;
    private int    positionX;
    private int    positionY;
    private String weapon;
    private int    lives;

    public GameState(String playerName) {
        this.playerName = playerName;
        this.health     = 100;
        this.level      = 1;
        this.score      = 0;
        this.positionX  = 0;
        this.positionY  = 0;
        this.weapon     = "Bare Hands";
        this.lives      = 3;
    }

    // ---- Game actions ----

    public void move(int x, int y) {
        this.positionX += x;
        this.positionY += y;
        System.out.println("  [" + playerName + "] Moved to (" + positionX + ", " + positionY + ")");
    }

    public void collectWeapon(String weapon) {
        this.weapon = weapon;
        System.out.println("  [" + playerName + "] Picked up weapon: " + weapon);
    }

    public void collectPoints(int points) {
        this.score += points;
        System.out.println("  [" + playerName + "] Collected " + points + " points. Total: " + score);
    }

    public void takeDamage(int damage) {
        this.health = Math.max(0, this.health - damage);
        System.out.println("  [" + playerName + "] Took " + damage + " damage. HP: " + health);
    }

    public void levelUp() {
        this.level++;
        this.health = 100;
        System.out.println("  [" + playerName + "] Level Up! Now at Level " + level + ". HP restored.");
    }

    public void die() {
        this.lives--;
        this.health = 0;
        System.out.println("  [" + playerName + "] Died! Lives remaining: " + lives);
    }

    // ---- Memento operations ----

    public GameMemento save(String savePointName) {
        System.out.println("  [" + playerName + "] Game saved: " + savePointName);
        return new GameMemento(savePointName, health, level, score,
                positionX, positionY, weapon, lives);
    }

    public void restore(GameMemento memento) {
        this.health    = memento.getHealth();
        this.level     = memento.getLevel();
        this.score     = memento.getScore();
        this.positionX = memento.getPositionX();
        this.positionY = memento.getPositionY();
        this.weapon    = memento.getWeapon();
        this.lives     = memento.getLives();
        System.out.println("  [" + playerName + "] Restored to save point: " + memento.getSavePointName());
    }

    public void printStatus() {
        System.out.printf("  Status → Level=%-2d | HP=%-4d | Score=%-6d | Pos=(%-2d,%-2d) | Weapon=%-18s | Lives=%d%n",
                level, health, score, positionX, positionY, weapon, lives);
    }

    public boolean isDead()     { return health <= 0; }
    public String getPlayerName() { return playerName; }
}
