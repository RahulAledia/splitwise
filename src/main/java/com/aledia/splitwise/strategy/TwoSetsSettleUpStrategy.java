package com.aledia.splitwise.strategy;

import com.aledia.splitwise.Repository.UserExpenseRepository;
import com.aledia.splitwise.models.Expense;
import com.aledia.splitwise.models.User;
import com.aledia.splitwise.models.UserExpense;
import com.aledia.splitwise.models.UserExpenseType;
import jakarta.persistence.criteria.CriteriaBuilder;
import org.antlr.v4.runtime.misc.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class TwoSetsSettleUpStrategy implements SettleUpStrategy{


    private UserExpenseRepository userExpenseRepository;

    @Autowired
    public TwoSetsSettleUpStrategy(UserExpenseRepository userExpenseRepository){
        this.userExpenseRepository = userExpenseRepository;
    }

    @Override
    public List<Transaction> settle(List<Expense> expenses) {

        List<UserExpense> allUserExpenseForTheseExpenses = userExpenseRepository.findAllByExpenseIn(expenses);

        HashMap<User, Integer> moneyPaidExtra = new HashMap<>();

        for(UserExpense userExpense: allUserExpenseForTheseExpenses){
            User user = userExpense.getUser();
            int currentExtraPaid = 0;

            if(moneyPaidExtra.containsKey(user)){
                currentExtraPaid = moneyPaidExtra.get(user);
            }

            if(userExpense.getUserExpenseType().equals(UserExpenseType.PAID)){
                moneyPaidExtra.put(user, currentExtraPaid + userExpense.getAmount());
            } else{
                moneyPaidExtra.put(user, currentExtraPaid - userExpense.getAmount());
            }

        }

        TreeSet<Pair<User, Integer>> extraPaid = new TreeSet<>();
        TreeSet<Pair<User, Integer>> lessPaid = new TreeSet<>();

        for(Map.Entry<User, Integer> userAmount: moneyPaidExtra.entrySet()){
            if(userAmount.getValue() < 0){
                lessPaid.add(new Pair<>(userAmount.getKey(), userAmount.getValue()));
            } else {
                extraPaid.add(new Pair<>(userAmount.getKey(), userAmount.getValue()));
            }
        }

        List<Transaction> transactions = new ArrayList<>();

        while(!lessPaid.isEmpty()){

            Pair<User, Integer> lessPaidPair = lessPaid.pollFirst(); // get and removes the first value;
            Pair<User, Integer> extraPaidPair = extraPaid.pollFirst();

            Transaction t = new Transaction();
            t.setFrom(lessPaidPair.a);
            t.setTo(extraPaidPair.a);

            if(Math.abs(lessPaidPair.b) < extraPaidPair.b){ // vikram : -100 // deepika 200
                t.setAmount(Math.abs(lessPaidPair.b));

                if(!(extraPaidPair.b - Math.abs(lessPaidPair.b) == 0)){
                extraPaid.add(new Pair<>(extraPaidPair.a, extraPaidPair.b - Math.abs(lessPaidPair.b)));
                }
            } else { // vikram : -200 // deepika 100
                t.setAmount(extraPaidPair.b);

                if(!(extraPaidPair.b - Math.abs(lessPaidPair.b) == 0)){
                    lessPaid.add(new Pair<>(lessPaidPair.a, lessPaidPair.b + extraPaidPair.b));
                }
            }

        }

        return null;
    }

}
