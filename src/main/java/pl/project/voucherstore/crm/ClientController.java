package pl.project.voucherstore.crm;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
public class ClientController {

    @Autowired
    ClientsRepository clientsRepository;

    @PostMapping("/api/clients")
    public void addClient(@Valid @RequestBody Client client) {
        clientsRepository.save(client);
    }

    @GetMapping("/api/clients")
    public Iterable<Client> clients() {
        return clientsRepository.findAll();
    }

    @DeleteMapping("/api/clients/{id}")
    public void delete(@PathVariable Integer id) {
        clientsRepository.deleteById(id);
    }
}
