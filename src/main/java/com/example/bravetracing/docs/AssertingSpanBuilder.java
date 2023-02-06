package com.example.bravetracing.docs;

import io.micrometer.tracing.Span;
import io.micrometer.tracing.TraceContext;
import io.micrometer.tracing.docs.EventValue;

import java.util.concurrent.TimeUnit;

/**
 * A {@link Span.Builder} that can perform assertions on itself.
 *
 * @author Marcin Grzejszczak
 * @since 3.1.0
 */
public interface AssertingSpanBuilder extends Span.Builder {

    /**
     * @return a {@link DocumentedSpan} with span configuration
     */
    DocumentedSpan getDocumentedSpan();

    /**
     * @return wrapped {@link Span.Builder}
     */
    Span.Builder getDelegate();

    @Override
    default AssertingSpanBuilder tag(String key, String value) {
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
    default AssertingSpanBuilder tag(TagKey key, String value) {
        DocumentedSpanAssertions.assertThatKeyIsValid(key, getDocumentedSpan());
        getDelegate().tag(key.getKey(), value);
        return this;
    }

    @Override
    default AssertingSpanBuilder event(String value) {
        DocumentedSpanAssertions.assertThatEventIsValid(value, getDocumentedSpan());
        getDelegate().event(value);
        return this;
    }

    /**
     * Sets an event on a span.
     * @param value event
     * @return this, for chaining
     */
    default AssertingSpanBuilder event(EventValue value) {
        DocumentedSpanAssertions.assertThatEventIsValid(value, getDocumentedSpan());
        getDelegate().event(value.getValue());
        return this;
    }

    @Override
    default AssertingSpanBuilder name(String name) {
        DocumentedSpanAssertions.assertThatNameIsValid(name, getDocumentedSpan());
        getDelegate().name(name);
        return this;
    }

    @Override
    default AssertingSpanBuilder error(Throwable throwable) {
        getDelegate().error(throwable);
        return this;
    }

    @Override
    default AssertingSpanBuilder remoteServiceName(String remoteServiceName) {
        getDelegate().remoteServiceName(remoteServiceName);
        return this;
    }

    @Override
    default Span.Builder remoteIpAndPort(String ip, int port) {
        getDelegate().remoteIpAndPort(ip, port);
        return this;
    }

    @Override
    default AssertingSpanBuilder setParent(TraceContext context) {
        getDelegate().setParent(context);
        return this;
    }

    @Override
    default AssertingSpanBuilder setNoParent() {
        getDelegate().setNoParent();
        return this;
    }

    @Override
    default AssertingSpanBuilder kind(Span.Kind spanKind) {
        getDelegate().kind(spanKind);
        return this;
    }

    @Override
    default AssertingSpan start() {
        Span span = getDelegate().start();
        DocumentedSpan documentedSpan = getDocumentedSpan();
        return new AssertingSpan() {
            @Override
            public Span event(String value, long time, TimeUnit timeUnit) {
                return null;
            }

            @Override
            public void end(long time, TimeUnit timeUnit) {

            }

            @Override
            public DocumentedSpan getDocumentedSpan() {
                return documentedSpan;
            }

            @Override
            public Span getDelegate() {
                return span;
            }

            @Override
            public boolean isStarted() {
                return true;
            }

            @Override
            public String toString() {
                return getDelegate().toString();
            }
        };
    }

    /**
     * @param documentedSpan span configuration
     * @param builder builder to wrap in assertions
     * @return asserting span builder
     */
    static AssertingSpanBuilder of(DocumentedSpan documentedSpan, Span.Builder builder) {
        if (builder == null) {
            return null;
        }
        else if (builder instanceof AssertingSpanBuilder) {
            return (AssertingSpanBuilder) builder;
        }
        return new ImmutableAssertingSpanBuilder(documentedSpan, builder);
    }

}

