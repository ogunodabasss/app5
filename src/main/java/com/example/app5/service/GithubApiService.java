package com.example.app5.service;

import com.example.app5.util.GithubApiUtil;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
public class GithubApiService {

    public Map<String, List<GithubApiUtil.CommitApi.CommitDto>> getLastOneMountCommit(final String userName, final LocalDateTime since, final LocalDateTime until) {
    /*
        Map<String, List<GithubApiUtil.CommitApi.CommitDto>> map = new HashMap<>();
        map.put("app",new ArrayList<>(){{
            add(new GithubApiUtil.CommitApi.CommitDto("xx1",LocalDateTime.now(),"ogun",
                    new GithubApiUtil.CommitDetailApi.CommitDetailDto("xx1",LocalDateTime.now(),"ogun","xxx1","patch1")));
            add(new GithubApiUtil.CommitApi.CommitDto("xx2",LocalDateTime.now(),"ogun",
                    new GithubApiUtil.CommitDetailApi.CommitDetailDto("xx2",LocalDateTime.now(),"ogun","xxx2","patch2")));
            add(new GithubApiUtil.CommitApi.CommitDto("xx3",LocalDateTime.now(),"ogun",
                    new GithubApiUtil.CommitDetailApi.CommitDetailDto("xx3",LocalDateTime.now(),"ogun","xxx3","patch3")));
        }});

        return map;

     */

        return GithubApiUtil.getLastOneMountCommit(userName, since, until);

    }
}
