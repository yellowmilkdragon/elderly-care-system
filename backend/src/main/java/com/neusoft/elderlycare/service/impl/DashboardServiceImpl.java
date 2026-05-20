package com.neusoft.elderlycare.service.impl;

import com.neusoft.elderlycare.common.DemoDataStore;
import com.neusoft.elderlycare.service.DashboardService;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.Map;

@Service
public class DashboardServiceImpl implements DashboardService {

    private final DemoDataStore demoDataStore;

    public DashboardServiceImpl(DemoDataStore demoDataStore) {
        this.demoDataStore = demoDataStore;
    }

    @Override
    public Map<String, Object> summary() {
        Map<String, Object> summary = new LinkedHashMap<>();
        long occupiedBeds = demoDataStore.getBeds().stream().filter(bed -> bed.getBedStatus() == 2).count();
        long freeBeds = demoDataStore.getBeds().stream().filter(bed -> bed.getBedStatus() == 1).count();
        long outwardBeds = demoDataStore.getBeds().stream().filter(bed -> bed.getBedStatus() == 3).count();
        long caregiverCount = demoDataStore.getUsers().stream().filter(user -> Long.valueOf(2).equals(user.getRoleId())).count();
        long outwardPending = demoDataStore.getOutwards().stream().filter(outward -> outward.getAuditStatus() == 0).count();
        long backdownPending = demoDataStore.getBackdowns().stream().filter(backdown -> backdown.getAuditStatus() == 0).count();

        summary.put("customerCount", demoDataStore.getCustomers().size());
        summary.put("occupiedBeds", occupiedBeds);
        summary.put("freeBeds", freeBeds);
        summary.put("outwardBeds", outwardBeds);
        summary.put("caregiverCount", caregiverCount);
        summary.put("outwardPending", outwardPending);
        summary.put("backdownPending", backdownPending);
        return summary;
    }
}
