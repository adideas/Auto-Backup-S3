# File array

This package allows you to store an array in a file. This is convenient when you need to store the same type of information.

> Author: [Alexey Vlasov](https://github.com/adideas)

## Use

```java
public class ArrayFile extends FileArray<Long> {
    public IndexFileStore(File file) {
        super(Long.class, file);
    }

    @Override
    protected void read(Long value) {
        // Method read item from file
    }
    
    // You logic
}
```