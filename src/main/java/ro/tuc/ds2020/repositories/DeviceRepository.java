package ro.tuc.ds2020.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ro.tuc.ds2020.entities.Device;

public interface DeviceRepository  extends JpaRepository<Device, Long> {
}
