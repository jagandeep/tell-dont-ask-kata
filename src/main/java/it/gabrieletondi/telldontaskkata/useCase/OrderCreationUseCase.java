package it.gabrieletondi.telldontaskkata.useCase;

import it.gabrieletondi.telldontaskkata.domain.Order;
import it.gabrieletondi.telldontaskkata.domain.OrderItem;
import it.gabrieletondi.telldontaskkata.domain.OrderStatus;
import it.gabrieletondi.telldontaskkata.domain.Product;
import it.gabrieletondi.telldontaskkata.repository.OrderRepository;
import it.gabrieletondi.telldontaskkata.repository.ProductCatalog;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static java.math.BigDecimal.valueOf;

public class OrderCreationUseCase {
    private final OrderRepository orderRepository;
    private final ProductCatalog productCatalog;

    public OrderCreationUseCase(OrderRepository orderRepository, ProductCatalog productCatalog) {
        this.orderRepository = orderRepository;
        this.productCatalog = productCatalog;
    }

    public void run(SellItemsRequest request) {
        Order order = new Order();
        order.setStatus(OrderStatus.CREATED);
        order.setItems(new ArrayList<>());
        order.setCurrency("EUR");
        order.setTax(getTax(request));
        order.setTotal(getTaxedAmount(request));
        order.setItems(getOrderItems(request));
        orderRepository.save(order);
    }

    private List<OrderItem> getOrderItems(SellItemsRequest request) {
        List<OrderItem> orderItems = request.getRequests().stream()
                .map(this::createOrderItem).collect(Collectors.toList());
        return orderItems;
    }

    private BigDecimal getTax(SellItemsRequest request) {
        BigDecimal tax = new BigDecimal("0.00");
        for (SellItemRequest itemRequest : request.getRequests()) {
            Product product = getProduct(itemRequest);
            final int quantity = itemRequest.getQuantity();
            tax = tax.add(product.getTaxAmount(quantity));

        }
        return tax;
    }

    private BigDecimal getTaxedAmount(SellItemsRequest request) {
        BigDecimal taxedAmount = new BigDecimal("0.00");
        for (SellItemRequest itemRequest : request.getRequests()) {
            Product product = getProduct(itemRequest);
            final int quantity = itemRequest.getQuantity();
            taxedAmount = taxedAmount.add(product.getTaxedAmount(quantity));

        }
        return taxedAmount;
    }

    private OrderItem createOrderItem(SellItemRequest itemRequest) {
        final OrderItem orderItem = new OrderItem();
        Product product = getProduct(itemRequest);
        orderItem.setProduct(product);
        orderItem.setQuantity(itemRequest.getQuantity());
        orderItem.setTax(product.getTaxAmount(itemRequest.getQuantity()));
        orderItem.setTaxedAmount(product.getTaxedAmount(itemRequest.getQuantity()));
        return orderItem;
    }

    private Product getProduct(SellItemRequest itemRequest) {
        Product product = productCatalog.getByName(itemRequest.getProductName());

        if (product == null) {
            throw new UnknownProductException();
        }
        return product;
    }

}
