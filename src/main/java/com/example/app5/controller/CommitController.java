package com.example.app5.controller;

import com.example.app5.entity.Commit;
import com.example.app5.entity.Developer;
import com.example.app5.service.CommitService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

import static com.example.app5.util.CacheData.ROW_PAGE_SIZE;

@Slf4j
@RequiredArgsConstructor
@Controller
public class CommitController {
    private  final CommitService commitService;

    @GetMapping(value = "/commits/{id}")
    public String developers(@NotNull Model model, @PathVariable("id") long developerId, @RequestParam(value = "page", defaultValue = "1") int pageNumber) {
        List<Commit> commits = commitService.findAllByDeveloperId(developerId,pageNumber, ROW_PAGE_SIZE);
        long count = commitService.countByDeveloperId(developerId);
        boolean hasPrev = pageNumber > 1;
        boolean hasNext = ((long) pageNumber * ROW_PAGE_SIZE) < count;
        model.addAttribute("commits", commits);
        model.addAttribute("hasPrev", hasPrev);
        model.addAttribute("prev", pageNumber - 1);
        model.addAttribute("hasNext", hasNext);
        model.addAttribute("next", pageNumber + 1);

        return "commit-list";
    }
}
