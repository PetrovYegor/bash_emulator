package bash_emulator.util;

import bash_emulator.FileInfo;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;

public class FileUtil {

    public static final String FILE_ATTRIBUTES_PATTERN = "%s %s %s %s";

    public static String fileSizeFormatter(FileInfo fileInfo){
        long size = fileInfo.getSize();
        if (size == 0){
            return "";
        } else if (size == -1L){
            return "[DIR]";
        } else {
            return String.format("%,d bytes", size);
        }
    }

    public static void showFileInfo(List<FileInfo> list){
        for (var item : list)
            System.out.println(String.format(FileUtil.FILE_ATTRIBUTES_PATTERN, item.getType().getName(), item.getFileName(), FileUtil.fileSizeFormatter(item), DateTimeUtil.toString(item.getLastModified())));
    }

    public static boolean isWriteable(Path path){
        return Files.isWritable(path);
    }
}
