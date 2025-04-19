package br.com.fiap.model;

import java.sql.Timestamp;

public class ExpenseCategory {
    private final Long id;
    private String name;
    private ExpenseCategoryType type;
    private Timestamp createdAt;
    private String color;
    private String icon;

    public ExpenseCategory(Long id, String name, ExpenseCategoryType type) {
        this.id = id;
        this.name = name;
        this.type = type;
    }

    public ExpenseCategory(Long id, String name, ExpenseCategoryType type, Timestamp createdAt, String color, String icon) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.createdAt = createdAt;
        this.color = color;
        this.icon = icon;
    }

    public Long getId() {
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
