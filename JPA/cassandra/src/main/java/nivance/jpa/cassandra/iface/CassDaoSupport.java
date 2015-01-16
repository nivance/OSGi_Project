package nivance.jpa.cassandra.iface;

import java.io.Serializable;
import java.util.List;

public interface CassDaoSupport<T, ID extends Serializable> {
	
	/**
	 * Saves a given entity. Use the returned instance for further operations as the save operation might have changed the
	 * entity instance completely.
	 * 
	 * @param entity
	 * @return the saved entity
	 */
	<S extends T> Object insert(S entity) ;
	
	<S extends T> Object insert(S entity,boolean ifNotExists) ;

	/**
	 * Saves all given entities.
	 * 
	 * @param entities
	 * @return the saved entities
	 * @throws IllegalArgumentException in case the given entity is (@literal null}.
	 */
	<S extends T> Object insert(List<S> entities) ;

	<S extends T> Object update(S entity) ;
	
	<S extends T> Object update(List<S> entities) ;
	/**
	 * Retrives an entity by its id.
	 * 
	 * @param id must not be {@literal null}.
	 * @return the entity with the given id or {@literal null} if none found
	 * @throws IllegalArgumentException if {@code id} is {@literal null}
	 */
	<S extends T> T findOne(S id) ;

	/**
	 * Returns whether an entity with the given id exists.
	 * 
	 * @param id must not be {@literal null}.
	 * @return true if an entity with the given id exists, {@literal false} otherwise
	 * @throws IllegalArgumentException if {@code id} is {@literal null}
	 */
	<S extends T> boolean exists(S s) ;

	/**
	 * Returns all instances of the type.
	 * 
	 * @return all entities
	 */
	List<T> findAll() ;

	/**
	 * Returns all instances of the type with the given IDs.
	 * 
	 * @param ids
	 * @return
	 */
	<S extends T> List<T> findAll(List<S> entities) ;

	/**
	 * Returns the number of entities available.
	 * 
	 * @return the number of entities
	 */
	long count() ;
	
	<S extends T> long count(S s) ;

	/**
	 * Deletes the entity with the given id.
	 * 
	 * @param id must not be {@literal null}.
	 * @throws IllegalArgumentException in case the given {@code id} is {@literal null}
	 */
	Object delete(ID id) ;

	/**
	 * Deletes a given entity.
	 * 
	 * @param entity
	 * @throws IllegalArgumentException in case the given entity is (@literal null}.
	 */
	Object delete(T entity) ;

	/**
	 * Deletes the given entities.
	 * 
	 * @param entities
	 * @throws IllegalArgumentException in case the given {@link Iterable} is (@literal null}.
	 */
	Object delete(List<? extends T> entities) ;

	/**
	 * Deletes all entities managed by the repository.
	 */
	Object deleteAll() ;
	
	public List<T> findByExample(T entity) ;
	
	
}
