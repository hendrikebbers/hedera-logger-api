package com.swirlds.logging.shipper.jul;

import com.swirlds.config.api.Configuration;
import com.swirlds.logging.api.extensions.shipper.LogShipper;
import com.swirlds.logging.api.extensions.shipper.LogShipperFactory;

public class JulShipperFactory implements LogShipperFactory {
    @Override
    public LogShipper apply(Configuration configuration) {
        return new JulShipper(configuration);
    }
}
