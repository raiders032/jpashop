package com.yuongthree.jpashop.controller;

import com.yuongthree.jpashop.domain.Member;
import com.yuongthree.jpashop.domain.Order;
import com.yuongthree.jpashop.domain.item.Item;
import com.yuongthree.jpashop.service.ItemService;
import com.yuongthree.jpashop.service.MemberService;
import com.yuongthree.jpashop.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.graalvm.compiler.core.common.type.ArithmeticOpTable;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@RequiredArgsConstructor
@Controller
public class OrderController {

    private final OrderService orderService;
    private final MemberService memberService;
    private final ItemService itemService;


    @GetMapping("/order")
    public String orderForm(Model model){
        List<Member> memberList = memberService.findAll();
        List<Item> items = itemService.findItems();
        model.addAttribute("members",memberList);
        model.addAttribute("items",items);
        return"order/orderForm";
    }

    @PostMapping("/order")
    public String order(@RequestParam Long memberId, @RequestParam Long itemId, @RequestParam int count){
        orderService.createOrder(memberId,itemId,count);
        return "redirect:/orders";

    }

}
