package com.example.Menu_Voting_System.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.Menu_Voting_System.model.MenuItem;
import java.util.List;

@Repository
public interface MenuItemRepository extends JpaRepository<MenuItem, Long> {
    List<MenuItem> findByCategory(MenuItem.Category category);
    List<MenuItem> findByOrderByVotesDesc();
    List<MenuItem> findByCategoryOrderByVotesDesc(MenuItem.Category category);
}
