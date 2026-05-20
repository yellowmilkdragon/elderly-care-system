package com.neusoft.elderlycare.controller;

import com.neusoft.elderlycare.common.ApiResponse;
import com.neusoft.elderlycare.common.DemoDataStore;
import com.neusoft.elderlycare.entity.NursingItem;
import com.neusoft.elderlycare.entity.NursingLevel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/nursing")
public class NursingController {

    private final DemoDataStore demoDataStore;

    public NursingController(DemoDataStore demoDataStore) {
        this.demoDataStore = demoDataStore;
    }

    @GetMapping("/items")
    public ApiResponse<List<Object>> items() {
        return ApiResponse.ok(List.copyOf(demoDataStore.getNursingItems()));
    }

    @GetMapping("/levels")
    public ApiResponse<List<Object>> levels() {
        return ApiResponse.ok(List.copyOf(demoDataStore.getNursingLevels()));
    }

    @PostMapping("/items")
    public ApiResponse<NursingItem> createItem(@RequestBody NursingItem item) {
        return ApiResponse.ok("创建成功", demoDataStore.saveNursingItem(item));
    }

    @PutMapping("/items/{id}")
    public ApiResponse<NursingItem> updateItem(@PathVariable Long id, @RequestBody NursingItem item) {
        item.setId(id);
        return ApiResponse.ok("更新成功", demoDataStore.saveNursingItem(item));
    }

    @PostMapping("/levels")
    public ApiResponse<NursingLevel> createLevel(@RequestBody NursingLevel level) {
        return ApiResponse.ok("创建成功", demoDataStore.saveNursingLevel(level));
    }

    @PutMapping("/levels/{id}")
    public ApiResponse<NursingLevel> updateLevel(@PathVariable Long id, @RequestBody NursingLevel level) {
        level.setId(id);
        return ApiResponse.ok("更新成功", demoDataStore.saveNursingLevel(level));
    }
}
