import com.hjc.practice.entity.Order;

import java.math.BigDecimal;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class Main {
//    public static void main(String[] args) {
//        List<Order> orders = Arrays.asList(
//                new Order("Laptop", new BigDecimal("1200.0")),
//                new Order("Phone", new BigDecimal("800.0")),
//                new Order("Laptop", new BigDecimal("1500.0")),
//                new Order("Tablet", new BigDecimal("500.0")),
//                new Order("Phone", new BigDecimal("900.0"))
//        );
//
//        Map<String, BigDecimal> collect = orders.stream().collect(
//                Collectors.groupingBy(
//                        Order::getProduct,
//                        Collectors.mapping(
//                                Order::getAmount,
//                                Collectors.reducing(BigDecimal.ZERO, BigDecimal::add)
//                        )
//                )
//        );
//        String noProducts = collect.entrySet().stream()
//                .max(Map.Entry.comparingByValue())
//                .map(Map.Entry::getKey)
//                .orElse("No Products");
//
//    }
}
