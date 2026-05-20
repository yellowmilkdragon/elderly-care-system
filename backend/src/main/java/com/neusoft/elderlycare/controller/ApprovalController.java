package com.neusoft.elderlycare.controller;

import com.neusoft.elderlycare.common.ApiResponse;
import com.neusoft.elderlycare.common.DemoDataStore;
import com.neusoft.elderlycare.dto.ApprovalAuditRequest;
import com.neusoft.elderlycare.entity.Backdown;
import com.neusoft.elderlycare.entity.Outward;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/approvals")
public class ApprovalController {

    private final DemoDataStore demoDataStore;

    public ApprovalController(DemoDataStore demoDataStore) {
        this.demoDataStore = demoDataStore;
    }

    @GetMapping("/outward")
    public ApiResponse<List<Object>> outward() {
        return ApiResponse.ok(List.copyOf(demoDataStore.getOutwards()));
    }

    @GetMapping("/backdown")
    public ApiResponse<List<Object>> backdown() {
        return ApiResponse.ok(List.copyOf(demoDataStore.getBackdowns()));
    }

    @PutMapping("/outward/{id}/audit")
    public ApiResponse<Outward> auditOutward(@PathVariable Long id, @RequestBody ApprovalAuditRequest request) {
        Outward outward = demoDataStore.auditOutward(id, request.auditStatus());
        if (outward == null) {
            return ApiResponse.fail("该外出申请已处理，不能重复审批");
        }
        return ApiResponse.ok("审批成功", outward);
    }

    @PutMapping("/backdown/{id}/audit")
    public ApiResponse<Backdown> auditBackdown(@PathVariable Long id, @RequestBody ApprovalAuditRequest request) {
        Backdown backdown = demoDataStore.auditBackdown(id, request.auditStatus());
        if (backdown == null) {
            return ApiResponse.fail("该退住申请已处理，不能重复审批");
        }
        return ApiResponse.ok("审批成功", backdown);
    }

    @PostMapping("/outward/{id}/return")
    public ApiResponse<Outward> returnFromOutward(@PathVariable Long id) {
        return ApiResponse.ok("返院登记成功", demoDataStore.markReturned(id));
    }
}
