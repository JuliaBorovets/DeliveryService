package ua.training.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ua.training.entity.order.OrderCheck;

@Repository
public interface OrderCheckRepository extends CrudRepository<OrderCheck, Long> {
}
