package com.neusoft.elderlycare.controller;

import com.neusoft.elderlycare.common.ApiResponse;
import com.neusoft.elderlycare.common.DemoDataStore;
import com.neusoft.elderlycare.dto.BedTransferRequest;
import com.neusoft.elderlycare.entity.Customer;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/beds")
public class BedController {

    // 鲍建华：床位管理模块接口入口，负责床位示意图、空闲床位查询和调床操作。
    private final DemoDataStore demoDataStore;

    public BedController(DemoDataStore demoDataStore) {
        this.demoDataStore = demoDataStore;
    }

    @GetMapping("/overview")
    public ApiResponse<Map<String, Object>> overview() {
        // 鲍建华：统计床位总数、空闲数、入住数和外出数，用于床位总览卡片。
        long freeBeds = demoDataStore.getBeds().stream().filter(bed -> bed.getBedStatus() == 1).count();
        long occupiedBeds = demoDataStore.getBeds().stream().filter(bed -> bed.getBedStatus() == 2).count();
        long outwardBeds = demoDataStore.getBeds().stream().filter(bed -> bed.getBedStatus() == 3).count();
        return ApiResponse.ok(Map.of(
                "totalBeds", demoDataStore.getBeds().size(),
                "freeBeds", freeBeds,
                "occupiedBeds", occupiedBeds,
                "outwardBeds", outwardBeds
        ));
    }

    @GetMapping("/available")
    public ApiResponse<List<Map<String, Object>>> available() {
        // 鲍建华：仅返回当前空闲床位，给调床弹窗提供候选床位。
        List<Map<String, Object>> availableBeds = demoDataStore.getBeds().stream()
                .filter(bed -> bed.getBedStatus() == 1)
                .map(bed -> Map.<String, Object>of(
                        "id", bed.getId(),
                        "roomNo", bed.getRoomNo(),
                        "bedNo", bed.getBedNo(),
                        "status", bed.getBedStatus()
                ))
                .toList();
        return ApiResponse.ok(availableBeds);
    }

    @GetMapping("/rooms")
    public ApiResponse<List<Map<String, Object>>> rooms() {
        // 鲍建华：按房间组织床位结构，并补充床位主人的姓名，便于绘制床位示意图。
        List<Map<String, Object>> roomData = demoDataStore.getRooms().stream()
                .map(room -> Map.<String, Object>of(
                        "id", room.getId(),
                        "roomFloor", room.getRoomFloor(),
                        "roomNo", room.getRoomNo(),
                        "beds", demoDataStore.getBeds().stream()
                                .filter(bed -> room.getRoomNo().equals(bed.getRoomNo()))
                                .map(bed -> Map.<String, Object>of(
                                        "id", bed.getId(),
                                        "bedNo", bed.getBedNo(),
                                        "bedStatus", bed.getBedStatus(),
                                        "ownerName", demoDataStore.getCustomers().stream()
                                                .filter(customer -> bed.getId().equals(customer.getBedId()))
                                                .map(Customer::getCustomerName)
                                                .findFirst()
                                                .orElse("鏆傛棤鍏ヤ綇")
                                ))
                                .toList()
                ))
                .toList();
        return ApiResponse.ok(roomData);
    }

    @PostMapping("/transfer")
    public ApiResponse<Customer> transfer(@RequestBody BedTransferRequest request) {
        // 鲍建华：执行调床，联动更新客户绑定床位和床位状态。
        return ApiResponse.ok("璋冨簥鎴愬姛", demoDataStore.transferBed(request.customerId(), request.newBedId()));
    }
}
