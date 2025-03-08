package br.com.fiap.model;

import java.time.LocalDate;

public class Recebimento extends Transaction {

    public Recebimento(double valor, LocalDate data, String descricao) {
        super(valor, data, descricao, TransactionType.INCOME);
    }
}
