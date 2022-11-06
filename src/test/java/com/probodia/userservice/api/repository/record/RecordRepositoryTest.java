package com.probodia.userservice.api.repository.record;

import com.probodia.userservice.api.entity.record.Records;
import com.probodia.userservice.api.entity.user.User;
import com.probodia.userservice.api.repository.user.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@SpringBootTest
class RecordRepositoryTest {

    @Autowired
    private RecordRepository recordRepository;
    @Autowired
    private UserRepository userRepository;

    @Test
    void findAllByUserAndTypeOrderByCreatedDateDesc() {
        int page = 1;
        int size = 2;
        PageRequest pageRequest = PageRequest.of(page, size);
        User user = userRepository.findByUserId("2343341101");
        List<String> filterType = new ArrayList<>();
        filterType.add("MEAL");
        filterType.add("MEDICINE");
        log.debug("user seq : {}",user.getUserSeq());
        Page<Records> result = recordRepository.findAllByUserAndTypeInOrderByCreatedDateDesc(pageRequest, user, filterType);
        log.debug("result = {}", result.getContent());

    }
}