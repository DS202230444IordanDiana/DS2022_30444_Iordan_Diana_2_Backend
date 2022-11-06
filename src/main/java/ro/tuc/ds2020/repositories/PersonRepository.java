package ro.tuc.ds2020.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ro.tuc.ds2020.entities.users.Person;

public interface PersonRepository extends JpaRepository<Person, Long> {
}
