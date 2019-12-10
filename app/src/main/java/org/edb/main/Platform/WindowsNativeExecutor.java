package org.edb.main.Platform;

import com.sun.jna.platform.win32.*;
import com.sun.jna.ptr.IntByReference;

import java.io.File;
import java.util.List;

public class WindowsNativeExecutor extends OSNativeExecutor {
    static final int MAX_BUFFER =4096;

    @Override
    public List<String> getCurPrograms() {
        return null;
    }

    @Override
    public List<String> getCurWebsites() {
        return null;
    }

    @Override
    public void taskKillWithProcessNames(List<String> targetProcesses) {
        boolean matchingProcess=false;
        WinDef.HWND foreground = User32.INSTANCE.GetForegroundWindow();
        String procName = getProcessNameWithHWND(foreground);


        for (String targetProcessName :
                targetProcesses) {
            if (targetProcessName.contains(procName)){
                matchingProcess=true;
                System.out.println(targetProcessName);
                break;
            }
        }

        if(procName=="")matchingProcess=false;

        System.out.println(procName);
        System.out.println(matchingProcess);
        if(matchingProcess){
            User32.INSTANCE.PostMessage(foreground, WinUser.WM_CLOSE, null, null);
        }
    }

    private String getProcessNameWithHWND(WinDef.HWND foreground){
        String imgName = getProcessImageNameWithHWND(foreground);
        return extractProcessName(imgName);
    }

    private String getProcessImageNameWithHWND(WinDef.HWND foreground) {
        IntByReference processId = new IntByReference();
        User32.INSTANCE.GetWindowThreadProcessId(foreground,processId);

        WinNT.HANDLE procHandle = Kernel32.INSTANCE.OpenProcess(Kernel32.PROCESS_QUERY_LIMITED_INFORMATION,
                false, processId.getValue());

        char[] buffer = new char[MAX_BUFFER];
        IntByReference bufferSize = new IntByReference(buffer.length);
        boolean successForGetProcessImageName = Kernel32.INSTANCE.QueryFullProcessImageName(
                procHandle, 0, buffer, bufferSize);

        Kernel32.INSTANCE.CloseHandle(procHandle);

        if(successForGetProcessImageName){
            return new String(buffer, 0, bufferSize.getValue());
        }
        else{
            return "";
        }
    }

    private String extractProcessName(String imgName) {
        if(imgName=="") return null;
        String[] words = imgName.split("\\\\");

        return words[words.length-1];
    }

}
