package connectionpool;

public class DBConnection {
    private String connectionId;
    private boolean inUse;

    public DBConnection(String connectionId) {
        this.connectionId = connectionId;
        this.inUse = false;
        System.out.println("[DB] Connection created: " + connectionId);
    }

    public String getConnectionId() { return connectionId; }
    public boolean isInUse()        { return inUse; }
    public void setInUse(boolean inUse) { this.inUse = inUse; }

    public void executeQuery(String query) {
        System.out.println("[" + connectionId + "] Executing: " + query);
    }
}