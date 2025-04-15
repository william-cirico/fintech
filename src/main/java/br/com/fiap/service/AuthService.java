package br.com.fiap.service;

import br.com.fiap.dao.UserDao;
import br.com.fiap.model.User;
import org.mindrot.jbcrypt.BCrypt;

import java.util.*;

/**
 * Classe responsável pelo gerenciamento de autenticação de usuários,
 * incluindo registro, login e armazenamento seguro de senhas.
 */
public class AuthService {
    UserDao userDao = new UserDao();

    /**
     * Retorna a lista de usuários cadastrados.
     *
     * @return Lista de usuários registrados.
     */

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
        // Criando e armazenando o novo usuário com a senha criptografada
        User newUser = new User(name, cpf, username, hashPassword(password));
        userDao.insert(newUser);
    }
    public void updateUser(User user){
        user.setPassword(hashPassword(user.getPassword()));
        userDao.update(user);
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
        Optional<User> foundUser = userDao.findAll().stream().
                filter(user-> user.getUsername().equalsIgnoreCase(username)).findFirst();
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
}

