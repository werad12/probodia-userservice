package com.probodia.userservice.api.repository.record;

import com.probodia.userservice.api.entity.record.Record;
import com.probodia.userservice.api.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RecordRepository extends JpaRepository<Record,Long> {
    List<Record> findAllByUser(User user);
}
