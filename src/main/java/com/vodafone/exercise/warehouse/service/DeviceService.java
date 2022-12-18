package com.vodafone.exercise.warehouse.service;

import java.util.List;

import com.vodafone.exercise.warehouse.exception.DeviceNotFoundException;
import com.vodafone.exercise.warehouse.model.Device;

public interface DeviceService {

	Device findDeviceById(Long deviceId) throws DeviceNotFoundException;

	Device addDevice(Device device);

	Device updateDevice(Long deviceId, Device device) throws DeviceNotFoundException;

	long removeDevice(Long deviceId) throws DeviceNotFoundException;

	List<Device> findAllActiveDevicesOrderedByPin();

}
