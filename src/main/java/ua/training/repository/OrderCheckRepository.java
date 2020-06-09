package ua.training.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ua.training.entity.order.OrderCheck;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface OrderCheckRepository extends CrudRepository<OrderCheck, Long> {

    List<OrderCheck> findAllByUser_Id(Long id);

    List<OrderCheck> findAll();

    @Query("select count (e) from OrderCheck e where year(e.creationDate) = :year ")
    Long ordersByCreationYear( @Param("year") int year);

    @Query("select count (e) from OrderCheck e where month(e.creationDate) = :month and year(e.creationDate) = :year ")
    Long ordersByCreationMonthsAndYear(@Param("month") int month, @Param("year") int year);

    @Query("select sum (e.priceInCents) from OrderCheck e where year(e.creationDate) = :year ")
    BigDecimal earningsByCreationYear(@Param("year") int year);

    @Query("select sum (e.priceInCents) from OrderCheck e where month(e.creationDate) = :month and year(e.creationDate) = :year ")
    BigDecimal earningsByCreationMonthsAndYear(@Param("month") int month, @Param("year") int year);
}
