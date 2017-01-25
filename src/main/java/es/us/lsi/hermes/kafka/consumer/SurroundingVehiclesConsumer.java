package es.us.lsi.hermes.kafka.consumer;

import es.us.lsi.hermes.Main;
import es.us.lsi.hermes.util.Constants;
import java.util.Collections;
import java.util.logging.Level;
import java.util.logging.Logger;
import kafka.utils.ShutdownableThread;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

public class SurroundingVehiclesConsumer extends ShutdownableThread {

    private static final Logger LOG = Logger.getLogger(SurroundingVehiclesConsumer.class.getName());
    public static final String TOPIC_SURROUNDING_VEHICLES = "SurroundingVehicles";

    private final KafkaConsumer<String, String> consumer;
    private final long pollTimeout;

    public SurroundingVehiclesConsumer(long pollTimeout) {
        // Podrá ser interrumpible.
        super("SurroundingVehiclesConsumer", true);
        this.consumer = new KafkaConsumer<>(Main.getKafkaProperties());
        this.pollTimeout = pollTimeout;
    }

    @Override
    public void doWork() {
        consumer.subscribe(Collections.singletonList(TOPIC_SURROUNDING_VEHICLES));
        ConsumerRecords<String, String> records = consumer.poll(pollTimeout);
        for (ConsumerRecord<String, String> record : records) {
            LOG.log(Level.FINE, "SurroundingVehiclesConsumer.doWork() - {0}: {1} [{2}] con offset {3}", new Object[]{record.topic(), Constants.dfISO8601.format(record.timestamp()), record.key(), record.offset()});
        }
    }
}
