# Simple logger

> Author: [Alexey Vlasov](https://github.com/adideas)

## [RFC-5424](https://www.rfc-editor.org/rfc/rfc5424)

This document describes the syslog protocol, which is used to convey
event notification messages.  This protocol utilizes a layered
architecture, which allows the use of any number of transport
protocols for transmission of syslog messages.  It also provides a
message format that allows vendor-specific extensions to be provided
in a structured way.

## Syslog Message Severities

| Numerical Code | Severity                                 | Enum     |
|----------------|------------------------------------------|----------|
| 0              | Emergency: system is unusable            | -        |
| 1              | Alert: action must be taken immediately  | ALERT    |
| 2              | Critical: critical conditions            | CRITICAL |
| 3              | Error: error conditions                  | ERROR    |
| 4              | Warning: warning conditions              | WARN     |
| 5              | Notice: normal but significant condition | NOTICE   |
| 6              | Informational: informational messages    | INFO     |
| 7              | Debug: debug-level messages              | DEBUG    |

## Example error message (CSV format)
| TIMESTAMP           | HOSTNAME | SEVERITY | MESSAGE |
|---------------------|----------|----------|---------|
| 2022-01-01 00:00:00 | MAIN     | -        | ???     |
| 2022-01-01 00:00:00 | MAIN     | ALERT    | ???     |
| 2022-01-01 00:00:00 | CRON     | CRITICAL | ???     |
| 2022-01-01 00:00:00 | MYSQL    | ERROR    | ???     |
| 2022-01-01 00:00:00 | FILES    | WARN     | ???     |
| 2022-01-01 00:00:00 | FILES    | NOTICE   | ???     |
| 2022-01-01 00:00:00 | FILES    | INFO     | ???     |
| 2022-01-01 00:00:00 | FILES    | DEBUG    | ???     |

## Raw csv
```csv
; 2022-01-01 00:00:00 ; MAIN     ; -        ; ???     ;\n
; 2022-01-01 00:00:00 ; CRON     ; CRITICAL ; ???     ;\n
```

## Make basic class

```java
public class MyLog extends Logger {
    public static void info(String msg) {
        String log = Logger.getLine(Column.HOSTNAME.MYSQL, Column.LEVEL.INFO, msg);
        // Send log to file or network
    }
}
```