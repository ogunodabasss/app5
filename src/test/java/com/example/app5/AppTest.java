package com.example.app5;


import com.example.app5.util.GithubApiUtil;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public class AppTest {

    @Test
    public void appTestCommit() {
        Map<String, List<GithubApiUtil.CommitApi.CommitDto>> res = GithubApiUtil.getLastOneMountCommit("ogunodabasss", LocalDateTime.of(2024, 3, 1, 0, 0, 0), LocalDateTime.now());
        System.out.println(res);
    }

    @Test
    public void appTest() {
        final String URL = """
                https://api.github.com/users/%s/repos""".formatted("1");
        System.out.println(URL);

    }

}
