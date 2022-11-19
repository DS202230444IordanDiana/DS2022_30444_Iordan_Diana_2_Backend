package ro.tuc.ds2020.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ro.tuc.ds2020.entities.Device;
import ro.tuc.ds2020.entities.Measurement;

import java.util.List;

public interface MeasurementsRepository  extends JpaRepository<Measurement, Long> {

    List<Measurement> findAllByDeviceId(Long id);
}
