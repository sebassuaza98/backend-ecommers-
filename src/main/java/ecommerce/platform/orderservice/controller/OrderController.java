package ecommerce.platform.orderservice.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import ecommerce.platform.exception.InsufficientStockException;
import ecommerce.platform.exception.ResourceNotFoundException;
import ecommerce.platform.orderservice.model.Order;
import ecommerce.platform.orderservice.service.OrderService;
import ecommerce.platform.resource.config.AppConstants;
import ecommerce.platform.resource.response.ApiResponse;

import java.util.List;

@CrossOrigin(origins = AppConstants.URL)
@RestController
@RequestMapping("/api/orders")
public class OrderController {
    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping
    public List<Order> getAllOrders() {
        return orderService.findAllOrders();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Order> getOrderById(@PathVariable Long id) {
        return new ResponseEntity<>(orderService.findOrderById(id), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<ApiResponse<Order>> createOrder(@RequestBody Order order) {
        try {
            Order createdOrder = orderService.createOrder(order);

            ApiResponse<Order> response = new ApiResponse<>(
                HttpStatus.CREATED.value(), 
                AppConstants.CREATE_ORDER, 
                createdOrder
            );
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (ResourceNotFoundException e) {
            ApiResponse<Order> errorResponse = new ApiResponse<>(
                HttpStatus.NOT_FOUND.value(), 
                e.getMessage()
            );
            return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
        } catch (InsufficientStockException e) {
            ApiResponse<Order> errorResponse = new ApiResponse<>(
                HttpStatus.BAD_REQUEST.value(), 
                e.getMessage()
            );
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            ApiResponse<Order> errorResponse = new ApiResponse<>(
                HttpStatus.INTERNAL_SERVER_ERROR.value(), 
                AppConstants.ERROR_ORDER
            );
            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}