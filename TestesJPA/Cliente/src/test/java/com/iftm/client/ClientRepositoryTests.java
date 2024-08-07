package com.iftm.client;

import static org.assertj.core.api.Assertions.assertThat;

import com.iftm.client.entities.Client;
import com.iftm.client.repositories.ClientRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@DataJpaTest
public class ClientRepositoryTests {

    @Autowired
    private ClientRepository clientRepository;

    @BeforeEach
    public void setup() {
       
        clientRepository.save(new Client(null, "Conceição Evaristo", "10619244881", 1500.0, Instant.parse("2020-07-13T20:50:00Z"), 2));
        clientRepository.save(new Client(null, "Lázaro Ramos", "10619244881", 2500.0, Instant.parse("1996-12-23T07:00:00Z"), 2));
        clientRepository.save(new Client(null, "Clarice Lispector", "10919444522", 3800.0, Instant.parse("1960-04-13T07:50:00Z"), 2));
        
    }

    // 1. Testar o metodo que retorna o cliente com um nome existente 
    @Test
    public void testFindByNameIgnoreCase_existingName() {
        List<Client> clients = clientRepository.findByNameIgnoreCase("Clarice Lispector");
        assertThat(clients).isNotEmpty();
        assertThat(clients.get(0).getName()).isEqualTo("Clarice Lispector");
    }

    // Testar o metodo que retorna o cliente com um nome não existente 
    @Test
    public void testFindByNameIgnoreCase_nonExistingName() {
        List<Client> clients = clientRepository.findByNameIgnoreCase("Non Existing");
        assertThat(clients).isEmpty();
    }

    // 2. Testar o metodo que retorna varios clientes com uma parte similar do nome
    @Test
    public void testFindByNameContainingIgnoreCase_existingText() {
        List<Client> clients = clientRepository.findByNameIgnoreCase("a");
        assertThat(clients).isNotEmpty();
    }

    // Testar um texto inexistente
    @Test
    public void testFindByNameIgnoreCase_nonExistingText() {
        List<Client> clients = clientRepository.findByNameIgnoreCase("xyz");
        assertThat(clients).isEmpty();
    }

    // Testar um nome vazio
    @Test
    public void testFindByNameIgnoreCase_emptyText() {
        List<Client> clients = clientRepository.findByNameIgnoreCase("");
        List<Client> allClients = clientRepository.findAll();
        assertThat(clients.size()).isEqualTo(allClients.size());
    }

    // 3. Testar o metodo que retorna varios clientes baseado no salario
    // busca os clientes com salarios superiores a um valor
    @Test
    public void testFindByIncomeGreaterThan() {
        List<Client> clients = clientRepository.findByIncomeGreaterThan(3000.0);
        assertThat(clients).isNotEmpty();
        assertThat(clients).allMatch(client -> client.getIncome() > 3000.0);
    }

// busca os clientes com salarios inferiores a um valor
    @Test
    public void testFindByIncomeLessThan() {
        List<Client> clients = clientRepository.findByIncomeLessThan(2000.0);
        assertThat(clients).isNotEmpty();
        assertThat(clients).allMatch(client -> client.getIncome() < 2000.0);
    }

    // busca os clientes com salarios dentro de uma faixa de valores
    @Test
    public void testFindByIncomeBetween() {
        List<Client> clients = clientRepository.findByIncomeBetween(2000.0, 4000.0);
        assertThat(clients).isNotEmpty();
        assertThat(clients).allMatch(client -> client.getIncome() >= 2000.0 && client.getIncome() <= 4000.0);
    }

    // 4. Testa o metodo que retorna varios clientes baseado na sua data de nascimento
    @Test
    public void testFindByBirthDateBetween() {
        Instant dataI = Instant.parse("1940-01-01T00:00:00Z");
        Instant dataT = Instant.now();
        List<Client> clients = clientRepository.findByBirthDateBetween(dataI, dataT);
        assertThat(clients).isNotEmpty();
    }

    // 5. Testar o metodo que atualiza um cliente
    @Test
    public void testUpdateClient() {
        Client client = clientRepository.findByNameIgnoreCase("Lázaro Ramos").get(0);
        client.setName("Lázaro New");
        client.setIncome(5000.0);
        client.setBirthDate(Instant.parse("1995-12-23T07:00:00Z"));
        clientRepository.save(client);

        Optional<Client> updatedClient = clientRepository.findById(client.getId());
        assertThat(updatedClient).isPresent();
        assertThat(updatedClient.get().getName()).isEqualTo("Lázaro New");
        assertThat(updatedClient.get().getIncome()).isEqualTo(5000.0);
        assertThat(updatedClient.get().getBirthDate()).isEqualTo(Instant.parse("1995-12-23T07:00:00Z"));
    }
}
