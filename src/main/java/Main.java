import java.io.File;
import java.util.Scanner;
import java.io.BufferedReader;
import java.io.FileReader;

//C:\Users\nvasileva\Downloads\access.log
public class Main {
    public static void main(String[] args) {
        int i = 1;
        int maxLineLength = 1024;

        while (true) {
            int totalLength = 0;
            int minLength = maxLineLength;
            int maxLength = 0;
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
                while ((line = reader.readLine()) != null) {
                    if (line.length() > maxLineLength) {
                        throw new MyCustomException("В файле обнаружена строка длиннее " + maxLineLength + " символов: " + line.substring(0, 100) + "...");
                    }
                    int length = line.length();
                    totalLength+=1;
                    if (length <minLength) minLength =  length;
                    if (length >maxLength) maxLength =  length;
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            System.out.println("Общее количество строк в файле: "+totalLength);
            System.out.println("Длина самой длинной строки в файле: "+minLength);
            System.out.println("Длина самой короткой строки в файле: "+maxLength);

        }

    }

}
