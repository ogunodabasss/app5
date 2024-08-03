package com.example.app5.controller;


import com.example.app5.entity.Commit;
import com.example.app5.entity.CommitDetail;
import com.example.app5.service.CommitDetailService;
import com.example.app5.service.CommitService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Controller
public class CommitDetailController {

    private final CommitDetailService commitDetailService;
    private final ApplicationContext context;

    @GetMapping(value = "/commits/commit-detail/{id}")
    public String commitDetail(@NotNull Model model, @PathVariable("id") long commitId) {
        Commit commit = context.getBean(CommitService.class).findById(commitId);
        CommitDetail commitDetail = commitDetailService.findById(commit.getCommitDetail().getId());
        commit.setCommitDetail(commitDetail);

        model.addAttribute("commits", List.of(commit));

        return "commit-detail-list";
    }
}
