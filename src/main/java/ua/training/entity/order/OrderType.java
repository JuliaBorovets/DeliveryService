package ua.training.entity.order;

public enum OrderType {
    DOCUMENTS(0, "type.documents"),
    SMALL(10, "type.small"),
    MIDDLE(20, "type.middle"),
    BIG(30, "type.big");

    private final int priceForType;
    private String name;

    OrderType(int priceForType, String name) {
        this.priceForType = priceForType;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public int getPriceForType() {
        return priceForType;
    }

    @Override
    public String toString() {
        return name;
    }
}
