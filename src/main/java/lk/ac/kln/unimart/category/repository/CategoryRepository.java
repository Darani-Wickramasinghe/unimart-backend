package lk.ac.kln.unimart.category.repository;

import lk.ac.kln.unimart.category.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    Optional<Category> findByIdAndActiveTrue(Long id);
}