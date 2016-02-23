package ru.kurtov.httpserver.statics;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

public class CacheStaticManager implements StaticManager {

    static ConcurrentHashMap<File, StaticItem> cache = new ConcurrentHashMap();
    
    @Override
    public StaticItem getItem(File file) throws IOException {
        StaticItem cacheItem = cache.get(file);

        //Если кэш отсутствует или
        //Если после последнего обращения к файлу он был изменен
        //Заменить кэш на новый
        if(
                   cacheItem == null 
                || cacheItem.getLastModified() != StaticItem.getLastModified(file)
        ) {
            cacheItem = new StaticItem(file);
        }
        
        cache.put(file, cacheItem);
        return cacheItem;
    }
}
