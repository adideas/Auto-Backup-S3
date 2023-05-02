# Simple logger

> Author: [Alexey Vlasov](https://github.com/adideas)

## API

| Method                           | About                                        |
|----------------------------------|----------------------------------------------|
| read(URL resource)               | Read resource file                           |
| read(URL resource, HashMap data) | Read file as template and replace parameters |

## USE

```java
import java.util.HashMap;

/*
Example you resource
My name is {{NAME}}!
*/
public class Main {
    public static void main(String[] args) {
        Resource.read(Main.class.getResource("YOU_RESOURCE"));
        
        Resource.read(Main.class.getResource("YOU_RESOURCE"), new HashMap<String, String>(){{
            put("{{NAME}}", "YOU OPTION");
        }});
    }
}
```