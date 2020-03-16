package ua.training.entity.order;

public enum OrderType {
    DOCUMENTS(10),
    SMALL(20),
    MIDDLE(30),
    BIG(40);

    private final int priceForType;

    OrderType(int priceForType) {
        this.priceForType = priceForType;
    }

    public int getPriceForType() {
        return priceForType;
    }
}
