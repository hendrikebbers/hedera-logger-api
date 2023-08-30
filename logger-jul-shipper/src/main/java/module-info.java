import com.swirlds.logging.api.extensions.shipper.LogShipperFactory;
import com.swirlds.logging.shipper.jul.JulShipperFactory;

module com.swirlds.logging.shipper.jul {
    requires java.logging;
    requires com.swirlds.logging.api;
    requires com.swirlds.config;

    provides LogShipperFactory
            with JulShipperFactory;
}