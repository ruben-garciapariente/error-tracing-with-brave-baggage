package com.example.bravetracing.async;

import com.example.bravetracing.docs.TagKey;
import com.example.bravetracing.docs.DocumentedSpan;

enum SleuthAsyncSpan implements DocumentedSpan {

    /**
     * Span that wraps a @Async annotation. Either continues an existing one or creates a
     * new one if there was no present one.
     */
    ASYNC_ANNOTATION_SPAN {
        @Override
        public String getName() {
            return "%s";
        }

        @Override
        public TagKey[] getTagKeys() {
            return Tags.values();
        }

    },

    /**
     * Span created whenever a Runnable needs to be instrumented.
     */
    ASYNC_RUNNABLE_SPAN {
        @Override
        public String getName() {
            return "%s";
        }

    },

    /**
     * Span created whenever a Callable needs to be instrumented.
     */
    ASYNC_CALLABLE_SPAN {
        @Override
        public String getName() {
            return "%s";
        }

    };

    enum Tags implements TagKey {

        /**
         * Class name where a method got annotated with @Async.
         */
        CLASS {
            @Override
            public String getKey() {
                return "class";
            }
        },

        /**
         * Method name that got annotated with @Async.
         */
        METHOD {
            @Override
            public String getKey() {
                return "method";
            }
        }

    }

}

