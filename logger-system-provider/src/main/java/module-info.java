import com.swirlds.logging.api.extensions.provider.LogProviderFactory;
import com.swirlds.logging.provider.system.SystemLoggerFinderImpl;
import com.swirlds.logging.provider.system.SystemLoggerProviderFactory;
import java.lang.System.LoggerFinder;

module com.swirlds.logging.provider.system {
    requires java.logging;
    requires com.swirlds.logging.api;
    requires com.swirlds.config;

    provides LoggerFinder with SystemLoggerFinderImpl;
    provides LogProviderFactory with SystemLoggerProviderFactory;
}