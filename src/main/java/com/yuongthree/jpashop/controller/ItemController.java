package com.yuongthree.jpashop.controller;

import com.yuongthree.jpashop.domain.item.Book;
import com.yuongthree.jpashop.domain.item.BookForm;
import com.yuongthree.jpashop.domain.item.Item;
import com.yuongthree.jpashop.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;

    @GetMapping("/items/new")
    public String createForm(Model model){
        model.addAttribute("form",new BookForm());
        return "items/createItemForm";
    }

    @PostMapping("/items/new")
    public String create(@Valid BookForm form, BindingResult result){
        if(result.hasErrors()){
            return "items/createItemForm";
        }
        Book book = new Book();
        book.setAuthor(form.getAuthor());
        book.setIsbn(form.getIsbn());
        book.setName(form.getName());
        book.setPrice(form.getPrice());
        book.setStockQuantity(form.getStockQuantity());
        itemService.saveItem(book);
        return "redirect:/items";
    }

    @GetMapping("/items")
    public String itemList(Model model){
        List<Item> itemList = itemService.findItems();
        model.addAttribute("items",itemList);
        return "items/itemList";
    }

    @GetMapping("/items/{id}/edit")
    public String updateForm(Model model, @PathVariable Long id){
        Book book = (Book)itemService.findOne(id);
        BookForm form = new BookForm();
        form.setId(id);
        form.setAuthor(book.getAuthor());
        form.setName(book.getName());
        form.setPrice(book.getPrice());
        form.setStockQuantity(book.getStockQuantity());
        model.addAttribute("form", form);
        return "items/updateItemForm";
    }

    @PostMapping("/items/{id}/edit")
    public String updateItem(@PathVariable Long id, @Valid BookForm form, BindingResult result){
        if(result.hasErrors()){
            return"items/updateItemForm";
        }
        itemService.updateItem(id,form.getAuthor(),form.getName(),form.getPrice(),form.getStockQuantity());

        return "redirect:/items";
    }


}

