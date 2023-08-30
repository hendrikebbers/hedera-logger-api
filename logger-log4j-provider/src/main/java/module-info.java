import com.swirlds.logging.api.extensions.provider.LogProviderFactory;
import com.swirlds.logging.provider.log4j.Log4JExtension;
import com.swirlds.logging.provider.log4j.Log4JLogProviderFactory;
import org.apache.logging.log4j.spi.Provider;

module com.swirlds.logging.provider.log4j {
    requires org.apache.logging.log4j;
    requires com.swirlds.base;
    requires com.swirlds.logging.api;
    requires com.swirlds.config;

    exports com.swirlds.logging.provider.log4j to org.apache.logging.log4j;

    provides LogProviderFactory with Log4JLogProviderFactory;
    provides Provider with Log4JExtension;
}