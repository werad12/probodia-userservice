package com.probodia.userservice.api.repository.record;

import com.probodia.userservice.api.entity.record.Records;
import com.probodia.userservice.api.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RecordRepository extends JpaRepository<Records,Long> {
    List<Records> findAllByUser(User user);
}
