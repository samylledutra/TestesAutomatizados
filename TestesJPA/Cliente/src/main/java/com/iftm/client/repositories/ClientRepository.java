package com.iftm.client.repositories;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.iftm.client.entities.Client;

import java.time.Instant;
import java.util.List;

public interface ClientRepository extends JpaRepository<Client, Long> {

    // Buscar clientes pelo seu nome usando LOWER 
    @Query("SELECT c FROM Client c WHERE LOWER(c.name) LIKE LOWER(CONCAT('%', :name, '%'))")
    List<Client> findByNameIgnoreCase(@Param("name") String name);

    // Buscar clientes com salários superiores a um valor
    List<Client> findByIncomeGreaterThan(Double income);

    // Buscar clientes com salários inferiores a um valor
    List<Client> findByIncomeLessThan(Double income);

    // Buscar clientes com salários dentro de uma faixa de valores
    List<Client> findByIncomeBetween(Double startIncome, Double endIncome);

    // Buscar clientes com data de nascimento dentro de uma faixa de valores
    List<Client> findByBirthDateBetween(Instant startDate, Instant endDate);
}
