package com.aledia.splitwise.strategy;

import com.aledia.splitwise.models.User;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Transaction {
    private User from;
    private User to;
    private Integer amount;
}
