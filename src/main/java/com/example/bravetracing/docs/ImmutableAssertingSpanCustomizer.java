package com.example.bravetracing.docs;

import io.micrometer.tracing.SpanCustomizer;

import java.util.Objects;

import static java.util.Objects.requireNonNull;

class ImmutableAssertingSpanCustomizer implements AssertingSpanCustomizer {

    private final DocumentedSpan documentedSpan;

    private final SpanCustomizer delegate;

    ImmutableAssertingSpanCustomizer(DocumentedSpan documentedSpan, SpanCustomizer delegate) {
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
        ImmutableAssertingSpanCustomizer that = (ImmutableAssertingSpanCustomizer) o;
        return Objects.equals(documentedSpan, that.documentedSpan) && Objects.equals(delegate, that.delegate);
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
    public SpanCustomizer getDelegate() {
        return this.delegate;
    }

}

