package com.mgm.amazing_volunteer.model;

import com.mgm.amazing_volunteer.util.enums.EventStatus;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "events")
public class Event extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long Id;

    @Column(name = "jotform_url")
    private String jotformUrl;
    @Column(name = "title", nullable = false)
    private String title;
    @Column(name = "point", nullable = false, columnDefinition = "INTEGER CHECK (point > 0 AND point <= 5)")
    private Integer point;
    @Column(name = "event_date", nullable = false)
    private Date eventDate;
    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private EventStatus status;
    @OneToMany(mappedBy = "event", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Submission> submissions = new ArrayList<>();
}
