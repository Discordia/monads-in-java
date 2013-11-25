package org.discordia.monad.fwk;

public interface Joinable<F,T> extends Functor<F,T> {
    public Functor<F, ?> join() throws Failure;
}
