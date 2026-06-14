package memento;

public class Main {

    public static void main(String[] args) {

        GameState    player    = new GameState("Arjun");
        GameCaretaker caretaker = new GameCaretaker();

        // ==================== LEVEL 1 START ====================
        System.out.println("==================== LEVEL 1 START ====================");
        player.printStatus();

        System.out.println("\n--- Exploring Level 1 ---");
        player.move(5, 3);
        player.collectPoints(100);
        player.collectWeapon("Sword");
        player.takeDamage(20);
        player.printStatus();

        // Save at checkpoint 1
        System.out.println("\n--- Checkpoint 1 reached ---");
        caretaker.save(player.save("Level1-Checkpoint1"));
        player.printStatus();

        // ==================== DEEPER INTO LEVEL 1 ====================
        System.out.println("\n==================== Deeper into Level 1 ====================");
        player.move(10, 7);
        player.collectPoints(250);
        player.collectWeapon("Fire Bow");
        player.takeDamage(35);
        player.printStatus();

        // Save at checkpoint 2
        System.out.println("\n--- Checkpoint 2 reached ---");
        caretaker.save(player.save("Level1-Checkpoint2"));
        player.printStatus();

        // ==================== BOSS FIGHT — PLAYER DIES ====================
        System.out.println("\n==================== Boss Fight ====================");
        player.move(3, 2);
        player.takeDamage(40);
        player.takeDamage(30);
        player.die();
        player.printStatus();

        // Restore last save point
        System.out.println("\n--- Restoring last save point ---");
        GameMemento lastSave = caretaker.popLastSave();
        if (lastSave != null) player.restore(lastSave);
        player.printStatus();

        // ==================== RETRY BOSS FIGHT ====================
        System.out.println("\n==================== Retry Boss Fight ====================");
        player.collectPoints(50);
        player.takeDamage(15);
        player.collectPoints(500);
        System.out.println("  [Arjun] Boss defeated!");
        player.printStatus();

        // ==================== LEVEL UP ====================
        System.out.println("\n==================== Level Up ====================");
        player.levelUp();
        player.move(0, 0);
        player.printStatus();

        // Save at Level 2 start
        System.out.println("\n--- Level 2 Start — saving ---");
        caretaker.save(player.save("Level2-Start"));

        // ==================== LEVEL 2 ====================
        System.out.println("\n==================== Level 2 ====================");
        player.move(8, 4);
        player.collectWeapon("Thunder Shield");
        player.collectPoints(700);
        player.takeDamage(50);
        player.printStatus();

        // Save mid-level
        System.out.println("\n--- Level 2 Mid save ---");
        caretaker.save(player.save("Level2-Midpoint"));

        // Something goes very wrong
        System.out.println("\n--- Fell into a trap! ---");
        player.takeDamage(70);
        player.takeDamage(60);
        player.die();
        player.printStatus();

        // ==================== ALL SAVE POINTS ====================
        System.out.println("\n==================== All Save Points ====================");
        caretaker.listSavePoints();

        // Restore to specific save — Level 2 start
        System.out.println("\n--- Restoring to Level2-Midpoint ---");
        GameMemento mid = caretaker.popLastSave();
        if (mid != null) player.restore(mid);
        player.printStatus();

        // ==================== REMAINING SAVE POINTS ====================
        System.out.println("\n==================== Remaining Save Points ====================");
        caretaker.listSavePoints();

        // ==================== RESTORE ALL THE WAY BACK ====================
        System.out.println("\n==================== Restore all the way back ====================");
        while (caretaker.hasSavePoints()) {
            GameMemento save = caretaker.popLastSave();
            System.out.println("\n  Restoring → " + save.getSavePointName());
            player.restore(save);
            player.printStatus();
        }
    }
}
