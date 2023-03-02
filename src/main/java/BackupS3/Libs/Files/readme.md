# Simple virtual file

> Author: [Alexey Vlasov](https://github.com/adideas)

## API

| Method         | About                                                |
|----------------|------------------------------------------------------|
| append(String) | Append text to file                                  |
| append(byte[]) | Append bytes to file                                 |
| getPath()      | Get file path. <em>If virtual file return null;</em> |
| remove()       | Remove file                                          |
| isFile()       | Check is virtual file                                |
| virtualLink()  | Get virtual link                                     |

## Temp file
This file simplifies the process of working with a temporary file.

## Make basic class
```java
class MyFile implements IFile {
    // You code
}
```
