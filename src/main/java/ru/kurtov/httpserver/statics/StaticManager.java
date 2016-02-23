package ru.kurtov.httpserver.statics;

import java.io.File;
import java.io.IOException;

public interface StaticManager {
    public StaticItem getItem(File file) throws IOException;
}
