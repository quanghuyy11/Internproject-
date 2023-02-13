package com.mgm.amazing_volunteer.repository;

import com.mgm.amazing_volunteer.model.Prize;
import com.mgm.amazing_volunteer.model.Request;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PrizeRepository extends JpaRepository<Prize, Long> {
    String GET_PRIZE_HAS_REQUEST_PENDING= "SELECT * FROM prizes p\n" +
                                          "INNER JOIN requests r ON p.id = r.prize_id\n" +
                                          "WHERE r.status = 'PENDING'";
    String GET_PRIZE_HAS_REQUEST_ACCEPTED= "SELECT * FROM prizes p\n" +
                                           "INNER JOIN requests r ON p.id = r.prize_id\n" +
                                           "WHERE r.status = 'ACCEPTED'";
    String GET_PRIZE_HAS_REQUEST_DECLINED= "SELECT * FROM prizes p\n" +
                                           "INNER JOIN requests r ON p.id = r.prize_id\n" +
                                           "WHERE r.status = 'DECLINED'";
    @Query(value = GET_PRIZE_HAS_REQUEST_ACCEPTED,nativeQuery = true)
    List<Prize> getPrizeHasRequestAccepted();
    @Query(value = GET_PRIZE_HAS_REQUEST_PENDING,nativeQuery = true)
    List<Prize> getPrizeHasRequestPending();
    @Query(value = GET_PRIZE_HAS_REQUEST_DECLINED,nativeQuery = true)
    List<Prize> getPrizeHasRequestDeclined();
}
