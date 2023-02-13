package com.mgm.amazing_volunteer.repository;

import com.mgm.amazing_volunteer.dto.submission.SubmittedUser;
import com.mgm.amazing_volunteer.model.Submission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SubmissionRepository extends JpaRepository<Submission, Long> {

    String getSubmittedUserByEventId = "SELECT \n" +
                                        "NEW com.mgm.amazing_volunteer.dto.submission.SubmittedUser(s.user.email, s.isParticipated) \n"+
                                        "FROM submissions s\n" +
                                        "INNER JOIN users u ON s.user.email = u.email \n" +
                                        "WHERE s.event.Id = :event_id";

    @Modifying
    @Query(value = "UPDATE submissions\n" +
            "SET is_participated = true\n" +
            "WHERE id=:id AND event_id=:event_id", nativeQuery = true)
    void updateSubmissionStatusByIdAndEventId(Long id,Long event_id);

    @Query(value = getSubmittedUserByEventId)
    List<SubmittedUser> getSubmittedUserByEventId(@Param("event_id") Long id);
}
