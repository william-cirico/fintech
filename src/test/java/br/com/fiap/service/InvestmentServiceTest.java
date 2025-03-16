package br.com.fiap.service;

import br.com.fiap.model.Account;
import br.com.fiap.service.InvestmentService;

import br.com.fiap.model.Investment;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;



import java.time.LocalDate;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class InvestmentServiceTest {

    @Mock
    private Account account;

    @Mock
    InvestmentService investmentService = new InvestmentService();

    @Test
    @DisplayName("Deve permitir realizar um investimento caso tenha saldo na conta")
    void shouldAddInvestment() {
        // ARRANGE
        Account account = new Account(UUID.randomUUID().toString(), "Conta teste", 1000);

        // ACT
        Investment investment = investmentService.addInvestment(account, 400, 10, LocalDate.now());

        // ASSERT
        assertEquals(600, account.getBalance(), "O saldo deve reduzir devido ao investimento.");
        assertEquals(400, investment.getAmount(), "Deve ter investido o valor");
        assertEquals(1, account.getInvestments().size(), "Deveria ter um investimento realizado");

    }

    @Test
    @DisplayName("Não deve permitir realizar o investimento com saldo insuficiente.")
    void shouldFailToAddInvestmentIfFromAccountDoesNotHaveEnoughBalance() {

        // ARRANGE
        Account account = new Account(UUID.randomUUID().toString(), "Conta teste", 1000);

        // ACT + ASSERT
        assertThrows(IllegalArgumentException.class,
                () -> investmentService.addInvestment(account, 2000, 10, LocalDate.now()),
                "Saldo insuficiente para realizar o investimento");
    }

    @Test
    @DisplayName("Não deve permitir adicionar um investimento com valor negativo")
    void shouldFailToAddInvestmentIfAmountIsNegative() {

        // ARRANGE
        Account account = new Account(UUID.randomUUID().toString(), "Conta teste", 1000);

        // ACT + ASSERT
        assertThrows(
                IllegalArgumentException.class,
                () -> investmentService.addInvestment(account, -400, 10, LocalDate.now()),
                "Não é possível investir um valor negativo.");
    }

    @Test
    @DisplayName("Não deve permitir realizar o investimento com rentabilidade negativa")
    void shouldFailToAddInvestmentIfProfitabilityIsNegative() {

        // ARRANGE
        Account account = new Account(UUID.randomUUID().toString(), "Conta teste", 1000);

        // ACT + ASSERT
        assertThrows(IllegalArgumentException.class,
                () -> investmentService.addInvestment(account, 2000, -10, LocalDate.now()),
                "Não é possível realizar um investimento com rentabilidade menor que 0");
    }
}


