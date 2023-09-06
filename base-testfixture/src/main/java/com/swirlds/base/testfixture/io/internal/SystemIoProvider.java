package com.swirlds.base.testfixture.io.internal;

import com.swirlds.base.testfixture.io.SystemErrProvider;
import com.swirlds.base.testfixture.io.SystemOutProvider;
import edu.umd.cs.findbugs.annotations.NonNull;
import java.io.ByteArrayOutputStream;
import java.util.Objects;
import java.util.stream.Stream;

public class SystemIoProvider implements SystemOutProvider, SystemErrProvider {

    private final ByteArrayOutputStream outputStream;

    private final StringBuilder internalBuilder = new StringBuilder();

    private boolean readAll = false;

    public SystemIoProvider(@NonNull final ByteArrayOutputStream outputStream) {
        this.outputStream = Objects.requireNonNull(outputStream, "outputStream must not be null");
    }

    private void readAll() {
        try {
            internalBuilder.setLength(0); // clear the builder
            internalBuilder.append(outputStream.toString());
        } catch (Exception e) {
            throw new RuntimeException("Error reading from outputStream", e);
        } finally {
            readAll = true;
        }
    }

    @Override
    public Stream<String> getLines() {
        if (!readAll) {
            readAll();
        }
        return internalBuilder.toString().lines();
    }

}
