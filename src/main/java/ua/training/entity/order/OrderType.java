package ua.training.entity.order;

public enum OrderType {
    SMALL(20),
    BIG(30);

    private final int priceForType;

    OrderType(int priceForType) {
        this.priceForType = priceForType;
    }

    public int getPriceForType() {
        return priceForType;
    }
}
