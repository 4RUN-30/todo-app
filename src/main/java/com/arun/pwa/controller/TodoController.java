package com.arun.pwa.controller;

import com.arun.pwa.model.Todo;
import com.arun.pwa.repository.TodoRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/todo")
public class TodoController {

    private final TodoRepository repo;

    public TodoController(TodoRepository repo) {
        this.repo = repo;
    }

    @PostMapping
    public Todo create(@RequestBody Todo todo) {
        System.out.println(todo.getTitle());
        return repo.save(todo);
    }

    @GetMapping
    public List<Todo> getAll() {
        return repo.findAll();
    }

    @GetMapping("/{id}")
    public Todo get(@PathVariable Long id) {
        return repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Todo not found"));
    }

    @PutMapping("/{id}")
    public Todo update(@PathVariable Long id, @RequestBody Todo updated) {
        return repo.findById(id).map(todo -> {
            todo.setTitle(updated.getTitle());
            todo.setPriority(updated.getPriority());
            todo.setCompleted(updated.getCompleted());
            todo.setCategory(updated.getCategory());
            todo.setCreatedAt(updated.getCreatedAt());
            todo.setCompletedAt(updated.getCompletedAt());
            return repo.save(todo);
        }).orElseThrow(() -> new RuntimeException("Todo not found"));
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        repo.deleteById(id);
    }
}
