package Lib;

import java.util.ArrayList;
import java.util.List;
import Lib.Discount.*;

public class PricingService {
    private final List<PromotionEntry> promotions;

    public PricingService() {
        promotions = new ArrayList<>();
    }

    private static class PromotionEntry {
        String sku;
        DiscountStrategy strategy;

        PromotionEntry(String sku, DiscountStrategy strategy) {
            this.sku = sku;
            this.strategy = strategy;
        }
    }

    public void addStrategy(String sku, DiscountStrategy strategy) {
        if (sku == null || sku.isBlank() || strategy == null) {
            throw new IllegalArgumentException("SKU and strategy must not be null or blank");
        }

        // ถ้ามีแล้วแทนที่
        for (PromotionEntry entry : promotions) {
            if (entry.sku.equals(sku)) {
                entry.strategy = strategy;
                return;
            }
        }

        promotions.add(new PromotionEntry(sku, strategy));
    }

    public double calculateItemPrice(CartItem item) {
        String sku = item.getProduct().getProductId();

        for (PromotionEntry entry : promotions) {
            if (entry.sku.equals(sku)) {
                return entry.strategy.calculatePrice(item);
            }
        }

        // ถ้าไม่มีโปรโมชั่น → ใช้ DefaultPricingStrategy
        DiscountStrategy defaultStrategy = new DefaultPricingStrategy();
        return defaultStrategy.calculatePrice(item);
    }
}

