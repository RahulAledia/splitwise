package com.aledia.splitwise.strategy;

import com.aledia.splitwise.models.Expense;

import java.util.List;

public interface SettleUpStrategy {
    List<Transaction> settle(List<Expense> expenses);
}
