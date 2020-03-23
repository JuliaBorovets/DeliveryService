package ua.training.entity.order;

public enum DeliveryDate {

    IN_A_MOMENT(0),
    QUICKLY(2),
    LONG(5);

    private int day;

    DeliveryDate(int day) {
        this.day = day;
    }

    public int getDay() {
        return day;
    }
}
