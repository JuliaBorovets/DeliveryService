package ua.training.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ua.training.entity.order.OrderType;

@Repository
public interface OrderTypeRepository extends CrudRepository<OrderType, Long> {

}
