import com.swirlds.logging.adapter.system.SystemLoggerFinderImpl;
import com.swirlds.logging.adapter.system.SystemLoggerProviderFactory;
import com.swirlds.logging.api.extensions.provider.LogProviderFactory;
import java.lang.System.LoggerFinder;

module com.swirlds.logging.adapter.system {
    requires java.logging;
    requires com.swirlds.logging.api;
    requires com.swirlds.config;

    provides LoggerFinder with SystemLoggerFinderImpl;
    provides LogProviderFactory with SystemLoggerProviderFactory;
}