package com.yuongthree.jpashop.service;

import com.yuongthree.jpashop.domain.*;
import com.yuongthree.jpashop.domain.item.Item;
import com.yuongthree.jpashop.repository.ItemRepository;
import com.yuongthree.jpashop.repository.MemberRepository;
import com.yuongthree.jpashop.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final MemberRepository memberRepository;
    private final ItemRepository itemRepository;

    //주문
    @Transactional
    public Long createOrder(Long memberId, Long itemId, int count){

        Member member = memberRepository.findOne(memberId);
        Item item = itemRepository.findOne(itemId);

        Delivery delivery = new Delivery();
        delivery.setAddress(member.getAddress());
        delivery.setStatus(DeliveryStatus.READY);


        OrderItem orderItem = OrderItem.createOrderItem(item, item.getPrice(), count);

        Order order = Order.createOrder(member,delivery,orderItem);

        //cascade
        orderRepository.save(order);
        return order.getId();
    }

    //취소
    @Transactional
    public void cancel(Long orderId){
        Order order = orderRepository.findOne(orderId);
        order.cancel();
    }

    //검색
    public Order findOrder(Long id){
        return orderRepository.findOne(id);
    }

    public List<Order> findOrders(OrderSearch orderSearch) {
        return orderRepository.findAllByString(orderSearch);
    }
}
