package org.egc.gis.data;

import com.google.common.collect.Maps;

import java.lang.reflect.Array;
import java.util.EnumSet;
import java.util.Map;
import java.util.function.Function;

/**
 * (如果添加了commons包，可以用里面的 EnumUtils 类)
 * usage example in enum class：
 * <pre>{@code
 *   private static final Function<String, GDALDriversEnum> func =
 *        EnumUtils.lookupMap(GDALDriversEnum.class, GDALDriversEnum::getExtension);
 *
 *   public static GDALDriversEnum lookupByExtension(String ext) {
 *        return func.apply(ext);
 *    }
 *  }
 * @author Bill O'Neil
 * https://dzone.com/articles/java-enum-lookup-by-name-or-field-without-throwing
 */
public class EnumUtils {
    public static <T, E extends Enum<E>> Function<T, E> lookupMap(Class<E> clazz, Function<E, T> mapper) {
        @SuppressWarnings("unchecked")
        E[] emptyArray = (E[]) Array.newInstance(clazz, 0);
        return lookupMap(EnumSet.allOf(clazz).toArray(emptyArray), mapper);
    }

    public static <T, E extends Enum<E>> Function<T, E> lookupMap(E[] values, Function<E, T> mapper) {
        Map<T, E> index = Maps.newHashMapWithExpectedSize(values.length);
        for (E value : values) {
            index.put(mapper.apply(value), value);
        }
        return index::get;
    }
}
