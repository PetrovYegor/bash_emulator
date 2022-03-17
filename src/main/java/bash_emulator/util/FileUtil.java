package bash_emulator.util;

import bash_emulator.FileInfo;

import java.util.Arrays;
import java.util.List;

public class FileUtil {
    public static final Character[] INVALID_WINDOWS_SPECIFIC_CHARS = {'"', '*', ':', '<', '>', '?', '\\', '|', 0x7F};
    public static final Character[] INVALID_UNIX_SPECIFIC_CHARS = {'\000'};
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

    public static Character[] getInvalidCharsByOS() {
        String os = System.getProperty("os.name").toLowerCase();
        if (os.contains("win")) {
            return INVALID_WINDOWS_SPECIFIC_CHARS;
        } else if (os.contains("nix") || os.contains("nux") || os.contains("mac")) {
            return INVALID_UNIX_SPECIFIC_CHARS;
        } else {
            return new Character[]{};
        }
    }

    //https://www.baeldung.com/java-validate-filename#1-using-stringcontains
    public static boolean validateStringFilenameUsingContains(String filename) {
        if (filename == null || filename.isEmpty() || filename.length() > 255) {
            return false;
        }
        return Arrays.stream(getInvalidCharsByOS())
                .noneMatch(ch -> filename.contains(ch.toString()));
    }
}
