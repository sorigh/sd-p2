package com.example.device_service.controller;

import com.example.device_service.dto.DeviceDTO;
import com.example.device_service.service.DeviceService;
import com.example.device_service.handlers.exceptions.ResourceNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/my-devices")
@CrossOrigin(origins = "http://localhost:3000")
public class MyDevicesController {

    private final DeviceService deviceService;

    public MyDevicesController(DeviceService deviceService) {
        this.deviceService = deviceService;
    }

    // Get devices for current user
    @GetMapping
    public ResponseEntity<List<DeviceDTO>> getMyDevices(@RequestHeader("Authorization") String token) {
        // extract user id from token (JWT)
        Long userId = extractUserIdFromToken(token);
        if (userId == null) {
            throw new ResourceNotFoundException("Invalid token or user not found");
        }

        List<DeviceDTO> devices = deviceService.findByOwnerId(userId);
        return ResponseEntity.ok(devices);
    }

    private Long extractUserIdFromToken(String token) {
        // token is like "Bearer <JWT>"
        if (token != null && token.startsWith("Bearer ")) {
            String jwt = token.substring(7);
            try {
                String payload = new String(java.util.Base64.getDecoder().decode(jwt.split("\\.")[1]));
                // Assuming payload has "id" field (adjust according to JWT structure)
                com.fasterxml.jackson.databind.JsonNode node = new com.fasterxml.jackson.databind.ObjectMapper().readTree(payload);
                return node.get("sub").asLong();
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
        return null;
    }
}
