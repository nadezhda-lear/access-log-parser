import java.time.LocalDateTime;
import java.time.Duration;
public class Statistics {
    private long totalTraffic;
    private LocalDateTime minTime;
    private LocalDateTime maxTime;

    public Statistics() {
        this.totalTraffic = 0;
        this.minTime = null;
        this.maxTime = null;
    }

    public void addEntry(LogEntry logEntry) {
        totalTraffic += logEntry.getResponseSize();

        if (minTime == null || logEntry.getTime().isBefore(minTime)) {
            minTime = logEntry.getTime();
        }

        if (maxTime == null || logEntry.getTime().isAfter(maxTime)) {
            maxTime = logEntry.getTime();
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

}