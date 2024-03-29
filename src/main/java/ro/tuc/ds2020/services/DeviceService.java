package ro.tuc.ds2020.services;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.jni.Local;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import ro.tuc.ds2020.controllers.handlers.exceptions.model.ResourceNotFoundException;
import ro.tuc.ds2020.dtos.DeviceDTO;
import ro.tuc.ds2020.dtos.MeasurementDTO;
import ro.tuc.ds2020.dtos.builders.DeviceBuilder;
import ro.tuc.ds2020.dtos.builders.MeasurementBuilder;
import ro.tuc.ds2020.entities.Device;
import ro.tuc.ds2020.entities.Measurement;
import ro.tuc.ds2020.entities.Notification;
import ro.tuc.ds2020.entities.users.Person;
import ro.tuc.ds2020.repositories.DeviceRepository;
import ro.tuc.ds2020.repositories.MeasurementsRepository;
import ro.tuc.ds2020.repositories.PersonRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
@AllArgsConstructor
public class DeviceService {


    private final DeviceRepository deviceRepository;
    private final PersonRepository personRepository;
    private final MeasurementsRepository measurementsRepository;
    private static final Logger LOGGER = LoggerFactory.getLogger(DeviceService.class);
    private SimpMessagingTemplate simpMessagingTemplate;

    public List<DeviceDTO> findDevices() {

        List<Device> devices = deviceRepository.findAll();

        return devices
                .stream()
                .map(DeviceBuilder::toDeviceDTO)
                .collect(Collectors.toList());
    }

    public Long insert(DeviceDTO deviceDTO) {
        Device device = DeviceBuilder.toEntity(deviceDTO);
        Optional<Person> owner = personRepository.findByUsername(deviceDTO.getOwnerUsername());
        if (!owner.isPresent()) {
            throw new ResourceNotFoundException(Person.class.getSimpleName() + " with username: " + deviceDTO.getOwnerUsername());
        }
        device.setOwner(owner.get());
        device = deviceRepository.save(device);
        return device.getId();
    }

    public DeviceDTO findById(Long id) {

        Optional<Device> device = deviceRepository.findById(id);
        if (!device.isPresent()) {
            log.error("Device with id {} was not found in db", id);
            throw new ResourceNotFoundException(Person.class.getSimpleName() + " with id: " + id);
        }
        log.error("Device with id {} was not found in db", id);

        return DeviceBuilder.toDeviceDTO(device.get());
    }

    public Long deleteById(Long id) {

        Optional<Device> device = deviceRepository.findById(id);
        if (!device.isPresent()) {
            log.error("Device with id {} was not found in db", id);
            throw new ResourceNotFoundException(Person.class.getSimpleName() + " with id: " + id);
        }

        deviceRepository.deleteById(id);
        return id;
    }

    public DeviceDTO updateDeviceDetails(DeviceDTO deviceDTO) {
        return DeviceBuilder.toDeviceDTO(updateDeviceFields(deviceDTO));
    }

    private Device updateDeviceFields(DeviceDTO deviceDTO) {
        Device updatedDevice = null;
        log.error("Device model: " + deviceDTO.getModel() + " device type:" + deviceDTO.getType());
        Optional<Device> device = deviceRepository.findById(deviceDTO.getId());
        if (!device.isPresent()) {
            log.error("Device with id {} was not found in db", deviceDTO.getId());
            throw new ResourceNotFoundException(Person.class.getSimpleName() + " with id: " + deviceDTO.getId());
        } else {
            updatedDevice = device.get();
            if (deviceDTO.getModel() != null) {
                if (!deviceDTO.getModel().isBlank()) {
                    updatedDevice.setModel(deviceDTO.getModel());
                }
            }
            if (deviceDTO.getLimit() != 0) {
                updatedDevice.setMaxLimit(deviceDTO.getLimit());

            }
            if (deviceDTO.getType() != null) {
                if (!deviceDTO.getType().isBlank()) {
                    updatedDevice.setType(deviceDTO.getType());
                }
            }
            if (deviceDTO.getOwnerUsername() != null) {
                if (!deviceDTO.getOwnerUsername().isBlank()) {
                    updatedDevice.setOwner(personRepository.findByUsername(deviceDTO.getOwnerUsername()).get());
                }
            }
        }
        deviceRepository.save(updatedDevice);
        return updatedDevice;
    }

    public List<DeviceDTO> findByUser(Long userId) {
        List<Device> devices = deviceRepository.findAllByOwnerId(userId);
        return devices
                .stream()
                .map(DeviceBuilder::toDeviceDTO)
                .collect(Collectors.toList());
    }

    public List<MeasurementDTO> findMeasurementsById(Long deviceId) {
        List<Measurement> measurements = measurementsRepository.findAllByDeviceId(deviceId);
        return measurements
                .stream()
                .map(MeasurementBuilder::toMeasurementDTO)
                .collect(Collectors.toList());
    }

    public MeasurementDTO insertMeasurement(MeasurementDTO measurementDTO) {

        Optional<Device> device = deviceRepository.findById(measurementDTO.getDeviceId());
        Measurement measurement = MeasurementBuilder.toEntity(measurementDTO);

        if (!device.isPresent()) {
            log.error("Device with id {} was not found in db", measurementDTO.getDeviceId());
            throw new ResourceNotFoundException(Person.class.getSimpleName() + " with id: " + measurementDTO.getDeviceId());
        } else {
            measurement.setDevice(device.get());
            measurementsRepository.save(measurement);
            return MeasurementBuilder.toMeasurementDTO(measurement);
        }
    }
    boolean checkTimeEquality(LocalDateTime currentTime, LocalDateTime time){
        LocalDate firstDate = currentTime.toLocalDate();
        LocalDate secondDate = time.toLocalDate();
        return firstDate.equals(secondDate) && currentTime.getHour() == time.getHour() && currentTime.getMinute() == time.getMinute();
    }
    public void checkHourlyConsumption(MeasurementDTO measurement) {
        LocalDateTime currentTime = measurement.getTime();
        Long id = measurement.getDeviceId();
        Optional<Device> device = deviceRepository.findById(id);
        List<Measurement> measurements = measurementsRepository.findAllByDeviceId(id);
        if (!device.isPresent()) {
            log.error("Device with id {} was not found in db", id);
            throw new ResourceNotFoundException(Person.class.getSimpleName() + " with id: " + id);
        } else {
            List<Measurement> minuteMeasurements = measurements.stream().filter((e) -> {
                return checkTimeEquality(currentTime, e.getTime());
            }).collect(Collectors.toList());
            float sum = 0f;
            for (Measurement m : minuteMeasurements) {
                sum += m.getValue();
            }
            System.out.println("Currently hour value: " + sum);
            String username = device.get().getOwner().getUsername();
            if (sum > device.get().getMaxLimit()) {
                System.out.println("Exceeded limit");
                String text = "Device with id" + " " + device.get().getId() + " " + "exceed the maximum value." + " " + "Last measurement:" + measurement.getValue() + ". " + "Hourly consumption: " + sum + " Maximum value: " + device.get().getMaxLimit();
                Notification notification = new Notification(username,text, device.get().getId(), LocalDateTime.now());
                simpMessagingTemplate.convertAndSend("/all/messages/"+ username, notification);
            }else{
                String text = "Device with id" + " " + device.get().getId() + " " + "read the following value:" + measurement.getValue() + ". " + "Hourly consumption: " + sum + " Maximum value: " + device.get().getMaxLimit();
                Notification notification = new Notification(username,text, device.get().getId(), LocalDateTime.now());
                simpMessagingTemplate.convertAndSend("/all/messages/"+ username, notification);
            }
        }
    }
}
