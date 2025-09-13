import java.io.File;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        int i = 1;
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
                i = i+1;
            }
        }
    }

}
