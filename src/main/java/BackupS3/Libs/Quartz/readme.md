# Archiver

> Author: [Alexey Vlasov](https://github.com/adideas)

## API

| Method                | About                              |
|-----------------------|------------------------------------|
| validate("* * * * *") | Check cron expression in date time |

## USE

```java
import java.util.Calendar;

public class Main {
    public static void main(String[] args) {
        new SimpleQuartz(Calendar.getInstance()).validate("* * * * *");
    }
}
```
