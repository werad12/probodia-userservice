package com.probodia.userservice.utils;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PageInfoUtil {

    private int page;
    private int size;
    private int totalElements;
    private int totalPages;

}
