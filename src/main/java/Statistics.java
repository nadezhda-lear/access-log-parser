import java.time.LocalDateTime;
import java.time.Duration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;


public class Statistics {
    private long totalTraffic;
    private LocalDateTime minTime;
    private LocalDateTime maxTime;
    private HashSet<String> hashSet;
    static HashSet<String> allSitePaths = new HashSet<>();

    private HashSet<String> hashBrouser = new HashSet<>();
    private HashMap<String, Integer> hshSetSystem = new HashMap<>();
    private String path;
    private Long responseSize;
    private String userAgent;

    private String browser;
    double total = 0;


    public Statistics() {
        this.totalTraffic = 0;
        this.minTime = null;
        this.maxTime = null;
        this.responseSize = 0L;
        this.path = "";
        this.userAgent = "";
        this.browser = "";

    }

    public void addEntry(LogEntry logEntry) {
        totalTraffic += logEntry.getResponseSize();

        if (minTime == null || logEntry.getTime().isBefore(minTime)) {
            minTime = logEntry.getTime();
        }

        if (maxTime == null || logEntry.getTime().isAfter(maxTime)) {
            maxTime = logEntry.getTime();
        }
        if (logEntry.getResponseCode() == 200) {
            path = logEntry.getPath();
            allSitePaths.add(String.valueOf(path));
        }
        if (logEntry.getUserAgent().split(" ").length > 3) {
            UserAgent uBroweser = new UserAgent(logEntry.getUserAgent());
            browser = uBroweser.getBrowser();
            if (browser != null) {
                total += 1;
                if (hshSetSystem.containsKey(browser)) {
                    int k = hshSetSystem.get(browser);
                    hshSetSystem.put(browser, k += 1);
                } else {
                    hshSetSystem.put(browser, 1);
                }
            }

        }
    }

    public double getTrafficRate() {
        if (minTime == null || maxTime == null) {
            return 0.0;
        }

        Duration duration = Duration.between(minTime, maxTime);
        long hours = duration.toHours();

        if (hours == 0) {
            return 0.0;
        }

        return (double) totalTraffic / hours;
    }

    //вывод всех путей в файле
    public HashSet<String> allSitePaths() {
        return allSitePaths;
    }

    // вывод каждого браузера из лога
    public HashSet<String> hashBrouser() {
        return hashBrouser;
    }

    //Вывод каждой операционной системы из лога и ее количество
    public HashMap<String, Integer> hshSetSystem() {
        return hshSetSystem;
    }

    //расчет доли каждой операционной системы
    public HashMap<String, Double> shareOfEachOperatingSystem() {
        HashMap<String, Double> shareOfEachOperatingSystem = new HashMap<>();
        ;
        for (Map.Entry<String, Integer> entry : hshSetSystem.entrySet()) {
            String key = entry.getKey();
            Integer value = entry.getValue();
            double v = (double) value;
            Double quantityBroser = (v / total);
            shareOfEachOperatingSystem.put(key, quantityBroser);
        }
        return shareOfEachOperatingSystem;
    }

}
