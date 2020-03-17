package ua.training.entity.order;

public enum Destination {
    NONE(0),
    INNER(10),
    COUNTRY(20);

    private final int priceForDestination;

    Destination(int priceForDestination) {
        this.priceForDestination = priceForDestination;
    }

    public int getPriceForDestination() {
        return priceForDestination;
    }
}