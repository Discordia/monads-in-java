package org.discordia.monad.fwk;

public abstract class Monad<M, A> implements Joinable<M, A> {

    public <T> Monad<M, T> bind(Applicable<A, Monad<M, T>> f) throws Failure {

        Applicable<Monad<M, A>, Monad<M, Monad<M, T>>> a = (Applicable <Monad<M, A>, Monad<M, Monad <M, T>>>) fmap(f);

        Monad<M, Monad<M, T>> mmonad = a.apply(this);

        return (Monad<M, T>) mmonad.join();
    }


    public Monad<M, A> fail(String ex) throws Failure {
        throw new Failure(ex);
    }
}
