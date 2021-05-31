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
        Order order = createOrder(request);
        orderRepository.save(order);
    }

    private Order createOrder(SellItemsRequest request) {
        Order order = new Order();
        order.setStatus(OrderStatus.CREATED);
        order.setItems(new ArrayList<>());
        order.setCurrency("EUR");
        order.setTotal(new BigDecimal("0.00"));
        order.setTax(new BigDecimal("0.00"));

        order.setItems(createOrderItems(request));
        order.setTax(order.calculateTax());
        order.setTotal(order.calculateTotal());
        return order;
    }

    private List<OrderItem> createOrderItems(SellItemsRequest request) {
        return  request.getRequests()
                .stream()
                .map(this::createOrderItem)
                .collect(Collectors.toList());
    }

    private OrderItem createOrderItem(SellItemRequest itemRequest) {
        Product product = getProduct(itemRequest);
        final int quantity = itemRequest.getQuantity();
        return OrderItem.createOrderItem(product, quantity);
    }

    private Product getProduct(SellItemRequest itemRequest) {
        Product product = productCatalog.getByName(itemRequest.getProductName());

        if (product == null) {
            throw new UnknownProductException();
        }
        return product;
    }
}
