package ru.kurtov.httpserver.statics;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;

public class StaticItem {
    private final long lastModified;
    private final int etag;
    private final byte[] data;
    
    static long getLastModified(File file) {
        return file.lastModified();
    }

    StaticItem(File file) throws IOException {
        Path path = file.toPath();
        
        this.lastModified = getLastModified(file);
        this.data = Files.readAllBytes(path);
        this.etag = Arrays.hashCode(data);
    }
    
    public long getLastModified() {
        return lastModified;
    }

    public int getEtag() {
        return etag;
    }

    public byte[] getData() {
        return data;
    }
}
