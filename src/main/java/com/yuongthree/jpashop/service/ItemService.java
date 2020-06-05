package com.yuongthree.jpashop.service;

import com.yuongthree.jpashop.domain.item.Book;
import com.yuongthree.jpashop.domain.item.BookForm;
import com.yuongthree.jpashop.domain.item.Item;
import com.yuongthree.jpashop.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class ItemService {

    private final ItemRepository itemRepository;

    @Transactional
    public void saveItem(Item item){
        itemRepository.save(item);
    }

    public Item findOne(Long id){
        return itemRepository.findOne(id);
    }

    public List<Item> findItems(){
        return itemRepository.findAll();
    }

    @Transactional
    public void updateItem(Long id, String author, String name, int price, int stockQuantity) {
        Book book = (Book)itemRepository.findOne(id);
        book.setAuthor(author);
        book.setName(name);
        book.setPrice(price);
        book.setStockQuantity(stockQuantity);
    }
}
