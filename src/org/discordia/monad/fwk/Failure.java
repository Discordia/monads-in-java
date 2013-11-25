package org.discordia.monad.fwk;

public class Failure extends Exception {
    public Failure(String reason) {
        super(reason);
    }
}
