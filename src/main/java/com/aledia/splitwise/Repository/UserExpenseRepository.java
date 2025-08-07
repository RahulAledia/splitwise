package com.aledia.splitwise.Repository;

import com.aledia.splitwise.models.Expense;
import com.aledia.splitwise.models.User;
import com.aledia.splitwise.models.UserExpense;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserExpenseRepository extends JpaRepository<UserExpense, Long> {
        List<UserExpense> findAllByUser(User user);

        List<UserExpense> findAllByExpenseIn(List<Expense> expenses);
}
