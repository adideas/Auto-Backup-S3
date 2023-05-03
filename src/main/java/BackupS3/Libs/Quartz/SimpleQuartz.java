package BackupS3.Libs.Quartz;

import java.util.Calendar;

/**
 * This class allows you to check the cron expression
 * (whether the expression matches the passed date)
 *
 * @author Alexey Vlasov <a href="https://github.com/adideas">github</a>
 * @version 1.0
 * @since 1.1
 */
@SuppressWarnings("SameParameterValue")
public class SimpleQuartz {
    /**
     * [* * * * *] -> [MINUTE, HOUR_OF_DAY, .etc]
     */
    private final int[] calendarOctet = new int[]{
            Calendar.MINUTE, Calendar.HOUR_OF_DAY, Calendar.DAY_OF_MONTH, Calendar.MONTH, Calendar.DAY_OF_WEEK
    };
    private final Calendar calendar;
    public SimpleQuartz(Calendar calendar) { this.calendar = calendar; }

    /**
     * Start check cron expression
     * @param expression Cron expression
     * @since 1.1
     */
    public boolean validate(String expression) {
        String[] config = expression.split(" ");

        if (config.length != 5) { return false; }

        for (int octet = 0; octet < 5; octet++) {
            if (!validate(config[octet], octet)) {
                return false;
            }
        }

        return true;
    }

    /**
     * Validate expression
     * @param expression Cron expression
     * @param octet index of cron expression
     * @since 1.1
     */
    private boolean validate(String expression, int octet) {
        if (expression.length() < 1) {
            return false;
        }

        char[] exp = expression.toCharArray();

        if (exp.length == 1 && exp[0] == '*') {
            return true;
        }

        if (exp.length <= 2) {
            return isNumber(exp) && toNumber(exp) == getValueCalendar(octet);
        }

        // - operator
        if (exp.length <= 5 && (exp[1] == '-' || exp[2] == '-')) {
            String[] splitExp = expression.split("-");
            int[] values = new int[]{Integer.parseInt(splitExp[0]), Integer.parseInt(splitExp[1])};
            int value = getValueCalendar(octet);
            return values[0] <= value && value <= values[1];
        }

        return false;
    }

    /**
     * Get values from calendar
     * @param octet index cron expression
     * @since 1.1
     */
    @SuppressWarnings("MagicConstant")
    private int getValueCalendar(int octet) {
        int value = calendar.get(calendarOctet[octet]);
        return octet == 3 ? value + 1 : value; // Month + 1
    }

    /**
     * Convert to number
     * @param symbol char
     * @return Convert char to number
     * @since 1.1
     */
    private static int toNumber(char symbol) {
        return symbol - '0';
    }

    /**
     * Convert to number
     * @param symbols char array
     * @return Convert char array to number
     * @since 1.1
     */
    private static int toNumber(char[] symbols) {
        return toNumber(symbols, 0, symbols.length);
    }

    /**
     * Convert to number
     * @param symbols char array
     * @param start index from
     * @param end index to
     * @return Convert char array to number
     * @since 1.1
     */
    private static int toNumber(char[] symbols, int start, int end) {
        int value = 0;
        for(;start < end && end <= symbols.length; start++) {
            value = (value * 10) + toNumber(symbols[start]);
        }
        return value;
    }

    /**
     * Check is number
     * @param symbol char
     * @return If this char is number
     * @since 1.1
     */
    private static boolean isNumber(char symbol) {
        return symbol >= '0' && symbol <= '9';
    }

    /**
     * Check is number
     * @param symbols char array
     * @return If this char array is number
     * @since 1.1
     */
    private static boolean isNumber(char[] symbols) {
        return isNumber(symbols, 0, symbols.length);
    }

    /**
     * Check is number
     * @param symbols char array
     * @param start index from
     * @param end index to
     * @return If this char array is number
     * @since 1.1
     */
    private static boolean isNumber(char[] symbols, int start, int end) {
        for(;start < end && end <= symbols.length; start++) {
            if (!isNumber(symbols[start])) { return false; }
        }
        return true;
    }
}