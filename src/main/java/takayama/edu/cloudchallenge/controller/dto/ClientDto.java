package takayama.edu.cloudchallenge.controller.dto;

import takayama.edu.cloudchallenge.domain.model.Client;

public record ClientDto(
    String cpf,
	String name) {

    public ClientDto(Client model) {
        this(
            model.getCpf(),
            model.getName()
        );        
    }

    public Client toModel() {
        Client model = new Client();
        model.setName(this.name);
        model.setCpf(this.cpf);
        return model;
    }
}
