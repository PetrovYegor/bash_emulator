package bash_emulator.util;

import bash_emulator.Controller;
import bash_emulator.FileInfo;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;

public class Util {

    public static String[] inputStringSplitter(String str){
        return str.trim().split("\\s+");
    }

    public static Path currentPathResolver(String str){
        return Paths.get(Controller.getCurrentDirectory()).resolve(str);
    }

    public static Path pathResolver(Path path, String str){
        return path.resolve(str);
    }

    public static Path getParentPath(Path path){
        return path.getParent();
    }


}
