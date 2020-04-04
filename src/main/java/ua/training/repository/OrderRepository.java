package ua.training.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ua.training.entity.order.Order;
import ua.training.entity.order.OrderStatus;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    // List<Order> findOrderByOwnerId(long ownerId);

    //List<Order> findOrderByOrderStatus(OrderStatus orderStatus);

    Page<Order> findOrderByOwnerId(Pageable pageable, long ownerId);

    Page<Order> findOrderByOrderStatus(Pageable pageable, OrderStatus orderStatus);


}
