package memento;

// Memento — immutable snapshot of game state, no setters
public class GameMemento {

    private final String savePointName;
    private final int    health;
    private final int    level;
    private final int    score;
    private final int    positionX;
    private final int    positionY;
    private final String weapon;
    private final int    lives;

    public GameMemento(String savePointName, int health, int level,
                       int score, int positionX, int positionY,
                       String weapon, int lives) {
        this.savePointName = savePointName;
        this.health        = health;
        this.level         = level;
        this.score         = score;
        this.positionX     = positionX;
        this.positionY     = positionY;
        this.weapon        = weapon;
        this.lives         = lives;
    }

    public String getSavePointName() { return savePointName; }
    public int    getHealth()        { return health; }
    public int    getLevel()         { return level; }
    public int    getScore()         { return score; }
    public int    getPositionX()     { return positionX; }
    public int    getPositionY()     { return positionY; }
    public String getWeapon()        { return weapon; }
    public int    getLives()         { return lives; }

    @Override
    public String toString() {
        return String.format("[%s] Level=%d | HP=%d | Score=%d | Pos=(%d,%d) | Weapon=%s | Lives=%d",
                savePointName, level, health, score, positionX, positionY, weapon, lives);
    }
}
