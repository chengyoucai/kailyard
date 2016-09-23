package net.kailyard.template.utils;

public final class ArrayUtils {
    private ArrayUtils(){}

    public static <T> boolean isNotEmpty(T[] array) {
        return array != null && array.length != 0;
    }
}
