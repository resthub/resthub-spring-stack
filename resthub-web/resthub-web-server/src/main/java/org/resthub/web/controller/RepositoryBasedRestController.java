package org.resthub.web.controller;

import org.resthub.common.exception.NotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.Serializable;
import java.util.Set;

/**
 * Abstract REST controller using a repository implementation
 * <p/>
 * <p>
 * You should extend this class when you want to use a 2 layers pattern : Repository and Controller. This is the default
 * controller implementation to use if you have no service (also called business) layer. You will be able to transform
 * it to a ServiceBasedRestController later easily if needed.
 * </p>
 *
 * <p>Default implementation uses "id" field (usually a Long) in order to identify resources in web request.
 * If your want to identity resources by a slug (human readable identifier), your should override findById() method with for example :
 *
 * <pre>
 * <code>
   {@literal @}Override
   public Sample findById({@literal @}PathVariable String id) {
        Sample sample = this.repository.findByName(id);
        if (sample == null) {
            throw new NotFoundException();
        }
        return sample;
   }
   </code>
 * </pre>
 *
 *
 * @param <T>  Your resource class to manage, maybe an entity or DTO class
 * @param <ID> Resource id type, usually Long or String
 * @param <R>  The repository class
 * @see ServiceBasedRestController
 */
public abstract class RepositoryBasedRestController<T, ID extends Serializable, R extends PagingAndSortingRepository>
        implements RestController<T, ID> {

    protected R repository;

    protected Logger logger = LoggerFactory.getLogger(RepositoryBasedRestController.class);

    /**
     * You should override this setter in order to inject your repository with @Inject annotation
     *
     * @param repository The repository to be injected
     */
    public void setRepository(R repository) {
        this.repository = repository;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public T create(@RequestBody T resource) {
        return (T)this.repository.save(resource);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public T update(@PathVariable ID id, @RequestBody T resource) {
        Assert.notNull(id, "id cannot be null");

        T retrievedResource = this.findById(id);
        if (retrievedResource == null) {
            throw new NotFoundException();
        }

        return (T)this.repository.save(resource);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Iterable<T> findAll() {
        return repository.findAll();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Page<T> findPaginated(@RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
                                 @RequestParam(value = "size", required = false, defaultValue = "10") Integer size,
                                 @RequestParam(value = "direction", required = false, defaultValue = "") String direction,
                                 @RequestParam(value = "properties", required = false) String properties) {
        Assert.isTrue(page > 0, "Page index must be greater than 0");
        Assert.isTrue(direction.isEmpty() || direction.equalsIgnoreCase(Sort.Direction.ASC.toString()) || direction.equalsIgnoreCase(Sort.Direction.DESC.toString()), "Direction should be ASC or DESC");
        if(direction.isEmpty()) {
            return this.repository.findAll(new PageRequest(page - 1, size));
        } else {
            Assert.notNull(properties);
            return this.repository.findAll(new PageRequest(page - 1, size, new Sort(Sort.Direction.fromString(direction.toUpperCase()), properties.split(","))));
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public T findById(@PathVariable ID id) {
        T entity = (T)this.repository.findOne(id);
        if (entity == null) {
            throw new NotFoundException();
        }

        return entity;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Iterable<T> findByIds(@RequestParam(value="ids[]") Set<ID> ids){
        Assert.notNull(ids, "ids list cannot be null");
        return this.repository.findAll(ids);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void delete() {
        Iterable<T> list = repository.findAll();
        for (T entity : list) {
            repository.delete(entity);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void delete(@PathVariable ID id) {
        T resource = this.findById(id);
        this.repository.delete(resource);
    }

}
