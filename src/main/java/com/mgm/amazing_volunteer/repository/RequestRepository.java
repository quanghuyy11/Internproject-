package com.mgm.amazing_volunteer.repository;

import com.mgm.amazing_volunteer.dto.request.RequestDto;
import com.mgm.amazing_volunteer.model.Request;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RequestRepository extends JpaRepository<Request, Long> {
    @Query(value = "SELECT * FROM requests r INNER JOIN users u on r.user_id = u.email WHERE u.email = :email AND is_deleted = false ORDER BY r.created_at DESC", nativeQuery = true)
    List<Request> findByUser_Email(String email);
    @Query(value = "SELECT * FROM requests r WHERE r.status='PENDING' AND r.is_deleted=false ORDER BY r.created_at DESC", nativeQuery = true)
    List<Request> findAllPendingRequest();
    @Query(value = "SELECT * FROM requests r WHERE EXTRACT(YEAR FROM r.created_at) = :year ORDER BY r.status = 'PENDING' DESC, r.created_at DESC",nativeQuery = true)
    List<Request> findAllRequestByYear(@Param("year") int year);
}
