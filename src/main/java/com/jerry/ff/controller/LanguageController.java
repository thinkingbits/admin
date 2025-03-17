package com.jerry.ff.controller;

import com.jerry.ff.model.vo.LanguageVO;
import com.jerry.ff.service.LanguageService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/languages")
@RequiredArgsConstructor
public class LanguageController {

    private final LanguageService languageService;

    @PostMapping
    public ResponseEntity<LanguageVO> create(@RequestBody LanguageVO languageVO) {
        return ResponseEntity.ok(languageService.create(languageVO));
    }

    @PutMapping("/{id}")
    public ResponseEntity<LanguageVO> update(@PathVariable Long id, @RequestBody LanguageVO languageVO) {
        return ResponseEntity.ok(languageService.update(id, languageVO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        languageService.delete(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<LanguageVO> findById(@PathVariable Long id) {
        return ResponseEntity.ok(languageService.findById(id));
    }

    @GetMapping
    public ResponseEntity<Page<LanguageVO>> findAll(Pageable pageable) {
        return ResponseEntity.ok(languageService.findAll(pageable));
    }

    @GetMapping("/all")
    public ResponseEntity<List<LanguageVO>> findAll() {
        return ResponseEntity.ok(languageService.findAll());
    }
} 