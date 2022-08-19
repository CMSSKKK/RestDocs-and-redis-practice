package kr.ron2.restdocspractice.stock.application;

public interface StockService {

    Long order(Long stockId, Long quantity);

    void decrease(Long stockId, Long quantity);
}
