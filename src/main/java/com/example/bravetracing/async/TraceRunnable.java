package com.example.bravetracing.async;

import com.example.bravetracing.async.SleuthAsyncSpan;
import io.micrometer.tracing.Span;
import io.micrometer.tracing.SpanNamer;
import io.micrometer.tracing.Tracer;

/**
 * Runnable that passes Span between threads. The Span name is taken either from the
 * passed value or from the {@link SpanNamer} interface.
 *
 * @author Spencer Gibb
 * @author Marcin Grzejszczak
 * @since 1.0.0
 */
// public as most types in this package were documented for use
public class TraceRunnable implements Runnable {

    /**
     * Since we don't know the exact operation name we provide a default name for the
     * Span.
     */
    private static final String DEFAULT_SPAN_NAME = "async";

    private final Tracer tracer;

    private final Runnable delegate;

    private final Span parent;

    private final String spanName;

    public TraceRunnable(Tracer tracer, SpanNamer spanNamer, Runnable delegate) {
        this(tracer, spanNamer, delegate, null);
    }

    public TraceRunnable(Tracer tracer, SpanNamer spanNamer, Runnable delegate, String name) {
        this.tracer = tracer;
        this.delegate = delegate;
        this.parent = tracer.currentSpan();
        this.spanName = name != null ? name : spanNamer.name(delegate, DEFAULT_SPAN_NAME);
    }

    @Override
    public void run() {
        Span childSpan = SleuthAsyncSpan.ASYNC_RUNNABLE_SPAN.wrap(this.tracer.nextSpan(this.parent))
                .name(this.spanName);
        try (Tracer.SpanInScope ws = this.tracer.withSpan(childSpan.start())) {
            this.delegate.run();
        }
        catch (Exception | Error e) {
            childSpan.error(e);
            throw e;
        }
        finally {
            childSpan.end();
        }
    }

}

