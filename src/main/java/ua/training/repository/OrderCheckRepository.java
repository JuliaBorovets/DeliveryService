package ua.training.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ua.training.entity.order.OrderCheck;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface OrderCheckRepository extends CrudRepository<OrderCheck, Long> {

    List<OrderCheck> findAllByUser_Id(Long id);

    @Query("select e from OrderCheck e where year(e.creationDate) = :year")
    List<OrderCheck> findAllByCreationYear(@Param("year") int year);

    List<OrderCheck> findAllByCreationDateAfter(LocalDate localDate);

}
