package BackupS3.App.Stories;

import BackupS3.Libs.Store.FileArray;

import java.io.File;
import java.util.HashSet;

/**
 * This class implements a file archive. Stores the hash of the sum of the files.
 *
 * @author Alexey Vlasov <a href="https://github.com/adideas">github</a>
 * @version 1.0
 */
public class IndexFileStore extends FileArray<Long> {
    private final static HashSet<Long> cache = new HashSet<>();

    /**
     * This class implements a file archive. Stores the hash of the sum of the files.
     * @param file File
     * @since 1.0
     */
    public IndexFileStore(File file) throws NoSuchMethodException { super(Long.class, file); }

    /**
     * {@inheritDoc}
     * @param value Item from file
     */
    @Override
    protected void read(Long value) { cache.add(value); }

    /**
     * Method for checking that a file has been processed
     *
     * @param TRUE_is_check_exist_FALSE_add Check if no exists | add
     * @param hash hash sum
     *
     * @apiNote
     * TRUE_is_check_exist_FALSE_add = 1 (true), 2 (false)
     *  <li>1) {true - exists, false - no exists} </li>
     *  <li>2) only add hash </li>
     *
     * @return bool or null
     * @since 1.0
     */
    public Boolean existOrAdd(Boolean TRUE_is_check_exist_FALSE_add, Long hash) {
        if (!init) { init(); }
        if (TRUE_is_check_exist_FALSE_add) {
            return cache.contains(hash);
        } else {
            write(hash);
            cache.add(hash);
        }
        return null;
    }
}
