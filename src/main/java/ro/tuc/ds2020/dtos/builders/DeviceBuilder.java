package ro.tuc.ds2020.dtos.builders;

import ro.tuc.ds2020.dtos.DeviceDTO;
import ro.tuc.ds2020.entities.Device;

public class DeviceBuilder {

    public static Device toEntity(DeviceDTO deviceDTO) {
        return new Device(
                deviceDTO.getType(),
                deviceDTO.getModel());
    }

    public static DeviceDTO toDeviceDTO(Device device){
        return new DeviceDTO(
                device.getId(),
                device.getType(),
                device.getModel(),
                device.getOwner().getUsername()
        );

    }
}
