package connectionpool;

import java.util.ArrayList;
import java.util.List;

public class DBConnectionPool {
    private List<DBConnection> pool = new ArrayList<>();
    private int maxSize;

    public DBConnectionPool(int maxSize) {
        this.maxSize = maxSize;
        for (int i = 1; i <= maxSize; i++) {
            pool.add(new DBConnection("CONN-" + i));
        }
    }

    public DBConnection acquire() {
        for (DBConnection conn : pool) {
            if (!conn.isInUse()) {
                conn.setInUse(true);
                System.out.println("[Pool] Acquired: " + conn.getConnectionId());
                return conn;
            }
        }
        System.out.println("[Pool] All connections busy. Request rejected.");
        return null;
    }

    public void release(DBConnection conn) {
        if (conn != null) {
            conn.setInUse(false);
            System.out.println("[Pool] Released: " + conn.getConnectionId());
        }
    }

    public void printStatus() {
        System.out.println("[Pool Status]");
        for (DBConnection conn : pool) {
            System.out.println("  " + conn.getConnectionId() + " -> " + (conn.isInUse() ? "IN USE" : "FREE"));
        }
    }
}