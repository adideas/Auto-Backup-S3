package BackupS3.Libs.Gzip;

import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;

public class TreeFiles extends SimpleFileVisitor<Path> {

    final TarArchiveOutputStream stream;
    final Path source;

    TreeFiles(TarArchiveOutputStream stream, Path source) {
        super();
        this.stream = stream;
        this.source = source;
    }

    @Override
    public FileVisitResult visitFile(Path file,BasicFileAttributes attributes) throws IOException {

        // only copy files, no symbolic links
        if (attributes.isSymbolicLink()) {
            return FileVisitResult.CONTINUE;
        }

        // get filename
        Path targetFile = source.relativize(file);

        TarArchiveEntry tarEntry = new TarArchiveEntry(file.toFile(), targetFile.toString());

        stream.putArchiveEntry(tarEntry);

        Files.copy(file, stream);

        stream.closeArchiveEntry();

        return FileVisitResult.CONTINUE;
    }

    @Override
    public FileVisitResult visitFileFailed(Path file, IOException exc) {
        System.err.printf("Unable to tar.gz : %s%n%s%n", file, exc);
        return FileVisitResult.CONTINUE;
    }
}

/*
Files.walkFileTree(source, new SimpleFileVisitor<Path>()  {
    @Override
    public FileVisitResult visitFile(Path file,BasicFileAttributes attributes) {
        // only copy files, no symbolic links
        if (attributes.isSymbolicLink()) {
            return FileVisitResult.CONTINUE;
        }
        // get filename
        Path targetFile = source.relativize(file);
        try {
            TarArchiveEntry tarEntry = new TarArchiveEntry(file.toFile(), targetFile.toString());
            tarArchiveOutputStream.putArchiveEntry(tarEntry);
            Files.copy(file, tarArchiveOutputStream);
            tarArchiveOutputStream.closeArchiveEntry();
            System.out.printf("file : %s%n", file);
        } catch (IOException e) {
            System.err.printf("Unable to tar.gz : %s%n%s%n", file, e);
        }
        return FileVisitResult.CONTINUE;
    }
    @Override
    public FileVisitResult visitFileFailed(Path file, IOException exc) {
        System.err.printf("Unable to tar.gz : %s%n%s%n", file, exc);
        return FileVisitResult.CONTINUE;
    }
});
*/