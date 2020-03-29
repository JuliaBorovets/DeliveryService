package ua.training.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ua.training.dto.OrderDTO;
import ua.training.entity.order.Order;
import ua.training.entity.order.OrderStatus;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    Optional<Order> findOrderById(Long id);

    List<Order> findOrderByOwnerId(long ownerId);


    List<Order> findOrderByOrderStatus(OrderStatus orderStatus);

    Page<Order> findAllBy(Pageable pageable);
    
}
