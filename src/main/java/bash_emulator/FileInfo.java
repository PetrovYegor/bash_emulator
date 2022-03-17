package bash_emulator;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

public class FileInfo {


    //перечисление, которое может указывать на тип файла или тип директории
    public enum FileType {
        FILE("F"), DIRECTORY("D");

        private String name;

        public String getName(){
            return name;
        }

        FileType(String name){
            this.name = name;
        }
    }
    //тут описываем, что мы вообще хотим знать о файле

    private String fileName;
    private FileType type;
    private long size;
    private LocalDateTime lastModified;

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String filename) {
        this.fileName = filename;
    }

    public FileType getType() {
        return type;
    }

    public void setType(FileType type) {
        this.type = type;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public LocalDateTime getLastModified() {
        return lastModified;
    }

    public void setLastModified(LocalDateTime lastModified) {
        this.lastModified = lastModified;
    }

    @Override
    public String toString() {
        return "FileInfo{" +
                "filename='" + fileName + '\'' +
                ", type=" + type +
                ", size=" + size +
                ", lastModified=" + lastModified +
                '}';
    }

    //FileInfo можно строить из пути к файлам
    //когда задаём FileInfo, мы указываем путь к файлу на диске
    //и по этому пути у нас должен быть создан объект FileInfo
    public FileInfo(Path path){ //Наша задача здесь - это по указанному пути построить FileInfo. А FileInfo мы уже будет отображать непосредственно в консли. Это обёртка для данных о файле
        //мы можем указать любой путь: диск С, папка один, файл такой-то, но имя самого файла - это, например 1.txt. Мы из пути выдёргиваем имя самого файла и преобразовываем к строке
        try {
            this.fileName = path.getFileName().toString();
            this.size = Files.size(path);//отдаём путь к файлу, получаем размер в байтах. Может бросить эксепшн checked, обработаем его так, что должен выбраситься рантайм эксепшн (доработать, чтобы человекочитаемая ошибка была)
            //система не понимает, что есть директория, а что файл, в системе и файлы и директории - это всё файлы. Поэтоиу с помощью FileInfo можно указывать как на файл, так и на директорию. Нужно эти вещи разделять
            this.type = Files.isDirectory(path) ? FileType.DIRECTORY : FileType.FILE;
            if (this.type == FileType.DIRECTORY){//это пригодится при сортировке файлов в таблице
                this.size = -1;
            }                                 //запрашиваем время последней модификации файла по указанному пути, потом преобразуем это в Instant, а з него уже получаем LocalDateTime
            this.lastModified = LocalDateTime.ofInstant(Files.getLastModifiedTime(path).toInstant(), ZoneOffset.ofHours(0));
        } catch (IOException e) {
            throw new RuntimeException("Unable to create file info from path");//если из файла не удалось создать файл инфо, это битый объект, остановим создание этого объекта и программа завершится (нечего работать с кривыми файлами). Мы не обрабатываем IO исключение, т.к. его надо будет сверху обрабатывать, поэтому кидаем uncheked
        }



    }
}
