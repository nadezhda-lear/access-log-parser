import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;
import java.io.BufferedReader;
import java.io.FileReader;

//C:\Users\nvasileva\Downloads\access.log
public class Main {
    public static void main(String[] args) {
        int total = 0;
        int i = 1;
        int maxLineLength = 1024;

        while (true) {
            System.out.println("Введите путь к файлу  и нажмите <Enter>: ");
            String path = new Scanner(System.in).nextLine();
            File file = new File(path);
            boolean fileExists = file.exists();
            boolean isDirectory = file.isDirectory();

            if (fileExists != true || isDirectory != false) {
                System.out.println("Путь указан Не верно. ");
                continue;
            } else {
                System.out.println("Путь указан верно. Это файл номер " + i);
                i = i + 1;
            }
            try {
                FileReader fileReader = new FileReader(path);
                BufferedReader reader = new BufferedReader(fileReader);
                String line;
                Statistics s = new Statistics();
                while ((line = reader.readLine()) != null) {
                    if (line.length() > maxLineLength) {
                        throw new MyCustomException("В файле обнаружена строка длиннее " + maxLineLength + " символов: " + line.substring(0, 100) + "...");
                    }

                    total = total + 1;
                    LogEntry log = new LogEntry(line);

                    s.addEntry(log);
                }
                System.out.println("Средний объем трафика в час: " + s.getTrafficRate());
               System.out.println("Все пути сайта "+s.allSitePaths());
                System.out.println("Все некорректные пути сайта "+s.allIncorrectSitePaths());
                System.out.println("Доля каждой операционной системы "+s.shareOfEachOperatingPlatform());
                System.out.println("Доля каждого браузера "+s.shareOfEachOperatingBrouser());
            } catch (Exception ex) {
                ex.printStackTrace();
            }


        }
    }
}
