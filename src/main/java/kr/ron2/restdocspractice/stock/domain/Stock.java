package kr.ron2.restdocspractice.stock.domain;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class Stock {

    @Id
    private Long id;
    private String name;
    private Long quantity;

    public static Stock of(String name, Long quantity) {
        return new Stock(null, name, quantity);
    }

    public void decrease(Long quantity) {
        if (this.quantity - quantity < 0) {
            throw new IllegalArgumentException("재고 부족");
        }
        this.quantity -= quantity;
    }

}
