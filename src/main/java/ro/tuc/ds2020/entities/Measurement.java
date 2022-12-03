package ro.tuc.ds2020.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Measurement {

    @Id
    @SequenceGenerator(name = "seq",
            sequenceName = "seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq")
    private Long id;
    private LocalDateTime time;
    private float value;

    @ManyToOne
    @JoinColumn(name = "device_id", nullable = false)
    private Device device;

    public Measurement(LocalDateTime time, float value) {
        this.time = time;
        this.value = value;
    }

    @Override
    public String toString() {
        return "Measurement{" +
                "id=" + id +
                ", time=" + time +
                ", value=" + value +
                ", device=" + device.getId() +
                '}';
    }
}
