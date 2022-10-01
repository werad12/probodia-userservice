package com.probodia.userservice.api.repository.record;

import com.probodia.userservice.api.entity.enums.base.TimeTagCode;
import com.probodia.userservice.api.entity.record.Records;
import com.probodia.userservice.api.entity.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Repository
public interface RecordRepository extends JpaRepository<Records,Long> {
    List<Records> findAllByUser(User user);

    Page<Records> findAllByUserOrderByCreatedDateDesc(Pageable pageable, User user);

    Page<Records> findAllByUserAndTypeInOrderByCreatedDateDesc(Pageable pageable, User user, List<String> typeList);
    List<Records> findAllByUserAndRecordDateBetween(User user, String start, String end);

    List<Records> findAllByUserAndRecordDateBetweenAndType(User user, String start, String end,String type);

    List<Records> findAllByUserAndRecordDateBetweenAndTypeInAndTimeTagInOrderByCreatedDateDesc(User user,
                                                                                                String start,
                                                                                                String end,
                                                                                                      List<String> typeList,
                                                                                                List<TimeTagCode> timeTagCodes);

    void deleteAllByUser(User user);
}
