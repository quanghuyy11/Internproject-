package com.mgm.amazing_volunteer.model;

import com.mgm.amazing_volunteer.dto.submission.SubmittedUser;
import lombok.*;
import org.hibernate.annotations.NamedNativeQuery;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "submissions")
public class Submission extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "event_id", nullable = false)
    private Event event;
    @Column(name = "is_participated")
    private boolean isParticipated = false;
}
