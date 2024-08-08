package com.ai.aichatbackend.controller;

import com.ai.aichatbackend.common.Constants.R;
import com.ai.aichatbackend.domain.Notebook;
import com.ai.aichatbackend.service.NotebookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author mac
 */
@RestController
@RequestMapping("/api/note")
public class NotebookController {

    private final NotebookService notebookService;

    @Autowired
    public NotebookController(NotebookService notebookService) {
        this.notebookService = notebookService;
    }

    @PostMapping("/getNotebook")
    public R getNotebook() {
        return R.ok(notebookService.getNotebook());
    }

    @PostMapping("/saveNotebook")
    public R saveNotebook(@RequestBody List<Notebook> notebooks) {
        return R.ok(notebookService.saveNotebook(notebooks));
    }

    @PostMapping("/cleanNotebook")
    public R cleanNotebook(@RequestBody Notebook notebook) {
        return R.ok(notebookService.cleanNotebook(notebook));
    }
}
