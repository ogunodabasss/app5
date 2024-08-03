package com.example.app5.service;

import com.example.app5.controller.req.DeveloperReq;
import com.example.app5.entity.Commit;
import com.example.app5.entity.CommitDetail;
import com.example.app5.entity.Developer;
import com.example.app5.repository.DeveloperRepository;
import com.example.app5.util.GithubApiUtil;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.ApplicationContext;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@RequiredArgsConstructor
@Service
public class DeveloperService {
    private static final ConcurrentMap<Long, Boolean> CONCURRENT_MAP = new ConcurrentHashMap<>();
    private final DeveloperRepository repository;
    private final ApplicationContext context;

    @Transactional
    public Developer add(@NotNull DeveloperReq userReq) {
        Developer user = Developer.builder().userName(userReq.getUserName()).mail(userReq.getMail()).lastUpdateTime(LocalDateTime.of(1970, 1, 1, 0, 0, 0)).build();

        return repository.save(user);
    }

    @Transactional
    public void update(long id, @NotNull DeveloperReq userReq) {
        System.err.println(userReq);
        repository.update(id, userReq.getUserName(), userReq.getMail());
    }

    @Transactional
    public void updateLastUpdateTime(Long id) {
        if (CONCURRENT_MAP.containsKey(id)) throw new RuntimeException("wait for the process to finish");
        else CONCURRENT_MAP.put(id, Boolean.TRUE);
        try {
            var nowTime = LocalDateTime.now();
            final var developer = repository.findById(id).orElseThrow();
            var beforeTime = developer.getLastUpdateTime();
            final List<Commit> commits = new ArrayList<>();
            Map<String, List<GithubApiUtil.CommitApi.CommitDto>> map = context.getBean(GithubApiService.class).getLastOneMountCommit(developer.getUserName(), beforeTime, nowTime);
            for (String key : map.keySet()) {
                var value = map.get(key);
                for (GithubApiUtil.CommitApi.CommitDto commitDto : value) {
                    var commit = Commit.builder()
                            .author(commitDto.author())
                            .time(commitDto.time())
                            .hash(commitDto.hash())
                            .developer(Developer.builder().id(id).build())
                            .commitDetail(CommitDetail.builder()
                                    .message(commitDto.CommitDetailDto().message())
                                    .patch(commitDto.CommitDetailDto().patch())
                                    .build()
                            )
                            .build();
                    System.err.println("patch size........: "+commit.getCommitDetail().getPatch().length());
                    commits.add(commit);
                }
            }
            var _ = context.getBean(CommitService.class).saveAll(commits);
            repository.updateLastUpdateTime(id, nowTime);
        } finally {
            CONCURRENT_MAP.remove(id);
        }
    }


    @Transactional(readOnly = true)
    public List<Developer> findAll(int pageNumber, int rowPerPage) {
        List<Developer> developers = new ArrayList<>();
        Pageable sortedByLastUpdateDesc = PageRequest.of(pageNumber - 1, rowPerPage, Sort.by("lastUpdateTime").descending());
        repository.findAll(sortedByLastUpdateDesc).forEach(developers::add);
        return developers;
    }

    @Transactional(readOnly = true)
    private boolean existsById(Long id) {
        return repository.existsById(id);
    }

    @Transactional(readOnly = true)
    public Developer findById(Long id) {
        return repository.findById(id).orElse(null);
    }

    @Transactional
    public void deleteById(Long id) throws Exception {
        if (!existsById(id)) {
            throw new Exception("Cannot find Note with id: " + id);
        } else {
            repository.deleteById(id);
        }
    }

    @Transactional(readOnly = true)
    public Long count() {
        return repository.count();
    }
}
