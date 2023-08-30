import com.swirlds.logging.api.extensions.provider.LogProvider;

module com.swirlds.logging.adapter.jul {
    requires java.logging;
    requires com.swirlds.logging.api;
    requires com.swirlds.config;

    provides LogProvider
            with com.swirlds.logging.adapter.jul.JulAdapter;
}