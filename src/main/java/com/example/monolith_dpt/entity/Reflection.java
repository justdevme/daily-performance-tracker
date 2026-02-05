package com.example.monolith_dpt.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@ToString
@NoArgsConstructor
public class Reflection {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String content;  // Reflection content

    @ManyToOne
    @JoinColumn(name = "week_id")
    private Week week;
}
