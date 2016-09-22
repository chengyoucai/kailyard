package net.kailyard.utils;

import com.google.common.base.Splitter;
import com.google.common.primitives.Longs;

/**
 */
public final class StringUtils {
    private StringUtils(){}

    /**
     *
     * @param ids
     * @return
     */
    public static Iterable<Long> convertToLongs(String ids, String separator){
        return Longs.stringConverter().convertAll(Splitter.on(separator).omitEmptyStrings().split(ids));
    }
}
