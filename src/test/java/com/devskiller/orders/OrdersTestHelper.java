package com.devskiller.orders;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.net.URISyntaxException;
import java.nio.file.Paths;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;
import org.assertj.core.api.Condition;

import static org.assertj.core.api.Assertions.assertThat;

public class OrdersTestHelper {

	static class ZeroOrNull extends Condition<Integer> {

		@Override
		public boolean matches(Integer value) {
			return value == null || value.equals(0);
		}
	}

	static Gson gson = new GsonBuilder().registerTypeAdapter(LocalDateTime.class, new JsonDeserializer<LocalDateTime>() {
		@Override
		public LocalDateTime deserialize(JsonElement json, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
			return ZonedDateTime.parse(json.getAsJsonPrimitive().getAsString()).toLocalDateTime();
		}
	}).create();
	static Type listType = new TypeToken<ArrayList<OrdersAnalyzer.Order>>() {
	}.getType();

	static List<OrdersAnalyzer.Order> readOrders(String fileName) throws URISyntaxException, FileNotFoundException {
		return gson.fromJson(new FileReader(Paths.get(OrdersAnalyzerTest.class.getResource(fileName).toURI()).toFile()), listType);
	}


	public static Map<DayOfWeek, Integer> expectedResults(List<OrdersAnalyzer.Order> orders) {
		Map<DayOfWeek, BigDecimal> sums = new HashMap<>();
		Map<DayOfWeek, Integer> counts = new HashMap<>();

		for (DayOfWeek dayOfWeek : DayOfWeek.values()) {
			sums.put(dayOfWeek, BigDecimal.ZERO);
			counts.put(dayOfWeek, 0);
		}
		for (int idx = 0; idx < orders.size(); idx++) {
			OrdersAnalyzer.Order order = orders.get(idx);
			DayOfWeek dayOfWeek = order.creationDate.getDayOfWeek();
			BigDecimal orderValue = order.orderLines.stream()
					.map(line -> line.unitPrice.multiply(BigDecimal.valueOf(line.quantity)))
					.reduce(BigDecimal.ZERO, BigDecimal::add);

			sums.put(dayOfWeek, sums.get(dayOfWeek).add(orderValue));
			counts.put(dayOfWeek, counts.get(dayOfWeek)+1);
		}
		Map<DayOfWeek, Integer> avgs = new HashMap<>();
		sums.forEach(((dayOfWeek, sum) -> avgs.put(dayOfWeek, counts.get(dayOfWeek) != 0 ?
				sums.get(dayOfWeek).divide(BigDecimal.valueOf(counts.get(dayOfWeek)), BigDecimal.ROUND_HALF_UP).setScale(0, BigDecimal.ROUND_HALF_UP).intValue()
		: 0)));
		return avgs;
	}

	private static void assertWeekdayValues(DayOfWeek dayOfWeek, Map<DayOfWeek, Integer> result, Map<DayOfWeek, Integer> expected) {
		Integer val = expected.get(dayOfWeek);
		if (val == null || val.equals(0)) {
			assertThat(result.get(dayOfWeek)).as("for " + dayOfWeek.name()).is(new ZeroOrNull());
		} else {
			assertThat(result.get(dayOfWeek)).as("for " + dayOfWeek.name()).isEqualTo(val);
		}
	}

	static void testResults(OrdersAnalyzer ordersAnalyzer, DayOfWeek dayOfWeeek, List<OrdersAnalyzer.Order> orders) {
		Map<DayOfWeek, Integer> result = ordersAnalyzer.averageDailySales(orders);
		Map<DayOfWeek, Integer> expectedResult = expectedResults(orders);

		assertWeekdayValues(dayOfWeeek, result, expectedResult);
	}
}
