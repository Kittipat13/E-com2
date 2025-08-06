package Lib.Discount;
import Lib.CartItem;

public class BogoDiscountStrategy implements DiscountStrategy {
    @Override
    public double calculatePrice(CartItem item) {
        int quantity = item.getQuantity();
        double unitPrice = item.getProduct().getPrice();

        int chargeableQty = (quantity / 2) + (quantity % 2); // จ่ายครึ่ง + เหลือเศษ
        return chargeableQty * unitPrice;
    }
}
