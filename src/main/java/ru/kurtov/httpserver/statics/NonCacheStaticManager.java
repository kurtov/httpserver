package ru.kurtov.httpserver.statics;

import java.io.File;
import java.io.IOException;

public class NonCacheStaticManager implements StaticManager {

    @Override
    public StaticItem getItem(File file) throws IOException {
        return new StaticItem(file);
    }
}
