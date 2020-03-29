package ua.training.entity.order;

public enum Destination {
    NONE(0, "destination.none", 0),
    INNER(10, "destination.inner", 2),
    COUNTRY(20, "destination.country", 5);

    private final int priceForDestination;
    private String name;
    private int day;


    Destination(int priceForDestination, String name, int day) {
        this.priceForDestination = priceForDestination;
        this.name = name;
        this.day = day;
    }

    public String getName() {
        return name;
    }

    public int getDay() {
        return day;
    }

    public int getPriceForDestination() {
        return priceForDestination;
    }

    @Override
    public String toString() {
        return name;
    }
}