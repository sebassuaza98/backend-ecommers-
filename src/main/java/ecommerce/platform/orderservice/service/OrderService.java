package ecommerce.platform.orderservice.service;

import ecommerce.platform.catalogoservice.model.Product;
import ecommerce.platform.exception.InsufficientStockException;
import ecommerce.platform.exception.ResourceNotFoundException;
import ecommerce.platform.orderservice.model.Order;
import ecommerce.platform.orderservice.model.OrderItem;
import ecommerce.platform.orderservice.repository.OrderRepository;
import ecommerce.platform.resource.config.AppConstants;
import ecommerce.platform.orderservice.repository.OrderItemRepository;
import org.springframework.stereotype.Service;
import ecommerce.platform.catalogoservice.service.ProductService;

import java.util.List;

@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final ProductService productService; 

    public OrderService(OrderRepository orderRepository, OrderItemRepository orderItemRepository, ProductService productService) {
        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
        this.productService = productService; 
    }

    public List<Order> findAllOrders() {
        return orderRepository.findAll();
    }

    public Order findOrderById(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(AppConstants.NOT_ORDER + id));
    }

    public Order createOrder(Order order) {
        for (OrderItem item : order.getItems()) {
            Product product = productService.findProductById(item.getProductId());
    
            if (product == null) {
                throw new ResourceNotFoundException(AppConstants.NOT_ORDER);
            }
            if (item.getQuantity() > product.getStock()) {
                throw new InsufficientStockException(AppConstants.EXCCESDS);
            }
            productService.updateProductStock(item.getProductId(), item.getQuantity());
    
            item.setOrder(order);
        }
        return orderRepository.save(order);
    }
}
