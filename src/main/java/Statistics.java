import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;


public class Statistics {
    private long totalTraffic;
    private long errorCount;
    private long totalLine;
    String ipAdd;
    private LocalDateTime minTime;
    private LocalDateTime maxTime;
    private String sec;
    private HashSet<String> hashSet;
    static HashSet<String> allSitePaths = new HashSet<>();
    static HashSet<String> allIncorrectSitePaths = new HashSet<>();
    private HashSet<String> hashRever = new HashSet<>();
    private HashMap<String, Integer> hshSetBrouser = new HashMap<>();
    private HashMap<String, Integer> hshSetPlatform = new HashMap<>();
    private HashMap<String, Integer> hshSetUsers = new HashMap<>();
    private HashMap<String, Integer> hshSetSecond = new HashMap<>();

    private HashMap<String, Integer> hshSetBot = new HashMap<>();
    private String path;
    private Long responseSize;
    private String userAgent;
    private String refer;
    private String browser;
    String platform;
    Boolean bot;
    double totalB = 0;
    double totalP = 0;
    double totalBt = 0;

    public Statistics() {
        this.totalTraffic = 0;
        this.totalLine = 0;
        this.errorCount = 0;
        this.sec = null;
        this.minTime = null;
        this.maxTime = null;
        this.responseSize = 0L;
        this.path = "";
        this.userAgent = "";
        this.browser = "";
        this.bot = false;
        this.ipAdd = "";
        this.refer = "";

    }

    public void addEntry(LogEntry logEntry) {
        totalTraffic += logEntry.getResponseSize();
        totalLine += 1;

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

        if (logEntry.getResponseCode() == 404) {
            path = logEntry.getPath();
            allIncorrectSitePaths.add(String.valueOf(path));
        }

        if (logEntry.getResponseCode() >= 400 && logEntry.getResponseCode() <= 499) {
            errorCount++;
        } else if (logEntry.getResponseCode() >= 500 && logEntry.getResponseCode() <= 599) {
            errorCount++;
        }

        //Метод, возвращающий список сайтов, со страниц которых есть ссылки на текущий сайт hashRever
        refer = logEntry.getRefer();
        if (refer != null && !refer.equals("-")) {
            //надо найти домен
            if (!refer.contains("%3A%2F%2F")) {
                String domen = "";
                String[] parts = refer.split("/");
                domen = parts[2];
                if (!hashRever.contains(domen)) {
                    hashRever.add(domen);
                }
            } else {
                String domen = "";
                String firstBrackets = "";
                int openParenIndex = refer.indexOf('F');
                int closeParenIndex = refer.indexOf('&');
                if (openParenIndex != -1 && closeParenIndex != -1) {
                    domen = refer.substring(openParenIndex + 1, closeParenIndex);
                    ///  String[] parts = firstBrackets.split(";");
                    // domen = parts[2];
                    if (!hashRever.contains(domen)) {
                        hashRever.add(domen);
                    }
                }
            }
        }



        if (logEntry.getUserAgent().split(" ").length > 3) {
            UserAgent userAgent = new UserAgent(logEntry.getUserAgent());
            browser = userAgent.getBrowser();
            platform = userAgent.getPlatform();
            bot = userAgent.isBot();
            if (browser != null) {
                totalB += 1;
                if (hshSetBrouser.containsKey(browser)) {
                    int k = hshSetBrouser.get(browser);
                    hshSetBrouser.put(browser, k += 1);
                } else {
                    hshSetBrouser.put(browser, 1);
                }
            }
            if (platform != null) {
                totalP += 1;
                if (hshSetPlatform.containsKey(platform)) {
                    int l = hshSetPlatform.get(platform);
                    hshSetPlatform.put(platform, l += 1);
                } else {
                    hshSetPlatform.put(platform, 1);
                }
            }
            if (bot) {
                totalBt += 1;
            } else {
                //обираем статистику по уникальному пользователю
                ipAdd = logEntry.getIpAdd();
                if (ipAdd != null) {
                    if (hshSetUsers.containsKey(ipAdd)) {
                        int l = hshSetUsers.get(ipAdd);
                        hshSetUsers.put(ipAdd, l += 1);
                    } else {
                        hshSetUsers.put(ipAdd, 1);
                    }
                }
                //фиксировать количество посещений за одну каждую секунду
                sec = String.valueOf(logEntry.getTime());
                if (hshSetSecond.containsKey(sec)) {
                    int li = hshSetSecond.get(sec);
                    hshSetSecond.put(sec, li += 1);
                } else {
                    hshSetSecond.put(sec, 1);
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

    public HashSet<String> allIncorrectSitePaths() {
        return allIncorrectSitePaths;
    }

    //Вывод каждого браузера из лога и ее количество
    public HashMap<String, Integer> hshSetBrouser() {
        return hshSetBrouser;
    }

    //Вывод каждой операционной системы из лога и ее количество
    public HashMap<String, Integer> hshSetPlatform() {
        return hshSetPlatform;
    }

    //Вывод уникальных пользователей
    public HashMap<String, Integer> hshSetUsers() {
        return hshSetUsers;
    }

    public HashMap<String, Integer> hshSetSecond() {
        return hshSetSecond;
    }

    //расчет доли каждого браузера
    public HashMap<String, Double> shareOfEachOperatingBrouser() {
        HashMap<String, Double> shareOfEachOperatingBrouser = new HashMap<>();
        ;
        for (Map.Entry<String, Integer> entry : hshSetBrouser.entrySet()) {
            String key = entry.getKey();
            Integer value = entry.getValue();
            double v = (double) value;
            Double quantityBroser = (v / totalB);
            shareOfEachOperatingBrouser.put(key, quantityBroser);
        }
        return shareOfEachOperatingBrouser;
    }

    //расчет доли каждой операционной системы
    public HashMap<String, Double> shareOfEachOperatingPlatform() {
        HashMap<String, Double> shareOfEachOperatingPlatform = new HashMap<>();
        ;
        for (Map.Entry<String, Integer> entry : hshSetPlatform.entrySet()) {
            String key = entry.getKey();
            Integer value = entry.getValue();
            double v = (double) value;
            Double quantityBroser = (v / totalP);
            shareOfEachOperatingPlatform.put(key, quantityBroser);
        }
        return shareOfEachOperatingPlatform;
    }

    // вывод ботов
    public double TotalBots() {
        return totalB;
    }

    //Метод подсчёта среднего количества посещений сайта за час..
    public double averageNumberOfWebsiteVisitsPerHour() {
        if (minTime == null || maxTime == null) {
            return 0.0;
        }
        Duration duration = Duration.between(minTime, maxTime);
        long hours = duration.toHours();

        if (hours == 0) {
            return 0.0;
        }
        return (double) totalLine - totalB / hours;
    }

    public double averageErrorsOfWebsiteVisitsPerHour() {
        if (minTime == null || maxTime == null) {
            return 0.0;
        }
        Duration duration = Duration.between(minTime, maxTime);
        long hours = duration.toHours();

        if (hours == 0) {
            return 0.0;
        }
        return (double) errorCount / hours;
    }

    public double calculatingAverageTrafficPerUser() {
//всего запросов от пользователей
        long totalUsers = hshSetUsers.values().stream()
                .mapToInt(Integer::intValue)
                .sum();
//количество пользователей
        int totalUnicUser = 0;
        for (Integer score : hshSetUsers.values()) {
            totalUnicUser += 1;
        }
        return (double) totalUnicUser / totalUsers;
    }


    public int calculatingMaxTrafficPerUser(){
        Integer maxPerUser =  Collections.max(hshSetUsers.values());
        return maxPerUser;
    }
    public String getIpAdd() {
        return ipAdd;
    }

    public int calculatingPeakWebsiteTrafficPerSecond(){
        Integer maxValue =  Collections.max(hshSetSecond.values());
        return  maxValue;}


    public HashSet<String> getHashRever() {
        return hashRever;
    }
}
