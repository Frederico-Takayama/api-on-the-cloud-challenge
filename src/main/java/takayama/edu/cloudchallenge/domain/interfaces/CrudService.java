package takayama.edu.cloudchallenge.domain.interfaces;

import java.util.List;

public interface CrudService<ID, T> {
    List<T> findAll();
	T findById(ID id);
	T insert(T entity);
	T update(T entity);
	void delete(ID id);
}
