package it.gabrieletondi.telldontaskkata.domain;

import java.math.BigDecimal;

import static java.math.RoundingMode.HALF_UP;

public class OrderItem {
    private Product product;
    private int quantity;
    private BigDecimal taxedAmount;
    private BigDecimal tax;

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getTaxedAmount() {
        return taxedAmount;
    }

    public void setTaxedAmount(BigDecimal taxedAmount) {
        this.taxedAmount = taxedAmount;
    }

    public BigDecimal getTax() {
        return tax;
    }

    public void setTax(BigDecimal tax) {
        this.tax = tax;
    }

    public BigDecimal calculateTaxAmount(int quantity) {
        return getProduct().getUnitaryTax().multiply(BigDecimal.valueOf(quantity));
    }

    public BigDecimal calculateTaxedAmount(int quantity) {
        return getProduct().getUnitaryTaxedAmount().multiply(BigDecimal.valueOf(quantity)).setScale(2, HALF_UP);
    }

    public static OrderItem createOrderItem(Product product, int quantity) {
        final OrderItem orderItem = new OrderItem();
        orderItem.setProduct(product);
        orderItem.setQuantity(quantity);
        orderItem.setTax(orderItem.calculateTaxAmount(quantity));
        orderItem.setTaxedAmount(orderItem.calculateTaxedAmount(quantity));// feeling uncomfortable can be simplified
        return orderItem;
    }

}
