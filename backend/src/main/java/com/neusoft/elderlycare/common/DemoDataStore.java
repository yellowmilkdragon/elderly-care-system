package com.neusoft.elderlycare.common;

import com.neusoft.elderlycare.entity.Backdown;
import com.neusoft.elderlycare.entity.Bed;
import com.neusoft.elderlycare.entity.Customer;
import com.neusoft.elderlycare.entity.NursingItem;
import com.neusoft.elderlycare.entity.NursingLevel;
import com.neusoft.elderlycare.entity.Outward;
import com.neusoft.elderlycare.entity.Room;
import com.neusoft.elderlycare.entity.User;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Component
public class DemoDataStore {

    private final List<User> users = new ArrayList<>();
    private final List<Customer> customers = new ArrayList<>();
    private final List<Room> rooms = new ArrayList<>();
    private final List<Bed> beds = new ArrayList<>();
    private final List<NursingItem> nursingItems = new ArrayList<>();
    private final List<NursingLevel> nursingLevels = new ArrayList<>();
    private final List<Outward> outwards = new ArrayList<>();
    private final List<Backdown> backdowns = new ArrayList<>();

    @PostConstruct
    void init() {
        seedUsers();
        seedRoomsAndBeds();
        seedCustomers();
        seedNursing();
        seedApprovals();
    }

    public List<User> getUsers() {
        return users;
    }

    public List<Customer> getCustomers() {
        return customers;
    }

    public List<Room> getRooms() {
        return rooms;
    }

    public List<Bed> getBeds() {
        return beds;
    }

    public List<NursingItem> getNursingItems() {
        return nursingItems;
    }

    public List<NursingLevel> getNursingLevels() {
        return nursingLevels;
    }

    public List<Outward> getOutwards() {
        return outwards;
    }

    public List<Backdown> getBackdowns() {
        return backdowns;
    }

    public synchronized Customer saveCustomer(Customer payload) {
        if (payload.getId() == null) {
            payload.setId(nextId(customers.stream().map(Customer::getId).toList()));
            payload.setCreateTime(LocalDateTime.now());
            payload.setIsDeleted(0);
            customers.add(payload);
            return payload;
        }

        Customer existing = customers.stream()
                .filter(item -> item.getId().equals(payload.getId()))
                .findFirst()
                .orElse(null);
        if (existing == null) {
            customers.add(payload);
            return payload;
        }

        existing.setCustomerName(payload.getCustomerName());
        existing.setCustomerAge(payload.getCustomerAge());
        existing.setCustomerSex(payload.getCustomerSex());
        existing.setRoomNo(payload.getRoomNo());
        existing.setBuildingNo(payload.getBuildingNo());
        existing.setBedId(payload.getBedId());
        existing.setUserId(payload.getUserId());
        existing.setLevelId(payload.getLevelId());
        existing.setBloodType(payload.getBloodType());
        existing.setContactTel(payload.getContactTel());
        existing.setFamilyMember(payload.getFamilyMember());
        existing.setExpirationDate(payload.getExpirationDate());
        existing.setCheckinDate(payload.getCheckinDate());
        return existing;
    }

    public synchronized NursingItem saveNursingItem(NursingItem payload) {
        if (payload.getId() == null) {
            payload.setId(nextId(nursingItems.stream().map(NursingItem::getId).toList()));
            payload.setCreateTime(LocalDateTime.now());
            payload.setIsDeleted(0);
            nursingItems.add(payload);
            return payload;
        }

        NursingItem existing = nursingItems.stream()
                .filter(item -> item.getId().equals(payload.getId()))
                .findFirst()
                .orElse(null);
        if (existing == null) {
            nursingItems.add(payload);
            return payload;
        }

        existing.setSerialNumber(payload.getSerialNumber());
        existing.setNursingName(payload.getNursingName());
        existing.setServicePrice(payload.getServicePrice());
        existing.setStatus(payload.getStatus());
        existing.setExecutionCycle(payload.getExecutionCycle());
        existing.setExecutionTimes(payload.getExecutionTimes());
        existing.setMessage(payload.getMessage());
        return existing;
    }

    public synchronized NursingLevel saveNursingLevel(NursingLevel payload) {
        if (payload.getId() == null) {
            payload.setId(nextId(nursingLevels.stream().map(NursingLevel::getId).toList()));
            payload.setCreateTime(LocalDateTime.now());
            payload.setIsDeleted(0);
            nursingLevels.add(payload);
            return payload;
        }

        NursingLevel existing = nursingLevels.stream()
                .filter(item -> item.getId().equals(payload.getId()))
                .findFirst()
                .orElse(null);
        if (existing == null) {
            nursingLevels.add(payload);
            return payload;
        }

        existing.setLevelName(payload.getLevelName());
        existing.setLevelStatus(payload.getLevelStatus());
        return existing;
    }

    public synchronized Outward auditOutward(Long id, Integer auditStatus) {
        Outward outward = outwards.stream().filter(item -> item.getId().equals(id)).findFirst().orElse(null);
        if (outward == null) {
            return null;
        }
        if (outward.getAuditStatus() != null && outward.getAuditStatus() != 0) {
            return null;
        }
        outward.setAuditStatus(auditStatus);
        outward.setAuditPerson("系统管理员");
        outward.setAuditTime(LocalDate.now());
        return outward;
    }

    public synchronized Backdown auditBackdown(Long id, Integer auditStatus) {
        Backdown backdown = backdowns.stream().filter(item -> item.getId().equals(id)).findFirst().orElse(null);
        if (backdown == null) {
            return null;
        }
        if (backdown.getAuditStatus() != null && backdown.getAuditStatus() != 0) {
            return null;
        }
        backdown.setAuditStatus(auditStatus);
        backdown.setAuditPerson("系统管理员");
        backdown.setAuditTime(LocalDate.now());
        return backdown;
    }

    public synchronized Outward markReturned(Long id) {
        Outward outward = outwards.stream().filter(item -> item.getId().equals(id)).findFirst().orElse(null);
        if (outward == null) {
            return null;
        }
        outward.setActualReturnTime(LocalDate.now());
        Customer customer = customers.stream()
                .filter(item -> item.getId().equals(outward.getCustomerId()))
                .findFirst()
                .orElse(null);
        if (customer != null) {
            Bed bed = beds.stream().filter(item -> item.getId().equals(customer.getBedId())).findFirst().orElse(null);
            if (bed != null) {
                bed.setBedStatus(2);
            }
        }
        return outward;
    }

    public synchronized Customer assignCaregiver(Long customerId, Long caregiverId) {
        Customer customer = customers.stream()
                .filter(item -> item.getId().equals(customerId))
                .findFirst()
                .orElse(null);
        if (customer == null) {
            return null;
        }
        customer.setUserId(caregiverId);
        return customer;
    }

    public synchronized Customer transferBed(Long customerId, Long newBedId) {
        Customer customer = customers.stream()
                .filter(item -> item.getId().equals(customerId))
                .findFirst()
                .orElse(null);
        if (customer == null) {
            return null;
        }

        Bed oldBed = beds.stream().filter(item -> item.getId().equals(customer.getBedId())).findFirst().orElse(null);
        Bed newBed = beds.stream().filter(item -> item.getId().equals(newBedId)).findFirst().orElse(null);
        if (newBed == null || newBed.getBedStatus() != 1) {
            return null;
        }

        if (oldBed != null) {
            oldBed.setBedStatus(1);
        }
        newBed.setBedStatus(2);
        customer.setBedId(newBedId);
        customer.setRoomNo(String.valueOf(newBed.getRoomNo()));
        return customer;
    }

    private Long nextId(List<Long> ids) {
        return ids.stream().filter(id -> id != null).max(Comparator.naturalOrder()).orElse(0L) + 1;
    }

    private void seedUsers() {
        users.add(buildUser(1L, "admin", "系统管理员", "13800000001", 1L));
        users.add(buildUser(2L, "admin1", "运营管理员", "13800000002", 1L));
        users.add(buildUser(3L, "care01", "张护工", "13800000003", 2L));
        users.add(buildUser(4L, "care02", "李护工", "13800000004", 2L));
    }

    private void seedRoomsAndBeds() {
        rooms.add(buildRoom(1L, "1F", 101));
        rooms.add(buildRoom(2L, "1F", 102));
        rooms.add(buildRoom(3L, "2F", 201));

        beds.add(buildBed(1L, 101, "101-A", 2));
        beds.add(buildBed(2L, 101, "101-B", 1));
        beds.add(buildBed(3L, 102, "102-A", 3));
        beds.add(buildBed(4L, 102, "102-B", 1));
        beds.add(buildBed(5L, 201, "201-A", 2));
        beds.add(buildBed(6L, 201, "201-B", 1));
    }

    private void seedCustomers() {
        customers.add(buildCustomer(1L, "王秀兰", 78, "101", "606", 1L, 3L, 1L, "A型"));
        customers.add(buildCustomer(2L, "赵建国", 82, "102", "606", 3L, 4L, 2L, "O型"));
        customers.add(buildCustomer(3L, "刘桂花", 75, "201", "606", 5L, 3L, 1L, "B型"));
    }

    private void seedNursing() {
        nursingItems.add(buildNursingItem(1L, "NC-001", "血压监测", new BigDecimal("18.00"), 1, "每日", "2"));
        nursingItems.add(buildNursingItem(2L, "NC-002", "助浴护理", new BigDecimal("68.00"), 1, "每周", "2"));
        nursingItems.add(buildNursingItem(3L, "NC-003", "康复训练", new BigDecimal("88.00"), 1, "每周", "3"));

        nursingLevels.add(buildNursingLevel(1L, "一级护理", 1));
        nursingLevels.add(buildNursingLevel(2L, "二级护理", 1));
    }

    private void seedApprovals() {
        Outward outward = new Outward();
        outward.setId(1L);
        outward.setCustomerId(2L);
        outward.setOutgoingReason("家属接回体检");
        outward.setOutgoingTime(LocalDate.now().minusDays(1));
        outward.setExpectedReturnTime(LocalDate.now().plusDays(1));
        outward.setAuditStatus(0);
        outward.setEscorted("赵先生");
        outward.setRelation("儿子");
        outward.setEscortedTel("13900000001");
        outwards.add(outward);

        Backdown backdown = new Backdown();
        backdown.setId(1L);
        backdown.setCustomerId(3L);
        backdown.setRetreatTime(LocalDate.now().plusDays(3));
        backdown.setRetreatType(0);
        backdown.setRetreatReason("合同到期后回家居住");
        backdown.setAuditStatus(0);
        backdowns.add(backdown);
    }

    private User buildUser(Long id, String username, String nickname, String phone, Long roleId) {
        User user = new User();
        user.setId(id);
        user.setUsername(username);
        user.setNickname(nickname);
        user.setPhoneNumber(phone);
        user.setRoleId(roleId);
        user.setPassword("123456");
        user.setCreateTime(LocalDateTime.now());
        user.setIsDeleted(0);
        return user;
    }

    private Room buildRoom(Long id, String floor, Integer roomNo) {
        Room room = new Room();
        room.setId(id);
        room.setRoomFloor(floor);
        room.setRoomNo(roomNo);
        return room;
    }

    private Bed buildBed(Long id, Integer roomNo, String bedNo, Integer status) {
        Bed bed = new Bed();
        bed.setId(id);
        bed.setRoomNo(roomNo);
        bed.setBedNo(bedNo);
        bed.setBedStatus(status);
        return bed;
    }

    private Customer buildCustomer(Long id, String name, Integer age, String roomNo, String buildingNo,
                                   Long bedId, Long userId, Long levelId, String bloodType) {
        Customer customer = new Customer();
        customer.setId(id);
        customer.setCustomerName(name);
        customer.setCustomerAge(age);
        customer.setCustomerSex(id.intValue() % 2);
        customer.setRoomNo(roomNo);
        customer.setBuildingNo(buildingNo);
        customer.setBedId(bedId);
        customer.setUserId(userId);
        customer.setLevelId(levelId);
        customer.setBloodType(bloodType);
        customer.setCheckinDate(LocalDate.now().minusMonths(id));
        customer.setExpirationDate(LocalDate.now().plusMonths(6));
        customer.setContactTel("1391234000" + id);
        customer.setFamilyMember("家属" + name);
        customer.setIdcard("21000019400101" + String.format("%04d", id));
        customer.setFilepath("/avatar/default.png");
        customer.setIsDeleted(0);
        customer.setCreateTime(LocalDateTime.now());
        return customer;
    }

    private NursingItem buildNursingItem(Long id, String serialNumber, String name, BigDecimal price,
                                         Integer status, String cycle, String times) {
        NursingItem item = new NursingItem();
        item.setId(id);
        item.setSerialNumber(serialNumber);
        item.setNursingName(name);
        item.setServicePrice(price);
        item.setStatus(status);
        item.setExecutionCycle(cycle);
        item.setExecutionTimes(times);
        item.setMessage(name + "服务说明");
        item.setCreateTime(LocalDateTime.now());
        item.setIsDeleted(0);
        return item;
    }

    private NursingLevel buildNursingLevel(Long id, String name, Integer status) {
        NursingLevel level = new NursingLevel();
        level.setId(id);
        level.setLevelName(name);
        level.setLevelStatus(status);
        level.setCreateTime(LocalDateTime.now());
        level.setIsDeleted(0);
        return level;
    }
}
