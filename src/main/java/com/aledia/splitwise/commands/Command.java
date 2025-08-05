package com.aledia.splitwise.commands;

public interface Command {
    boolean matches(String input);
    void executes(String input);
}

