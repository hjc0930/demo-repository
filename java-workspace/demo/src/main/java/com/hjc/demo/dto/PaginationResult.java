package com.hjc.demo.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class PaginationResult <T>{
    private Integer page;
    private Integer pageSize;
    private long total;
    private List<T> list;
}
