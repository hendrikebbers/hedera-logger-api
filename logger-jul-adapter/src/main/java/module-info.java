import com.swirlds.logging.adapter.jul.JulProviderFactory;
import com.swirlds.logging.api.extensions.provider.LogProviderFactory;

module com.swirlds.logging.adapter.jul {
    requires java.logging;
    requires com.swirlds.logging.api;
    requires com.swirlds.config;

    provides LogProviderFactory
            with JulProviderFactory;
}