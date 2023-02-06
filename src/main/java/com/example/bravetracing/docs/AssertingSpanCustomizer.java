package com.example.bravetracing.docs;

import io.micrometer.tracing.SpanCustomizer;
import io.micrometer.tracing.docs.EventValue;

/**
 * A {@link SpanCustomizer} that can perform assertions on itself.
 *
 * @author Marcin Grzejszczak
 * @since 3.1.0
 */
public interface AssertingSpanCustomizer extends SpanCustomizer {

    /**
     * @return a {@link DocumentedSpan} with span configuration
     */
    DocumentedSpan getDocumentedSpan();

    /**
     * @return wrapped {@link SpanCustomizer}
     */
    SpanCustomizer getDelegate();

    @Override
    default AssertingSpanCustomizer tag(String key, String value) {
        DocumentedSpanAssertions.assertThatKeyIsValid(key, getDocumentedSpan());
        getDelegate().tag(key, value);
        return this;
    }

    /**
     * Sets a tag on a span.
     * @param key tag key
     * @param value tag
     * @return this, for chaining
     */
    default AssertingSpanCustomizer tag(TagKey key, String value) {
        DocumentedSpanAssertions.assertThatKeyIsValid(key, getDocumentedSpan());
        getDelegate().tag(key.getKey(), value);
        return this;
    }

    @Override
    default AssertingSpanCustomizer event(String value) {
        DocumentedSpanAssertions.assertThatEventIsValid(value, getDocumentedSpan());
        getDelegate().event(value);
        return this;
    }

    /**
     * Sets an event on a span.
     * @param value event
     * @return this, for chaining
     */
    default AssertingSpanCustomizer event(EventValue value) {
        DocumentedSpanAssertions.assertThatEventIsValid(value, getDocumentedSpan());
        getDelegate().event(value.getValue());
        return this;
    }

    @Override
    default AssertingSpanCustomizer name(String name) {
        DocumentedSpanAssertions.assertThatNameIsValid(name, getDocumentedSpan());
        getDelegate().name(name);
        return this;
    }

    /**
     * @param documentedSpan span configuration
     * @param span span to wrap in assertions
     * @return asserting span customizer
     */
    static AssertingSpanCustomizer of(DocumentedSpan documentedSpan, SpanCustomizer span) {
        if (span instanceof AssertingSpanCustomizer) {
            return (AssertingSpanCustomizer) span;
        }
        return new ImmutableAssertingSpanCustomizer(documentedSpan, span);
    }

    /**
     * Returns the underlying delegate. Used when casting is necessary.
     * @param span span to check for wrapping
     * @param <T> type extending a span
     * @return unwrapped object
     */
    static <T extends SpanCustomizer> T unwrap(SpanCustomizer span) {
        if (span == null) {
            return null;
        }
        else if (span instanceof AssertingSpanCustomizer) {
            return (T) ((AssertingSpanCustomizer) span).getDelegate();
        }
        return (T) span;
    }

}
