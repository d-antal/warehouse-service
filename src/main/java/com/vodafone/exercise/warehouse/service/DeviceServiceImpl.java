package com.vodafone.exercise.warehouse.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.vodafone.exercise.warehouse.exception.DeviceNotFoundException;
import com.vodafone.exercise.warehouse.model.Device;
import com.vodafone.exercise.warehouse.repository.DeviceRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class DeviceServiceImpl implements DeviceService {

	private static final String DEVICE_NOT_FOUND_MESSAGE = "Device not found by id: ";
	private final DeviceRepository deviceRepository;

	@Override
	public Device findDeviceById(Long deviceId) throws DeviceNotFoundException {
		log.info("Call findDeviceById, id: " + deviceId);
		return deviceRepository.findById(deviceId).orElseThrow(() -> new DeviceNotFoundException(DEVICE_NOT_FOUND_MESSAGE + deviceId));
	}

	@Override
	public Device addDevice(Device device) {
		return deviceRepository.save(device);
	}

	@Override
	public Device updateDevice(Long deviceId, Device newDevice) throws DeviceNotFoundException {
		Device device = deviceRepository.findById(deviceId).orElseThrow(() -> new DeviceNotFoundException(DEVICE_NOT_FOUND_MESSAGE + deviceId));
		device.setStatus(newDevice.getStatus());
		device.setTemperature(newDevice.getTemperature());
		if (newDevice.getPin() != null) {
			device.setPin(newDevice.getPin());
		}
		return deviceRepository.save(device);
	}

	@Override
	public long removeDevice(Long deviceId) throws DeviceNotFoundException {
		Device device = deviceRepository.findById(deviceId).orElseThrow(() -> new DeviceNotFoundException(DEVICE_NOT_FOUND_MESSAGE + deviceId));
		deviceRepository.delete(device);
		return device.getId();
	}

	@Override
	public List<Device> findAllActiveDevicesOrderedByPin() {
		return deviceRepository.findActiveDevicesOrderedByPinAsc();
	}

}
