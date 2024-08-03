package com.example.app5.controller;

import com.example.app5.controller.req.DeveloperReq;
import com.example.app5.entity.Developer;
import com.example.app5.service.DeveloperService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.example.app5.util.CacheData.ROW_PAGE_SIZE;

@Slf4j
@RequiredArgsConstructor
@Controller
public class DeveloperController {

    private final DeveloperService developerService;


    @GetMapping(value = {"/developers/add"})
    public String showAddNote(Model model) {
        DeveloperReq developerReq = DeveloperReq.newInstance();
        model.addAttribute("add", true);
        model.addAttribute("developer", developerReq);

        return "developer-edit";
    }

    @PostMapping(value = "/developers/add")
    public String add(Model model, @ModelAttribute("developer") DeveloperReq developerReq) {
        try {
            Developer newUser = developerService.add(developerReq);
            return "redirect:/developers/" + newUser.getId();
        } catch (Exception ex) {
            String errorMessage = ex.getMessage();
            log.error(errorMessage, ex);

            model.addAttribute("errorMessage", errorMessage);
            model.addAttribute("add", true);
            return "developer-edit";
        }
    }


    @GetMapping(value = {"/developers/{id}/edit"})
    public String showEdit(Model model, @PathVariable long id) {
        Developer developer = null;
        try {
            developer = developerService.findById(id);
        } catch (Exception ex) {
            var message = ex.getMessage();
            model.addAttribute("errorMessage", message);
            log.error(ex.getMessage(), ex);
        }
        model.addAttribute("add", false);
        model.addAttribute("developer", developer);
        return "developer-edit";
    }

    @PostMapping(value = {"/developers/{id}/edit"})
    public String edit(Model model, @PathVariable long id, @ModelAttribute("developer") DeveloperReq developerReq) {
        try {
            developerService.update(id, developerReq);
            return "redirect:/developers/" + id;
        } catch (Exception ex) {
            String errorMessage = ex.getMessage();
            model.addAttribute("errorMessage", errorMessage);
            model.addAttribute("add", false);
            return "developer-edit";
        }
    }

    @GetMapping(value = {"/developers/{id}/updateLastUpdateTime"})
    public String updateLastUpdateTime(Model model, @PathVariable long id) {
        try {
            developerService.updateLastUpdateTime(id);
            return "redirect:/developers/" + id + "/edit";
        } catch (Exception ex) {
            String errorMessage = ex.getMessage();
            model.addAttribute("errorMessage", errorMessage);
            model.addAttribute("add", false);
            return "developer-edit";
        }
    }

    @GetMapping(value = "/developers")
    public String developers(@NotNull Model model, @RequestParam(value = "page", defaultValue = "1") int pageNumber) {
        List<Developer> developers = developerService.findAll(pageNumber, ROW_PAGE_SIZE);
        long count = developerService.count();
        boolean hasPrev = pageNumber > 1;
        boolean hasNext = ((long) pageNumber * ROW_PAGE_SIZE) < count;
        model.addAttribute("developers", developers);
        model.addAttribute("hasPrev", hasPrev);
        model.addAttribute("prev", pageNumber - 1);
        model.addAttribute("hasNext", hasNext);
        model.addAttribute("next", pageNumber + 1);

        return "developer-list";
    }

    @GetMapping(value = "/developers/{id}")
    public String getById(Model model, @PathVariable long id) {
        Developer developer = null;
        try {
            developer = developerService.findById(id);
            model.addAttribute("allowDelete", false);
        } catch (Exception ex) {
            model.addAttribute("errorMessage", ex.getMessage());
        }
        model.addAttribute("developer", developer);
        return "developer";
    }

    @GetMapping(value = {"/developers/{id}/delete"})
    public String showDeleteById(Model model, @PathVariable long id) {
        Developer developer = null;
        try {
            developer = developerService.findById(id);
        } catch (Exception ex) {
            model.addAttribute("errorMessage", ex.getMessage());
        }
        model.addAttribute("allowDelete", true);
        model.addAttribute("developer", developer);
        return "developer";
    }

    @PostMapping(value = {"/developers/{id}/delete"})
    public String deleteById(Model model, @PathVariable long id) {
        try {
            developerService.deleteById(id);
            return "redirect:/developers";
        } catch (Exception ex) {
            String errorMessage = ex.getMessage();
            log.error(errorMessage, ex);
            model.addAttribute("errorMessage", errorMessage);
            return "developer";
        }
    }


}
