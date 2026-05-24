package com.neusoft.elderlycare.terminal;

import com.neusoft.elderlycare.common.DemoDataStore;
import com.neusoft.elderlycare.entity.Backdown;
import com.neusoft.elderlycare.entity.Bed;
import com.neusoft.elderlycare.entity.Customer;
import com.neusoft.elderlycare.entity.NursingItem;
import com.neusoft.elderlycare.entity.NursingLevel;
import com.neusoft.elderlycare.entity.Outward;
import com.neusoft.elderlycare.entity.Room;
import com.neusoft.elderlycare.entity.User;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

public class TerminalConsole {

    private final DemoDataStore demoDataStore;
    private final Scanner scanner;

    public TerminalConsole(DemoDataStore demoDataStore) {
        this.demoDataStore = demoDataStore;
        this.scanner = new Scanner(System.in);
    }

    public void start() {
        println("======================================");
        println("  东软颐养中心管理系统 - 终端版");
        println("======================================");
        println("说明：当前为课程演示版本，使用内存演示数据。");
        boolean running = true;
        while (running) {
            showMainMenu();
            String choice = prompt("请选择功能编号");
            switch (choice) {
                case "1" -> customerMenu();
                case "2" -> bedMenu();
                case "3" -> nursingMenu();
                case "4" -> caregiverMenu();
                case "5" -> userMenu();
                case "6" -> approvalMenu();
                case "0" -> running = false;
                default -> println("输入无效，请重新选择。");
            }
        }
        println("系统已退出。");
    }

    private void showMainMenu() {
        println("");
        println("========== 主菜单 ==========");
        println("1. 客户管理");
        println("2. 床位管理");
        println("3. 护理模块");
        println("4. 健康管家");
        println("5. 用户管理");
        println("6. 审批管理");
        println("0. 退出系统");
    }

    private void customerMenu() {
        boolean back = false;
        while (!back) {
            println("");
            println("------ 客户管理 ------");
            println("1. 查看客户列表");
            println("2. 新增入住登记");
            println("3. 修改客户档案");
            println("0. 返回主菜单");
            String choice = prompt("请选择");
            switch (choice) {
                case "1" -> showCustomers();
                case "2" -> createCustomer();
                case "3" -> updateCustomer();
                case "0" -> back = true;
                default -> println("输入无效，请重新选择。");
            }
        }
    }

    private void showCustomers() {
        println("");
        println("客户列表：");
        for (Customer customer : demoDataStore.getCustomers()) {
            println(String.format(
                    "ID=%d 姓名=%s 年龄=%d 房间=%s 床位=%s 管家ID=%s 护理级别=%s",
                    customer.getId(),
                    customer.getCustomerName(),
                    customer.getCustomerAge(),
                    customer.getRoomNo(),
                    customer.getBedId(),
                    customer.getUserId(),
                    customer.getLevelId()
            ));
        }
    }

    private void createCustomer() {
        Customer customer = new Customer();
        customer.setCustomerName(prompt("请输入客户姓名"));
        customer.setCustomerAge(readInt("请输入年龄", 60, 120));
        customer.setCustomerSex(readInt("请输入性别（0女 1男）", 0, 1));
        customer.setRoomNo(prompt("请输入房间号"));
        customer.setBuildingNo(promptDefault("请输入楼栋号", "606"));
        customer.setBedId(readLong("请输入床位ID"));
        customer.setUserId(readLong("请输入健康管家ID"));
        customer.setLevelId(readLong("请输入护理级别ID"));
        customer.setBloodType(promptDefault("请输入血型", "A型"));
        customer.setContactTel(prompt("请输入联系电话"));
        customer.setFamilyMember(prompt("请输入家属姓名"));
        customer.setCheckinDate(readDate("请输入入住日期（yyyy-MM-dd）", LocalDate.now()));
        customer.setExpirationDate(readDate("请输入合同到期日期（yyyy-MM-dd）", LocalDate.now().plusMonths(6)));
        demoDataStore.saveCustomer(customer);
        println("客户入住登记成功。");
    }

    private void updateCustomer() {
        showCustomers();
        Long id = readLong("请输入要修改的客户ID");
        Customer existing = findCustomer(id);
        if (existing == null) {
            println("未找到该客户。");
            return;
        }
        Customer payload = new Customer();
        payload.setId(existing.getId());
        payload.setCustomerName(promptDefault("客户姓名", existing.getCustomerName()));
        payload.setCustomerAge(readIntDefault("客户年龄", existing.getCustomerAge(), 60, 120));
        payload.setCustomerSex(readIntDefault("客户性别（0女 1男）", existing.getCustomerSex(), 0, 1));
        payload.setRoomNo(promptDefault("房间号", existing.getRoomNo()));
        payload.setBuildingNo(promptDefault("楼栋号", existing.getBuildingNo()));
        payload.setBedId(readLongDefault("床位ID", existing.getBedId()));
        payload.setUserId(readLongDefault("健康管家ID", existing.getUserId()));
        payload.setLevelId(readLongDefault("护理级别ID", existing.getLevelId()));
        payload.setBloodType(promptDefault("血型", existing.getBloodType()));
        payload.setContactTel(promptDefault("联系电话", existing.getContactTel()));
        payload.setFamilyMember(promptDefault("家属姓名", existing.getFamilyMember()));
        payload.setCheckinDate(readDateDefault("入住日期（yyyy-MM-dd）", existing.getCheckinDate()));
        payload.setExpirationDate(readDateDefault("合同到期日期（yyyy-MM-dd）", existing.getExpirationDate()));
        demoDataStore.saveCustomer(payload);
        println("客户档案已更新。");
    }

    private void bedMenu() {
        boolean back = false;
        while (!back) {
            println("");
            println("------ 床位管理 ------");
            println("1. 查看床位示意图");
            println("2. 查看空闲床位");
            println("3. 办理调床");
            println("0. 返回主菜单");
            String choice = prompt("请选择");
            switch (choice) {
                case "1" -> showRooms();
                case "2" -> showAvailableBeds();
                case "3" -> transferBed();
                case "0" -> back = true;
                default -> println("输入无效，请重新选择。");
            }
        }
    }

    private void showRooms() {
        println("");
        for (Room room : demoDataStore.getRooms()) {
            println(String.format("房间：%s-%d", room.getRoomFloor(), room.getRoomNo()));
            demoDataStore.getBeds().stream()
                    .filter(bed -> room.getRoomNo().equals(bed.getRoomNo()))
                    .forEach(bed -> {
                        String owner = demoDataStore.getCustomers().stream()
                                .filter(customer -> bed.getId().equals(customer.getBedId()))
                                .map(Customer::getCustomerName)
                                .findFirst()
                                .orElse("无人入住");
                        println(String.format(
                                "  床位ID=%d 床号=%s 状态=%s 主人=%s",
                                bed.getId(),
                                bed.getBedNo(),
                                bedStatusLabel(bed.getBedStatus()),
                                owner
                        ));
                    });
        }
    }

    private void showAvailableBeds() {
        println("");
        println("空闲床位：");
        demoDataStore.getBeds().stream()
                .filter(bed -> bed.getBedStatus() == 1)
                .forEach(bed -> println(String.format("床位ID=%d 房间=%d 床号=%s", bed.getId(), bed.getRoomNo(), bed.getBedNo())));
    }

    private void transferBed() {
        showCustomers();
        showAvailableBeds();
        Long customerId = readLong("请输入客户ID");
        Long bedId = readLong("请输入目标床位ID");
        Customer updated = demoDataStore.transferBed(customerId, bedId);
        if (updated == null) {
            println("调床失败，请确认客户和床位状态是否正确。");
            return;
        }
        println("调床成功，新房间号为：" + updated.getRoomNo());
    }

    private void nursingMenu() {
        boolean back = false;
        while (!back) {
            println("");
            println("------ 护理模块 ------");
            println("1. 查看护理项目");
            println("2. 新增护理项目");
            println("3. 查看护理级别");
            println("4. 新增护理级别");
            println("0. 返回主菜单");
            String choice = prompt("请选择");
            switch (choice) {
                case "1" -> showNursingItems();
                case "2" -> createNursingItem();
                case "3" -> showNursingLevels();
                case "4" -> createNursingLevel();
                case "0" -> back = true;
                default -> println("输入无效，请重新选择。");
            }
        }
    }

    private void showNursingItems() {
        println("");
        for (NursingItem item : demoDataStore.getNursingItems()) {
            println(String.format(
                    "ID=%d 编号=%s 名称=%s 价格=%s 周期=%s 次数=%s 状态=%s",
                    item.getId(),
                    item.getSerialNumber(),
                    item.getNursingName(),
                    item.getServicePrice(),
                    item.getExecutionCycle(),
                    item.getExecutionTimes(),
                    item.getStatus()
            ));
        }
    }

    private void createNursingItem() {
        NursingItem item = new NursingItem();
        item.setSerialNumber(prompt("请输入项目编号"));
        item.setNursingName(prompt("请输入项目名称"));
        item.setServicePrice(readDecimal("请输入项目价格"));
        item.setExecutionCycle(prompt("请输入执行周期"));
        item.setExecutionTimes(prompt("请输入执行次数"));
        item.setStatus(readInt("请输入状态（1启用 2停用）", 1, 2));
        item.setMessage(promptDefault("请输入项目说明", "终端新增项目"));
        demoDataStore.saveNursingItem(item);
        println("护理项目新增成功。");
    }

    private void showNursingLevels() {
        println("");
        for (NursingLevel level : demoDataStore.getNursingLevels()) {
            println(String.format(
                    "ID=%d 名称=%s 状态=%s",
                    level.getId(),
                    level.getLevelName(),
                    level.getLevelStatus()
            ));
        }
    }

    private void createNursingLevel() {
        NursingLevel level = new NursingLevel();
        level.setLevelName(prompt("请输入护理级别名称"));
        level.setLevelStatus(readInt("请输入状态（1启用 2停用）", 1, 2));
        demoDataStore.saveNursingLevel(level);
        println("护理级别新增成功。");
    }

    private void caregiverMenu() {
        boolean back = false;
        while (!back) {
            println("");
            println("------ 健康管家 ------");
            println("1. 查看健康管家列表");
            println("2. 设置服务对象");
            println("0. 返回主菜单");
            String choice = prompt("请选择");
            switch (choice) {
                case "1" -> showCaregivers();
                case "2" -> assignCaregiver();
                case "0" -> back = true;
                default -> println("输入无效，请重新选择。");
            }
        }
    }

    private void showCaregivers() {
        println("");
        demoDataStore.getUsers().stream()
                .filter(user -> Long.valueOf(2L).equals(user.getRoleId()))
                .forEach(user -> {
                    List<String> customerNames = demoDataStore.getCustomers().stream()
                            .filter(customer -> user.getId().equals(customer.getUserId()))
                            .map(Customer::getCustomerName)
                            .toList();
                    println(String.format(
                            "管家ID=%d 姓名=%s 账号=%s 服务对象=%s",
                            user.getId(),
                            user.getNickname(),
                            user.getUsername(),
                            customerNames.isEmpty() ? "暂无" : String.join("、", customerNames)
                    ));
                });
    }

    private void assignCaregiver() {
        showCustomers();
        showCaregivers();
        Long customerId = readLong("请输入客户ID");
        Long caregiverId = readLong("请输入健康管家ID");
        Customer updated = demoDataStore.assignCaregiver(customerId, caregiverId);
        if (updated == null) {
            println("分配失败，请确认客户和健康管家ID。");
            return;
        }
        println("服务对象分配成功。");
    }

    private void userMenu() {
        println("");
        println("------ 用户管理 ------");
        List<User> users = demoDataStore.getUsers().stream()
                .sorted(Comparator.comparing(User::getId))
                .toList();
        for (User user : users) {
            println(String.format(
                    "ID=%d 姓名=%s 账号=%s 手机=%s 角色=%s",
                    user.getId(),
                    user.getNickname(),
                    user.getUsername(),
                    user.getPhoneNumber(),
                    Long.valueOf(1L).equals(user.getRoleId()) ? "系统管理员" : "健康管家"
            ));
        }
    }

    private void approvalMenu() {
        boolean back = false;
        while (!back) {
            println("");
            println("------ 审批管理 ------");
            println("1. 查看外出登记");
            println("2. 审批外出登记");
            println("3. 查看退住登记");
            println("4. 审批退住登记");
            println("5. 办理返院登记");
            println("0. 返回主菜单");
            String choice = prompt("请选择");
            switch (choice) {
                case "1" -> showOutwards();
                case "2" -> auditOutward();
                case "3" -> showBackdowns();
                case "4" -> auditBackdown();
                case "5" -> markReturn();
                case "0" -> back = true;
                default -> println("输入无效，请重新选择。");
            }
        }
    }

    private void showOutwards() {
        println("");
        for (Outward outward : demoDataStore.getOutwards()) {
            println(String.format(
                    "ID=%d 客户ID=%d 原因=%s 外出时间=%s 预计返院=%s 审批状态=%s",
                    outward.getId(),
                    outward.getCustomerId(),
                    outward.getOutgoingReason(),
                    outward.getOutgoingTime(),
                    outward.getExpectedReturnTime(),
                    auditStatusLabel(outward.getAuditStatus())
            ));
        }
    }

    private void auditOutward() {
        showOutwards();
        Long outwardId = readLong("请输入外出登记ID");
        int auditStatus = readInt("请输入审批结果（1通过 2驳回）", 1, 2);
        Outward outward = demoDataStore.auditOutward(outwardId, auditStatus);
        if (outward == null) {
            println("审批失败，记录不存在或已审批。");
            return;
        }
        println("外出登记审批完成。");
    }

    private void showBackdowns() {
        println("");
        for (Backdown backdown : demoDataStore.getBackdowns()) {
            println(String.format(
                    "ID=%d 客户ID=%d 退住时间=%s 原因=%s 审批状态=%s",
                    backdown.getId(),
                    backdown.getCustomerId(),
                    backdown.getRetreatTime(),
                    backdown.getRetreatReason(),
                    auditStatusLabel(backdown.getAuditStatus())
            ));
        }
    }

    private void auditBackdown() {
        showBackdowns();
        Long backdownId = readLong("请输入退住登记ID");
        int auditStatus = readInt("请输入审批结果（1通过 2驳回）", 1, 2);
        Backdown backdown = demoDataStore.auditBackdown(backdownId, auditStatus);
        if (backdown == null) {
            println("审批失败，记录不存在或已审批。");
            return;
        }
        println("退住登记审批完成。");
    }

    private void markReturn() {
        showOutwards();
        Long outwardId = readLong("请输入返院登记对应的外出ID");
        Outward outward = demoDataStore.markReturned(outwardId);
        if (outward == null) {
            println("返院登记失败，未找到对应外出记录。");
            return;
        }
        println("返院登记完成。");
    }

    private Customer findCustomer(Long id) {
        return demoDataStore.getCustomers().stream()
                .filter(customer -> customer.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    private String bedStatusLabel(Integer status) {
        if (status == null) {
            return "未知";
        }
        return switch (status) {
            case 1 -> "空闲";
            case 2 -> "入住";
            case 3 -> "外出";
            default -> "未知";
        };
    }

    private String auditStatusLabel(Integer status) {
        if (status == null || status == 0) {
            return "待审批";
        }
        if (status == 1) {
            return "已通过";
        }
        if (status == 2) {
            return "已驳回";
        }
        return "未知";
    }

    private String prompt(String message) {
        System.out.print(message + "：");
        return scanner.nextLine().trim();
    }

    private String promptDefault(String message, String defaultValue) {
        String value = prompt(message + "（默认：" + defaultValue + "）");
        return value.isEmpty() ? defaultValue : value;
    }

    private int readInt(String message, int min, int max) {
        while (true) {
            try {
                int value = Integer.parseInt(prompt(message));
                if (value < min || value > max) {
                    println("输入超出范围，请重新输入。");
                    continue;
                }
                return value;
            } catch (NumberFormatException ex) {
                println("请输入有效整数。");
            }
        }
    }

    private int readIntDefault(String message, Integer defaultValue, int min, int max) {
        while (true) {
            String input = prompt(message + "（默认：" + defaultValue + "）");
            if (input.isEmpty()) {
                return defaultValue;
            }
            try {
                int value = Integer.parseInt(input);
                if (value < min || value > max) {
                    println("输入超出范围，请重新输入。");
                    continue;
                }
                return value;
            } catch (NumberFormatException ex) {
                println("请输入有效整数。");
            }
        }
    }

    private Long readLong(String message) {
        while (true) {
            try {
                return Long.parseLong(prompt(message));
            } catch (NumberFormatException ex) {
                println("请输入有效数字。");
            }
        }
    }

    private Long readLongDefault(String message, Long defaultValue) {
        while (true) {
            String input = prompt(message + "（默认：" + defaultValue + "）");
            if (input.isEmpty()) {
                return defaultValue;
            }
            try {
                return Long.parseLong(input);
            } catch (NumberFormatException ex) {
                println("请输入有效数字。");
            }
        }
    }

    private BigDecimal readDecimal(String message) {
        while (true) {
            try {
                return new BigDecimal(prompt(message));
            } catch (NumberFormatException ex) {
                println("请输入有效金额。");
            }
        }
    }

    private LocalDate readDate(String message, LocalDate defaultValue) {
        while (true) {
            String input = prompt(message + "（默认：" + defaultValue + "）");
            if (input.isEmpty()) {
                return defaultValue;
            }
            try {
                return LocalDate.parse(input);
            } catch (Exception ex) {
                println("请输入正确日期格式，例如 2026-05-24。");
            }
        }
    }

    private LocalDate readDateDefault(String message, LocalDate defaultValue) {
        return readDate(message, defaultValue);
    }

    private void println(String message) {
        System.out.println(message);
    }
}
