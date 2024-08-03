package com.example.app5.entity;

import com.example.app5.entity.mappedSuperclass.BaseEntity;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;


@Accessors(fluent = false)
@Data
@SuperBuilder
@ToString(onlyExplicitlyIncluded = true, callSuper = true)
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler", "fieldHandler"})


@Entity
@Table
public class Developer extends BaseEntity {
    @ToString.Include
    @EqualsAndHashCode.Include
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ToString.Include
    @NotNull
    @Size(min = 3, max = 30)
    @Column(nullable = false, length = 30, unique = true)
    private String userName;

    @ToString.Include
    @Email(regexp = "^([_a-zA-Z0-9-]+(\\.[_a-zA-Z0-9-]+)*@[a-zA-Z0-9-]+(\\.[a-zA-Z0-9-]+)*(\\.[a-zA-Z]{1,6}))?$")
    @NotNull
    @Size(min = 3, max = 50)
    @Column(nullable = false, length = 50, unique = true)
    private String mail;

    @ToString.Include
    @NotNull
    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false)
    private LocalDateTime lastUpdateTime;

}
