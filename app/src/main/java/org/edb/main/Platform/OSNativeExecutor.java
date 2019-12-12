package org.edb.main.Platform;

import java.util.List;

public abstract class OSNativeExecutor {
    public abstract List<String> getCurPrograms();

    public abstract List<String> getCurWebsites();

    public abstract void taskKillWithProcessNames(List<String> targetProcesses);
}
