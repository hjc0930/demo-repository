package com.hjc.practice;

import com.hjc.practice.entity.Employee;
import com.hjc.practice.entity.Order;
import com.hjc.practice.entity.Person;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class PracticeMain {
    public static void main(String[] args) {
        /**
         * Question 1：给定一个整数 List，请：
         * 1.找出所有大于 50 的数字
         * 2.将这些数字乘以 2
         * 3.收集到一个新的 List 中
         */
        List<Integer> list = Arrays.asList(10, 20, 30, 40, 50, 60, 70, 80, 90, 100);

        System.out.println("--Question 1--");
        list.stream()
                .filter(i -> i > 50)
                .map(i -> i * 2)
                .toList()
                .forEach(System.out::println);
        /**
         * Question 2：给定一个 Person类（有 name 和 age 属性）的 List，请：
         * 1. 找出所有年龄大于 18 岁的人
         * 2. 提取他们的名字
         * 3. 收集到一个 Set 中（去重）
         */
        List<Person> peoples = Arrays.asList(
                new Person("Alice", 25),
                new Person("Bob", 17),
                new Person("Charlie", 32),
                new Person("David", 15),
                new Person("Eve", 28)
        );
        System.out.println("--Question 2--");

        peoples.stream()
                .filter(person -> person.getAge() > 18)
                .map(Person::getName)
                .collect(Collectors.toSet())
                .forEach(System.out::println);

        /**
         * Question 3: 有一个订单列表，每个订单有产品名称和金额，请：
         * 1.按产品名称分组
         * 2.计算每个产品的总销售额
         * 3.找出销售额最高的产品
         */

        List<Order> orders = Arrays.asList(
                new Order("Laptop", new BigDecimal("1200.0")),
                new Order("Phone", new BigDecimal("800.0")),
                new Order("Laptop", new BigDecimal("1500.0")),
                new Order("Tablet", new BigDecimal("500.0")),
                new Order("Phone", new BigDecimal("900.0"))
        );

        System.out.println("--Question 3--");
        Map<String, BigDecimal> salesByProduct = orders.stream()
                .collect(Collectors.groupingBy(
                        Order::getProduct,
                        Collectors.mapping(Order::getAmount, Collectors.reducing(BigDecimal.ZERO, BigDecimal::add))
                )
        );
        String topProduct = salesByProduct.entrySet()
                .stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse("No Products");
        System.out.println(topProduct);

        /**
         * Question 4：从一个字符串列表中：
         * 1.筛选出长度大于 5 的字符串
         * 2.转换为大写
         * 3.按字母顺序排序
         * 4.用逗号连接成一个字符串
         */
        List<String> words = Arrays.asList("apple", "banana", "cherry", "date", "elderberry", "fig");
        System.out.println("--Question 4--");
        String newWord = words.stream()
                .filter(i -> i.length() > 5)
                .map(String::toUpperCase)
                .sorted()
                .collect(Collectors.joining(","));
        System.out.println(newWord);

        /**
         * Question 5: 有一个包含多个列表的列表，请：
         * 1.将所有元素扁平化为一个流
         * 2.找出所有偶数
         * 3.排序后收集到新集合中
         */
        List<List<Integer>> numberLists = Arrays.asList(
                Arrays.asList(1, 2, 3),
                Arrays.asList(4, 5, 6),
                Arrays.asList(7, 8, 9, 10)
        );
        System.out.println("--Question 5--");
        numberLists.stream()
                .flatMap(Collection::stream)
                .filter(number -> number % 2 == 0)
                .sorted()
                .toList()
                .forEach(System.out::println);

        /**
         * Question 6: 从一组数字中：
         * 1.计算平均值
         * 2.找出最大值和最小值
         * 3.统计大于平均值的数字个数
         */
        List<Integer> numbers = Arrays.asList(10, 20, 30, 40, 50, 60, 70, 80, 90, 100);

        System.out.println("--Question 6--");
        double average = numbers.stream()
                .mapToInt(Integer::intValue)
                .average()
                .orElse(0);
        long count = numbers.stream()
                .filter(i -> i > average)
                .count();

        System.out.println("Average: " + average);
        System.out.println("Count greater than average: " + count);
        numbers.stream().max(Integer::compareTo).ifPresent(i -> System.out.println("Max: " + i));
        numbers.stream().min(Integer::compareTo).ifPresent(i -> System.out.println("Min: " + i));

        /**
         * Question 7: 使用并行流处理大量数据：
         * 1.生成 1 到 100,000 的数字列表
         * 2.使用并行流找出所有素数
         * 3.统计素数个数
         */
        List<Integer> numbers2 = IntStream.rangeClosed(1, 100000)
                .boxed()
                .toList();
        System.out.println("--Question 7--");
        long count1 = numbers2.parallelStream()
                .filter(n -> n > 1 && IntStream.rangeClosed(2, (int) Math.sqrt(n))
                        .noneMatch(divisor -> n % divisor == 0))
                .count();
        System.out.println("Count of primes: " + count1);
        /**
         * Question 8: 有一个员工列表，每个员工有姓名、部门和工资，请：
         * 1.按部门分组
         * 2.计算每个部门的平均工资
         * 3.找出平均工资最高的部门
         */
        List<Employee> employees = Arrays.asList(
                new Employee("Alice", "IT", new BigDecimal("75000")),
                new Employee("Bob", "HR", new BigDecimal("65000")),
                new Employee("Charlie", "IT", new BigDecimal("80000")),
                new Employee("David", "Sales", new BigDecimal("55000")),
                new Employee("Eve", "HR", new BigDecimal("70000"))
        );
        System.out.println("--Question 8--");
        Map<String, Double> avgSalaryByDept = employees
                .stream()
                .collect(Collectors.groupingBy(
                        Employee::getName,
                        Collectors.averagingDouble(e -> e.getSalary().doubleValue())
                ));
        String s = avgSalaryByDept.entrySet()
                .stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse("No Departments");

        System.out.println("Department with highest average salary: " + s);
    }
}
