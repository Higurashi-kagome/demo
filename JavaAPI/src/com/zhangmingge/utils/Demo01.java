package com.zhangmingge.utils;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Stream to List<br>
 * https://mkyong.com/java8/java-8-convert-a-stream-to-list/
 */
public class Demo01 {
    public static void main(String[] args) {

        Stream<String> language = Stream.of("java", "python", "node");

        List<String> result = language.collect(Collectors.toList());

        result.forEach(System.out::println);

    }
}
