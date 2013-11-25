package org.discordia.monad.fwk;

public interface Functor<F, T> {

    public <T1> Applicable<? extends Functor<F, T>, ? extends Functor<F, T1>> fmap(Applicable<T, T1> f) throws Failure;

    public T arg() throws Failure;
}