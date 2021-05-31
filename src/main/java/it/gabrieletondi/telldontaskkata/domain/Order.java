package it.gabrieletondi.telldontaskkata.domain;

import java.math.BigDecimal;
import java.util.List;

import static it.gabrieletondi.telldontaskkata.domain.OrderStatus.*;

public class Order {
    private BigDecimal total;
    private String currency;
    private List<OrderItem> items;
    private BigDecimal tax;
    private OrderStatus status;
    private int id;

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public List<OrderItem> getItems() {
        return items;
    }

    public void setItems(List<OrderItem> items) {
        this.items = items;
    }

    public BigDecimal getTax() {
        return tax;
    }

    public void setTax(BigDecimal tax) {
        this.tax = tax;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public BigDecimal calculateTax() {
        BigDecimal tax = new BigDecimal("0.00");
        for(OrderItem orderItem: getItems()) {
            tax = tax.add(orderItem.getTax());
        }
        return tax;
    }

    public BigDecimal calculateTotal() {
        BigDecimal total = new BigDecimal("0.00");
        for(OrderItem orderItem: getItems()) {
            total = total.add(orderItem.getTaxedAmount());
        }
        return total;
    }

    public boolean isShipped() {
        return this.status.equals(OrderStatus.SHIPPED);
    }

    public boolean isApproved() {
        return this.status.equals(OrderStatus.APPROVED);
    }

    public boolean isRejected() {
        return this.status.equals(OrderStatus.REJECTED);
    }

    public boolean isNotShippable() {
        return this.status.equals(CREATED) || this.status.equals(REJECTED);
    }

    public boolean isShippable() {
        return this.status.equals(SHIPPED);
    }
}
