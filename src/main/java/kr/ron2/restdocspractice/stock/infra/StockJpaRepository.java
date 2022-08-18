package kr.ron2.restdocspractice.stock.infra;

import kr.ron2.restdocspractice.stock.domain.Stock;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StockJpaRepository extends JpaRepository<Stock, Long> {
}
