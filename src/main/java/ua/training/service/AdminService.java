package ua.training.service;

import ua.training.controller.exception.OrderNotFoundException;

public interface AdminService {

    void shipAllOrders() throws OrderNotFoundException;

    void shipOrder(Long orderId) throws OrderNotFoundException;

}
