import Lib.*;
import Lib.Discount.*;

public class App {
    private static int passedCount = 0;
    private static int failedCount = 0;

    // เมธอดเช็คผลลัพธ์การทดสอบ
    private static void check(String testName, boolean condition) {
        if (condition) {
            System.out.println("PASSED: " + testName);
            passedCount++;
        } else {
            System.out.println("FAILED: " + testName);
            failedCount++;
        }
    }

    public static void main(String[] args) {
        System.out.println("=== Starting Comprehensive E-commerce Tests ===");

        // --- Setup สินค้า ---
        Product apple = new Product("P001", "Apple", 10.0);
        Product soda = new Product("P002", "Soda", 5.0);
        Product bread = new Product("P003", "Bread", 20.0);

        ProductCatalog catalog = new ProductCatalog();
        catalog.addProduct(apple);
        catalog.addProduct(soda);
        catalog.addProduct(bread);

        PricingService pricingService = new PricingService();
        pricingService.addStrategy("P001", new BogoDiscountStrategy());                // BOGO
        pricingService.addStrategy("P002", new BulkDiscountStrategy(6, 0.10));         // Bulk discount 10%

        // --- ทดสอบ ShoppingCart ---
        System.out.println("\n--- Testing ShoppingCart ---");
        ShoppingCart cart = new ShoppingCart(pricingService, catalog);

        check("New cart should be empty", cart.getItemCount() == 0 && cart.getTotalPrice() == 0.0);

        cart.addItem("P001", 3); // Apple x3 (BOGO: pay for 2)
        check("Add new item correctly", cart.getItemCount() == 1 && cart.getTotalPrice() == 20.0);

        cart.addItem("P001", 2); // Apple +2 => x5 (BOGO: pay for 3)
        check("Add existing item increases quantity", cart.getItemCount() == 1 && cart.getTotalPrice() == 30.0);

        cart.addItem("P002", 5); // Soda x5
        check("Add another item", cart.getItemCount() == 2 && cart.getTotalPrice() == 55.0); // 30 + 25

        cart.addItem("P002", 1); // Soda x6 (Bulk: 10% discount)
        check("Add item to meet bulk discount threshold", cart.getItemCount() == 2 &&
              cart.getTotalPrice() == 30.0 + (30.0 * 0.9)); // 30 + 27 = 57.0

        // ✅ ดักจับกรณีไม่พบสินค้า
        try {
            cart.addItem("P999", 1); // ไม่พบสินค้า
        } catch (IllegalArgumentException e) {
            // expected
        }
        check("Adding non-existent product does not change cart", cart.getItemCount() == 2 && cart.getTotalPrice() == 57.0);

        // ✅ ดักจับกรณีจำนวนเป็น 0
        try {
            cart.addItem("P003", 0); // Quantity 0
        } catch (IllegalArgumentException e) {
            // expected
        }
        check("Adding item with zero quantity does not change cart", cart.getItemCount() == 2 && cart.getTotalPrice() == 57.0);

        cart.removeItem("P002"); // ลบ Soda
        check("Remove item correctly updates count and price", cart.getItemCount() == 1 && cart.getTotalPrice() == 30.0);

        cart.removeItem("P999"); // ลบสินค้าที่ไม่มี
        check("Removing non-existent item does not change cart", cart.getItemCount() == 1 && cart.getTotalPrice() == 30.0);

        cart.clearCart();
        check("Clear cart works correctly", cart.getItemCount() == 0 && cart.getTotalPrice() == 0.0);

        // --- ทดสอบ PricingService โดยตรง ---
        System.out.println("\n--- Testing PricingService and Strategies ---");

        CartItem singleApple = new CartItem(apple, 1);   // ไม่เข้า BOGO
        CartItem twoApples = new CartItem(apple, 2);     // เข้า BOGO
        CartItem fiveSodas = new CartItem(soda, 5);      // ไม่เข้า bulk
        CartItem tenSodas = new CartItem(soda, 10);      // เข้า bulk
        CartItem normalBread = new CartItem(bread, 3);   // ไม่มีโปรโมชั่น

        check("BOGO Strategy (1 item)", pricingService.calculateItemPrice(singleApple) == 10.0);
        check("BOGO Strategy (2 items)", pricingService.calculateItemPrice(twoApples) == 10.0);
        check("Bulk Strategy (below threshold)", pricingService.calculateItemPrice(fiveSodas) == 25.0);
        check("Bulk Strategy (above threshold)", pricingService.calculateItemPrice(tenSodas) == 45.0);
        check("Default Strategy", pricingService.calculateItemPrice(normalBread) == 60.0);

        // เปลี่ยนโปรโมชั่น Apple เป็น bulk 3 ชิ้นลด 50%
        pricingService.addStrategy("P001", new BulkDiscountStrategy(3, 0.50));
        CartItem threeApples = new CartItem(apple, 3);
        check("Promotion update works", pricingService.calculateItemPrice(threeApples) == 15.0); // 30 * 0.5 = 15.0

        // --- ทดสอบ ProductCatalog ---
        System.out.println("\n--- Testing ProductCatalog ---");
        check("Find existing product", catalog.findById("P001").equals(apple));
        check("Find non-existent product returns null", catalog.findById("P999") == null);

        // --- สรุปผลการทดสอบ ---
        System.out.println("\n-------------------------");
        System.out.println("== Test Summary ==");
        System.out.println("Passed: " + passedCount + ", Failed: " + failedCount);
        if (failedCount == 0) {
            System.out.println("Excellent! All tests passed!");
        } else {
            System.out.println("Some tests failed.");
        }
    }
}

