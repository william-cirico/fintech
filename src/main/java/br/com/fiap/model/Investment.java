package br.com.fiap.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class Investment {
    private Long id;
    private double amount;
    private LocalDate date;
    private String investmentType;
    private String risk;
    private String liquidity;
    private LocalDate dueDate;
    private double profitability;
    private LocalDateTime createdAt;
    private long account_id;

    public Investment( double amount, double profitability, LocalDate date){
        this.amount = amount;
        this.profitability = profitability;
        this.date = date;
    }
    public Investment(double amount, LocalDate date, String investmentType, String risk, String liquidity, LocalDate dueDate, double profitability, long account_id){
        this.amount = amount;
        this.date = date;
        this.investmentType = investmentType;
        this.risk = risk;
        this.liquidity = liquidity;
        this.dueDate = dueDate;
        this.profitability = profitability;
        this.account_id = account_id;
    }

    public Investment(Long id, double amount, double profitability, LocalDate date, String investmentType,
                      String risk, String liquidity, LocalDate dueDate, LocalDateTime createdAt) {
        this.id = id;
        this.amount = amount;
        this.date = date;
        this.investmentType = investmentType;
        this.risk = risk;
        this.liquidity = liquidity;
        this.dueDate = dueDate;
        this.profitability = profitability;
        this.createdAt = createdAt;

    }

    public void setAccount_id(long account_id) {
        this.account_id = account_id;
    }

    public Long getAccountID() { return account_id;}

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public double getAmount() {
        return amount;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public void setInvestmentType(String investmentType) {
        this.investmentType = investmentType;
    }

    public void setRisk(String risk) {
        this.risk = risk;
    }

    public void setLiquidity(String liquidity) {
        this.liquidity = liquidity;
    }

    public void setDueDate(LocalDate dataVencimento) {
        this.dueDate = dataVencimento;
    }

    public LocalDate getDate() {
        return date;
    }

    public String getInvestmentType() {
        return investmentType;
    }

    public String getRisk() {
        return risk;
    }

    public String getLiquidity() {
        return liquidity;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public double getProfitability() {
        return profitability;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public Long getId() {
        return id;
    }

    @Override
    public String toString() {

        if(risk == null){
            risk = "Não informado";
        }

        if(investmentType == null){
            investmentType = "Não informado";
        }
        if(liquidity == null){
            liquidity = "Não informado";
        }


        return "Valor: " + amount + "\n" +
                "Data: " + date + "\n" +
                "Tipo do investimento: " + investmentType + "\n" +
                "Risco: " + risk + "\n" +
                "Liquidez: " + liquidity + "\n" +
                "DataVencimento: " + dueDate + "\n" +
                "Rentabilidade: " + profitability + "\n";
    }
}
