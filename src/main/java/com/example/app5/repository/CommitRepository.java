package com.example.app5.repository;

import com.example.app5.entity.Commit;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface CommitRepository extends JpaRepository<Commit,Long> {

    @Query(value = """
            SELECT COUNT(*)
            FROM commit
            WHERE developer_id = ?1
            """,nativeQuery = true)
    long countByDeveloperId(Long developerId);

    Page<Commit> findAllByDeveloperId(Long developerId, Pageable pageable);

}
