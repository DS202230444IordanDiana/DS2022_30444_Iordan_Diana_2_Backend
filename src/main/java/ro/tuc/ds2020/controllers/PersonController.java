package ro.tuc.ds2020.controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ro.tuc.ds2020.dtos.PersonDTO;
import ro.tuc.ds2020.dtos.PersonDetailsDTO;
import ro.tuc.ds2020.services.PersonService;

import javax.validation.Valid;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@CrossOrigin
@RequestMapping(value = "/person")
public class PersonController {

    private final PersonService personService;

    @Autowired
    public PersonController(PersonService personService) {
        this.personService = personService;
    }

    @GetMapping()
    public ResponseEntity<List<PersonDTO>> getPersons() {
        List<PersonDTO> dtos = personService.findPersons();
        for (PersonDTO dto : dtos) {
            Link personLink = linkTo(methodOn(PersonController.class)
                    .getPersonById(dto.getId())).withRel("personDetails");
            dto.add(personLink);
        }
        return new ResponseEntity<>(dtos, HttpStatus.OK);
    }

    @PostMapping()
    public ResponseEntity<Long> insertPerson(@Valid @RequestBody PersonDetailsDTO personDTO) {
        Long personID = personService.insert(personDTO);
        return new ResponseEntity<>(personID, HttpStatus.CREATED);
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<PersonDetailsDTO> getPersonById(@PathVariable("id") Long personId) {
        PersonDetailsDTO dto = personService.findPersonById(personId);
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @GetMapping(path = "/get/{username}")
    public ResponseEntity<PersonDetailsDTO> getPersonByUsername(@PathVariable("username") String username) {
        PersonDetailsDTO dto = personService.findPersonByUsername(username);
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<Long> deletePerson(@PathVariable("id") Long personId) {
        personService.deleteById(personId);
        return new ResponseEntity<>(personId, HttpStatus.OK);
    }

    @PutMapping(path = "/update")
    public ResponseEntity<PersonDetailsDTO> updatePersonDetails(@Valid @RequestBody PersonDTO personDTO) {
        PersonDetailsDTO personDetailsDTO =  personService.updatePersonDetails(personDTO);
        return new ResponseEntity<>(personDetailsDTO, HttpStatus.OK);
    }
}
