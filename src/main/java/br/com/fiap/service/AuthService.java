package br.com.fiap.service;

import br.com.fiap.dao.UserDao;
import br.com.fiap.factory.UserValidationFactory;
import br.com.fiap.model.User;
import br.com.fiap.validations.CPFValidation;
import br.com.fiap.validations.PasswordMatchValidation;
import br.com.fiap.validations.UserValidation;
import br.com.fiap.validations.UsernameValidation;
import org.mindrot.jbcrypt.BCrypt;

import java.util.*;

/**
 * Classe responsável pelo gerenciamento de autenticação de usuários,
 * incluindo registro, login e armazenamento seguro de senhas.
 */
public class AuthService {
    private final UserDao userDao;
    private final UserValidationFactory userValidationFactory;

    public AuthService(UserDao userDao) {
        this.userDao = userDao;
        this.userValidationFactory = new UserValidationFactory(userDao);
    }

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
        User userValidation = new User(null, name, cpf, username, password);


        List<UserValidation> validations = userValidationFactory.createRegisterUserValidations(password, confirmPassword);

        for (UserValidation validation : validations){
            validation.validate(userValidation);
        }

        // Criando e armazenando o novo usuário com a senha criptografada
        User newUser = new User(null, name, cpf, username, hashPassword(password));
        userDao.insert(newUser);
    }

    public void updateUser(User user, boolean decisionAlterPassword){
        User userDataBefore = userDao.findById(user.getId());

        if(decisionAlterPassword){
            if(userDataBefore.getPassword().equals(user.getPassword())){
                throw new IllegalArgumentException("A nova senha deve ser diferente da senha atual");
            }
        }
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
        Optional<User> foundUser = userDao.findByUsername(username);
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
}

