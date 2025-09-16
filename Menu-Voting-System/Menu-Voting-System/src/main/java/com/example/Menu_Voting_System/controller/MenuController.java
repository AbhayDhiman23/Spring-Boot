package com.example.Menu_Voting_System.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.Menu_Voting_System.model.MenuItem;

import com.example.Menu_Voting_System.repository.MenuItemRepository;


import java.util.List;

@Controller
public class MenuController {

    private final MenuItemRepository repository;

    public MenuController(MenuItemRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/")
    public String index(@RequestParam(required = false) MenuItem.Category category, Model model) {
        model.addAttribute("categories", MenuItem.Category.values());
        model.addAttribute("selectedCategory", category);
        
        List<MenuItem> menuItems = (category != null) ? 
            repository.findByCategory(category) :
            repository.findAll();
        model.addAttribute("menuItems", menuItems);
        
        return "index";
    }

    @PostMapping("/add-item")
    public String addItem(MenuItem item, RedirectAttributes redirectAttributes) {
        repository.save(item);
        redirectAttributes.addFlashAttribute("message", "Item added successfully!");
        return "redirect:/";
    }

    @PostMapping("/edit-item/{id}")
    public String editItem(@PathVariable Long id, MenuItem updatedItem, RedirectAttributes redirectAttributes) {
        MenuItem item = repository.findById(id).orElseThrow();
        
        item.setName(updatedItem.getName());
        item.setDescription(updatedItem.getDescription());
        item.setPrice(updatedItem.getPrice());
        item.setCategory(updatedItem.getCategory());
        item.setImageUrl(updatedItem.getImageUrl());
        
        repository.save(item);
        redirectAttributes.addFlashAttribute("message", "Item updated successfully!");
        return "redirect:/";
    }

    @PostMapping("/delete-item/{id}")
    public String deleteItem(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        repository.deleteById(id);
        redirectAttributes.addFlashAttribute("message", "Item deleted successfully!");
        return "redirect:/";
    }

    @PostMapping("/vote/{id}")
    public String voteItem(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        MenuItem item = repository.findById(id).orElseThrow();
        item.setVotes(item.getVotes() + 1);
        repository.save(item);
        redirectAttributes.addFlashAttribute("message", "Vote recorded!");
        return "redirect:/";
    }

    @PostMapping("/unvote/{id}")
    public String unvoteItem(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        MenuItem item = repository.findById(id).orElseThrow();
        if (item.getVotes() > 0) {
            item.setVotes(item.getVotes() - 1);
            repository.save(item);
            redirectAttributes.addFlashAttribute("message", "Vote removed!");
        }
        return "redirect:/";
    }
}