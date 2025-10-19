import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UserAgent {

    String platform;
    String browser;

    String bot;

    public UserAgent(String line) {


        String[] parts = line.split(" ");
        if (parts.length < 3) {
            throw new MyCustomException("не корректный user agent" + line);
        }

        int str = parts[1].length();
        String part1 = parts[1].substring(1, str - 1);
        if (part1.equals("Windows") || part1.equals("Linux") || part1.equals("macOS ")) {
            this.platform = part1;
        }

        Pattern pattern = Pattern.compile("(Chrome|Firefox|Edge|Opera|Safari|OPR|Chromium|MSIE)\\/(\\S)");
        Matcher matcher = pattern.matcher(line);
        if (matcher.find()) {
            this.browser = matcher.group(1);
        }
         pattern = Pattern.compile("(compatible|bot|Bot)\\/(\\S)");
         matcher = pattern.matcher(line);
        if (matcher.find()) {
            this.bot = matcher.group(1);
        }

    }

    public String getPlatform() {
        return platform;
    }
    public String getBrowser() {
        return browser;
    }
    public String getBot() {
        return bot;
    }

    public Boolean isBot() {
        if (bot!=null){return true;}
        return false;
    }
}
