package org.discordia.monad.fwk;

public abstract class Maybe<A> extends Monad<Maybe, A> {

    public static final Maybe<?> NOTHING = new Maybe() {
        public String toString() {
            return "Nothing";
        }

        public Object arg() throws Failure {
            throw new Failure("Cannot extract a value from Nothing.");
        }

        public Functor join() throws Failure {
            return this;
        }

        protected Maybe mbBind(Applicable f) {
            return this;
        }
    };

    public static final class Just<J> extends Maybe<J> {
        private final J unit;

        public Just(J unit) {
            this.unit = unit;
        }

        public Maybe<?> join() throws Failure {
            try {
                return (Maybe<?>) unit;
            } catch (ClassCastException ex) {
                throw new Failure("Joining on a flat structure!");
            }
        }

        public String toString() {
            return "Just " + unit;
        }

        public J arg() throws Failure {
            return unit;
        }

        protected <T> Maybe<T> mbBind(Applicable<J, Monad<Maybe, T>> f) throws Failure {
            return (Maybe<T>) f.apply(unit);
        }
    }


    public static <T> Maybe<T> pure(T x) {
        return new Just<T>(x);
    }

    public Maybe<A> fail(String ex) throws Failure {
        return (Maybe<A>) NOTHING;
    }

    protected abstract <T> Maybe<T> mbBind(Applicable<A, Monad<Maybe, T>> arg) throws Failure;

    public <T> Applicable<Maybe<A>, Maybe<T>> fmap(final Applicable<A, T> f) throws Failure {
        return new Applicable<Maybe<A>, Maybe<T>>() {
            public Maybe<T> apply(Maybe<A> arg) throws Failure {
                Applicable<A, Monad<Maybe, T>> liFted = new Applicable<A, Monad<Maybe, T>>() {
                    public Maybe<T> apply(A arg) throws Failure {
                        return Maybe.pure(f.apply(arg));
                    }
                };

                return arg.mbBind(liFted);
            }
        };
    }
}
