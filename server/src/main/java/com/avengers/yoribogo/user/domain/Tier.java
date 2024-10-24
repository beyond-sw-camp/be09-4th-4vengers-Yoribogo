package com.avengers.yoribogo.user.domain;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "tier")
@Data
public class Tier {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "tier_id")
    private Long tierId;

    @Column(name = "tier_name", nullable = false)
    private String tierName;

    @Column(name = "tier_criteria", nullable = false)
    private Long tierCriteria;

    @Column(name = "tier_image", nullable = false, columnDefinition = "TEXT")
    private String tierImage;
}
