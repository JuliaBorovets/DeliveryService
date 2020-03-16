package ua.training.controller;

public interface OrderValidation {

    String DESCRIPTION = "[a-z]{2,100}";
    String DOUBLE_NUMBER = "((-|\\+)?[0-9]+(\\.[0-9]+)?)+";
}
