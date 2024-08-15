package takayama.edu.cloudchallenge.service;

import java.util.List;
import static java.util.Optional.ofNullable;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import takayama.edu.cloudchallenge.domain.interfaces.ClientService;
import takayama.edu.cloudchallenge.domain.model.Account;
import takayama.edu.cloudchallenge.domain.model.Client;
import takayama.edu.cloudchallenge.repository.AccountRepository;
import takayama.edu.cloudchallenge.repository.ClientRepository;
import takayama.edu.cloudchallenge.service.exception.BusinessException;
import takayama.edu.cloudchallenge.service.exception.NotFoundException;


@Service
public class ClientServiceImpl implements ClientService {
    private final boolean SUCCESS = true;

    @Autowired
    ClientRepository clientRepository;

    @Autowired
    AccountRepository accountRepository;

    @Transactional(readOnly = true)
    @Override    
    public List<Client> findAll() {
        return clientRepository.findAll();
    }

    @Transactional(readOnly = true)
    @Override
    public Client findById(String cpf) {
        Client client = clientRepository.findByCpf(cpf);

        if(client != null) {
            return client;
        } else {
            throw new NotFoundException("There is no client with cpf: " + cpf);
        }
    }

    @Transactional
    @Override
    public Client insert(Client entity) {
        ofNullable(entity).orElseThrow(() -> new BusinessException("Client to create must not be null."));

        Client client = clientRepository.findByCpf(entity.getCpf());

        if(client != null) {
            throw new BusinessException("Cpf already exists in database: " + entity.getCpf());
        }
        Account account = new Account();
        account.setBalance(0.0);
        accountRepository.save(account);
        client = new Client(entity.getCpf(), entity.getName(), account);

        return clientRepository.save(client);
    }

    @Transactional
    @Override
    public Client update(Client client) {
        Client clientDb = findById(client.getCpf());
        clientDb.setName(client.getName());

        return clientRepository.save(clientDb);
    }

    @Override
    public void delete(String cpf) {
        Client client = findById(cpf);

        clientRepository.delete(client);
    }

    @Transactional(readOnly = true)
    @Override
    public Double checkBalance(String cpf) {
        Client client = findById(cpf);

        return client.getAccount().getBalance();
    }

    @Transactional
    @Override
    public boolean deposit(String cpf, Double value) {
        Client client = findById(cpf);
        Double balance = client.getAccount().getBalance();

        if(value > 0) {
            client.getAccount().setBalance(balance + value);
            accountRepository.save(client.getAccount());
            return SUCCESS;
        } else {
            throw new BusinessException("Invalid value");
        }
    }

    @Transactional
    @Override
    public boolean withdraw(String cpf, Double value) {
        Client client = findById(cpf);
        Double balance = client.getAccount().getBalance();

        if(value > 0  && balance >= value) {
            client.getAccount().setBalance(balance - value);
            accountRepository.save(client.getAccount());
            return SUCCESS;
        } else {
            throw new BusinessException("Invalid value or insufficient balance");
        }
    }
}
