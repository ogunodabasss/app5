package com.example.app5.entity;

import com.example.app5.entity.mappedSuperclass.BaseEntity;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Accessors(fluent = false)
@Data
@SuperBuilder
@ToString(callSuper = true)
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler", "fieldHandler"})

@Entity
@Table
public class Commit extends BaseEntity {

    @EqualsAndHashCode.Include
    @OneToOne(optional = false, fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(referencedColumnName = "id", unique = true)
    @NotNull
    protected CommitDetail commitDetail;


    @EqualsAndHashCode.Include
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @EqualsAndHashCode.Include
    @ManyToOne(optional = false, fetch = FetchType.LAZY, cascade = {CascadeType.MERGE, CascadeType.DETACH, CascadeType.REFRESH})
    @JoinColumn(referencedColumnName = "id", nullable = false)
    @NotNull
    private Developer developer;

    @NotNull
    @Size(min = 3, max = 100)
    @Column(nullable = false, length = 100)
    private String hash;

    @NotNull
    @Size(min = 3, max = 100)
    @Column(nullable = false, length = 100)
    private String author;

    @NotNull
    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false)
    private LocalDateTime time;
}
