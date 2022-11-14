package ro.tuc.ds2020.services;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ro.tuc.ds2020.controllers.handlers.exceptions.model.ResourceNotFoundException;
import ro.tuc.ds2020.dtos.PersonDTO;
import ro.tuc.ds2020.dtos.PersonDetailsDTO;
import ro.tuc.ds2020.dtos.builders.PersonBuilder;
import ro.tuc.ds2020.entities.users.Person;
import ro.tuc.ds2020.repositories.PersonRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class PersonService {
    private static final Logger LOGGER = LoggerFactory.getLogger(PersonService.class);
    private final PersonRepository personRepository;

    @Autowired
    public PersonService(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    public List<PersonDTO> findPersons() {
        List<Person> personList = personRepository.findAll();
        return personList.stream()
                .map(PersonBuilder::toPersonDTO)
                .collect(Collectors.toList());
    }

    public PersonDetailsDTO findPersonById(Long id) {
        Optional<Person> prosumerOptional = personRepository.findById(id);
        if (!prosumerOptional.isPresent()) {
            log.error("Person with id {} was not found in db", id);
            throw new ResourceNotFoundException(Person.class.getSimpleName() + " with id: " + id);
        }
        return PersonBuilder.toPersonDetailsDTO(prosumerOptional.get());
    }

    public Long insert(PersonDetailsDTO personDTO) {
        Person person = PersonBuilder.toEntity(personDTO);
        person = personRepository.save(person);
        log.info("Person with id {} was inserted in db", person.getId());
        return person.getId();
    }

    public Long deleteById(Long id) {
        personRepository.deleteById(id);
        log.info("Person with username {} was deleted from db", id);
        return id;
    }

    public PersonDetailsDTO updatePersonDetails(PersonDTO personDTO) {

        Person person = updateUserFields(personDTO);
        return PersonBuilder.toPersonDetailsDTO(personRepository.save(person));
    }

    private Person updateUserFields(PersonDTO personDTO) {
        Person updatedPerson;
        Optional<Person> person = personRepository.findById(personDTO.getId());
        if (!person.isPresent()) {
            log.error("Person with id {} was not found in db", personDTO.getId());
            throw new ResourceNotFoundException(Person.class.getSimpleName() + " with id: " + personDTO.getId());
        } else {
            updatedPerson = person.get();
            if (personDTO.getPassword() != null) {
                if (!personDTO.getPassword().isBlank()) {
                    updatedPerson.setPassword(personDTO.getPassword());
                }
            }

            if (personDTO.getAddress() != null) {
                if (!personDTO.getAddress().isBlank()) {
                    updatedPerson.setAddress(personDTO.getAddress());
                }
            }

            if (personDTO.getAge() != 0) {
                updatedPerson.setAge(personDTO.getAge());
            }

            if (personDTO.getUsername() != null) {
                updatedPerson.setUsername(personDTO.getUsername());
            }
        }
        return updatedPerson;
    }

    public PersonDetailsDTO findPersonByUsername(String username) {
        Optional<Person> prosumerOptional = personRepository.findByUsername(username);
        if (!prosumerOptional.isPresent()) {
            log.error("Person with username {} was not found in db", username);
            throw new ResourceNotFoundException(Person.class.getSimpleName() + " with id: " + username);
        }
        return PersonBuilder.toPersonDetailsDTO(prosumerOptional.get());
    }
}
