package proiect_252.util;

import proiect_252.model.Order;

import java.util.Comparator;

public class OrderComparatorByPrice implements Comparator<Order> {
    @Override
    public int compare(Order o1, Order o2) {
        return Double.compare(o1.getTotalAmount(), o2.getTotalAmount());
    }
}