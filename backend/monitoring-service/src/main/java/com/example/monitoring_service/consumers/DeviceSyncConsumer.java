package com.example.monitoring_service.consumers;

import com.example.monitoring_service.config.RabbitMQConfig;
import com.example.monitoring_service.dtos.DeviceSyncDTO;
import com.example.monitoring_service.entities.MonitoredDevice;
import com.example.monitoring_service.repositories.MonitoredDeviceRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class DeviceSyncConsumer {
    private static final Logger LOGGER = LoggerFactory.getLogger(DeviceSyncConsumer.class);
    private final MonitoredDeviceRepository repository;

    public DeviceSyncConsumer(MonitoredDeviceRepository repository) {
        this.repository = repository;
    }

    /**
     * listens to device create event
     * adds to db monitored_devices.
     */
    @RabbitListener(queues = RabbitMQConfig.DEVICE_CREATED_QUEUE)
    public void handleDeviceCreated(DeviceSyncDTO event) {
        LOGGER.info("Received Device CREATED Sync Event for deviceId: {}", event.getDeviceId());

        MonitoredDevice newDevice = new MonitoredDevice();
        newDevice.setId(event.getDeviceId());
        newDevice.setName(event.getName());

        repository.save(newDevice);
        
        LOGGER.info("✅ Device synced to Monitoring DB: {} ({})", event.getDeviceId(), event.getName());
    }
}