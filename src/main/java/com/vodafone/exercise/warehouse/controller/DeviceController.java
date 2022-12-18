package com.vodafone.exercise.warehouse.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.vodafone.exercise.warehouse.exception.DeviceNotFoundException;
import com.vodafone.exercise.warehouse.model.Device;
import com.vodafone.exercise.warehouse.service.DeviceService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@Validated
@RequiredArgsConstructor
@RequestMapping("/warehouse")
public class DeviceController {

	private final DeviceService deviceService;

	@GetMapping("/devices")
	public List<Device> getAllActiveDevicesOrderedByPin() {
		return deviceService.findAllActiveDevicesOrderedByPin();
	}

	@GetMapping("/devices/{id}")
	public Device getDeviceById(@PathVariable(value = "id") Long deviceId) throws DeviceNotFoundException {
		return deviceService.findDeviceById(deviceId);
	}

	@PostMapping("/devices")
	@ResponseStatus(HttpStatus.CREATED)
	public Device addDevice(@Valid @RequestBody Device device) {
		return deviceService.addDevice(device);
	}

	@PutMapping("/devices/{id}")
	public Device updateDevice(@PathVariable(value = "id") Long deviceId, @Valid @RequestBody Device newDevice) throws DeviceNotFoundException {
		return deviceService.updateDevice(deviceId, newDevice);
	}

	@DeleteMapping("/devices/{id}")
	public void removeDevice(@PathVariable(value = "id") Long deviceId) throws DeviceNotFoundException {
		deviceService.removeDevice(deviceId);
	}
}
