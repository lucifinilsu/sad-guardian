package com.sad.assistant.live.guardian.compiler;

import java.io.File;
import java.io.FileFilter;

public class FileUtils {

    private static boolean isDir(final File file) {
        return file != null && file.exists() && file.isDirectory();
    }

    public static long scanAllFilesDir(final File dir, FileFilter filter, IFileScanedCallback scaned) {
        if (!isDir(dir)) return -1;
        long len = 0;
        File[] files = dir.listFiles();
        if (files != null && files.length != 0) {
            for (File file : files) {
                if (file!=null && (filter==null || filter.accept(file))){
                    if (file.isDirectory()) {
                        long t= scanAllFilesDir(file,filter,scaned);
                        if (t>-1){
                            len +=t;
                        }
                    } else {
                        if (scaned!=null){
                            scaned.onScaned(file);
                        }
                        len += file.length();
                    }
                }

            }
        }
        return len;
    }

}
