package interpreter;

public class Order {
    private boolean newUser;
    private double amount;
    private boolean peakHour;

    public Order(boolean newUser, double amount, boolean peakHour) {
        this.newUser = newUser;
        this.amount = amount;
        this.peakHour = peakHour;
    }

    public boolean isNewUser()  { return newUser; }
    public double getAmount()   { return amount; }
    public boolean isPeakHour() { return peakHour; }
}