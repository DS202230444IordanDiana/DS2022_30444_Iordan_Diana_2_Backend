package ro.tuc.ds2020.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.jni.Local;
import org.springframework.stereotype.Component;
import ro.tuc.ds2020.dtos.MeasurementDTO;
import ro.tuc.ds2020.dtos.builders.MeasurementBuilder;
import ro.tuc.ds2020.entities.Measurement;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Slf4j
@Component
public class JSONConverter {

    private ObjectMapper mapper;

    public JSONConverter() {
        this.mapper = new ObjectMapper();
    }

    public MeasurementDTO convertToMeasurement(String measurementJSON) throws JsonProcessingException {
        JsonNode actualObj = mapper.readTree(measurementJSON);

        MeasurementDTO measurementDTO = new MeasurementDTO();
        measurementDTO.setDeviceId(actualObj.get("deviceId").asLong());
        measurementDTO.setTime(convertTimestamp(actualObj.get("time")));
        measurementDTO.setValue(Float.valueOf(actualObj.get("value").asText()));

        return measurementDTO;
    }


    public LocalDateTime convertTimestamp(JsonNode jsonNode){
        return LocalDateTime.of(
                jsonNode.get("year").asInt(),
                jsonNode.get("monthValue").asInt(),
                jsonNode.get("dayOfMonth").asInt(),
                jsonNode.get("hour").asInt(),
                jsonNode.get("minute").asInt(),
                jsonNode.get("second").asInt(),
                jsonNode.get("nano").asInt());
    }
}
