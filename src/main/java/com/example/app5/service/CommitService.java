package com.example.app5.service;

import com.example.app5.entity.Commit;
import com.example.app5.repository.CommitRepository;
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
public class CommitService {

    private final CommitRepository repository;


    @Transactional(readOnly = true)
    public List<Commit> findAllByDeveloperId(long developerId, int pageNumber, int rowPerPage) {
        List<Commit> commits = new ArrayList<>();
        Pageable sortedByLastUpdateDesc = PageRequest.of(pageNumber - 1, rowPerPage, Sort.by("id").ascending());
        repository.findAllByDeveloperId(developerId, sortedByLastUpdateDesc).forEach(commits::add);
        return commits;
    }

    @Transactional(readOnly = true)
    private boolean existsById(Long id) {
        return repository.existsById(id);
    }

    @Transactional(readOnly = true)
    public Commit findById(Long id) {
        return repository.findById(id).orElse(null);
    }

    @Transactional
    public Commit save(Commit commit) {
        return repository.save(commit);
    }

    @Transactional(readOnly = true)
    public Long count() {
        return repository.count();
    }

    @Transactional(readOnly = true)
    public long countByDeveloperId(long developerId) {
        return repository.countByDeveloperId(developerId);
    }

    @Transactional
    public List<Commit> saveAll(Iterable<Commit> commits) {
        return repository.saveAll(commits);
    }
}
