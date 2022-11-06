package com.probodia.userservice;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;

@SpringBootTest
@Slf4j
public class dateTest {

    @Test
    void dateTest(){

        String strDate = "2017-11-30 12:11:32";
        Date date = new Date(strDate);

        log.debug("Date : {}",date);

    }

}
