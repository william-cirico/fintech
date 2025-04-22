package unit.service;

import br.com.fiap.dao.UserDao;
import br.com.fiap.exceptions.CpfAlreadyExistsException;
import br.com.fiap.exceptions.PasswordsDoNotMatchException;
import br.com.fiap.exceptions.UsernameAlreadyExistsException;
import br.com.fiap.model.User;
import br.com.fiap.service.AuthService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mindrot.jbcrypt.BCrypt;
import org.mockito.Mock;

import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


class AuthServiceTest {
    private final UserDao userDaoMock = mock(UserDao.class);
    private final AuthService service = new AuthService(userDaoMock);

    @Test
    @DisplayName("Deve permitir cadastrar um usuário válido")
    void shouldRegisterUser() {
        // ARRANGE
        when(userDaoMock.existsByCPF("cpf")).thenReturn(false);
        when(userDaoMock.findByUsername("email")).thenReturn(Optional.empty());
        when(userDaoMock.insert(any(User.class))).thenReturn(any());

        // ACT + ASSERT
        assertDoesNotThrow(
            () -> service.registerUser("name", "cpf", "email", "password", "password"),
            "Não deve lançar uma exceção ao criar o usuário"
        );
        verify(userDaoMock, times(1)).insert(any(User.class));
    }

    @Test
    @DisplayName("Não deve permitir o cadastro de um usuário com um e-mail já existente")
    void shouldFailToRegisterUserWithExistingEmail() {
        // ARRANGE
        String existingEmail = "email@email.com";
        User existingUser = new User(1L, "nome", "cpf", existingEmail, "password");
        when(userDaoMock.findByUsername(existingEmail)).thenReturn(Optional.of(existingUser));

        // ACT + ASSERT
        assertThrows(
                UsernameAlreadyExistsException.class,
                () -> service.registerUser("name", "cpf", existingEmail, "password", "password"),
                "Deveria lançar exceção ao cadastrar um usuário com um e-mail já existente"
        );
    }

    @Test
    @DisplayName("Não deve permitir o cadastro de um usuário com um cpf já existente")
    void shouldFailToRegisterUserWithExistingCpf() {
        // ARRANGE
        String existingCpf = "cpf";
        when(userDaoMock.existsByCPF(existingCpf)).thenReturn(true);

        // ACT + ASSERT
        assertThrows(
                CpfAlreadyExistsException.class,
                () -> service.registerUser("name", existingCpf, "username", "password", "password"),
                "Deveria lançar exceção ao cadastrar um usuário com um cpf já existente"
        );
    }

    @Test
    @DisplayName("Não deve permitir o cadastro se as senhas forem diferentes")
    void shouldFailToRegisterUserIfPasswordsDoesNotMatch() {
        assertThrows(
                PasswordsDoNotMatchException.class,
                () -> service.registerUser("name", "cpf", "email", "password", "differentPassword"),
                "Deveria lançar exceção se as senhas não forem iguais"
        );
    }

    @Test
    @DisplayName("Não deve permitir login com e-mail que não existe")
    void shouldFailToLoginUserIfEmailDoesNotExist() {
        // ARRANGE
        when(userDaoMock.findByUsername("email")).thenReturn(Optional.empty());

        // ACT + ASSERT
        assertThrows(
                NoSuchElementException.class,
                () -> service.login("email", "password"),
                "Deveria lançar exceção ao logar com um e-mail que não existe"
        );
    }

    @Test
    @DisplayName("Não deve permitir login se a senha estiver incorreta")
    void shouldFailToLoginUserIfPasswordDoesNotMatch() {
        // ARRANGE
        User user = new User(1L, "nome", "cpf", "email", "password");
        when(userDaoMock.findByUsername("email")).thenReturn(Optional.of(user));

        // ACT + ASSERT
        assertThrows(
                IllegalArgumentException.class,
                () -> service.login("email", "wrongPassword"),
                "Deveria lançar uma exceção se a senha for inválida"
        );
    }

    @Test
    @DisplayName("Deve permitir logar um usuário")
    void shouldLoginUser() {
        // ARRANGE
        String hashedPassword = BCrypt.hashpw("password", BCrypt.gensalt());
        User user = new User(1L, "nome", "cpf", "email", hashedPassword);
        when(userDaoMock.findByUsername("email")).thenReturn(Optional.of(user));

        // ACT + ASSERT
        Assertions.assertDoesNotThrow(
                () -> service.login("email", "password"),
                "Não deveria lançar exceção ao logar um usuário válido"
        );
    }

    @Test
    @DisplayName("A senha armazenada para o usuário criado deve ser um hash")
    void shouldStorePasswordAsHash() {
        String senhaOriginal = "senha123";

        final User[] capturedUser = new User[1];

        doAnswer(invocation -> {
            capturedUser[0] = invocation.getArgument(0);
            return null;
        }).when(userDaoMock).insert(any(User.class));

        // ACT
        service.registerUser("Nome", "12345678900", "email@teste.com", senhaOriginal, senhaOriginal);

        // ASSERT
        assertNotNull(capturedUser[0], "Usuário deveria ter sido passado para o DAO");
        assertNotEquals(senhaOriginal, capturedUser[0].getPassword(), "A senha não deve ser armazenada em texto puro");
        assertTrue(capturedUser[0].getPassword().startsWith("$2a$") || capturedUser[0].getPassword().startsWith("$2b$"),
                "A senha deve estar criptografada com BCrypt");
        assertTrue(BCrypt.checkpw(senhaOriginal, capturedUser[0].getPassword()), "O hash deve corresponder à senha original");
    }
}