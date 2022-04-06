package com.devskiller.orders;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

public class OrdersAnalyzer {

    static class Order {
        int orderId;
        LocalDateTime creationDate;
        List<OrderLine> orderLines;
    }

    static class OrderLine {
        int productId;
        String name;
        int quantity;
        BigDecimal unitPrice;
    }

    public Map<DayOfWeek, Integer> averageDailySales(List<Order> orders) {
        var daysOfWeek = DayOfWeek.values();

        EnumMap<DayOfWeek, BigDecimal> orderSales = new EnumMap<>(DayOfWeek.class);
        EnumMap<DayOfWeek, Integer> orderFrequency = new EnumMap<>(DayOfWeek.class);

        filterOrderSalesAndFrequencyPerDaysOfWeek(orders, orderSales, orderFrequency);

        return calculateAverageDailySales(daysOfWeek, orderSales, orderFrequency);
    }

    private EnumMap<DayOfWeek, Integer> calculateAverageDailySales(DayOfWeek[] daysOfWeek, EnumMap<DayOfWeek, BigDecimal> orderSales, EnumMap<DayOfWeek, Integer> orderFrequency) {
        EnumMap<DayOfWeek, Integer> averageDailySales = new EnumMap<>(DayOfWeek.class);

        Arrays.stream(daysOfWeek)
              .forEach(dayOfWeek -> averageDailySales.put(dayOfWeek, 0));

        averageDailySales.replaceAll((w, v) -> orderSales.getOrDefault(w, BigDecimal.ZERO)
                                                         .divide(BigDecimal.valueOf(
                                                                orderFrequency.getOrDefault(w, 1)),
                                                        2,
                                                        RoundingMode.HALF_UP)
                                                         .setScale(0, RoundingMode.HALF_UP)
                                                         .intValue());

        return averageDailySales;
    }

    private void filterOrderSalesAndFrequencyPerDaysOfWeek(List<Order> orders, EnumMap<DayOfWeek, BigDecimal> orderSales, EnumMap<DayOfWeek, Integer> orderFrequency) {
        for (var order : orders) {
            var day = order.creationDate.getDayOfWeek();
            var value = BigDecimal.ZERO;

            for (var product : order.orderLines) {
                value = value.add(product.unitPrice.multiply(BigDecimal.valueOf(product.quantity)));
            }

            orderFrequency.put(day, orderFrequency.getOrDefault(day, 0) + 1);
            orderSales.put(day, orderSales.getOrDefault(day, BigDecimal.ZERO)
                                          .add(value));
        }
    }
}
