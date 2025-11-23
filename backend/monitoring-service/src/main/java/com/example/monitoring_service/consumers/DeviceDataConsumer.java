package com.example.monitoring_service.consumers;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import com.example.monitoring_service.dtos.MeasurementDTO;
import com.example.monitoring_service.entities.EnergyConsumption;
import com.example.monitoring_service.repositories.EnergyConsumptionRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class DeviceDataConsumer {
    private static final Logger LOGGER = LoggerFactory.getLogger(DeviceDataConsumer.class);
    private final EnergyConsumptionRepository repository;
    // Key: "deviceId_yyyy-MM-dd-HH" (e.g., "1_2025-11-22-19")
    // Value: Current accumulated consumption for that hour
    private final Map<String, Double> hourlyAccumulator = new ConcurrentHashMap<>();

    // The primary message consumer, listening to the data queue
    @RabbitListener(queues = "${monitoring.queue.data}")
    public void handleDeviceData(MeasurementDTO measurement) {
        try {
            Long deviceId = measurement.getDevice_id();
            Double value = measurement.getMeasurement_value();
            LocalDateTime dateTime = measurement.getLocalTimestamp();
            
            LOGGER.info("Received 10-min reading from Device {}: {} kWh at {}", 
                    deviceId, value, dateTime.withMinute(0).withSecond(0).withNano(0));

            // 2. Create the unique hourly aggregation key
            String hourKey = String.format("%d_%d-%02d-%02d-%02d",
                    deviceId, 
                    dateTime.getYear(), 
                    dateTime.getMonthValue(), 
                    dateTime.getDayOfMonth(), 
                    dateTime.getHour());

            // 3. Accumulate the reading
            Double accumulated = hourlyAccumulator.merge(hourKey, value, Double::sum);
            
            LOGGER.debug("Current hourly total for {}: {} kWh", hourKey, accumulated);

            // 4. CHECK FOR COMPLETION (Simplified Logic for Demo)
            // A realistic hourly job would use a separate scheduler (like @Scheduled) 
            // to run every hour and flush old entries. 
            // For immediate feedback, we simulate a 'flush' at the beginning of the next hour (e.g., when time.getMinute() is 0 or 10).
            if (dateTime.getMinute() >= 50 && accumulated > 0.0) { // check end of hour readings
                 saveHourlyConsumption(hourKey, accumulated, dateTime);
            }

        } catch (Exception e) {
            LOGGER.error("Error processing message: {}", measurement, e);
        }
    }
    
    // Saves the final hourly total to the database
    private void saveHourlyConsumption(String hourKey, Double totalConsumption, LocalDateTime timeOfLastReading) {
        // Find the start of the next hour (the reporting timestamp)
        LocalDateTime endOfHour = timeOfLastReading.toLocalDate().atTime(timeOfLastReading.getHour(), 0).plusHours(1);

        EnergyConsumption hourlyEntry = new EnergyConsumption();
        hourlyEntry.setDeviceId(Long.parseLong(hourKey.split("_")[0]));
        hourlyEntry.setTimestamp(endOfHour);
        hourlyEntry.setConsumptionKwh(totalConsumption);

        repository.save(hourlyEntry);
        LOGGER.info("✅ SAVED HOURLY AGGREGATION for Device {} ({} kWh)", hourlyEntry.getDeviceId(), totalConsumption);

        // Clear the entry from the in-memory map after successful saving
        hourlyAccumulator.remove(hourKey); 
    }
}
