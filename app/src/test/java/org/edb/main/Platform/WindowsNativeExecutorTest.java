package org.edb.main.Platform;

import org.junit.Test;

import java.util.ArrayList;

public class WindowsNativeExecutorTest {

    @Test
    public void taskKillWithProcessNames(){
        for (int i = 0; i < 5; i++) {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            ArrayList<String> list = new ArrayList<String>();
            list.add("Steam.exe");
            WindowsNativeExecutor executor = new WindowsNativeExecutor();
            executor.taskKillWithProcessNames(list);
        }
    }
}
