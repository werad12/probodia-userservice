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
@SpringBootTest(properties = "spring.profiles.active:test")
@Slf4j
class BpressureServiceTest {

    @Autowired
    BpressureService bpressureService;

    @Test
    void savePressure() {
        log.debug("A");
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