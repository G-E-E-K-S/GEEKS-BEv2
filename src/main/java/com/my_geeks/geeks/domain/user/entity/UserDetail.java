package com.my_geeks.geeks.domain.user.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class UserDetail {
    @Id
    @Column(name = "user_detail_id")
    private Long id;
}
