package es.us.lsi.hermes;

import es.us.lsi.hermes.kafka.consumer.SurroundingVehiclesConsumer;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main {

    private static final Logger LOG = Logger.getLogger(Main.class.getName());

    // Valores para la configuración del 'consumer' de Kafka.
    private static Properties kafkaProperties;

    public static void main(String[] args) {
        LOG.log(Level.INFO, "main() - Cargar la configuración");
        
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        try {
            InputStream input = classLoader.getResourceAsStream("Kafka.properties");
            kafkaProperties = new Properties();
            kafkaProperties.load(input);
        } catch (IOException ex) {
            LOG.log(Level.SEVERE, "main() - Error al cargar el archivo de propiedades de Kafka", ex);
        }

        long pollTimeout = Long.parseLong(kafkaProperties.getProperty("consumer.poll.timeout.ms", "5000"));
        LOG.log(Level.INFO, "main() - Los 'consumers' de Kafka harán sondeos para ver si hay nuevos datos cada {0} milisegundos", pollTimeout);

        LOG.log(Level.INFO, "main() - Inicialización del consumidor de 'Surrounding Vehicles' de Kafka");
        SurroundingVehiclesConsumer surroundingVehiclesConsumer = new SurroundingVehiclesConsumer(pollTimeout);
        surroundingVehiclesConsumer.start();
    }

    public static Properties getKafkaProperties() {
        return kafkaProperties;
    }
}
