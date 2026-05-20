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

    private final DemoDataStore demoDataStore;

    public BedController(DemoDataStore demoDataStore) {
        this.demoDataStore = demoDataStore;
    }

    @GetMapping("/overview")
    public ApiResponse<Map<String, Object>> overview() {
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
                                                .orElse("暂无入住")
                                ))
                                .toList()
                ))
                .toList();
        return ApiResponse.ok(roomData);
    }

    @PostMapping("/transfer")
    public ApiResponse<Customer> transfer(@RequestBody BedTransferRequest request) {
        return ApiResponse.ok("调床成功", demoDataStore.transferBed(request.customerId(), request.newBedId()));
    }
}
