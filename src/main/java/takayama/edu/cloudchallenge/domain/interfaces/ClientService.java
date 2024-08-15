package takayama.edu.cloudchallenge.domain.interfaces;

import takayama.edu.cloudchallenge.domain.model.Client;

/**
 * Interface which defines Client's Strategy design pattern.
 * 
 * @author takayama
 */
public interface ClientService extends CrudService<String, Client>{
	Double checkBalance(String cpf);
    boolean deposit(String cpf, Double value);
    boolean withdraw(String cpf, Double value);
}