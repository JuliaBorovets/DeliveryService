package ua.training.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ua.training.controller.exception.BankTransactionException;
import ua.training.controller.exception.OrderCreateException;
import ua.training.controller.exception.OrderNotFoundException;
import ua.training.dto.OrderDTO;
import ua.training.entity.order.Order;
import ua.training.entity.user.User;

import java.math.BigDecimal;
import java.util.List;

public interface OrderService {

    List<OrderDTO> findAllUserOrder(Long userId);

    void createOrder(OrderDTO orderDTO, User user) throws OrderCreateException;

    Order getOrderById(Long id) throws OrderNotFoundException;

    void orderToShip() throws OrderNotFoundException;

    Page<OrderDTO> findPaginated(Pageable pageable, List<OrderDTO> orders);

    void payForOrder(Long orderId) throws BankTransactionException;

    List<OrderDTO> findAllPaidOrdersDTO();

    BigDecimal calculatePrice(OrderDTO orderDTO);
}
