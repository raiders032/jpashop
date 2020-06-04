package com.yuongthree.jpashop.service;

import com.yuongthree.jpashop.domain.*;
import com.yuongthree.jpashop.domain.item.Book;
import com.yuongthree.jpashop.domain.item.Item;
import com.yuongthree.jpashop.exception.NotEnoughStockException;
import com.yuongthree.jpashop.repository.ItemRepository;
import com.yuongthree.jpashop.repository.MemberRepository;
import com.yuongthree.jpashop.repository.OrderRepository;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;


@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class OrderServiceTest {

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private ItemRepository itemRepository;

    @Test
    public void 주문생성(){
        //given
        String name = "JPA";
        int stock = 1000;
        int count = 10;
        int price = 10000;

        Member member = createMember();

        Book book = createBook(stock, price, name);

        //when
        Long savedId = orderService.createOrder(member.getId(), book.getId(),count);

        //then
        Order order = orderRepository.findOne(savedId);
        Assert.assertEquals("상품 주문시 상태는 ORDER",OrderStatus.ORDER,order.getStatus());
        Assert.assertEquals("상품 주문시 재고가 줄어야 한다.",stock-count, book.getStockQuantity());
        Assert.assertEquals("주문 전체 가격", count*price,order.getTotalPrice());

    }

    @Test(expected = NotEnoughStockException.class)
    public void 주문생성_재고수량초과(){
        //given
        String name = "JPA";
        int stock = 10;
        int count = 20;
        int price = 10000;
        Member member = createMember();
        Book book = createBook(stock, price, name);

        //when
        Long savedId = orderService.createOrder(member.getId(), book.getId(),count);

        //then
        fail("NotEnoughStockException 예외가 발생해야 한다.");

    }

    @Test
    public void 주문취소(){
        //given
        String name = "JPA";
        int stock = 1000;
        int count = 10;
        int price = 10000;

        Member member = createMember();
        Book book = createBook(stock, price, name);
        Long orderId = orderService.createOrder(member.getId(), book.getId(),count);

        //when
        orderService.cancel(orderId);

        //then
        Order order = orderRepository.findOne(orderId);
        Assert.assertEquals("주문 상태가 CANCEL", OrderStatus.CANCEL, order.getStatus());
        Assert.assertEquals("재고 수량이 다시 올라야한다.", stock, book.getStockQuantity());
    }

    private Member createMember() {
        Member member = new Member();
        member.setName("nys");
        member.setAddress(new Address("seoul", "MP", "123"));
        memberRepository.save(member);
        return member;
    }

    private Book createBook(int stock, int price, String name) {
        Book book = new Book();
        book.setName(name);
        book.setPrice(price);
        book.setStockQuantity(stock);
        itemRepository.save(book);
        return book;
    }

}