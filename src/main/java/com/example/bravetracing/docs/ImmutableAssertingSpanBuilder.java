package com.example.bravetracing.docs;

import io.micrometer.tracing.Span;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

import static java.util.Objects.requireNonNull;

class ImmutableAssertingSpanBuilder implements AssertingSpanBuilder {

    private final DocumentedSpan documentedSpan;

    private final Span.Builder delegate;

    ImmutableAssertingSpanBuilder(DocumentedSpan documentedSpan, Span.Builder delegate) {
        requireNonNull(documentedSpan);
        requireNonNull(delegate);
        this.documentedSpan = documentedSpan;
        this.delegate = delegate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ImmutableAssertingSpanBuilder that = (ImmutableAssertingSpanBuilder) o;
        return Objects.equals(documentedSpan, that.documentedSpan) && Objects.equals(delegate, that.delegate);
    }

    @Override
    public String toString() {
        return this.delegate.toString();
    }

    @Override
    public int hashCode() {
        return Objects.hash(documentedSpan, delegate);
    }

    @Override
    public DocumentedSpan getDocumentedSpan() {
        return this.documentedSpan;
    }

    @Override
    public Span.Builder getDelegate() {
        return this.delegate;
    }

    @Override
    public Span.Builder startTimestamp(long startTimestamp, TimeUnit unit) {
        return null;
    }
}
