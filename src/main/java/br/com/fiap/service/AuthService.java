package br.com.fiap.service;

import br.com.fiap.model.User;
import org.mindrot.jbcrypt.BCrypt;

import java.util.*;

/**
 * Classe responsável pelo gerenciamento de autenticação de usuários,
 * incluindo registro, login e armazenamento seguro de senhas.
 */
public class AuthService {

    // Lista de usuários cadastrados (armazenamento temporário em memória)
    private final List<User> users = new ArrayList<>();

    /**
     * Retorna a lista de usuários cadastrados.
     *
     * @return Lista de usuários registrados.
     */
    public List<User> getUsers() {
        return users;
    }

    /**
     * Registra um novo usuário no sistema, verificando se o e-mail já está cadastrado e garantindo
     * que as senhas coincidem antes de armazená-las de forma segura.
     *
     * @param name Nome do usuário.
     * @param cpf CPF do usuário.
     * @param username E-mail utilizado como identificador do usuário.
     * @param password Senha de acesso (será armazenada de forma criptografada).
     * @param confirmPassword Confirmação da senha.
     * @throws IllegalArgumentException Se as senhas não forem iguais ou se o e-mail já estiver cadastrado.
     */
    public void registerUser(String name, String cpf, String username, String password, String confirmPassword) {
        // Verificando se as senhas são iguais
        if (!password.equals(confirmPassword)) {
            throw new IllegalArgumentException("As senhas não são iguais");
        }

        // Verificando se já existe um usuário cadastrado com o e-mail
        if (usernameAlreadyRegistered(username)) {
            throw new IllegalArgumentException("E-mail " + username + " já está cadastrado");
        }

        // Criando e armazenando o novo usuário com a senha criptografada
        User newUser = new User(UUID.randomUUID().toString(), name, cpf, username, hashPassword(password));
        users.add(newUser);
    }

    /**
     * Realiza o login de um usuário, verificando se o e-mail e a senha correspondem a um usuário cadastrado.
     *
     * @param username E-mail do usuário.
     * @param password Senha inserida no login.
     * @return O objeto {@link User} correspondente ao usuário autenticado.
     * @throws NoSuchElementException Se o usuário não for encontrado.
     * @throws IllegalArgumentException Se a senha estiver incorreta.
     */
    public User login(String username, String password) {
        Optional<User> foundUser = users.stream()
                .filter(u -> u.getUsername().equalsIgnoreCase(username))
                .findFirst();

        if (foundUser.isEmpty()) {
            throw new NoSuchElementException("Usuário não encontrado.");
        }

        User user = foundUser.get();
        if (!verifyPassword(password, user.getPassword())) {
            throw new IllegalArgumentException("Usuário ou senha inválidos");
        }

        return user;
    }

    /**
     * Verifica se uma senha fornecida corresponde à senha criptografada armazenada.
     *
     * @param password Senha fornecida pelo usuário.
     * @param hashedPassword Senha armazenada no sistema (criptografada).
     * @return {@code true} se a senha corresponder, {@code false} caso contrário.
     */
    private boolean verifyPassword(String password, String hashedPassword) {
        return BCrypt.checkpw(password, hashedPassword);
    }

    /**
     * Criptografa uma senha antes de armazená-la no sistema.
     *
     * @param password Senha em texto puro.
     * @return Senha criptografada utilizando o algoritmo BCrypt.
     */
    private String hashPassword(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }

    /**
     * Verifica se um e-mail já foi cadastrado no sistema.
     *
     * @param username E-mail a ser verificado.
     * @return {@code true} se o e-mail já estiver cadastrado, {@code false} caso contrário.
     */
    private boolean usernameAlreadyRegistered(String username) {
        return users.stream().anyMatch(u -> u.getUsername().equalsIgnoreCase(username));
    }
}
