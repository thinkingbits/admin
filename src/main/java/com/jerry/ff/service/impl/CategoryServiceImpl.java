package com.jerry.ff.service.impl;

import com.jerry.ff.exception.BusinessException;
import com.jerry.ff.exception.ResourceNotFoundException;
import com.jerry.ff.model.dto.CategoryDTO;
import com.jerry.ff.model.entity.Category;
import com.jerry.ff.model.vo.CategoryVO;
import com.jerry.ff.repository.CategoryRepository;
import com.jerry.ff.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    @Override
    @Cacheable(value = "categories")
    public List<CategoryVO> getAllCategories() {
        List<Category> categories = categoryRepository.findAll();
        return categories.stream()
                .map(this::convertToCategoryVO)
                .collect(Collectors.toList());
    }

    @Override
    @Cacheable(value = "categories", key = "#id")
    public CategoryVO getCategory(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("分类不存在，ID: " + id));
        return convertToCategoryVO(category);
    }

    @Override
    @Cacheable(value = "parentCategories")
    public List<CategoryVO> getParentCategories() {
        List<Category> parentCategories = categoryRepository.findAllParentCategories();
        return parentCategories.stream()
                .map(this::convertToCategoryVO)
                .collect(Collectors.toList());
    }

    @Override
    @Cacheable(value = "childCategories", key = "#parentId")
    public List<CategoryVO> getChildCategories(Long parentId) {
        List<Category> childCategories = categoryRepository.findByParentId(parentId);
        return childCategories.stream()
                .map(this::convertToCategoryVO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    @CacheEvict(value = {"categories", "parentCategories", "childCategories"}, allEntries = true)
    public CategoryVO createCategory(CategoryDTO categoryDTO) {
        if (categoryRepository.existsByName(categoryDTO.getName())) {
            throw new BusinessException(400, "分类名称已存在");
        }

        Category parent = null;
        if (categoryDTO.getParentId() != null) {
            parent = categoryRepository.findById(categoryDTO.getParentId())
                    .orElseThrow(() -> new ResourceNotFoundException("父分类不存在，ID: " + categoryDTO.getParentId()));
        }

        Category category = Category.builder()
                .name(categoryDTO.getName())
                .description(categoryDTO.getDescription())
                .parent(parent)
                .build();

        Category savedCategory = categoryRepository.save(category);
        return convertToCategoryVO(savedCategory);
    }

    @Override
    @Transactional
    @CacheEvict(value = {"categories", "parentCategories", "childCategories"}, allEntries = true)
    public CategoryVO updateCategory(Long id, CategoryDTO categoryDTO) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("分类不存在，ID: " + id));

        if (!category.getName().equals(categoryDTO.getName()) && 
                categoryRepository.existsByName(categoryDTO.getName())) {
            throw new BusinessException(400, "分类名称已存在");
        }

        Category parent = null;
        if (categoryDTO.getParentId() != null) {
            if (categoryDTO.getParentId().equals(id)) {
                throw new BusinessException(400, "分类不能作为自己的父分类");
            }
            parent = categoryRepository.findById(categoryDTO.getParentId())
                    .orElseThrow(() -> new ResourceNotFoundException("父分类不存在，ID: " + categoryDTO.getParentId()));
        }

        category.setName(categoryDTO.getName());
        category.setDescription(categoryDTO.getDescription());
        category.setParent(parent);

        Category updatedCategory = categoryRepository.save(category);
        return convertToCategoryVO(updatedCategory);
    }

    @Override
    @Transactional
    @CacheEvict(value = {"categories", "parentCategories", "childCategories"}, allEntries = true)
    public void deleteCategory(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("分类不存在，ID: " + id));
        
        if (!category.getChildren().isEmpty()) {
            throw new BusinessException(400, "该分类下有子分类，无法删除");
        }
        
        categoryRepository.delete(category);
    }

    private CategoryVO convertToCategoryVO(Category category) {
        CategoryVO.CategoryVOBuilder builder = CategoryVO.builder()
                .id(category.getId())
                .name(category.getName())
                .description(category.getDescription())
                .createTime(category.getCreateAt())
                .updateTime(category.getUpdateAt());

        if (category.getParent() != null) {
            builder.parentId(category.getParent().getId());
            builder.parentName(category.getParent().getName());
        }

        if (!category.getChildren().isEmpty()) {
            List<CategoryVO> childrenVO = category.getChildren().stream()
                    .map(child -> CategoryVO.builder()
                            .id(child.getId())
                            .name(child.getName())
                            .description(child.getDescription())
                            .parentId(category.getId())
                            .parentName(category.getName())
                            .createTime(child.getCreateAt())
                            .updateTime(child.getUpdateAt())
                            .build())
                    .collect(Collectors.toList());
            builder.children(childrenVO);
        }

        return builder.build();
    }
} 