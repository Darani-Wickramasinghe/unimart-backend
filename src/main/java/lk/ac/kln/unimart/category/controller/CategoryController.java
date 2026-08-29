package lk.ac.kln.unimart.category.controller;

import lk.ac.kln.unimart.category.dto.CategoryResponse;
import lk.ac.kln.unimart.category.repository.CategoryRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/categories")
public class CategoryController {

    private final CategoryRepository categories;

    public CategoryController(CategoryRepository categories) {
        this.categories = categories;
    }

    @GetMapping
    public List<CategoryResponse> list() {
        return categories.findAll().stream()
                .filter(c -> c.isActive())
                .map(c -> new CategoryResponse(c.getId(), c.getName()))
                .toList();
    }
}