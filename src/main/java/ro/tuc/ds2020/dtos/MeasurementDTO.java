package ro.tuc.ds2020.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MeasurementDTO {

    private Long id;
    private LocalDateTime time;
    private float value;
    private long deviceId;

    @Override
    public String toString() {
        return "{" +
                "id=" + id +
                ", time=" + time +
                ", value=" + value +
                ", deviceId=" + deviceId +
                '}';
    }
}
