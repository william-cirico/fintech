package br.com.fiap.service;

import br.com.fiap.model.User;
import org.mindrot.jbcrypt.BCrypt;

import java.util.*;

public class AuthService {
    private final List<User> users = new ArrayList<>();

    public User registerUser(String name, String cpf, String username, String password, String confirmPassword) {
        // Verificando se as senhas são iguais
        if (!password.equals(confirmPassword)) {
            throw new IllegalArgumentException("As senhas não são iguais");
        }

        // Verificando se já existe um usuário cadastrado com o e-mail
        if (usernameAlreadyRegistered(username)) {
            throw new IllegalArgumentException("E-mail " + username + " já está cadastrado");
        }

        User newUser = new User(UUID.randomUUID().toString(), name, cpf, username, hashPassword(password));
        users.add(newUser);

        return newUser;
    }


    public User login(String username, String password) {
        Optional<User> foundUser = users.stream().filter(u -> u.getUsername().equalsIgnoreCase(username)).findFirst();

        if (foundUser.isEmpty()) {
            throw new NoSuchElementException("Usuário não encontrado.");
        }

        User user = foundUser.get();
        if (!verifyPassword(password, user.getPassword())) {
            throw new IllegalArgumentException("Usuário ou senha inválidos");
        }

        return user;
    }

    private boolean verifyPassword(String password, String hashedPassword) {
        return BCrypt.checkpw(password, hashedPassword);
    }

    private String hashPassword(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }

    private boolean usernameAlreadyRegistered(String username) {
        return users.stream().anyMatch(u -> u.getUsername().equalsIgnoreCase(username));
    }
}
