package com.vodafone.exercise.warehouse.configuration;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.vodafone.exercise.warehouse.model.Device;
import com.vodafone.exercise.warehouse.repository.DeviceRepository;

import lombok.Generated;

@Generated
@Configuration
public class LoadWarehouseDatabase {

	@Bean
	CommandLineRunner initDb(DeviceRepository deviceRepository) {
		List<Device> devices = new ArrayList<>();
		return args -> {
			for (int i = 1; i < 100; i++) {
				devices.add(i % 2 == 0 ? new Device(Long.valueOf(i), "READY", (byte) -1, new Random().nextInt(9000000) + 1000000)
									 : ( new Device(Long.valueOf(i), "ACTIVE", (byte) new Random().nextInt(1, 11), new Random().nextInt(9000000) + 1000000)));
			}
			deviceRepository.saveAll(devices);
		};
	}
}
