package Lib;

import java.util.ArrayList;
import java.util.Iterator;

public class ShoppingCart {
    private final PricingService pricingService;
    private final ProductCatalog productCatalog;
    private final ArrayList<CartItem> cartItems;

    public ShoppingCart(PricingService pricingService, ProductCatalog productCatalog) {
        this.pricingService = pricingService;
        this.productCatalog = productCatalog;
        this.cartItems = new ArrayList<>();
        checkRep();
    }

    private void checkRep() {
        if (cartItems == null) {
            throw new IllegalStateException("cartItems must not be null");
        }
        // ตรวจสอบว่าผลิตภัณฑ์ไม่มีซ้ำกันใน cartItems
        ArrayList<String> seenProductIds = new ArrayList<>();
        for (CartItem item : cartItems) {
            String productId = item.getProduct().getProductId();
            if (productId == null || productId.isBlank()) {
                throw new IllegalStateException("productId must not be null or blank");
            }
            if (seenProductIds.contains(productId)) {
                throw new IllegalStateException("Duplicate productId in cartItems: " + productId);
            }
            seenProductIds.add(productId);
        }
    }

    public void addItem(String productId, int quantity) {
        if (productId == null || productId.isBlank() || quantity <= 0) {
            throw new IllegalArgumentException("Invalid productId or quantity");
        }

        Product product = productCatalog.findById(productId);
        if (product == null) {
            throw new IllegalArgumentException("Product not found for id: " + productId);
        }

        // หา CartItem ที่มี product นี้ในตะกร้า
        for (CartItem item : cartItems) {
            if (item.getProduct().getProductId().equals(productId)) {
                item.increaseQuantity(quantity);
                checkRep();
                return;
            }
        }

        // ถ้าไม่มี CartItem นี้ ให้สร้างใหม่และเพิ่มเข้าไป
        CartItem newItem = new CartItem(product, quantity);
        cartItems.add(newItem);
        checkRep();
    }

    public void removeItem(String productId) {
        Iterator<CartItem> iter = cartItems.iterator();
        while (iter.hasNext()) {
            CartItem item = iter.next();
            if (item.getProduct().getProductId().equals(productId)) {
                iter.remove();
                checkRep();
                return;
            }
        }
    }

    public double getTotalPrice() {
        double total = 0.0;
        for (CartItem item : cartItems) {
            total += pricingService.calculateItemPrice(item);
        }
        return total;
    }

    public int getItemCount() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getItemCount'");
    }

    public void clearCart() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'clearCart'");
    }
}

