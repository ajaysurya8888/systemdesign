package factory;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

public class Main {

    public static void main(String[] args) {

        List<Order> orders = Arrays.asList(
                new Order("ORD001", "Alice",
                        Arrays.asList("Idly", "Sambhar", "Coffee"),
                        120.00, "DELIVERED", LocalDateTime.of(2024, 6, 14, 9, 30)),
                new Order("ORD002", "Bob",
                        Arrays.asList("Biriyani", "Raitha", "Pepsi"),
                        350.00, "DELIVERED", LocalDateTime.of(2024, 6, 14, 13, 0)),
                new Order("ORD003", "Carol",
                        Arrays.asList("Poori", "Kuruma"),
                        180.00, "OUT_FOR_DELIVERY", LocalDateTime.of(2024, 6, 14, 14, 15))
        );

        String[] formats = {"CSV", "PDF", "EXCEL"};

        for (String format : formats) {
            System.out.println();
            OrderExporter exporter = ExporterFactory.create(format);
            exporter.export(orders);
        }
    }
}