package com.example.app5.entity;

import com.example.app5.entity.mappedSuperclass.BaseEntity;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;

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
public class CommitDetail extends BaseEntity {

    @ToString.Include
    @EqualsAndHashCode.Include
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Nullable
    @Size(max = 500)
    @Column(length = 500)
    private String message;

    @NotNull
    @Size(max = 150_000)
    @Column(nullable = false, length = 150_000)
    private String patch;
}
