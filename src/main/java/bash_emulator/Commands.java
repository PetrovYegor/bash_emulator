package bash_emulator;

public enum Commands {
     cd("копирвание файлов"),ls("содержимое текущего каталога"),mkdir("создание директории"), touch("Создание файла с указанным именем или его обновление"), cat("Чтение файла и вывод содержания"), tail("Вывод последних 10 строк"),cp("Копирование файла или директории"),exit("Выход из программы");
    private String description;

    Commands(String description){
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
