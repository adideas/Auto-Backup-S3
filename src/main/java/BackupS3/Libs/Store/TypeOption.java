package BackupS3.Libs.Store;

import java.io.RandomAccessFile;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class TypeOption<T> {
    private final Method[] function = new Method[2];
    private final int size;

    public TypeOption(String write, String read, Class<?> writeType, int size) throws NoSuchMethodException {
        function[0] = RandomAccessFile.class.getDeclaredMethod(write, writeType);
        function[1] = RandomAccessFile.class.getDeclaredMethod(read);
        this.size = size;
    }

    public T invoke(RandomAccessFile object) throws InvocationTargetException, IllegalAccessException {
        //noinspection unchecked
        return (T) function[1].invoke(object);
    }

    public void invoke(RandomAccessFile object, T value) throws InvocationTargetException, IllegalAccessException {
        function[0].invoke(object, value);
    }

    public int getSize() {
        return this.size;
    }
}
