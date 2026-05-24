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

    // 刘文康：护理模块接口入口，负责护理项目和护理级别维护。
    private final DemoDataStore demoDataStore;

    public NursingController(DemoDataStore demoDataStore) {
        this.demoDataStore = demoDataStore;
    }

    @GetMapping("/items")
    public ApiResponse<List<Object>> items() {
        // 刘文康：查询护理项目列表。
        return ApiResponse.ok(List.copyOf(demoDataStore.getNursingItems()));
    }

    @GetMapping("/levels")
    public ApiResponse<List<Object>> levels() {
        // 刘文康：查询护理级别列表。
        return ApiResponse.ok(List.copyOf(demoDataStore.getNursingLevels()));
    }

    @PostMapping("/items")
    public ApiResponse<NursingItem> createItem(@RequestBody NursingItem item) {
        // 刘文康：新增护理项目。
        return ApiResponse.ok("鍒涘缓鎴愬姛", demoDataStore.saveNursingItem(item));
    }

    @PutMapping("/items/{id}")
    public ApiResponse<NursingItem> updateItem(@PathVariable Long id, @RequestBody NursingItem item) {
        // 刘文康：更新护理项目配置。
        item.setId(id);
        return ApiResponse.ok("鏇存柊鎴愬姛", demoDataStore.saveNursingItem(item));
    }

    @PostMapping("/levels")
    public ApiResponse<NursingLevel> createLevel(@RequestBody NursingLevel level) {
        // 刘文康：新增护理级别。
        return ApiResponse.ok("鍒涘缓鎴愬姛", demoDataStore.saveNursingLevel(level));
    }

    @PutMapping("/levels/{id}")
    public ApiResponse<NursingLevel> updateLevel(@PathVariable Long id, @RequestBody NursingLevel level) {
        // 刘文康：更新护理级别配置。
        level.setId(id);
        return ApiResponse.ok("鏇存柊鎴愬姛", demoDataStore.saveNursingLevel(level));
    }
}
