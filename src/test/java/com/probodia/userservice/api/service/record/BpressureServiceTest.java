package com.probodia.userservice.api.service.record;

import com.probodia.userservice.api.controller.auth.AuthController;
import com.probodia.userservice.api.controller.record.BPressureController;
import com.probodia.userservice.api.repository.record.BPressureRepository;
import com.probodia.userservice.config.security.JwtConfig;
import com.probodia.userservice.oauth.token.AuthTokenProvider;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
@Slf4j
@ActiveProfiles("test")
class BpressureServiceTest {

    @Autowired
    BpressureService bpressureService;

    @MockBean(name = "bPressureRepository")
    BPressureRepository bPressureRepository;

    @MockBean(name = "jwtConfig")
    JwtConfig jwtConfig;

    @MockBean(name = "authController")
    AuthController authController;

    @MockBean(name = "bPressureController")
    BPressureController bPressureController;

    @MockBean(name = "tokenProvider")
    AuthTokenProvider tokenProvider;

//    @Autowired
//    BPressureRepository bPressureRepository;

    @Test
    void savePressure() {
        log.info("A");
    }

    @Test
    void updateBPressure() {
    }

    @Test
    void findBPressureByUserAndId() {
    }

    @Test
    void deleteBPressure() {
    }
}