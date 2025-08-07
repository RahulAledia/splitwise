package com.aledia.splitwise.Services.expense;

import com.aledia.splitwise.Repository.UserExpenseRepository;
import com.aledia.splitwise.Repository.UserRepository;
import com.aledia.splitwise.models.Expense;
import com.aledia.splitwise.models.User;
import com.aledia.splitwise.models.UserExpense;
import com.aledia.splitwise.strategy.Transaction;
import com.aledia.splitwise.strategy.TwoSetsSettleUpStrategy;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ExpenseService {

    private UserRepository userRepository;
    private UserExpenseRepository userExpenseRepository;
    private TwoSetsSettleUpStrategy twoSetsSettleUpStrategy;

    @Autowired
    public ExpenseService(UserRepository userRepository, UserExpenseRepository userExpenseRepository, TwoSetsSettleUpStrategy twoSetsSettleUpStrategy){
        this.userRepository = userRepository;
        this.userExpenseRepository = userExpenseRepository;
        this.twoSetsSettleUpStrategy = twoSetsSettleUpStrategy;
    }

    public List<Transaction> settleUpUser(Long userId){
        Optional<User> userOptional = userRepository.findById(userId);

        if(userOptional.isEmpty()){
            // throw exception.
            return null;
        }

        List<UserExpense> userExpenses = userExpenseRepository.findAllByUser(userOptional.get());
        List<Expense> expensesInvolvingUser = new ArrayList<>();

        for(UserExpense userExpense: userExpenses){
            expensesInvolvingUser.add(userExpense.getExpense());
        }

        List<Transaction> transactions = twoSetsSettleUpStrategy.settle(expensesInvolvingUser);

        return null;
    }
}
