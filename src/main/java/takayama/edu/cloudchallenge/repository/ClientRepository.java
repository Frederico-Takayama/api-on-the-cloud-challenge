package takayama.edu.cloudchallenge.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import takayama.edu.cloudchallenge.domain.model.Client;

@Repository
public interface ClientRepository extends JpaRepository<Client, String>{

    Client findByCpf(String cpf);
}
