package com.jerry.ff.controller;

import com.jerry.ff.model.vo.AgeVO;
import com.jerry.ff.service.AgeService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ages")
@RequiredArgsConstructor
public class AgeController {

    private final AgeService ageService;

    @PostMapping
    public ResponseEntity<AgeVO> create(@RequestBody AgeVO ageVO) {
        return ResponseEntity.ok(ageService.create(ageVO));
    }

    @PutMapping("/{id}")
    public ResponseEntity<AgeVO> update(@PathVariable Long id, @RequestBody AgeVO ageVO) {
        return ResponseEntity.ok(ageService.update(id, ageVO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        ageService.delete(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<AgeVO> findById(@PathVariable Long id) {
        return ResponseEntity.ok(ageService.findById(id));
    }

    @GetMapping
    public ResponseEntity<Page<AgeVO>> findAll(Pageable pageable) {
        return ResponseEntity.ok(ageService.findAll(pageable));
    }

    @GetMapping("/all")
    public ResponseEntity<List<AgeVO>> findAll() {
        return ResponseEntity.ok(ageService.findAll());
    }
} 