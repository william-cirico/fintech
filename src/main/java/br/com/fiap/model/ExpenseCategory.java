package br.com.fiap.model;

public class ExpenseCategory {
    private final int id;
    private String name;
    private ExpenseCategoryType type;

    public ExpenseCategory(int id, String name, ExpenseCategoryType type) {
        this.id = id;
        this.name = name;
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ExpenseCategoryType getType() {
        return type;
    }

    public void setType(ExpenseCategoryType type) {
        this.type = type;
    }
}
