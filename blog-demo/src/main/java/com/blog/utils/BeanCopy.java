package com.blog.utils;

import org.springframework.beans.BeanUtils;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.stream.Collectors;

public class BeanCopy {
    public static <O, T> List<T> copyBeanList(List<O> originList, Class<T> targetClazz) {
        return originList.stream()
                .map(list -> {
                    try {
                        T targetLit = targetClazz.getDeclaredConstructor().newInstance();

                        BeanUtils.copyProperties(list, targetLit);

                        return targetLit;
                    } catch (InstantiationException e) {
                        throw new RuntimeException(e);
                    } catch (IllegalAccessException e) {
                        throw new RuntimeException(e);
                    } catch (InvocationTargetException e) {
                        throw new RuntimeException(e);
                    } catch (NoSuchMethodException e) {
                        throw new RuntimeException(e);
                    }
                }).collect(Collectors.toList());
    }
}
