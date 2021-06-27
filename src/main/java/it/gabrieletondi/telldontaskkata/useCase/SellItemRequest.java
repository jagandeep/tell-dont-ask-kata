package it.gabrieletondi.telldontaskkata.useCase;

import it.gabrieletondi.telldontaskkata.domain.OrderItem;
import it.gabrieletondi.telldontaskkata.domain.Product;
import it.gabrieletondi.telldontaskkata.repository.ProductCatalog;

public class SellItemRequest {
    private final ProductCatalog productCatalog;
    private int quantity;
    private String productName;

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public int getQuantity() {
        return quantity;
    }

    public String getProductName() {
        return productName;
    }

    public SellItemRequest(ProductCatalog productCatalog) {
        this.productCatalog = productCatalog;
    }

    OrderItem createOrderItem() {
        final int quantity = getQuantity();
        return OrderItem.createOrderItem(this.getProduct(), quantity);
    }

    public Product getProduct() {
        Product product = productCatalog.getByName(this.getProductName());

        if (product == null) {
            throw new UnknownProductException();
        }
        return product;
    }
}
