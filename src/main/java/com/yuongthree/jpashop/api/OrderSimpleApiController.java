package com.yuongthree.jpashop.api;

import com.yuongthree.jpashop.domain.Address;
import com.yuongthree.jpashop.domain.Order;
import com.yuongthree.jpashop.domain.OrderSearch;
import com.yuongthree.jpashop.domain.OrderStatus;
import com.yuongthree.jpashop.repository.OrderRepository;
import javafx.beans.property.SimpleListProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class OrderSimpleApiController {

    private final OrderRepository orderRepository;

    @GetMapping("/api/v2/simple-orders")
    public Result<List<SimpleOrderDto>> ordersV2(){
        List<Order> orders = orderRepository.findAllByString(new OrderSearch());
        List<SimpleOrderDto> orderDtoList = orders.stream().map(SimpleOrderDto::new).collect(Collectors.toList());
        return new Result<>(orderDtoList);
    }

    @Data
    static class SimpleOrderDto{
        private Long orderId;
        private String name;
        private LocalDateTime orderDate; //주문시간
        private OrderStatus orderStatus;
        private Address address;

        public SimpleOrderDto(Order o){
            orderId=o.getId();
            name = o.getMember().getName();
            orderDate = o.getOrderDate();
            orderStatus=o.getStatus();
            address=o.getDelivery().getAddress();
        }
    }

    @Data
    @AllArgsConstructor
    static class Result<T>{
        private T data;
    }

}
