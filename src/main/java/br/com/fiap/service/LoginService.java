package br.com.fiap.service;

public class LoginService {
    public boolean autenticar(String password, String hashedPassword) {
        return true;
    }

    public String generatePasswordHash(String password) {
        return "3126483126hafdiuhsudfhasd";
    }
}
