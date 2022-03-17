package bash_emulator;

import bash_emulator.util.FileUtil;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.nio.file.attribute.FileTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static bash_emulator.util.Util.currentPathResolver;
import static bash_emulator.util.Util.inputStringSplitter;

public class Controller {

    public void initialize(){
        System.out.println(System.getProperty("user.home"));
        System.setProperty("user.dir", System.getProperty("user.home"));
    }

    public static String getCurrentDirectory(){
        return System.getProperty("user.dir");
    }

    //мб назвать получше
    //Отдаём директорию, она печатает в консоль список файлов, которые там есть
    public void showFiles(Path path){

        //Опционально - с атрибутами файлов и/или их владельцев!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
        //метод, который по пути вернёт поток путей
        try {
            List<FileInfo> list =  Files.list(path)
                    .map(FileInfo::new)//теперь мы хотим пачку путей преобразовать к FileInfo. Берём каждый путь из потока и отдаём в конструктор FileInfo. На этом моменте получим список объектов FileInfo
                    .collect(Collectors.toList());//после этого собираю всё в лист и отдаю в консоль лист

            Collections.sort(list, new Comparator<FileInfo>() {
                @Override
                public int compare(FileInfo o1, FileInfo o2) {
                    return o1.getType().toString().compareTo(o2.getType().toString());
                }
            });
            FileUtil.showFileInfo(list);
        } catch (IOException e) {
            System.out.println("По какой-то причине не удалось получить список файлов по указанной директории");
        }

    }
    //https://www.delftstack.com/ru/howto/java/create-directory-java/#:~:text=%D0%BA%D0%B0%D1%82%D0%B0%D0%BB%D0%BE%D0%B3%20%D0%BD%D0%B0%20Java.-,%D0%98%D1%81%D0%BF%D0%BE%D0%BB%D1%8C%D0%B7%D1%83%D0%B9%D1%82%D0%B5%20%D1%84%D1%83%D0%BD%D0%BA%D1%86%D0%B8%D1%8E%20mkdir()%20%D0%B4%D0%BB%D1%8F%20%D1%81%D0%BE%D0%B7%D0%B4%D0%B0%D0%BD%D0%B8%D1%8F%20%D0%BA%D0%B0%D1%82%D0%B0%D0%BB%D0%BE%D0%B3%D0%BE%D0%B2%20%D0%B2%20Java,mkdir()%20%2C%20%D1%81%D0%BE%D0%B7%D0%B4%D0%B0%D0%B2%D0%B0%D1%8F%20%D1%82%D1%80%D0%B5%D0%B1%D1%83%D0%B5%D0%BC%D1%8B%D0%B9%20%D0%BA%D0%B0%D1%82%D0%B0%D0%BB%D0%BE%D0%B3.
    public void makeDirectory(String str){
        String[] array = inputStringSplitter(str);

        try{
            Path dstPath = currentPathResolver(array[1]);
            if (!Files.exists(dstPath)){
                Files.createDirectory(dstPath);
                System.out.println("New Directory created! " + dstPath);
            } else {
                System.out.println("mkdir: cannot create directory ‘" + dstPath + "’: File exists");
            }
        }catch(Exception ex){
            System.out.println("Не удалось создать каталог ");
        } finally {
            printCurrentDirectoryIn();
        }

    }

    public void showHelp(){
        for (Commands item : Commands.values()){
            System.out.println("--" + item + " " + item.getDescription());
            printCurrentDirectoryIn();
        }
    }

    public void changeDirectory(String str){
        String[] array = inputStringSplitter(str);

        try{
            Path dstPath = currentPathResolver(array[1]);
            if (!Files.exists(dstPath)){
                System.out.println("cd: " + array[1] + ": No such file or directory");
            } else {
                System.setProperty("user.dir", dstPath.toString());
            }
        }catch(Exception ex){
            System.out.println("Не перейти в указанный каталог");
        } finally {
            printCurrentDirectoryIn();
        }
    }
    //https://ru.stackoverflow.com/questions/629482/java-%D1%81%D0%BE%D0%B7%D0%B4%D0%B0%D1%82%D1%8C-%D1%84%D0%B0%D0%B9%D0%BB-%D0%B2-%D0%BF%D0%B0%D0%BF%D0%BA%D0%B5
    public void createOrUpdateFile(String str){
        // ОБЯЗАТЕЛЬНО сделать, чтобы можно было только ок расширения (т.е. субстринг от последней точки)

        try{

            String[] array = inputStringSplitter(str);
            Path path1 = currentPathResolver(array[1]);

            //File f = new File(array[1]);
            if (!Files.exists(path1)){
                Files.createFile(path1);
                setCurrentTime(path1);
            } else {
                setCurrentTime(path1);
            }

        }catch(Exception ex){
            System.out.println("Не удалось выполнить команду touch");
        }finally {
            printCurrentDirectoryIn();
        }

    }

    public void readAndPrint(String str)  {
        String[] array = inputStringSplitter(str);
        Path path1 = currentPathResolver(array[1]);

        String content;
        try{
            BufferedReader reader = new BufferedReader(Files.newBufferedReader(path1));
            while ((content = reader.readLine()) != null){
                System.out.println(content);
            }
            reader.close();
        }catch (Exception ex){
            System.out.println("Не удалось считать содержимое файла");
        }finally {

            printCurrentDirectoryIn();
        }
    }

    public void printLastLines(String str){
        //(по умолчанию 10) !!!!!!!!!!!!!!!!!!!!!!!!!!!!!! предусмотреть изменённый вывод
        String[] array = inputStringSplitter(str);
        Path path1 = currentPathResolver(array[1]);

        try{
            BufferedReader reader = new BufferedReader(Files.newBufferedReader(path1));
            List<String> list = new ArrayList<>();
            String content;
            File f = new File(path1.toString());
            if (f.exists() && !f.isDirectory()){
                while ((content = reader.readLine()) != null){
                    list.add(content);
                }

                if (list.size() <= 10){
                    for (var item : list)
                        System.out.println(item);
                } else {
                    for (int i = list.size() - 10; i < list.size(); i++)
                        System.out.println(list.get(i));
                }
            }
            reader.close();
        }catch(Exception ex){
            System.out.println("Не удалось прочитать содержимое файла");
        }finally {
            printCurrentDirectoryIn();
    }
    }

    public void copyFile(String str){
        String[] array = inputStringSplitter(str);
        Path path1;
        Path path2;
        boolean isFlagExists = array[1].equalsIgnoreCase("-r");

        if (isFlagExists) {
            path1 = currentPathResolver(array[2]);
            path2 = currentPathResolver(array[3]);
        } else {
            path1 = currentPathResolver(array[1]);
            path2 = currentPathResolver(array[2]);
        }
        File f1 = new File(path1.toString());

        File f2 = new File(path2.toString());


        boolean f1IsExist = f1.exists();
        boolean f2IsExist = f2.exists();
        boolean f1IsDirectory = f1.isDirectory();
        boolean f2IsDirectory = f2.isDirectory();

        try{
            if(areFilesEquals(f1.getAbsolutePath(), f2.getAbsolutePath())){
                System.out.println("cp: "+ f1.getName() + " and " + f2.getName() + " are the same file");
                return;
            }


            if (f1IsExist){
                if (!f1IsDirectory){
                    if (f2IsExist){
                        if (!f2IsDirectory){
                            Files.copy(path1, path2, StandardCopyOption.REPLACE_EXISTING);
                            setCurrentTime(path2);
                        } else {
                            Path temp = path2.resolve(path1.getFileName());
                            Files.copy(path1, temp, StandardCopyOption.REPLACE_EXISTING);
                            setCurrentTime(temp);
                        }
                    } else {
                        Files.copy(path1, path2);
                        setCurrentTime(path2);
                    }
                } else {//!f1IsDirectory
                    if (f2IsExist){
                        if(!f2IsDirectory){
                            if(!isFlagExists){
                                System.out.println("cp: -r not specified; omitting directory '" + f1.getName() + "'");
                            } else {
                                System.out.println("cp: cannot overwrite non-directory '" + f2.getName() + "' with directory '" + f1.getName() + "'");
                            }
                        } else {
                            if(!isFlagExists){
                                System.out.println("cp: -r not specified; omitting directory '" + f1.getName() + "'");
                            } else {
                                Path temp = path2.resolve(path1.getFileName());
                                Files.walk(path1)
                                        .forEach(source -> {
                                            Path destination = Paths.get(temp.toString(), source.toString()
                                                    .substring(path1.toString().length()));
                                            try {
                                                if(!Files.exists(destination)){
                                                    Files.copy(source, destination);
                                                    setCurrentTime(destination);
                                                } else {
                                                    if (!Files.isDirectory(destination)){
                                                        Files.copy(source, destination, StandardCopyOption.REPLACE_EXISTING);
                                                        setCurrentTime(destination);
                                                    }
                                                }

                                            } catch (IOException e) {
                                                e.printStackTrace();
                                            }
                                        });
                            }
                        }
                    } else {
                        if (!isFlagExists){
                            System.out.println("cp: -r not specified; omitting directory '" + f1.getName() + "'");
                        } else {
                            //https://www.baeldung.com/java-copy-directory#using-the-javanio-api
                            Files.walk(path1)
                                    .forEach(source -> {
                                        Path destination = Paths.get(path2.toString(), source.toString()
                                                .substring(path1.toString().length()));
                                        try {
                                            Files.copy(source, destination);
                                            setCurrentTime(destination);
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }
                                    });
                        }
                    }
                }
            } else {
                System.out.println("cp: cannot stat '" + f1.getName()+ "': No such file or directory");
            }
        }catch (Exception ex) {
            System.out.println("Ошибка копирования");
        }finally {
            printCurrentDirectoryIn();
        }
    }

    public static void setCurrentTime(Path path){
        try{
            Files.setLastModifiedTime(path, FileTime.fromMillis(System.currentTimeMillis()));
        } catch (Exception ex){
            System.out.println("Не удалось установить время");
        }
    }

    public static void printCurrentDirectoryIn(){
        System.out.println(Controller.getCurrentDirectory());
    }

    public static boolean areFilesEquals(String str1, String str2){
        return str1.equals(str2);
    }
}
