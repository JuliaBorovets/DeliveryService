package ua.training.entity.order;

public enum Destination {
    NONE(10),
    INNER(20),
    COUNTRY(30);

    private final int priceForDestination;

    Destination(int priceForDestination) {
        this.priceForDestination = priceForDestination;
    }

    public int getPriceForDestination() {
        return priceForDestination;
    }
}