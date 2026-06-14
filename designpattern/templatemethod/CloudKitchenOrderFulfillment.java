package templatemethod;

public class CloudKitchenOrderFulfillment extends OrderFulfillment {

    @Override
    protected void receiveOrder() {
        System.out.println("[Cloud Kitchen] Order auto-accepted via POS system.");
    }

    @Override
    protected void prepareFood() {
        System.out.println("[Cloud Kitchen] Chef preparing meal.");
    }

    @Override
    protected void packOrder() {
        System.out.println("[Cloud Kitchen] Packed in tamper-proof sealed container.");
    }

    // Cloud kitchen sends both WhatsApp and app notifications
    @Override
    protected void sendNotification() {
        System.out.println("[Cloud Kitchen] Sending WhatsApp + app notification.");
    }
}