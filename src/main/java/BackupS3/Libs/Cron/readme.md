# Simple cron

This package is not a complete CRON instance. It implements the basic functionality of CRON.

> Author: [Alexey Vlasov](https://github.com/adideas)

# API

## Make basic class
```java
public class Cron extends Scheduler {
    @Override
    public int getMaxThread() {
        return 1; // 1 thread
    }

    @Override
    public long periodInSeconds() {
        return 1L; // 1 min
    }

    @Override
    protected void handle() {
        // Your logic
    }

    public static void start() {
        Cron cron = new Cron();

        Executors.newSingleThreadScheduledExecutor()
                .scheduleAtFixedRate(
                        cron,
                        100,
                        periodInSeconds() * 60000, // 1 min
                        TimeUnit.MILLISECONDS
                );
    }
}
```

## Start demon
```java
public class Main {
    /** 
     * Run demon
     */
    public static void main(String[] args) {
        Cron.start();
    }
}
```

## Basic methods
```java
public class Cron extends Scheduler {
    @Override
    protected void handle() {
        this.everyTick((service) -> {});
        
        int hour = 12;
        int minute = 0;
        this.daily(hour, minute, (service) -> {});
    }
}
```