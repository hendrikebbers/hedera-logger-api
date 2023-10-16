package com.swirlds.logging.api.internal.event;

import com.swirlds.logging.api.extensions.event.LogMessage;
import edu.umd.cs.findbugs.annotations.Nullable;

/**
 * A log message that can be parameterized with arguments. The arguments are inserted into the message at the positions
 * of the {} placeholders.
 * <p>
 * The implementation is copied from slf4j for first tests. need to be replaced in future. SLF4J is using MIT license
 * (https://github.com/qos-ch/slf4j/blob/master/LICENSE.txt). Based on that we can use it in our project for now
 *
 * @param messagePattern the message pattern
 * @param args           the arguments
 * @see LogMessage
 */
public record ParameterizedLogMessage(@Nullable String messagePattern, @Nullable Object... args) implements LogMessage {

    static final char DELIM_START = '{';
    static final String DELIM_STR = "{}";
    private static final char ESCAPE_CHAR = '\\';

    @Override
    public String getMessage() {
        if (messagePattern == null) {
            return "";
        }

        if (args == null) {
            return messagePattern;
        }

        int i = 0;
        int j;
        // use string builder for better multicore performance
        StringBuilder sbuf = new StringBuilder(messagePattern.length() + 50);

        int L;
        for (L = 0; L < args.length; L++) {

            j = messagePattern.indexOf(DELIM_STR, i);

            if (j == -1) {
                // no more variables
                if (i == 0) { // this is a simple string
                    return messagePattern;
                } else { // add the tail string which contains no variables and return
                    // the result.
                    sbuf.append(messagePattern, i, messagePattern.length());
                    return sbuf.toString();
                }
            } else {
                if (isEscapedDelimeter(messagePattern, j)) {
                    if (!isDoubleEscaped(messagePattern, j)) {
                        L--; // DELIM_START was escaped, thus should not be incremented
                        sbuf.append(messagePattern, i, j - 1);
                        sbuf.append(DELIM_START);
                        i = j + 1;
                    } else {
                        // The escape character preceding the delimiter start is
                        // itself escaped: "abc x:\\{}"
                        // we have to consume one backward slash
                        sbuf.append(messagePattern, i, j - 1);
                        deeplyAppendParameter(sbuf, args[L]);
                        i = j + 2;
                    }
                } else {
                    // normal case
                    sbuf.append(messagePattern, i, j);
                    deeplyAppendParameter(sbuf, args[L]);
                    i = j + 2;
                }
            }
        }
        // append the characters following the last {} pair.
        sbuf.append(messagePattern, i, messagePattern.length());
        return sbuf.toString();
    }

    private static boolean isEscapedDelimeter(String messagePattern, int delimeterStartIndex) {

        if (delimeterStartIndex == 0) {
            return false;
        }
        char potentialEscape = messagePattern.charAt(delimeterStartIndex - 1);
        if (potentialEscape == ESCAPE_CHAR) {
            return true;
        } else {
            return false;
        }
    }

    private static boolean isDoubleEscaped(String messagePattern, int delimeterStartIndex) {
        if (delimeterStartIndex >= 2 && messagePattern.charAt(delimeterStartIndex - 2) == ESCAPE_CHAR) {
            return true;
        } else {
            return false;
        }
    }

    private static void deeplyAppendParameter(StringBuilder sbuf, Object o) {
        if (o == null) {
            sbuf.append("null");
            return;
        }
        try {
            String oAsString = o.toString();
            sbuf.append(oAsString);
        } catch (Throwable t) {
            sbuf.append("[FAILED toString()]");
        }
    }
}