import com.swirlds.logging.api.extensions.provider.LogProviderFactory;
import com.swirlds.logging.provider.jul.JulLogProviderFactory;

module com.swirlds.logging.provider.jul {
    requires java.logging;
    requires com.swirlds.logging.api;
    requires com.swirlds.config;

    provides LogProviderFactory
            with JulLogProviderFactory;
}