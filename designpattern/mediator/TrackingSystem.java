package mediator;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;
import java.util.Map;

public class TrackingSystem extends Colleague {

    private final Map<String, String> orderStatusLog = new LinkedHashMap<>();
    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("HH:mm:ss");

    public TrackingSystem(LogisticsMediator mediator) {
        super(mediator);
    }

    public void updateStatus(String orderId, String status) {
        String entry = "[" + LocalTime.now().format(FMT) + "] " + status;
        orderStatusLog.put(orderId, entry);
        System.out.println("[TrackingSystem]   Order " + orderId + " → " + entry);
    }

    public void printLog() {
        System.out.println("\n[TrackingSystem] Full Order Log:");
        orderStatusLog.forEach((id, log) ->
                System.out.println("  " + id + " : " + log));
    }

    @Override
    public String getName() { return "TrackingSystem"; }
}
