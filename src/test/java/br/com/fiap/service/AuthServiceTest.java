package br.com.fiap.service;

import br.com.fiap.model.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;


class AuthServiceTest {
    AuthService service = new AuthService();

    @Test
    @DisplayName("Deve permitir cadastrar um usuário válido")
    void shouldRegisterUser() {
        // ARRANGE
        int initialSize = service.getUsers().size();

        // ACT + ASSERT
        assertDoesNotThrow(
                () -> service.registerUser("name", "cpf", "email", "password", "password"),
                "Não deve lançar uma exceção ao criar o usuário"
        );
        assertEquals(initialSize + 1, service.getUsers().size(), "A lista de usuários deveria ter um usuário a mais");
    }

    @Test
    @DisplayName("Não deve permitir o cadastro de um usuário com um e-mail já existente")
    void shouldFailToRegisterUserWithExistingEmail() {
        // ARRANGE
        service.registerUser("name", "cpf", "email", "password", "password");
        int initialSize = service.getUsers().size();

        // ACT + ASSERT
        assertThrows(
                IllegalArgumentException.class,
                () -> service.registerUser("name", "cpf", "email", "password", "password"),
                "Deveria lançar exceção ao cadastrar um usuário com um e-mail já existente"
        );
        assertEquals(initialSize, service.getUsers().size(), "A quantidade de usuários cadastrados deveria ser 1");
    }

    @Test
    @DisplayName("Não deve permitir o cadastro se as senhas forem diferentes")
    void shouldFailToRegisterUserIfPasswordsDoesNotMatch() {
        assertThrows(
                IllegalArgumentException.class,
                () -> service.registerUser("name", "cpf", "email", "password", "differentPassword"),
                "Deveria lançar exceção se as senhas não forem iguais"
        );
    }

    @Test
    @DisplayName("Não deve permitir login com e-mail que não existe")
    void shouldFailToLoginUserIfEmailDoesNotExist() {
        // ARRANGE
        service.registerUser("name", "cpf", "differentEmail", "password", "password");

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
        service.registerUser("name", "cpf", "email", "password", "password");

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
        service.registerUser("name", "cpf", "email", "password", "password");

        // ACT + ASSERT
        assertDoesNotThrow(
                () -> service.login("email", "password"),
                "Não deveria lançar exceção ao logar um usuário válido"
        );
    }

    @Test
    @DisplayName("A senha armazenada para o usuário criado deve ser um hash")
    void shouldStorePasswordAsHash() {
        // ARRANGE
        service.registerUser("name", "cpf", "email", "password", "password");

        // ACT
        User user = service.login("email", "password");

        // ASSERT
        assertNotEquals("password", user.getPassword(), "A senha armazenada não pode ser igual a senha original");
    }
}