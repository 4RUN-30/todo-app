package com.arun.pwa.controller;

import com.arun.pwa.model.TodoDto;
import com.arun.pwa.service.AirtableService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/todos")
public class TodoController {

    private static final Logger logger = LoggerFactory.getLogger(TodoController.class);

    private final AirtableService airtableService;

    public TodoController(AirtableService airtableService) {
        this.airtableService = airtableService;
    }

    @GetMapping("/health")
    public String health() {
        return "OK";
    }

    @GetMapping
    public List<TodoDto> getTodos() {
        logger.info("GET /todos - fetching all todos");
        List<TodoDto> todos = airtableService.getAllTodos();
        logger.debug("Fetched {} todos", todos.size());
        return todos;
    }

    @PostMapping
    public void addTodo(@RequestBody String title) {
        logger.info("POST /todos - creating todo");
        logger.debug("Creating todo with title: {}", title);
        airtableService.createTodo(title);
    }

    @PutMapping("/{id}/complete")
    public void completeTodo(@PathVariable String id) {
        logger.info("PUT /todos/{}/complete - marking complete", id);
        airtableService.markCompleted(id);
    }

    @DeleteMapping("/{id}")
    public void deleteTodo(@PathVariable String id) {
        logger.info("DELETE /todos/{} - deleting", id);
        airtableService.deleteTodo(id);
    }
}
