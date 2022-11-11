package ro.tuc.ds2020.controllers;

import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ro.tuc.ds2020.dtos.DeviceDTO;
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



}
