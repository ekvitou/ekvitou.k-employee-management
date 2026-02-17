package com.ekvitou.employee_management.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "positions")
@Getter
@Setter
@NoArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler", "employees"})
public class Position extends Audit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, updatable = false)
    private String uuid;

    @Column(name = "name", nullable = false, unique = true, length = 100)
    private String positionName;

    @Column(name = "level", length = 50)
    private String positionLevel;

    @Column(nullable = false)
    private Boolean isDeleted = false;

    @OneToMany(mappedBy = "position", fetch = FetchType.LAZY)
    private Set<Employee> employees = new HashSet<>();

    @PrePersist
    public void prePersist() {
        if (uuid == null) {
            uuid = java.util.UUID.randomUUID().toString();
        }
    }
}

