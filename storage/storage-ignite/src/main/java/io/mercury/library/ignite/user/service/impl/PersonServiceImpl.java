package io.mercury.library.ignite.user.service.impl;

import io.mercury.library.ignite.user.dao.PersonRepository;
import io.mercury.library.ignite.user.model.Person;
import io.mercury.library.ignite.user.service.PersonService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

/**
 * Implements interface PersonService
 **/
@Service
public class PersonServiceImpl implements PersonService {

    @Resource
    private PersonRepository personRepository;

    /**
     * Save Person to Ignite DB
     *
     * @param person Person object.
     * @return The Person object saved in Ignite DB.
     */
    public Person save(Person person) {
        // If this username is not used then return null, if is used then return this Person
        return personRepository.save(person.getId(), person);
    }

    /**
     * Find a Person from Ignite DB with given name.
     *
     * @param name Person name.
     * @return The person found in Ignite DB
     */
    public Person findPersonByUsername(String name) {
        return personRepository.findByUsername(name);
    }

}
