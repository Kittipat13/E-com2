package Lib;

import java.util.ArrayList;
import java.util.List;

public class ProductCatalog {
    private final List<Product> products;

    public ProductCatalog() {
        this.products = new ArrayList<>();
        checkRep();
    }

    private void checkRep() {
        if (products == null) {
            throw new IllegalStateException("Product list must not be null.");
        }

        for (int i = 0; i < products.size(); i++) {
            Product p1 = products.get(i);
            for (int j = i + 1; j < products.size(); j++) {
                Product p2 = products.get(j);
                if (p1.equals(p2)) {
                    throw new IllegalStateException("Duplicate products with same productId found.");
                }
            }
        }
    }

    public void addProduct(Product product) {
        if (product == null) {
            throw new IllegalArgumentException("Product must not be null.");
        }

        // ตรวจว่าซ้ำหรือไม่
        if (findById(product.getProductId()) != null) {
            throw new IllegalArgumentException("Product with the same ID already exists.");
        }

        products.add(product);
        checkRep();
    }

    public Product findById(String productId) {
        for (Product product : products) {
            if (product.getProductId().equals(productId)) {
                return product;
            }
        }
        return null; // ถ้าไม่เจอ
    }

    public List<Product> getAllProducts() {
        return new ArrayList<>(products); // คืนค่า copy เพื่อความปลอดภัย
    }
}

