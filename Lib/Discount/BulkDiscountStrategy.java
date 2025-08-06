package Lib.Discount;
import Lib.CartItem;

public class BulkDiscountStrategy implements DiscountStrategy {
    private final int minimumQuantity;
    private final double discountPercentage; // เช่น 0.10 สำหรับ 10%

    public BulkDiscountStrategy(int minimumQuantity, double discountPercentage) {
        if (minimumQuantity <= 0 || discountPercentage < 0 || discountPercentage > 1) {
            throw new IllegalArgumentException("Invalid bulk discount parameters");
        }
        this.minimumQuantity = minimumQuantity;
        this.discountPercentage = discountPercentage;
    }

    @Override
    public double calculatePrice(CartItem item) {
        double price = item.getProduct().getPrice();
        int quantity = item.getQuantity();

        if (quantity >= minimumQuantity) {
            return quantity * price * (1 - discountPercentage);
        } else {
            return quantity * price;
        }
    }
}
