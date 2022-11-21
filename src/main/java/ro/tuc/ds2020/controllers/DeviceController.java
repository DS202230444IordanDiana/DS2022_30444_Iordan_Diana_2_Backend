package ro.tuc.ds2020.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ro.tuc.ds2020.dtos.DeviceDTO;
import ro.tuc.ds2020.dtos.MeasurementDTO;
import ro.tuc.ds2020.services.DeviceService;

import javax.validation.Valid;
import java.util.List;

@RestController
@CrossOrigin
@RequestMapping(path = "/device")
public class DeviceController {

    private final DeviceService deviceService;

    @Autowired
    public DeviceController(DeviceService deviceService) {
        this.deviceService = deviceService;
    }

    @GetMapping()
    public ResponseEntity<List<DeviceDTO>> getDevices() {
        List<DeviceDTO> dtos = deviceService.findDevices();

        return new ResponseEntity<>(dtos, HttpStatus.OK);
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<DeviceDTO> getDeviceById( @PathVariable("id") Long deviceId) {

        DeviceDTO dto = deviceService.findById(deviceId);
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @PostMapping()
    public ResponseEntity<Long> insertDevice(@Valid @RequestBody DeviceDTO deviceDTO){
        Long deviceId = deviceService.insert(deviceDTO);
        return  new ResponseEntity<>(deviceId, HttpStatus.CREATED);
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<Long> deleteDevice(@PathVariable("id") Long deviceId){
        deviceService.deleteById(deviceId);
        return new ResponseEntity<>(deviceId, HttpStatus.OK);
    }

    @PutMapping(path = "/update")
    public ResponseEntity<DeviceDTO> updateDeviceDetails(@Valid @RequestBody DeviceDTO deviceDTO){
        return new ResponseEntity<>(deviceService.updateDeviceDetails(deviceDTO), HttpStatus.OK);
    }

    @GetMapping(path = "/user/{id}")
    public ResponseEntity<List<DeviceDTO>> getDevicesByUser( @PathVariable("id") Long userId) {

        List<DeviceDTO> dto = deviceService.findByUser(userId);
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @GetMapping(path = "/measurements/{id}")
    public ResponseEntity<List<MeasurementDTO>> getMeasurementsForDevice( @PathVariable("id") Long deviceId) {
        List<MeasurementDTO> dto = deviceService.findMeasurementsById(deviceId);
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @PostMapping(path = "/measurement/add")
    public ResponseEntity<MeasurementDTO> insertMeasurement(@Valid @RequestBody MeasurementDTO measurementDTO) {
        MeasurementDTO dto = deviceService.insertMeasurement(measurementDTO);
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @GetMapping(path = "/test")
    public ResponseEntity<String> test() {
       return new ResponseEntity<>("It works", HttpStatus.OK);
    }
}
