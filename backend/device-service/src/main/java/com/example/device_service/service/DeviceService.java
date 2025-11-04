package com.example.device_service.service;

import com.example.device_service.dto.DeviceDTO;
import com.example.device_service.dto.DeviceDetailsDTO;
import com.example.device_service.dto.builders.DeviceBuilder;
import com.example.device_service.entity.Device;
import com.example.device_service.handlers.exceptions.ResourceNotFoundException;
import com.example.device_service.repository.DeviceRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class DeviceService {

    private final DeviceRepository repository;

    public DeviceService(DeviceRepository repository) {
        this.repository = repository;
    }

    public List<DeviceDTO> findAll() {
        return repository.findAll().stream()
                .map(DeviceBuilder::toDeviceDTO)
                .collect(Collectors.toList());
    }

    public DeviceDetailsDTO findById(Long id) {
        Device d = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Device not found: " + id));
        return DeviceBuilder.toDeviceDetailsDTO(d);
    }

    public Long create(DeviceDetailsDTO dto) {
        Device device = DeviceBuilder.fromDetailsDTO(dto);
        device.setCreatedAt(Instant.now());
        device.setUpdatedAt(Instant.now());
        Device saved = repository.save(device);
        return saved.getId();
    }

    public void update(Long id, DeviceDetailsDTO dto) {
    Device device = repository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Device not found: " + id));

    if (dto.getName() != null)
        device.setName(dto.getName());
    if (dto.getType() != null)
        device.setType(dto.getType());
    if (dto.getLocation() != null)
        device.setLocation(dto.getLocation());
    if (dto.getOwnerId() != null)
        device.setOwnerId(dto.getOwnerId());
    if (dto.getMaximumHourlyEnergyConsumption() != null) 
        device.setMaximumHourlyEnergyConsumption(dto.getMaximumHourlyEnergyConsumption());

    device.setUpdatedAt(Instant.now());
    repository.save(device);
}


    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Device not found: " + id);
        }
        repository.deleteById(id);
    }

    public List<DeviceDTO> findByOwnerId(Long ownerId) {
        return repository.findByOwnerId(ownerId).stream()
                .map(DeviceBuilder::toDeviceDTO)
                .collect(Collectors.toList());
    }

    // assign/unassign helpers
    public void assignToUser(Long deviceId, Long ownerId) {
        Device device = repository.findById(deviceId)
                .orElseThrow(() -> new ResourceNotFoundException("Device not found: " + deviceId));
        device.setOwnerId(ownerId);
        device.setUpdatedAt(Instant.now());
        repository.save(device);
    }

    public void unassign(Long deviceId) {
        Device device = repository.findById(deviceId)
                .orElseThrow(() -> new ResourceNotFoundException("Device not found: " + deviceId));
        device.setOwnerId(null);
        device.setUpdatedAt(Instant.now());
        repository.save(device);
    }
}