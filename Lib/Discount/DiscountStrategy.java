package Lib.Discount;
import Lib.CartItem;

public interface DiscountStrategy {
    double calculatePrice(CartItem item);
}
