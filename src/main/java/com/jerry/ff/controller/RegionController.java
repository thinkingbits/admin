package com.jerry.ff.controller;

import com.jerry.ff.model.vo.RegionVO;
import com.jerry.ff.service.RegionService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/regions")
@RequiredArgsConstructor
public class RegionController {

    private final RegionService regionService;

    @PostMapping
    public ResponseEntity<RegionVO> create(@RequestBody RegionVO regionVO) {
        return ResponseEntity.ok(regionService.create(regionVO));
    }

    @PutMapping("/{id}")
    public ResponseEntity<RegionVO> update(@PathVariable Long id, @RequestBody RegionVO regionVO) {
        return ResponseEntity.ok(regionService.update(id, regionVO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        regionService.delete(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<RegionVO> findById(@PathVariable Long id) {
        return ResponseEntity.ok(regionService.findById(id));
    }

    @GetMapping
    public ResponseEntity<Page<RegionVO>> findAll(Pageable pageable) {
        return ResponseEntity.ok(regionService.findAll(pageable));
    }

    @GetMapping("/all")
    public ResponseEntity<List<RegionVO>> findAll() {
        return ResponseEntity.ok(regionService.findAll());
    }
} 