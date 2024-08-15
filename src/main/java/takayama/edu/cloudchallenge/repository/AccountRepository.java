package takayama.edu.cloudchallenge.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import takayama.edu.cloudchallenge.domain.model.Account;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long>{
    
}
