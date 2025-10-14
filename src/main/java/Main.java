import java.io.File;
import java.util.Scanner;
import java.io.BufferedReader;
import java.io.FileReader;

//C:\Users\nvasileva\Downloads\access.log
public class Main {
    public static void main(String[] args) {
        int i = 1;
        int maxLineLength = 1024;
        String yandexText = "YandexBot";
        String googleText = "Googlebot";
        while (true) {
            System.out.println("Введите путь к файлу  и нажмите <Enter>: ");
            String path = new Scanner(System.in).nextLine();
            File file = new File(path);
            boolean fileExists = file.exists();
            boolean isDirectory = file.isDirectory();
            float yandexCount = 0;
            float googleCount = 0;
            float totalCount = 0;
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
                    totalCount+=1;
                    int openParenIndex = line.indexOf('(');
                    int closeParenIndex = line.indexOf(')');
                    String firstBrackets = "";
                    String fragment = "";
                    String searchFragment = "";
                    if (closeParenIndex < openParenIndex) {
                        line = line.substring(closeParenIndex+1);
                    }
                     openParenIndex = line.indexOf('(');
                     closeParenIndex = line.indexOf(')');
                    if (openParenIndex != -1 && closeParenIndex != -1) {
                        firstBrackets = line.substring(openParenIndex + 1, closeParenIndex);
                        String[] parts = firstBrackets.split(";");
                        if (parts.length >= 2) {
                            fragment = parts[1];
                        }
                        int slashIndex = fragment.indexOf('/');
                        if (slashIndex != -1) {
                            searchFragment = fragment.substring(0, slashIndex);
                            if (yandexText.equals(searchFragment.replaceAll("\\s", ""))) yandexCount += 1;
                            if (googleText.equals(searchFragment.replaceAll("\\s", ""))) googleCount += 1;
                        }
                    }
              }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            float yandexCountTotal = yandexCount/totalCount*100;
            float googleCountTotal = googleCount/totalCount*100;
            System.out.println("Доля запроcов от YandexBot " +yandexCountTotal);
            System.out.println("Доля запроcов от Googlebot " +googleCountTotal);
        }
    }
}
