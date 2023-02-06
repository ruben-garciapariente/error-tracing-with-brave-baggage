package com.example.bravetracing.docs;

import io.micrometer.tracing.Span;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

import static java.util.Objects.requireNonNull;

class ImmutableAssertingSpan implements AssertingSpan {

    private final DocumentedSpan documentedSpan;

    private final Span delegate;

    boolean isStarted;

    ImmutableAssertingSpan(DocumentedSpan documentedSpan, Span delegate) {
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
        ImmutableAssertingSpan that = (ImmutableAssertingSpan) o;
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
    public Span getDelegate() {
        return this.delegate;
    }

    @Override
    public AssertingSpan start() {
        this.isStarted = true;
        return AssertingSpan.super.start();
    }

    @Override
    public Span event(String value, long time, TimeUnit timeUnit) {
        return null;
    }

    @Override
    public void end(long time, TimeUnit timeUnit) {

    }

    @Override
    public boolean isStarted() {
        return this.isStarted;
    }

}
