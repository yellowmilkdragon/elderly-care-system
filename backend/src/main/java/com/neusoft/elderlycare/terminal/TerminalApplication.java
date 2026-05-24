package com.neusoft.elderlycare.terminal;

import com.neusoft.elderlycare.common.DemoDataStore;

import java.lang.reflect.Method;

public class TerminalApplication {

    public static void main(String[] args) {
        DemoDataStore demoDataStore = new DemoDataStore();
        initializeDemoData(demoDataStore);

        TerminalConsole console = new TerminalConsole(demoDataStore);
        console.start();
    }

    private static void initializeDemoData(DemoDataStore demoDataStore) {
        try {
            Method initMethod = DemoDataStore.class.getDeclaredMethod("init");
            initMethod.setAccessible(true);
            initMethod.invoke(demoDataStore);
        } catch (Exception ex) {
            throw new IllegalStateException("终端版初始化演示数据失败", ex);
        }
    }
}
