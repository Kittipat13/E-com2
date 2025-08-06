package Lib;

public class CartItem {
    private final Product product;
    private int quantity;

    public CartItem(Product product, int quantity) {
        this.product = product;
        this.quantity = quantity;
        checkRep();
    }

    private void checkRep() {
        if (product == null) {
            throw new IllegalArgumentException("Product must not be null.");
        }
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be greater than 0.");
        }
    }

    public Product getProduct() {
        return product;
    }

    public int getQuantity() {
        return quantity;
    }

    /**
     * เพิ่มจำนวนสินค้าจากของเดิม
     */
    public void increaseQuantity(int amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Amount must be greater than 0.");
        }
        quantity += amount;
        checkRep();
    }

    @Override
    public String toString() {
        return String.format("CartItem[product=%s, quantity=%d]", product, quantity);
    }
}

