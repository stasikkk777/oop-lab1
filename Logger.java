import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.EnumMap;
import java.util.Map;
import java.util.Random;

public class Logger {
    // shared by all loggers
    private static LogLevel level = LogLevel.INFO;
    private static String format = "[%t] [%l] %n: %m";

    private static final DateTimeFormatter TIME = DateTimeFormatter.ofPattern("HH:mm:ss");
    private static final Random RANDOM = new Random();
    private static final Map<LogLevel, String[]> RANDOM_MESSAGES = new EnumMap<>(LogLevel.class);

    static {
        RANDOM_MESSAGES.put(LogLevel.TRACE,
                new String[] {"Stepping through the input", "Still tracing", "Checking next character"});
        RANDOM_MESSAGES.put(LogLevel.INFO,
                new String[] {"Operation completed", "Everything looks good", "Here is your result"});
        RANDOM_MESSAGES.put(LogLevel.WARN,
                new String[] {"Something looks odd", "Proceed with caution", "Unexpected, but not fatal"});
        RANDOM_MESSAGES.put(LogLevel.ERROR,
                new String[] {"Operation failed", "Something went wrong", "Invalid input detected"});
    }

    // belongs to each logger object
    private final String name;

    public Logger(String name) {
        this.name = name;
    }

    public static void setLevel(LogLevel newLevel) {
        level = newLevel;
    }

    public static void setFormat(String newFormat) {
        format = newFormat;
    }

    public void log(LogLevel msgLevel, String message) {
        if (msgLevel.compareTo(level) < 0) {
            return;
        }
        System.out.println(formatMessage(msgLevel, message));
    }

    public void trace(String message) {
        log(LogLevel.TRACE, message);
    }

    public void info(String message) {
        log(LogLevel.INFO, message);
    }

    public void warn(String message) {
        log(LogLevel.WARN, message);
    }

    public void error(String message) {
        log(LogLevel.ERROR, message);
    }

    private String formatMessage(LogLevel msgLevel, String message) {
        StringBuilder result = new StringBuilder();
        // one pass, so a "%n" inside the message text is not replaced again
        for (int i = 0; i < format.length(); i++) {
            char c = format.charAt(i);
            if (c == '%' && i + 1 < format.length()) {
                char next = format.charAt(i + 1);
                String value = valueFor(next, msgLevel, message);
                if (value != null) {
                    result.append(value);
                    i++;
                    continue;
                }
            }
            result.append(c);
        }
        return result.toString();
    }

    private String valueFor(char placeholder, LogLevel msgLevel, String message) {
        switch (placeholder) {
            case 't': return LocalTime.now().format(TIME);
            case 'm': return message;
            case 'n': return name;
            case 'l': return msgLevel.name();
            case 'r': return pickRandom(msgLevel);
            default: return null;
        }
    }

    private static String pickRandom(LogLevel msgLevel) {
        String[] options = RANDOM_MESSAGES.get(msgLevel);
        return options[RANDOM.nextInt(options.length)];
    }
}
