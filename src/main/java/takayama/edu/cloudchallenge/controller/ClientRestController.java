package takayama.edu.cloudchallenge.controller;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import takayama.edu.cloudchallenge.controller.dto.ClientDto;
import takayama.edu.cloudchallenge.domain.interfaces.ClientService;
import takayama.edu.cloudchallenge.domain.model.Client;


/*
 * Rest API controller for client. This class acts as a facade to other subsystems.
*/
@CrossOrigin
@RestController
@RequestMapping("/clients")
@Tag(name = "Clients Controller", description = "RESTful API for managing clients.")
public class ClientRestController {

    @Autowired
    private ClientService clientService;

    @GetMapping
    @Operation(summary = "Get all users", description = "Retrieve a list of all registered users")
    @ApiResponses(value = { 
            @ApiResponse(responseCode = "200", description = "Operation successful")
    })
	public ResponseEntity<List<ClientDto>> findAll() {
		// return ResponseEntity.ok(clientService.findAll());
        var clients = clientService.findAll();
        var clientsDto = clients.stream().map(ClientDto::new).collect(Collectors.toList());
        return ResponseEntity.ok(clientsDto);
	}

	@GetMapping("/{cpf}")
    @Operation(summary = "Get a client by cpf", description = "Retrieve a specific client based on its cpf")
    @ApiResponses(value = { 
            @ApiResponse(responseCode = "200", description = "Operation successful"),
            @ApiResponse(responseCode = "404", description = "Client not found")
    })
	public ResponseEntity<ClientDto> findByCpf(@PathVariable String cpf) {
        // return ResponseEntity.ok(clientService.findById(cpf));
        Client client = clientService.findById(cpf);
        ClientDto clientDto = new ClientDto(client);
        return ResponseEntity.ok(clientDto);
	}

	@PostMapping
    @Operation(summary = "Create a new client", description = "Create a new client and return the created client's data")
    @ApiResponses(value = { 
            @ApiResponse(responseCode = "201", description = "Client created successfully"),
            @ApiResponse(responseCode = "422", description = "Invalid client data provided")
    })
	public ResponseEntity<ClientDto> insert(@RequestBody ClientDto clientDto) {
        var client = clientService.insert(clientDto.toModel());
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{cpf}")
                .buildAndExpand(client.getCpf())
                .toUri();
        return ResponseEntity.created(location).body(new ClientDto(client));
	}

	@PutMapping
    @Operation(summary = "Update a client", description = "Update the data of an existing client based on its cpf")
    @ApiResponses(value = { 
            @ApiResponse(responseCode = "200", description = "Client updated successfully"),
            @ApiResponse(responseCode = "404", description = "Client not found"),
            @ApiResponse(responseCode = "422", description = "Invalid Client data provided")
    })
	public ResponseEntity<ClientDto> atualizar(@RequestBody ClientDto clientDto) {
        Client client = clientService.update(clientDto.toModel());
        return ResponseEntity.ok(new ClientDto(client));
	}

	@DeleteMapping("/{cpf}")
    @Operation(summary = "Delete a client", description = "Delete an existing client based on its ID")
    @ApiResponses(value = { 
            @ApiResponse(responseCode = "204", description = "Client deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Client not found")
    })
	public ResponseEntity<Void> deletar(@PathVariable String cpf) {
        clientService.delete(cpf);
		return ResponseEntity.ok().build();	
	}
    
    @GetMapping("/balance/{cpf}")
    @Operation(summary = "Get a client's balance based on cpf", description = "Retrieve a specific client's balance based on its cpf")
    @ApiResponses(value = { 
            @ApiResponse(responseCode = "200", description = "Operation successful"),
            @ApiResponse(responseCode = "404", description = "Client not found")
    })
	public ResponseEntity<Double> checkBalanceByCpf(@PathVariable String cpf) {
        return ResponseEntity.ok(clientService.checkBalance(cpf));
	}

    @PutMapping("/deposit/{value}")
    @Operation(summary = "Deposits a value in client's balance", description = "Update the balance of an existing client based on its cpf")
    @ApiResponses(value = { 
            @ApiResponse(responseCode = "200", description = "Client updated successfully"),
            @ApiResponse(responseCode = "404", description = "Client not found"),
            @ApiResponse(responseCode = "422", description = "Invalid Client data provided")
    })
	public ResponseEntity<Double> deposit(@PathVariable Double value, @RequestBody ClientDto clientDto) {
        String cpf = clientDto.toModel().getCpf();
        clientService.deposit(cpf, value);
        return ResponseEntity.ok(clientService.checkBalance(cpf));
	}

    @PutMapping("/withdraw/{value}")
    @Operation(summary = "Withdraw a value in client's balance", description = "Update the balance of an existing client based on its cpf")
    @ApiResponses(value = { 
            @ApiResponse(responseCode = "200", description = "Client updated successfully"),
            @ApiResponse(responseCode = "404", description = "Client not found"),
            @ApiResponse(responseCode = "422", description = "Invalid Client data provided")
    })
	public ResponseEntity<Double> withdraw(@PathVariable Double value, @RequestBody ClientDto clientDto) {
        String cpf = clientDto.toModel().getCpf();
        clientService.withdraw(cpf, value);
        return ResponseEntity.ok(clientService.checkBalance(cpf));
	}

}
