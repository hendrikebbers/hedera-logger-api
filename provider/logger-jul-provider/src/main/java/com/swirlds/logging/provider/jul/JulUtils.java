package com.swirlds.logging.provider.jul;

import com.swirlds.logging.api.Level;
import edu.umd.cs.findbugs.annotations.NonNull;
import java.text.MessageFormat;
import java.util.Objects;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.LogRecord;

/**
 * Some utility methods for JUL (java.util.logging).
 */
public class JulUtils {

    /**
     * Converts a JUL level to a Swirlds Logging level.
     *
     * @param level the JUL level to convert
     * @return the Swirlds level
     */
    @NonNull
    public static Level convertFromJul(@NonNull final java.util.logging.Level level) {
        if (level == null) {
            return Level.ERROR;
        }
        if (Objects.equals(java.util.logging.Level.SEVERE, level)) {
            return Level.ERROR;
        }
        if (Objects.equals(java.util.logging.Level.WARNING, level)) {
            return Level.WARN;
        }
        if (Objects.equals(java.util.logging.Level.INFO, level)) {
            return Level.INFO;
        }
        if (Objects.equals(java.util.logging.Level.CONFIG, level)) {
            return Level.DEBUG;
        }
        if (Objects.equals(java.util.logging.Level.FINE, level)) {
            return Level.DEBUG;
        }
        if (Objects.equals(java.util.logging.Level.FINER, level)) {
            return Level.DEBUG;
        }
        if (Objects.equals(java.util.logging.Level.FINEST, level)) {
            return Level.TRACE;
        }
        if (Objects.equals(java.util.logging.Level.ALL, level)) {
            return Level.TRACE;
        }
        return Level.ERROR;
    }

    /**
     * Translates the given key using the given bundle. If the key cannot be translated, the key itself is returned.
     *
     * @param bundle the bundle to use for translation
     * @param key    the key to translate
     * @return the translated key or the key itself if it cannot be translated
     */
    @NonNull
    public static String translateOrKey(@NonNull final ResourceBundle bundle, @NonNull final String key) {
        if (bundle == null || key == null) {
            return "";
        }
        try {
            return bundle.getString(key);
        } catch (Exception x) {
            return key;
        }
    }

    /**
     * Extracts the message from the given log record. If the message cannot be extracted, an empty string is returned.
     *
     * @param record the record to extract the message from
     * @return the message
     */
    @NonNull
    public static String extractMessage(@NonNull final LogRecord record) {
        if (record == null) {
            return "";
        }
        final String message = record.getMessage();
        if (message == null) {
            return "";
        }
        final String translatedMessage = Optional.ofNullable(record.getResourceBundle())
                .map(resourceBundle -> translateOrKey(resourceBundle, message))
                .orElse(message);
        return Optional.ofNullable(record.getParameters())
                .map(parameters -> MessageFormat.format(translatedMessage, parameters))
                .orElse(translatedMessage);
    }
}
