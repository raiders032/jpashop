package com.yuongthree.jpashop.api;

import com.yuongthree.jpashop.domain.*;
import com.yuongthree.jpashop.repository.OrderRepository;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class OrderApiController {

    private final OrderRepository orderRepository;

    @GetMapping("/api/v2/orders")
    public OrderResponse<List<OrderDto>> ordersV2(){
        List<Order> orders = orderRepository.findAllByString(new OrderSearch());
        List<OrderDto> orderDtos = orders.stream()
                .map(OrderDto::new).collect(Collectors.toList());
        return new OrderResponse<>(orderDtos);
    }

    @GetMapping("/api/v3/orders")
    public OrderResponse<List<OrderDto>> orderV3(){
        List<Order> orders = orderRepository.findAllWithItem();
        List<OrderDto> collect = orders.stream().map(OrderDto::new).collect(Collectors.toList());
        return new OrderResponse<>(collect);
    }

    @GetMapping("/api/v3.1/orders")
    OrderResponse<List<OrderDto>> ordersV3_page(@RequestParam(defaultValue = "0") int offset, @RequestParam(defaultValue = "100") int limit){
        List<Order> orders = orderRepository.findAllWithMemberDelivery(offset,limit);
        List<OrderDto> collect = orders.stream().map(OrderDto::new).collect(Collectors.toList());
        return new OrderResponse<>(collect);
    }

    @AllArgsConstructor
    @Getter
    static class OrderResponse<T>{
        private T data;
    }

    @Getter
    @AllArgsConstructor
    static class OrderDto{
        private Long orderId;
        private String name;
        private LocalDateTime orderDate; //주문시간
        private OrderStatus orderStatus;
        private Address address;
        private List<OrderItemDto> orderItems;

        public OrderDto(Order order) {
            orderId = order.getId();
            name = order.getMember().getName();
            orderDate = order.getOrderDate();
            orderStatus = order.getStatus();
            address = order.getDelivery().getAddress();
            orderItems = order.getOrderItems().stream()
                    .map(OrderItemDto::new).collect(Collectors.toList());
        }
    }

    @Getter
    static class OrderItemDto{

        private String itemName;//상품 명
        private int orderPrice; //주문 가격
        private int count;

        public OrderItemDto(OrderItem orderItem) {
            itemName = orderItem.getItem().getName();
            orderPrice = orderItem.getOrderPrice();
            count = orderItem.getCount();
        }
    }
}
