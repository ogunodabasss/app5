package com.example.app5.repository;

import com.example.app5.entity.CommitDetail;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CommitDetailRepository extends JpaRepository<CommitDetail, Long> {

    @Query(value = """
            SELECT COUNT(*)
            FROM commit_detail
            WHERE developer_id = ?1
            """, nativeQuery = true)
    long countById(Long id);

    Page<CommitDetail> findAllById(Long id, Pageable pageable);


}
