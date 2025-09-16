package com.example.Menu_Voting_System.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.Menu_Voting_System.model.MenuItem;
import com.example.Menu_Voting_System.repository.MenuItemRepository;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/menu")
@CrossOrigin(origins = "*")
public class MenuItemController {

    private final MenuItemRepository repository;

    public MenuItemController(MenuItemRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public List<MenuItem> getAllItems() {
        return repository.findAll();
    }

    @GetMapping("/category/{category}")
    public List<MenuItem> getItemsByCategory(@PathVariable MenuItem.Category category) {
        return repository.findByCategory(category);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MenuItem> getItemById(@PathVariable Long id) {
        Optional<MenuItem> item = repository.findById(id);
        return item.map(ResponseEntity::ok)
                  .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public MenuItem addItem(@RequestBody MenuItem item) {
        return repository.save(item);
    }

    @PutMapping("/{id}")
    public ResponseEntity<MenuItem> updateItem(@PathVariable Long id, @RequestBody MenuItem updatedItem) {
        if (!repository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        updatedItem.setId(id);
        return ResponseEntity.ok(repository.save(updatedItem));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteItem(@PathVariable Long id) {
        if (!repository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        repository.deleteById(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{id}/vote")
    public ResponseEntity<MenuItem> voteItem(@PathVariable Long id) {
        Optional<MenuItem> itemOpt = repository.findById(id);
        if (itemOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        MenuItem item = itemOpt.get();
        item.setVotes(item.getVotes() + 1);
        return ResponseEntity.ok(repository.save(item));
    }

    @PostMapping("/{id}/unvote")
    public ResponseEntity<MenuItem> unvoteItem(@PathVariable Long id) {
        Optional<MenuItem> itemOpt = repository.findById(id);
        if (itemOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        MenuItem item = itemOpt.get();
        if (item.getVotes() > 0) {
            item.setVotes(item.getVotes() - 1);
        }
        return ResponseEntity.ok(repository.save(item));
    }
}
