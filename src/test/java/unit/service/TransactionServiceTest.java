package unit.service;

import br.com.fiap.dao.AccountDao;
import br.com.fiap.dao.ExpenseDao;
import br.com.fiap.dao.IncomeDao;
import br.com.fiap.dao.TransferDao;
import br.com.fiap.model.Account;
import br.com.fiap.model.ExpenseCategory;
import br.com.fiap.service.TransactionService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
class TransactionServiceTest {
    private final ExpenseDao expenseDao = mock(ExpenseDao.class);
    private final IncomeDao incomeDao = mock(IncomeDao.class);
    private final AccountDao accountDao = mock(AccountDao.class);
    private final TransferDao transferDao = mock(TransferDao.class);

    @Mock
    private Account account;

    @Mock
    private ExpenseCategory expenseCategory;

    private final TransactionService service = new TransactionService(incomeDao, accountDao, expenseDao, transferDao);

    @Test
    @DisplayName("Não deve permitir adicionar uma despesa com valor negativo")
    void shouldFailToAddExpenseIfAmountIsNegative() {
        // ACT + ASSERT
        assertThrows(
                IllegalArgumentException.class,
                () -> service.addExpense(account, LocalDate.now(), -100, expenseCategory),
                "Deveria lançar exceção ao tentar adicionar despesa negativa"
        );
    }

    @Test
    @DisplayName("Não deve permitir adicionar uma despesa com valor zero")
    void shouldFailToAddExpenseIfAmountIsZero() {
        assertThrows(
                IllegalArgumentException.class,
                () -> service.addExpense(
                        account,
                        LocalDate.now(),
                        0,
                        expenseCategory
                ),
                "Deveria lançar exceção ao tentar adicionar uma despesa com valor zero"
        );
    }

    @Test
    @DisplayName("Não deve permitir adicionar despesa se o saldo for insuficiente")
    void shouldFailToAddExpenseIfAccountDoesNotHaveBalance() {
        // ARRANGE
        Account account = new Account(null, "Conta teste", 99, LocalDateTime.now(), null);

        assertThrows(
                IllegalArgumentException.class,
                () -> service.addExpense(account, LocalDate.now(), 100, expenseCategory),
                "Deveria lançar exceção ao tentar adicionar despesa sem saldo"
        );
        assertEquals(99, account.getBalance(), "O saldo deve permanecer inalterado");
    }

    @Test
    @DisplayName("Deve permitir adicionar uma despesa válida")
    void shouldAddExpense() {
        // ARRANGE
        Account account = new Account(null, "Conta teste", 100, LocalDateTime.now(), null);

        // ACT
        service.addExpense(account, LocalDate.now(), 100, expenseCategory);

        // ASSERT
        assertEquals(0, account.getBalance(), "O saldo deveria ser 0 após a despesa");
    }

    @Test
    @DisplayName("Não deve permitir adicionar uma receita com valor negativo")
    void shouldFailToAddIncomeIfAmountIsNegative() {
        assertThrows(
                IllegalArgumentException.class,
                () -> service.addIncome(account, LocalDate.now(), -100),
                "Deveria lançar exceção ao tentar adicionar uma receita negativa"
        );
    }

    @Test
    @DisplayName("Não deve permitir adicionar uma receita com valor zero")
    void shouldFailToAddIncomeIfAmountIsZero() {
        assertThrows(
                IllegalArgumentException.class,
                () -> service.addIncome(account, LocalDate.now(), 0),
                "Deveria lançar exceção ao tentar adicionar uma receita com valor zero"
        );
    }

    @Test
    @DisplayName("Deve permitir adicionar uma receita válida")
    void shouldAddIncome() {
        // ARRANGE
        Account account = new Account(null, "Conta teste", 200, LocalDateTime.now(), null);

        // ACT
        service.addIncome(account, LocalDate.now(), 100);

        // ASSERT
        assertEquals(300, account.getBalance(), "O saldo deveria ser 300 após a receita");
    }

    @Test
    @DisplayName("Não deve permitir adicionar uma transferência se as contas de origem e destino forem iguais")
    void shouldFailToAddTransferIfAccountsAreEqual() {
        Account account = new Account(null, "Teste", 500, LocalDateTime.now(), null);

        assertThrows(
                IllegalArgumentException.class,
                () -> service.addTransfer(account, account, LocalDate.now(), 100),
                "Deveria lançar exceção ao adicionar transferência entre contas iguais"
        );
    }

    @Test
    @DisplayName("Não deve permitir adicionar uma transferência com o valor negativo")
    void shouldFailToAddTransferIfAmountIsNegative() {
        // ARRANGE
        Account from = new Account(null, "Conta A", 500, LocalDateTime.now(), null);
        Account to = new Account(null, "Conta B", 100, LocalDateTime.now(), null);

        // ACT + ASSERT
        assertThrows(
                IllegalArgumentException.class,
                () -> service.addTransfer(from, to, LocalDate.now(), -100),
                "Deveria lançar exceção ao adicionar transferência com valor negativo"
        );
        assertEquals(500, from.getBalance(), "O saldo da conta de origem deveria ser 500");
        assertEquals(100, to.getBalance(), "O saldo da conta de destino deveria ser 100");
    }

    @Test
    @DisplayName("Não deve permitir adicionar uma transferência com valor zero")
    void shouldFailToAddTransferIfAmountIsZero() {
        // ARRANGE
        Account from = new Account(null, "Conta A", 500, LocalDateTime.now(), null);
        Account to = new Account(null, "Conta B", 100, LocalDateTime.now(), null);

        // ACT + ASSERT
        assertThrows(
                IllegalArgumentException.class,
                () -> service.addTransfer(from, to, LocalDate.now(), 0),
                "Deveria lançar exceção ao tentar adicionar uma transferência com valor zero"
        );
        assertEquals(500, from.getBalance(), "O saldo da conta de origem deveria ser 500");
        assertEquals(100, to.getBalance(), "O saldo da conta de destino deveria ser 100");
    }

    @Test
    @DisplayName("Não deve permitir adicionar uma transferência se a conta origem não possui saldo suficiente")
    void shouldFailToAddTransferIfFromAccountDoesNotHaveEnoughBalance() {
        // ARRANGE
        Account from = new Account(null, "Conta A", 49, LocalDateTime.now(), null);
        Account to = new Account(null, "Conta B", 100, LocalDateTime.now(), null);

        // ACT + ASSERT
        assertThrows(
                IllegalArgumentException.class,
                () -> service.addTransfer(from, to, LocalDate.now(), 50),
                "Deveria lançar execeção ao adicionar transferência se a conta de destino não possuir saldo suficiente"
        );
        assertEquals(49, from.getBalance(), "O saldo da conta de origem deveria ser 49");
        assertEquals(100, to.getBalance(), "O saldo da conta de destino deveria ser 100");
    }

    @Test
    @DisplayName("Deve permitir adicionar uma transferência válida")
    void shouldAddTransfer() {
        // ARRANGE
        Account from = new Account(null, "Conta A", 100, LocalDateTime.now(), null);
        Account to = new Account(null, "Conta B", 100, LocalDateTime.now(), null);

        // ACT + ASSERT
        assertDoesNotThrow(() -> service.addTransfer(from, to, LocalDate.now(), 100), "Não deveria lançar exceção ao adicionar uma transferência válida");
        assertEquals(0, from.getBalance(), "O saldo da conta de origem deveria ser 0");
        assertEquals(200, to.getBalance(), "O saldo da conta de destino deveria ser 200");
    }
}