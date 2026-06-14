package command;

import java.util.ArrayList;
import java.util.List;

// Receiver — actual cart operations happen here
public class Cart {

    private final List<CartItem> items = new ArrayList<>();
    private String couponCode = null;
    private double discount   = 0.0;

    public void addItem(CartItem item) {
        for (CartItem existing : items) {
            if (existing.getName().equals(item.getName())) {
                existing.setQuantity(existing.getQuantity() + item.getQuantity());
                System.out.println("  [Cart] Increased quantity → " + existing);
                return;
            }
        }
        items.add(item);
        System.out.println("  [Cart] Added → " + item);
    }

    public boolean removeItem(String itemName) {
        CartItem target = findItem(itemName);
        if (target != null) {
            items.remove(target);
            System.out.println("  [Cart] Removed → " + target);
            return true;
        }
        System.out.println("  [Cart] Item not found: " + itemName);
        return false;
    }

    public void applyCoupon(String code, double discountAmount) {
        this.couponCode = code;
        this.discount   = discountAmount;
        System.out.println("  [Cart] Coupon applied: " + code + " → ₹" + discountAmount + " off");
    }

    public void removeCoupon() {
        System.out.println("  [Cart] Coupon removed: " + couponCode);
        this.couponCode = null;
        this.discount   = 0.0;
    }

    public List<CartItem> clearAll() {
        List<CartItem> snapshot = new ArrayList<>(items);
        items.clear();
        couponCode = null;
        discount   = 0.0;
        System.out.println("  [Cart] Cart cleared.");
        return snapshot;
    }

    public void restoreAll(List<CartItem> snapshot) {
        items.clear();
        items.addAll(snapshot);
        System.out.println("  [Cart] Cart restored with " + snapshot.size() + " item(s).");
    }

    public CartItem findItem(String name) {
        return items.stream()
                .filter(i -> i.getName().equals(name))
                .findFirst().orElse(null);
    }

    public void printCart() {
        if (items.isEmpty()) {
            System.out.println("  Cart is empty.");
            return;
        }
        double subtotal = 0;
        for (CartItem item : items) {
            System.out.printf("    %-25s ₹%.2f%n", item, item.getTotal());
            subtotal += item.getTotal();
        }
        System.out.println("    " + "-".repeat(35));
        if (couponCode != null) {
            System.out.printf("    Coupon (%s)             -₹%.2f%n", couponCode, discount);
        }
        System.out.printf("    Total                     ₹%.2f%n", subtotal - discount);
    }
}
