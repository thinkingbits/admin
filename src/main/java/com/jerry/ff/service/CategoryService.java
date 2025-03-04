package com.jerry.ff.service;

import com.jerry.ff.model.dto.CategoryDTO;
import com.jerry.ff.model.vo.CategoryVO;

import java.util.List;

public interface CategoryService {
    
    List<CategoryVO> getAllCategories();
    
    CategoryVO getCategory(Long id);
    
    List<CategoryVO> getParentCategories();
    
    List<CategoryVO> getChildCategories(Long parentId);
    
    CategoryVO createCategory(CategoryDTO categoryDTO);
    
    CategoryVO updateCategory(Long id, CategoryDTO categoryDTO);
    
    void deleteCategory(Long id);
} 