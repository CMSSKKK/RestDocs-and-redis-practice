package kr.ron2.restdocspractice.stock.application;

import kr.ron2.restdocspractice.stock.domain.Stock;
import kr.ron2.restdocspractice.stock.infra.StockJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class StockServiceImpl implements StockService {

    private final StockJpaRepository stockJpaRepository;

    @Override
    public Long order(Long stockId, Long quantity) {
        Stock stock = stockJpaRepository.findById(stockId)
                .orElseThrow(NoSuchElementException::new);

        stock.decrease(quantity);

        return stock.getQuantity();
    }
}
