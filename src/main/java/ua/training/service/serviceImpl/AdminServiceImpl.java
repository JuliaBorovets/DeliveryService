package ua.training.service.serviceImpl;

import ua.training.controller.exception.OrderNotFoundException;
import ua.training.service.AdminService;

public class AdminServiceImpl implements AdminService {
    public void orderToShip() throws OrderNotFoundException {
//        List<OrderDTO> orders = findAllPaidOrdersDTO();
//
//        for (OrderDTO o : orders) {
//            orderSetShippedStatus(o.getDtoId());
//        }
//
    }

    //    private void orderSetShippedStatus(Long id) throws OrderNotFoundException {
//        Order order = getOrderById(id);
//        if (isPaid(order)) {
//            order.setOrderStatus(OrderStatus.SHIPPED);
//            order.setDeliveryDate(LocalDate.now().plusDays(order.getDestination().getDay()));
//            orderRepository.save(order);
//        }
//
//    }
//
}
