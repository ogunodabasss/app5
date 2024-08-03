package com.example.app5.repository;

import com.example.app5.entity.Developer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;

public interface DeveloperRepository extends JpaRepository<Developer, Long> {

    @Modifying
    @Query(value = """
            UPDATE developer
            SET user_name = ?2 , mail = ?3
            WHERE id = ?1
            """, nativeQuery = true)
    void update(Long id, String userName, String mail);

    @Modifying
    @Query(value = """
            UPDATE developer
            SET last_update_time = ?2
            WHERE id = ?1
            """, nativeQuery = true)
    void updateLastUpdateTime(Long id, LocalDateTime lastUpdateTime);
}
