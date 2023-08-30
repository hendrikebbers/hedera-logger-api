import com.swirlds.logging.adapter.system.SystemLoggerFinderImpl;
import com.swirlds.logging.api.extensions.provider.LogProvider;

module com.swirlds.logging.adapter.system {
    requires java.logging;
    requires com.swirlds.logging.api;
    requires com.swirlds.config;

    provides java.lang.System.LoggerFinder with SystemLoggerFinderImpl;
    provides LogProvider with com.swirlds.logging.adapter.system.SystemLoggerAdapter;
}