package com.example.app5.service;

import com.example.app5.entity.CommitDetail;
import com.example.app5.repository.CommitDetailRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class CommitDetailService {

    private final CommitDetailRepository repository;

    @Transactional
    public List<CommitDetail> saveAll(Iterable<CommitDetail> commits) {
        return repository.saveAll(commits);
    }


    @Transactional(readOnly = true)
    public CommitDetail findById(long id) {
        return repository.findById(id).orElseThrow();
    }

    @Transactional(readOnly = true)
    public List<CommitDetail> findAllById(long id, int pageNumber, int rowPerPage) {
        List<CommitDetail> commitDetails = new ArrayList<>();
        Pageable sortedByLastUpdateDesc = PageRequest.of(pageNumber - 1, rowPerPage, Sort.by("id").ascending());
        repository.findAllById(id, sortedByLastUpdateDesc).forEach(commitDetails::add);

        return commitDetails;
    }

    @Transactional(readOnly = true)
    public long countById(long id) {
        return repository.countById(id);
    }
}
