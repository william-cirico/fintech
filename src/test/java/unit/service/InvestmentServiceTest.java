package unit.service;

import br.com.fiap.dao.InvestmentDao;
import br.com.fiap.model.Account;

import br.com.fiap.model.Investment;
import br.com.fiap.service.InvestmentService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;


import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class InvestmentServiceTest {

    @Mock
    private Account account;

    private InvestmentDao investmentDao = mock(InvestmentDao.class);

    private InvestmentService investmentService = new InvestmentService(investmentDao);

    //TODO: Ajustar os testes do investmentService

//    @Test
//    @DisplayName("Deve permitir realizar um investimento caso tenha saldo na conta")
//    void shouldAddInvestment() {
//        // ARRANGE
//        Account account = new Account(null, "Conta teste", 1000, LocalDateTime.now(), 1L);
//
//        // ACT
//        Investment investment = investmentService.addInvestment(account, 400, LocalDateTime.now(), "type", "risk", "liquidity", LocalDateTime.now());
//
//        // ASSERT
//        assertEquals(600, account.getBalance(), "O saldo deve reduzir devido ao investimento.");
//        assertEquals(400, investment.getAmount(), "Deve ter investido o valor");
//        assertEquals(1, account.getInvestments().size(), "Deveria ter um investimento realizado");
//    }
//
//    @Test
//    @DisplayName("Não deve permitir realizar o investimento com saldo insuficiente.")
//    void shouldFailToAddInvestmentIfFromAccountDoesNotHaveEnoughBalance() {
//
//        // ARRANGE
//        Account account = new Account(null, "Conta teste", 1000);
//
//        // ACT + ASSERT
//        assertThrows(IllegalArgumentException.class,
//                () -> investmentService.addInvestment(account, 2000, 10, LocalDate.now()),
//                "Saldo insuficiente para realizar o investimento");
//    }
//
//    @Test
//    @DisplayName("Não deve permitir adicionar um investimento com valor negativo")
//    void shouldFailToAddInvestmentIfAmountIsNegative() {
//
//        // ARRANGE
//        Account account = new Account(null, "Conta teste", 1000);
//
//        // ACT + ASSERT
//        assertThrows(
//                IllegalArgumentException.class,
//                () -> investmentService.addInvestment(account, -400, 10, LocalDate.now()),
//                "Não é possível investir um valor negativo.");
//    }
//
//    @Test
//    @DisplayName("Não deve permitir realizar o investimento com rentabilidade negativa")
//    void shouldFailToAddInvestmentIfProfitabilityIsNegative() {
//
//        // ARRANGE
//        Account account = new Account(null, "Conta teste", 1000);
//
//        // ACT + ASSERT
//        assertThrows(IllegalArgumentException.class,
//                () -> investmentService.addInvestment(account, 2000, -10, LocalDate.now()),
//                "Não é possível realizar um investimento com rentabilidade menor que 0");
//    }
//
//    @Test
//    @DisplayName("Não deve permitir realizar o investimento com saldo menor do que o valor do investimento e a rentabilidade negativa")
//    void shouldFailToAddInvestmentIfDoesNotHaveEnoughBalanceAndProfitabilityIsNegative() {
//
//        // ARRANGE
//        Account account = new Account(null, "Conta teste", 1000);
//
//        // ACT + ASSERT
//        assertThrows(IllegalArgumentException.class,
//                () -> investmentService.addInvestment(account, 2000, -10, LocalDate.now()),
//                "Não é possível realizar um investimento maior do que o saldo disponível e com rentabilidade negativa.");
//    }
}


