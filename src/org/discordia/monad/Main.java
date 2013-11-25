package org.discordia.monad;

import org.discordia.monad.fwk.Applicable;
import org.discordia.monad.fwk.Failure;
import org.discordia.monad.fwk.Maybe;
import org.discordia.monad.fwk.Monad;

public class Main {

    public abstract class SetterM<Receiver, Datum> implements Applicable<Datum, Monad<Maybe, Receiver>> {
        private final Receiver receiver;

        protected SetterM(Receiver r) {
            this.receiver = r;
        }

        // a monadic set function
        public final Monad<Maybe, Receiver> apply(Datum d) throws Failure {
            set(receiver, d);
            return Maybe.pure(receiver);
        }

        // subclasses implement with the particular set method being called
        protected abstract void set(Receiver r, Datum d);
    }

    public abstract class GetterM<Container, Returned>
            implements Applicable<Container, Monad<Maybe, Returned>> {


        public Monad<Maybe, Returned> apply(Container c) throws Failure {
            Maybe<Returned> ans = (Maybe<Returned>)Maybe.NOTHING;
            Returned result = get(c);

            // c inside the monad is guaranteed to be an instance
            // but result has NO guarantees!

            if(result != null) {
                ans = Maybe.pure(result);
                // and now ans is inside the monad it must have a value.
                // ... even if that value is NOTHING
            }

            return ans;
        }


        protected abstract Returned get(Container c);
    }


    public Main() {
        C c = new C(1, 2);
        B b = new B(c);
        A a = new A(b);
        Bar bar = new Bar(a);

        final Foo foo = new Foo();

        GetterM<Bar, A> getterA = new GetterM<Bar,A>() {
            protected A get(Bar b) {
                return b.getA();
            }
        };

        GetterM<A, B> getterBfromA = new GetterM<A, B>() {
            protected B get(A a) {
                return a.getB();
            }
        };

        GetterM<B, C> getterCfromB = new GetterM<B, C>() {
            @Override
            protected C get(B b) {
                return b.getC();
            }
        };

        GetterM<C, Integer> getterDFromC = new GetterM<C, Integer>() {
            @Override
            protected Integer get(C c) {
                return c.getD();
            }
        };

        SetterM<Foo, Integer> setterDinFoo = new SetterM<Foo, Integer>(foo) {
            protected void set(Foo f, Integer d) {
                f.setD(d);
            }
        };

        System.out.println("Foo D value before: " + foo.getD());

        try {
            getterA.apply(bar).bind(getterBfromA).bind(getterCfromB).bind(getterDFromC).bind(setterDinFoo);
        } catch (Failure failure) {
            failure.printStackTrace();
        }

        System.out.println("Foo D value after: " + foo.getD());
    }

    public static void main(String[] args) {
        new Main();
    }
}
