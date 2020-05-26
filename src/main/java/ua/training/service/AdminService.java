package ua.training.service;

import ua.training.controller.exception.OrderNotFoundException;

public interface AdminService {

    void orderToShip() throws OrderNotFoundException;
}
