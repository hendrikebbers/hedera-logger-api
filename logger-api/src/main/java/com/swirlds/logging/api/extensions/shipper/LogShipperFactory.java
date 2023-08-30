package com.swirlds.logging.api.extensions.shipper;

import com.swirlds.config.api.Configuration;
import java.util.function.Function;

public interface LogShipperFactory extends Function<Configuration, LogShipper> {
}
