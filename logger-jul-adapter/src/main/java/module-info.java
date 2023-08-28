import com.swirlds.logging.api.extensions.LogAdapter;

module com.swirlds.logging.adapter.jul {
    requires java.logging;
    requires com.swirlds.logging.api;
    requires com.swirlds.config;

    provides LogAdapter
            with com.swirlds.logging.adapter.jul.JulAdapter;
}