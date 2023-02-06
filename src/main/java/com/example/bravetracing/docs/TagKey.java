package com.example.bravetracing.docs;

import java.util.Arrays;

/**
 * Represents a tag key.
 *
 * @author Marcin Grzejszczak
 * @since 3.1.0
 */
public interface TagKey {

    /**
     * @return tag key
     */
    String getKey();

    /**
     * Merges arrays of tags.
     * @param tags array of tags
     * @return a merged array of tags
     */
    static TagKey[] merge(TagKey[]... tags) {
        return Arrays.stream(tags).flatMap(Arrays::stream).toArray(TagKey[]::new);
    }

}
