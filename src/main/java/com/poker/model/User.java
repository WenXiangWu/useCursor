package com.poker.model;

import java.io.Serializable;

import lombok.Data;
import javax.persistence.*;

@Entity
@Data
public class User implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    private String email;
    private String nickname;
    private int chips = 1000;  // 默认筹码
    private boolean enabled = true;
}