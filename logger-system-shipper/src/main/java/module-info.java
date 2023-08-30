import com.swirlds.logging.api.extensions.shipper.LogShipperFactory;
import com.swirlds.logging.shipper.system.SystemLoggerFinderImpl;
import com.swirlds.logging.shipper.system.SystemLoggerProviderFactory;
import java.lang.System.LoggerFinder;

module com.swirlds.logging.shipper.system {
    requires java.logging;
    requires com.swirlds.logging.api;
    requires com.swirlds.config;

    provides LoggerFinder with SystemLoggerFinderImpl;
    provides LogShipperFactory with SystemLoggerProviderFactory;
}