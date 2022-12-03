package ro.tuc.ds2020.services;


import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;
import ro.tuc.ds2020.dtos.MeasurementDTO;

@Component
@AllArgsConstructor
public class QueueConsumer {

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private JSONConverter jsonConverter;

    @Autowired
    private DeviceService deviceService;

    @RabbitListener(queues = {"${queue.name}"})
    public void receive(@Payload String fileBody){
        try {
            MeasurementDTO measurement = jsonConverter.convertToMeasurement(fileBody);
            MeasurementDTO insertedMeasurment = deviceService.insertMeasurement(measurement);
            System.out.println("Inserted in db:" + insertedMeasurment.toString());
            deviceService.checkHourlyConsumption(measurement.getTime(), measurement.getDeviceId());
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
