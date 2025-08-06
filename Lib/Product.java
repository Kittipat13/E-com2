package Lib;

public final class Product {
    private final String productId;
    private final String productName;
    private final double price;

    public Product(String productId, String productName, double price) {
        this.productId = productId;
        this.productName = productName;
        this.price = price;
        checkRep();
    }

    private void checkRep() {
        if (productId == null || productId.isBlank()) {
            throw new IllegalArgumentException("Product ID must not be null or blank.");
        }
        if (productName == null || productName.isBlank()) {
            throw new IllegalArgumentException("Product name must not be null or blank.");
        }
        if (price < 0) {
            throw new IllegalArgumentException("Price must not be negative.");
        }
    }

    public String getProductId() {
        return productId;
    }

    public String getProductName() {
        return productName;
    }

    public double getPrice() {
        return price;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Product)) return false;
        Product other = (Product) obj;
        return this.productId.equals(other.productId);
    }

    @Override
    public int hashCode() {
        return productId.hashCode();
    }

    @Override
    public String toString() {
        return String.format("Product[id=%s, name=%s, price=%.2f]", productId, productName, price);
    }
}
