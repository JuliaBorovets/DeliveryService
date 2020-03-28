package ua.training.entity.order;

public enum OrderStatus {
    PAID("status.paid"),
    NOT_PAID("status.not.paid"),
    SHIPPED("status.shipped");

    private String name;

    OrderStatus(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "status." + name().toLowerCase();
    }
}
