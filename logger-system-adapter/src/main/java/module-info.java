import com.swirlds.logging.adapter.system.LoggerFinderImpl;
import com.swirlds.logging.api.extensions.LogAdapter;

module com.swirlds.logging.adapter.system {
    requires java.logging;
    requires com.swirlds.logging.api;
    requires com.swirlds.config;

    provides java.lang.System.LoggerFinder with LoggerFinderImpl;
    provides LogAdapter with com.swirlds.logging.adapter.system.SystemLoggerAdapter;
}