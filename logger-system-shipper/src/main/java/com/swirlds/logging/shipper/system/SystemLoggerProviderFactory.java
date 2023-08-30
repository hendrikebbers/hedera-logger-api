package com.swirlds.logging.shipper.system;

import com.swirlds.config.api.Configuration;
import com.swirlds.logging.api.extensions.shipper.LogShipper;
import com.swirlds.logging.api.extensions.shipper.LogShipperFactory;

public class SystemLoggerProviderFactory implements LogShipperFactory {
    @Override
    public LogShipper apply(Configuration configuration) {
        return new SystemLoggerProvider(configuration);
    }
}
