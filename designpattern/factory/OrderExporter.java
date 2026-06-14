package factory;

import java.util.List;

public interface OrderExporter {
    void export(List<Order> orders);
    String getFormat();
}