package ua.training.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ua.training.entity.order.Order;
import ua.training.entity.order.Status;

import java.util.List;

@Repository
public interface OrderRepository extends CrudRepository<Order, Long> {

    List<Order> findOrderByOwnerId(Long ownerId);

    List<Order> findByStatusAndOwner_Id(Status status, Long ownerId);

    List<Order> findOrderByStatus(Status status);

}
