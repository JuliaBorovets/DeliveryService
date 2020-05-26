package ua.training.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ua.training.entity.order.OrderCheck;

import java.util.List;

@Repository
public interface OrderCheckRepository extends CrudRepository<OrderCheck, Long> {

    List<OrderCheck> findAllByUser_Id(Long id);
}
