package com.probodia.userservice.api.repository.record;

import com.probodia.userservice.api.entity.enums.base.TimeTagCode;
import com.probodia.userservice.api.entity.record.Records;
import com.probodia.userservice.api.entity.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

public interface RecordRepository extends JpaRepository<Records,Long> {
    List<Records> findAllByUser(User user);

    Page<Records> findAllByUserOrderByCreatedDateDesc(Pageable pageable, User user);

//    @Query(
//            value = "SELECT * FROM Record as r where r.user_seq = (:user) and r.rtype in (:types)  ORDER BY r.created_at desc",
//            countQuery = "SELECT count(*) FROM Record as r where r.user_seq = (:user) and r.rtype in (:types) ",
//            nativeQuery = true
//    )
//    Page<Records> findAllByUserAndTypeOrderByCreatedDateDesc(Pageable pageable, @Param("user") String userSeq,@Param("types") List<String> filterType);

    Page<Records> findAllByUserAndTypeInOrderByCreatedDateDesc(Pageable pageable, User user, List<String> typeList);
    List<Records> findAllByUserAndRecordDateBetween(User user, String start, String end);

    List<Records> findAllByUserAndRecordDateBetweenAndTypeInAndTimeTagInOrderByCreatedDateDesc(User user,
                                                                                                String start,
                                                                                                String end,
                                                                                                List<String> typeList,
                                                                                                List<TimeTagCode> timeTagCodes);

}
