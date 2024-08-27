package com.iftm.client.resources;

import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.startsWith;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.Instant;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.iftm.client.dto.ClientDTO;

@SpringBootTest
@AutoConfigureMockMvc
public class ClientResourcesTestsIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private Long existingId;
    private Long nonExistingId;
    private ClientDTO clientDTO;

    @BeforeEach
    public void setUp() throws Exception {
        clientDTO = new ClientDTO(null, "Clarice Lispector", "10919444522", 3800.0,
                Instant.parse("1960-04-13T07:50:00Z"), 2);
        String json = objectMapper.writeValueAsString(clientDTO);

        String response = mockMvc.perform(post("/clients/")
                .content(json)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        existingId = objectMapper.readTree(response).path("id").asLong();
        nonExistingId = 999L;
    }

    @Test
    public void findAll_RetornarTodosClientes() throws Exception {
        mockMvc.perform(get("/clients")
                .param("page", "0")
                .param("linesPerPage", "12")
                .param("direction", "ASC")
                .param("orderBy", "name"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").exists())
                .andExpect(jsonPath("$.content[0].id").exists())
                .andExpect(jsonPath("$.content[0].name").exists())
                .andExpect(jsonPath("$.content[0].cpf").exists());
    }

    @Test
    public void findById_RetornarClientes_WhenExists() throws Exception {
        mockMvc.perform(get("/clients/id/{id}", existingId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(existingId))
                .andExpect(jsonPath("$.name").value("Clarice Lispector"))
                .andExpect(jsonPath("$.cpf").value("10919444522"));
    }

    @Test
    public void findById_ShouldReturnNotFound_WhenIdDoesNotExist() throws Exception {
        mockMvc.perform(get("/clients/id/{id}", nonExistingId))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("Resource not found"))
                .andExpect(jsonPath("$.message").value("Entity not found"))
                .andExpect(jsonPath("$.path").value("/clients/" + nonExistingId));
    }

    @Test
    public void findByIncomeGreaterThan_ShouldReturnClients_WhenIncomeIsGreaterThan() throws Exception {
        double income = 3800.0;

        mockMvc.perform(get("/clients/income/")
                .param("income", String.valueOf(income))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content[0].income").value(income));
    }

    @Test
    public void findByCPFLike_RetornarClientes_WhenCpfMatchesPattern() throws Exception {
        clientDTO = new ClientDTO(null, "Test CPF", "10919444522", 3000.0,
                Instant.parse("1990-01-01T00:00:00Z"), 0);
        String json = objectMapper.writeValueAsString(clientDTO);

        mockMvc.perform(post("/clients/")
                .content(json)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());

        String cpf = "109194445";
        mockMvc.perform(get("/clients/cpf/")
                .param("cpf", cpf))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content.length()").value(greaterThan(0)))
                .andExpect(jsonPath("$.content[0].cpf").value(startsWith(cpf)));
    }


    @Test
    public void insert_CriarClientes_WhenDataIsValid() throws Exception {
        ClientDTO clientDTO = new ClientDTO(null, "João Son", "12345678901", 4500.0,
                Instant.parse("1980-10-20T07:50:00Z"), 1);
        String json = objectMapper.writeValueAsString(clientDTO);

        mockMvc.perform(post("/clients/")
                .content(json)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.name").value("João Son"));
    }

    @Test
    public void delete_RetornarNoContent_WhenIdExists() throws Exception {
        mockMvc.perform(delete("/clients/{id}", existingId))
                .andExpect(status().isNoContent());
    }

    @Test
    public void delete_RetornarNotFound_WhenIdNonExists() throws Exception {
        mockMvc.perform(delete("/clients/{id}", nonExistingId))
                .andExpect(status().isNotFound());
    }

    @Test
    public void update_RetornarUpdatedClient_WhenIdExists() throws Exception {
        ClientDTO clientDTO = new ClientDTO(null, "Maria Leny", "12345678900", 5000.0,
                Instant.parse("1985-10-20T07:50:00Z"), 1);
        String json = objectMapper.writeValueAsString(clientDTO);

        mockMvc.perform(put("/clients/{id}", existingId)
                .content(json)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Maria Leny"));
    }

    @Test
    public void update_RetornarNotFound_WhendNonExists() throws Exception {
        ClientDTO clientDTO = new ClientDTO(null, "Nao-Existe", "12345678900", 5000.0,
                Instant.parse("1985-10-20T07:50:00Z"), 1);
        String json = objectMapper.writeValueAsString(clientDTO);

        mockMvc.perform(put("/clients/{id}", nonExistingId)
                .content(json)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("Resource not found"));
    }
}
