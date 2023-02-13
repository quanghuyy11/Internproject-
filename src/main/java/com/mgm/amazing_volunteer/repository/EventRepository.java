package com.mgm.amazing_volunteer.repository;

import com.mgm.amazing_volunteer.model.Event;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface EventRepository extends JpaRepository<Event, Long> {
    @Query(value = "SELECT * FROM events e WHERE e.is_deleted=false", nativeQuery = true)
    List<Event> findInProgressEvents();

    @Query(value = "SELECT * FROM events e WHERE e.is_deleted=false", nativeQuery = true)
    List<Event> findNotDeletedEvents();

    @Query(value = "SELECT * FROM events e WHERE EXTRACT(YEAR FROM e.event_date) = :year ORDER BY e.event_date DESC", nativeQuery = true)
    Page<Event> findAllByYear(@Param("year") int year,Pageable paging);

    @Query(value = "SELECT * FROM events e WHERE EXTRACT(YEAR FROM e.event_date) = :year AND  e.status = :status", nativeQuery = true)
    Page<Event> findAllByYearAndStatus(@Param("status") String status,@Param("year") int year,Pageable paging);

    @Query(value = "SELECT * FROM events e WHERE EXTRACT(YEAR FROM e.event_date) = :year AND  e.is_deleted = true", nativeQuery = true)
    Page<Event> findAllByStatusDelete(@Param("year") int year,Pageable paging);

    @Query(value = "SELECT * FROM events", nativeQuery = true)
    Page<Event> findAllEvent(Pageable paging);

    @Modifying
    @Query(value = "UPDATE events SET is_deleted = true WHERE id = :id", nativeQuery = true)
    void deleteById(@Param("id") Long id);
}
