package proxy;

import java.util.List;

// Proxy: controls access to RealProductService based on user role
public class ProductServiceProxy implements ProductService {

    private final ProductService realService;
    private final User currentUser;

    public ProductServiceProxy(User currentUser) {
        this.realService = new RealProductService();
        this.currentUser = currentUser;
    }

    @Override
    public void addProduct(Product product) {
        if (!currentUser.isAdmin()) {
            System.out.println("[Proxy] Access denied for user '" + currentUser.getName()
                    + "' (role: " + currentUser.getRole() + "). Only admins can add products.");
            return;
        }
        System.out.println("[Proxy] Access granted for admin '" + currentUser.getName() + "'.");
        realService.addProduct(product);
    }

    @Override
    public List<Product> getAllProducts() {
        return realService.getAllProducts();
    }
}