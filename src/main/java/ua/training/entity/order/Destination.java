package ua.training.entity.order;

public enum Destination {
    NONE(0, 0),
    INNER(10, 2),
    COUNTRY(20, 5);

    private final int priceForDestination;

    private int day;


    Destination(int priceForDestination, int day) {
        this.priceForDestination = priceForDestination;

        this.day = day;
    }

    public int getDay() {
        return day;
    }

    public int getPriceForDestination() {
        return priceForDestination;
    }

}