import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LogEntry {

    private enum HttpMethod {
        GET, POST, PUT, DELETE, PATCH, OPTIONS, HEAD, TRACE
    }

    String ipAdd; //IP-адресу
    private final LocalDateTime  time ;//дате и времени запроса
    private final HttpMethod httpMethod;//методу запроса
    private final String path;//пути запроса
    private final int responseCode;// коду ответа
    private final Long responseSize;//размеру отданных сервером данных
    private final String refer;//размеру отданных сервером данных
    private String userAgent = "";//а также User-Agent

    public LogEntry(String line)


   {
        String[] parts = line.split(" ");
        this.ipAdd = parts[0];
        String dataTime = parts[3].substring(1);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MMM/yyyy:HH:mm:ss", Locale.ENGLISH);
        this.time = LocalDateTime.parse(dataTime, formatter);

        this.httpMethod = HttpMethod.valueOf(parts[5].replace("\"", ""));
        this.path = parts[6];
        this.responseCode = Integer.parseInt(parts[8]);
        this.responseSize = Long.parseLong(parts[9]);
        this.refer = parts[10].replace("\"", "");

        Pattern pattern = Pattern.compile("\".*?\"\\s*\"(.*?)\"$");
        Matcher matcher = pattern.matcher(line);
        if (matcher.find()) {
            // Группа 1 содержит извлеченную строку User-Agent без кавычек.
            this.userAgent = matcher.group(1);

        }
   }
    public LocalDateTime getTime() {
        return time;
    }
    public String getIpAdd() {
        return ipAdd;
    }
    public HttpMethod getHttpMethod() {
        return httpMethod;
    }
    public String getPath() {
        return path;
    }
    public int getResponseCode() {
        return responseCode;
    }
    public Long getResponseSize() {
        return responseSize;
    }
    public String getRefer() {
        return refer;
    }
    public String getUserAgent() {
        return userAgent;
    }
}
