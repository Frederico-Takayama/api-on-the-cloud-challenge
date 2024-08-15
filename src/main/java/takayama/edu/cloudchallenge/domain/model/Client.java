package takayama.edu.cloudchallenge.domain.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;

@Entity(name = "tb_client")
public class Client {
    @Id
	private String cpf;
	private String name;
	@OneToOne(cascade = CascadeType.ALL)
	private Account account;

	public Client() {

	}

	public Client(String cpf, String name, Account account) {
		this.cpf = cpf;
		this.name = name;
		this.account = account;
	}

    public String getCpf() {
		return cpf;
	}

	public void setCpf(String cpf) {
		this.cpf = cpf;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Account getAccount() {
		return account;
	}

	public void setAccount(Account account) {
		this.account = account;
	}
}

