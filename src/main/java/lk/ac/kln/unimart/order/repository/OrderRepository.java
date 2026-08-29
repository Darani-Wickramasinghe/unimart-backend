package lk.ac.kln.unimart.order.repository;

import lk.ac.kln.unimart.order.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
}