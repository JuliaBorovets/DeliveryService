package ua.training.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ua.training.entity.order.Order;
import ua.training.entity.order.Status;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order> findOrderByOwnerId(long ownerId);

//    List<Order> findOrderByOrderStatus(OrderStatus orderStatus);

    List<Order> findOrderByStatus(Status status);

}
