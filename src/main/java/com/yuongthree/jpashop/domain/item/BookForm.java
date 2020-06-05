package com.yuongthree.jpashop.domain.item;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Setter @Getter
public class BookForm {

    private Long id;

    @NotEmpty
    private String name;

    @Min(0)
    private int price;

    @Min(0)
    private int stockQuantity;

    @NotEmpty
    private String author;

    @NotEmpty
    private String isbn;
}
