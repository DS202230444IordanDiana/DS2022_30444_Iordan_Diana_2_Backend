package ro.tuc.ds2020.services;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ro.tuc.ds2020.controllers.handlers.exceptions.model.ResourceNotFoundException;
import ro.tuc.ds2020.dtos.DeviceDTO;
import ro.tuc.ds2020.dtos.builders.DeviceBuilder;
import ro.tuc.ds2020.entities.Device;
import ro.tuc.ds2020.entities.users.Person;
import ro.tuc.ds2020.repositories.DeviceRepository;
import ro.tuc.ds2020.repositories.PersonRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class DeviceService {


    private final DeviceRepository deviceRepository;
    private final PersonRepository personRepository;
    private static final Logger LOGGER = LoggerFactory.getLogger(DeviceService.class);


    @Autowired
    public DeviceService(DeviceRepository deviceRepository, PersonRepository personRepository) {
        this.deviceRepository = deviceRepository;
        this.personRepository = personRepository;
    }

    public List<DeviceDTO> findDevices() {

        List<Device> devices = deviceRepository.findAll();

        return devices
                .stream()
                .map(DeviceBuilder::toDeviceDTO)
                .collect(Collectors.toList());
    }

    public Long insert(DeviceDTO deviceDTO) {
        Device device = DeviceBuilder.toEntity(deviceDTO);
        Optional<Person> owner = personRepository.findById(deviceDTO.getOwnerId());

        if(!owner.isPresent()){

        }
        device.setOwner(owner.get());
        device = deviceRepository.save(device);
        return device.getId();
    }

    public DeviceDTO findById(Long id){

        Optional<Device> device = deviceRepository.findById(id);
        if(!device.isPresent()){
            log.error("Device with id {} was not found in db", id);
            throw new ResourceNotFoundException(Person.class.getSimpleName() + " with id: " + id);
        }
        log.error("Device with id {} was not found in db", id);

        return DeviceBuilder.toDeviceDTO(device.get());
    }

    public Long deleteById(Long id) {

        Optional<Device> device = deviceRepository.findById(id);
        if(!device.isPresent()){
            log.error("Device with id {} was not found in db", id);
            throw new ResourceNotFoundException(Person.class.getSimpleName() + " with id: " + id);
        }

        deviceRepository.deleteById(id);
        return id;
    }

    public DeviceDTO updateDeviceDetails(DeviceDTO deviceDTO) {
        //TODO: c
        return DeviceBuilder.toDeviceDTO(updateDeviceFields(deviceDTO));
    }

    private Device updateDeviceFields(DeviceDTO deviceDTO) {
        Device updatedDevice = null;
        Optional<Device> device = deviceRepository.findById(deviceDTO.getId());
        if(device.isPresent()){
            updatedDevice = device.get();
            if(deviceDTO.getModel() != null){
                updatedDevice.setModel(deviceDTO.getModel());
            }
            if(deviceDTO.getType() != null){
                updatedDevice.setModel(deviceDTO.getModel());
            }
            if(deviceDTO.getOwnerId() != null){
                //TODO: add validations
                updatedDevice.setOwner(personRepository.findById(deviceDTO.getOwnerId()).get());
            }
        }
        return updatedDevice;
    }
}
