package ua.training.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ua.training.entity.order.Destination;

@Repository
public interface DestinationRepository extends CrudRepository<Destination, Long> {
}
