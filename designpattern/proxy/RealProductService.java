package proxy;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RealProductService implements ProductService {

    private final List<Product> products = new ArrayList<>();

    @Override
    public void addProduct(Product product) {
        products.add(product);
        System.out.println("[RealProductService] Product added: " + product);
    }

    @Override
    public List<Product> getAllProducts() {
        return Collections.unmodifiableList(products);
    }
}