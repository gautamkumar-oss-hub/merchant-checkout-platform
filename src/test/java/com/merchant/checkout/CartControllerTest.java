package com.merchant.checkout;

import com.merchant.checkout.controller.CartController;
import com.merchant.checkout.model.Cart;
import com.merchant.checkout.model.CheckoutRequest;
import com.merchant.checkout.service.CartService;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CartControllerTest {

    @Test
    void createsAndRetrievesCart() {
        Cart storedCart = new Cart("customer-1", List.of(item("product-1", 2)));
        CartController controller = controllerWith(storedCart);
        CheckoutRequest request = request("customer-1", item("product-1", 2));

        ResponseEntity<String> created = controller.saveCart(request);
        ResponseEntity<Cart> found = controller.getCart(created.getBody());

        assertEquals(200, created.getStatusCode().value());
        assertNotNull(created.getBody());
        assertEquals(200, found.getStatusCode().value());
        assertEquals("customer-1", found.getBody().customerId());
        assertEquals(1, found.getBody().items().size());
    }

    @Test
    void returnsNotFoundForMissingCart() {
        CartController controller = controllerWith(null);

        ResponseEntity<Cart> response = controller.getCart("missing");

        assertTrue(response.getStatusCode().is4xxClientError());
    }

    @Test
    void deletesCart() {
        TrackingCartService service = new TrackingCartService();
        CartController controller = new CartController(service);

        ResponseEntity<Void> response = controller.deleteCart("cart-1");

        assertEquals(204, response.getStatusCode().value());
        assertTrue(service.wasDeleted());
    }

    private static class TrackingCartService extends CartService {
        private boolean deleted;

        TrackingCartService() {
            super(null, null);
        }

        @Override
        public void delete(String cartId) {
            deleted = true;
        }

        boolean wasDeleted() {
            return deleted;
        }
    }

    private CartController controllerWith(Cart cart) {
        return new CartController(new CartService(null, null) {
            @Override
            public Cart save(String cartId, String customerId, List<CheckoutRequest.CartItem> items) {
                return cart;
            }

            @Override
            public Cart find(String cartId) {
                return cart;
            }
        });
    }

    private CheckoutRequest request(String customerId, CheckoutRequest.CartItem item) {
        CheckoutRequest request = new CheckoutRequest();
        request.setCustomerId(customerId);
        request.setItems(List.of(item));
        return request;
    }

    private static CheckoutRequest.CartItem item(String productId, int quantity) {
        CheckoutRequest.CartItem item = new CheckoutRequest.CartItem();
        item.setProductId(productId);
        item.setQuantity(quantity);
        return item;
    }
}
