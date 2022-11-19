package ro.tuc.ds2020.dtos.builders;

import lombok.AllArgsConstructor;
import ro.tuc.ds2020.dtos.MeasurementDTO;
import ro.tuc.ds2020.entities.Measurement;

@AllArgsConstructor
public class MeasurementBuilder {

    public static Measurement toEntity(MeasurementDTO measurementDTO){
        return new Measurement(measurementDTO.getTime(), measurementDTO.getValue());
    }

    public static MeasurementDTO toMeasurementDTO(Measurement measurement) {
        DeviceBuilder deviceBuilder = new DeviceBuilder();
        return new MeasurementDTO(
                measurement.getId(),
                measurement.getTime(),
                measurement.getValue(),
                measurement.getDevice().getId());
    }
}


