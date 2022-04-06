package com.devskiller.orders;

import org.junit.Before;
import org.junit.Test;

import java.time.DayOfWeek;
import java.util.List;

public class OrdersAnalyzerTest {


    private List<OrdersAnalyzer.Order> orders;

    @Before
    public void setUp() throws Exception {
        orders = OrdersTestHelper.readOrders("/orders.json");
    }

    @Test
    public void shouldReturnCorrectValueForMonday() {
        // given
        OrdersAnalyzer ordersAnalyzer = new OrdersAnalyzer();

        // then
        OrdersTestHelper.testResults(ordersAnalyzer, DayOfWeek.MONDAY, orders);
    }

    @Test
    public void shouldReturnCorrectValueForTuesday() {
        //given
        OrdersAnalyzer ordersAnalyzer = new OrdersAnalyzer();

        // then
        OrdersTestHelper.testResults(ordersAnalyzer, DayOfWeek.TUESDAY, orders);
    }

    @Test
    public void shouldReturnCorrectValueForWednesday() {
        //given
        OrdersAnalyzer ordersAnalyzer = new OrdersAnalyzer();

        // then
        OrdersTestHelper.testResults(ordersAnalyzer, DayOfWeek.WEDNESDAY, orders);
    }

    @Test
    public void shouldReturnCorrectValueForThursday() {
        //given
        OrdersAnalyzer ordersAnalyzer = new OrdersAnalyzer();

        // then
        OrdersTestHelper.testResults(ordersAnalyzer, DayOfWeek.THURSDAY, orders);
    }

    @Test
    public void shouldReturnCorrectValueForFriday() {
        //given
        OrdersAnalyzer ordersAnalyzer = new OrdersAnalyzer();

        // then
        OrdersTestHelper.testResults(ordersAnalyzer, DayOfWeek.FRIDAY, orders);
    }

    @Test
    public void shouldReturnCorrectValueForSaturday() {
        //given
        OrdersAnalyzer ordersAnalyzer = new OrdersAnalyzer();

        // then
        OrdersTestHelper.testResults(ordersAnalyzer, DayOfWeek.SATURDAY, orders);
    }

    @Test
    public void shouldReturnCorrectValueForSunday() {
        //given
        OrdersAnalyzer ordersAnalyzer = new OrdersAnalyzer();

        // then
        OrdersTestHelper.testResults(ordersAnalyzer, DayOfWeek.SUNDAY, orders);
    }
}
