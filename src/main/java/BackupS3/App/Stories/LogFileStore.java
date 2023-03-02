package BackupS3.App.Stories;

import BackupS3.Libs.Store.FileArray;

import java.io.File;
import java.util.HashSet;
import java.util.function.Consumer;

/**
 * Implementation of the file control service.
 * It keeps track of a certain number of log files.
 *
 * @author Alexey Vlasov <a href="https://github.com/adideas">github</a>
 * @version 1.0
 */
public class LogFileStore extends FileArray<Long> {
    private final Consumer<Long> remove;
    private Integer count = null;
    private Integer index = null;
    private Integer read = 0;
    private Long[] values = null;
    private final HashSet<Long> cache = new HashSet<>();

    /**
     * Autoload file control service.
     *
     * @param file File
     * @param count Max count file
     * @param remove Action remove item
     * @throws Exception If you file not exists
     * @since 1.0
     */
    public LogFileStore(File file, Integer count, Consumer<Long> remove) throws Exception {
        super(Long.class, file);
        this.remove = remove;
        if (!file.exists()) { write(count.longValue()); write(0L); close(); }
        init();
    }

    /**
     * {@inheritDoc}
     *
     * @param value Item from file
     * @since 1.0
     */
    @Override
    protected void read(Long value) {
        if (count == null) {
            count = value.intValue(); values = new Long[count];
        } else if (index == null) { index = value.intValue(); } else { values[read] = value; cache.add(value); read++; }
    }

    /**
     * Update position in file
     * @since 1.0
     */
    private void nextIndex() {
        if (index + 1 >= count) { index = 0; } else { index++; }
        update(1, index.longValue());
    }

    /**
     * Add file hash to last file position
     *
     * @param value Hash file
     * @throws Exception If you file not exists
     * @since 1.0
     */
    public void add(Long value) throws Exception {
        if (cache.contains(value)) { return; }

        if (values[index] == null) { write(value); cache.add(value);}
        else {
            cache.remove(values[index]); this.remove.accept(values[index]); update(index + 2, value); cache.add(value);
        }

        values[index] = value;
        nextIndex();
    }

    /**
     * Only for test
     *
     * @return return all items
     * @since 1.0
     */
    public Long[] getValues() {
        return values;
    }
}
