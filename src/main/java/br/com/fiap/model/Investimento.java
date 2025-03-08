package br.com.fiap.model;

import java.time.LocalDate;

public class Investimento {
    private double valorAporte;
    private LocalDate dataAporte;
    private String tipoInvestimento;
    private String risco;
    private String liquidez;
    private LocalDate dataVencimento;
    private double rentabilidade;


    public Investimento(double valorAporte, double rentabilidade, LocalDate dataAporte){
        this.valorAporte = valorAporte;
        this.rentabilidade = rentabilidade;
        this.dataAporte = dataAporte;
    }

    public Investimento(double valorAporte, double rentabilidade, LocalDate dataAporte, String tipoInvestimento,
                        String risco, String liquidez, LocalDate dataVencimento) {
        this.valorAporte = valorAporte;
        this.dataAporte = dataAporte;
        this.tipoInvestimento = tipoInvestimento;
        this.risco = risco;
        this.liquidez = liquidez;
        this.dataVencimento = dataVencimento;
        this.rentabilidade = rentabilidade;
    }

    public void setValorAporte(double valorAporte) {
        this.valorAporte = valorAporte;
    }

    public double getValorAporte() {
        return valorAporte;
    }

    public void setDataAporte(LocalDate dataAporte) {
        this.dataAporte = dataAporte;
    }

    public void setTipoInvestimento(String tipoInvestimento) {
        this.tipoInvestimento = tipoInvestimento;
    }

    public void setRisco(String risco) {
        this.risco = risco;
    }

    public void setLiquidez(String liquidez) {
        this.liquidez = liquidez;
    }

    public void setDataVencimento(LocalDate dataVencimento) {
        this.dataVencimento = dataVencimento;
    }

    @Override
    public String toString() {
        return "valor: " + valorAporte + "\n" + "data: " + dataAporte +
                "\n" + "tipoInvestimento: " + tipoInvestimento + "\n" +
                "risco: " + risco + "\n" +
                "liquidez: " + liquidez + "\n" +
                "dataVencimento: " + dataVencimento +
                "\n" +  "rentabilidade: " + rentabilidade;
    }
}
