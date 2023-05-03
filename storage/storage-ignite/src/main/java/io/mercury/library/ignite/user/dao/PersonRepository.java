package io.mercury.library.ignite.user.dao;

import io.mercury.library.ignite.user.model.Person;
import org.apache.ignite.springdata.repository.IgniteRepository;
import org.apache.ignite.springdata.repository.config.RepositoryConfig;


/**
 * Apache Ignite Spring Data repository backed by Ignite Person's cache.
 **/
@RepositoryConfig(cacheName = "PersonCache")
public interface PersonRepository extends IgniteRepository<Person, Long> {

    /**
     * Find a person with given name in Ignite DB.
     *
     * @param name Person name.
     * @return The person whose name is the given name.
     */
    Person findByUsername(String name);

}
