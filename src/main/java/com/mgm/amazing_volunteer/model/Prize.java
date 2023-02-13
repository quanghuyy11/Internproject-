package com.mgm.amazing_volunteer.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "prizes")
public class Prize  extends  BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long Id;
    @Column(name = "prize_name", nullable = false)
    private String prize_name;
    @Column(name = "description", nullable = false)
    private String description;
    @Column(name = "point_archive", nullable = false)
    private Integer point_archive;

}
