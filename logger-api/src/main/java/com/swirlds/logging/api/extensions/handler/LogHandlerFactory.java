package com.swirlds.logging.api.extensions.handler;

import com.swirlds.config.api.Configuration;
import java.util.function.Function;

public interface LogHandlerFactory extends Function<Configuration, LogHandler> {

}
