package Interfaces;
import Models.Entity;

import java.util.Optional;

public interface Repository<ID, E extends Entity<ID>> {
    /**
     * Returns the entity with the specified id
     * @param id -the id of the entity to be returned
     * id must not be null
     * @return an {@code Optional} encapsulating the entity with the given id
     * @throws IllegalArgumentException
     * if id is null.
     */
    Optional<E> findOne(ID id);
    /**
     * @return an {@code Iterable} with all entities.

     */
    Iterable<E> findAll();
    /**
     * Saves the entity
     * @param entity
     * entity must be not null
     * @return an {@code Optional} - null if the entity was saved,
     * - the entity (id already exists)
     * @throws Exception
     * if the entity is not valid
     * @throws IllegalArgumentException
     * if the given entity is null. *
     */
    Optional<E> save(E entity);
    /**
     * removes the entity with the specified id
     * @param id
     * id must be not null
     * @return an {@code Optional}
     * - null if there is no entity with the given id,
     * - the removed entity, otherwise
     * @throws IllegalArgumentException
     * if the given id is null.
     */
    Optional<E> delete(ID id);
    /**
     * Updates the entity
     * @param entity
     * entity must not be null
     * @return an {@code Optional}
     * - null if the entity was updated
     * - otherwise (e.g. id does not exist) returns the entity.
     * @throws IllegalArgumentException
     * if the given entity is null.
     * @throws Exception
     * if the entity is not valid.
     */
    Optional<E> update(E entity);

}