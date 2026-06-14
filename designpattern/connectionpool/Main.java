package connectionpool;

public class Main {
    public static void main(String[] args) {
        DBConnectionPool pool = new DBConnectionPool(3);

        System.out.println("\n=== Initial Pool Status ===");
        pool.printStatus();

        System.out.println("\n=== Services Acquiring Connections ===");
        DBConnection conn1 = pool.acquire();
        conn1.executeQuery("SELECT * FROM orders WHERE status='PENDING'");

        DBConnection conn2 = pool.acquire();
        conn2.executeQuery("UPDATE users SET last_login=NOW() WHERE id=42");

        DBConnection conn3 = pool.acquire();
        conn3.executeQuery("INSERT INTO payments VALUES (101, 500, 'SUCCESS')");

        System.out.println("\n=== Pool Status (all in use) ===");
        pool.printStatus();

        System.out.println("\n=== New Service Tries to Connect (pool full) ===");
        DBConnection conn4 = pool.acquire();

        System.out.println("\n=== conn1 Releases After Query ===");
        pool.release(conn1);

        System.out.println("\n=== New Service Retries ===");
        DBConnection conn5 = pool.acquire();
        if (conn5 != null) conn5.executeQuery("SELECT * FROM restaurants WHERE city='NYC'");

        System.out.println("\n=== Final Pool Status ===");
        pool.printStatus();
    }
}