package com.vodafone.exercise.warehouse;

import java.util.Arrays;
import java.util.List;

import com.vodafone.exercise.warehouse.model.Device;

public final class DeviceTestConstants {
	static final byte TEMPERATURE_DEFAULT = -1;
	static final byte TEMPERATURE_ACTIVE = 5;
	static final Byte TEMPERATURE_TOO_HIGH = 11;
	static final Byte TEMPERATURE_TOO_LOW = -2;
	static final String STATUS_DEFAULT = "READY";
	static final String STATUS_ACTIVE = "ACTIVE";
	static final Integer PIN_1 = 1111111;
	static final Integer PIN_2 = 2222222;
	static final Integer PIN_3 = 3333333;
	static final Integer PIN_4 = 4444444;
	static final long ID_1 = 1l;
	static final long ID_2 = 2l;
	static final long ID_3 = 3l;
	static final long ID_4 = 4l;
	static final long ID_5 = 5l;
	static final long ID_NOT_EXIST = 123l;
	static final Device DEVICE_ACTIVE = new Device(ID_1, STATUS_ACTIVE, TEMPERATURE_ACTIVE, PIN_1);
	static final Device DEVICE_ACTIVE_2 = new Device(ID_5, STATUS_ACTIVE, TEMPERATURE_ACTIVE, PIN_1);
	static final Device DEVICE_ACTIVE_NO_PIN = new Device(ID_2, STATUS_ACTIVE, TEMPERATURE_ACTIVE, null);
	static final Device DEVICE_DEFAULT = new Device(ID_1, STATUS_DEFAULT, TEMPERATURE_DEFAULT, PIN_1);
	static final Device DEVICE_STATUS_NULL = new Device(ID_1, null, TEMPERATURE_ACTIVE, PIN_1);
	static final Device DEVICE_STATUS_BLANK = new Device(ID_1, "", TEMPERATURE_ACTIVE, PIN_1);
	static final Device DEVICE_TEMPERATURE_NULL = new Device(ID_1, STATUS_ACTIVE, null, PIN_1);
	static final Device DEVICE_TEMPERATURE_TOO_HIGH = new Device(ID_1, STATUS_ACTIVE, TEMPERATURE_TOO_HIGH, PIN_1);
	static final Device DEVICE_TEMPERATURE_TOO_LOW = new Device(ID_1, STATUS_ACTIVE, TEMPERATURE_TOO_LOW, PIN_1);
	static final String WAREHOUSE_URI = "/warehouse/devices";
	static final String WAREHOUSE_URI_SLASH = WAREHOUSE_URI + "/";
	static final String DEVICE_NOT_FOUND_ERROR_MESSAGE = "Device not found by id: ";
	static final String EXPECTED_EXCEPTION_NOT_FOUND = "DeviceNotFoundException was expected";
	static final List<Device> DEVICE_LIST = Arrays.asList(new Device(ID_1, STATUS_ACTIVE, TEMPERATURE_ACTIVE, PIN_4), new Device(ID_2, STATUS_ACTIVE, TEMPERATURE_ACTIVE, PIN_3),
														  new Device(ID_3, STATUS_ACTIVE, TEMPERATURE_ACTIVE, PIN_2), new Device(ID_4, STATUS_DEFAULT, TEMPERATURE_DEFAULT, PIN_1));


}
