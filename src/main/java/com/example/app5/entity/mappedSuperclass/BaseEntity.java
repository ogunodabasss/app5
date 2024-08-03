package com.example.app5.entity.mappedSuperclass;

import jakarta.persistence.MappedSuperclass;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Data
@NoArgsConstructor
@MappedSuperclass
public class BaseEntity {
}