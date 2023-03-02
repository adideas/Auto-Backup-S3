package BackupS3.Libs.Store;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Type;
import java.util.HashMap;

/**
 * This class implements a file archive.
 *
 * @param <T>
 * @author Alexey Vlasov <a href="https://github.com/adideas">github</a>
 * @version 1.0
 */
public abstract class FileArray<T extends Number> {
    @SuppressWarnings("FieldCanBeLocal")
    private final HashMap<Type, TypeOption<T>> methods = new HashMap<>(){{
        put(Integer.class, new TypeOption<>("writeInt", "readInt", int.class, Integer.BYTES));
        put(Long.class, new TypeOption<>("writeLong", "readLong", long.class, Long.BYTES));
        put(Float.class, new TypeOption<>("writeFloat", "readFloat", float.class, Float.BYTES));
        put(Double.class, new TypeOption<>("writeDouble", "readDouble", double.class, Double.BYTES));
    }};
    private final TypeOption<T> method;
    private final File file;
    private RandomAccessFile stream = null;
    protected boolean init;

    public FileArray(Class<T> clazz, File file) throws NoSuchMethodException {
        this.method = this.methods.get(clazz);
        this.file = file;
    }

    /**
     * Get file stream for later reading
     *
     * @return Stream file
     * @throws IOException If you file not exists
     * @since 1.0
     */
    private RandomAccessFile getStream() throws IOException {
        if (stream == null) {
            stream = new RandomAccessFile(file, "rw");
        }
        return stream;
    }

    /**
     * Method append item to file
     *
     * @param value Item to file
     * @since 1.0
     */
    protected void write(T value) {
        try {
            getStream().seek(getStream().length());
            this.method.invoke(getStream(), value);
        } catch (InvocationTargetException | IllegalAccessException | IOException ignore) {}
    }

    /**
     * Method update item from file
     *
     * @param index Index item file (index * bytes type)
     * @param value Value(Type)
     * @since 1.0
     */
    @SuppressWarnings("SameParameterValue")
    protected void update(long index, T value) {
        try {
            RandomAccessFile stream = getStream();
            long length = stream.length();
            long typeSize = this.method.getSize();

            if (index * typeSize < length) {
                stream.seek(index * typeSize);
                this.method.invoke(stream, value);
                stream.seek(length);
            }
        } catch (IOException | InvocationTargetException | IllegalAccessException ignore) {}
    }

    /**
     * Method read item from file
     *
     * @param value Item from file
     * @since 1.0
     */
    protected abstract void read(T value);

    /**
     * Loading data from a file (Init file)
     *
     * @since 1.0
     */
    public void init() {
        try {
            RandomAccessFile stream = getStream();

            for(long i = 0; i < stream.length() / this.method.getSize(); i++) {
                read(this.method.invoke(getStream()));
            }
            init = true;
        } catch (InvocationTargetException | IllegalAccessException | IOException ignore) {}
    }

    /**
     * Close the file stream and save the file
     *
     * @throws IOException If you file not exist
     * @since 1.0
     */
    public void close() throws IOException {
        getStream().close();
        stream = null;
    }
}