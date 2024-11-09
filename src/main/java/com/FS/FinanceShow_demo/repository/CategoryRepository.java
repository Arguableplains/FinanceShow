package com.FS.FinanceShow_demo.repository;

import com.FS.FinanceShow_demo.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    public Optional<Category> findByName(String name);
    public List<Category> findByUser_Id(Long userId);
}
