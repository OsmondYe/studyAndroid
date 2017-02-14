package baiscKnowledge.innerclassTest;

/**
 * closure是一个可调用对象，它记录了一些信息，信息来自创建他的作用域
 */


interface Incrementable {
    void increment();
}

class Callee1 implements Incrementable {
    private int i = 0;

    @Override
    public void increment() {
        i++;
    }
}

class MyIncrement {
    static void f(MyIncrement mi) {
        mi.increment();
    }

    public void increment() {
        System.out.println("other operation");
    }
}

class Callee2 extends MyIncrement {
    private int i = 0;

    @Override
    public void increment() {
        super.increment();
        i++;
        System.out.println(i);
    }

    public Incrementable getCallbackReference() {
        return new AClousure();
    }

    private class AClousure implements Incrementable {
        @Override
        public void increment() {
            Callee2.this.increment();
        }
    }

}

class Caller {
    private Incrementable callbackReference;

    Caller(Incrementable cbh) {
        callbackReference = cbh;
    }

    void go() {
        callbackReference.increment();
    }
}

public class Closure {
    public static void test()

    {
        Callee1 c1 = new Callee1();
        Callee2 c2 = new Callee2();
        MyIncrement.f(c2);

        Caller caller1 = new Caller(c1);
        Caller caller2 = new Caller(c2.getCallbackReference());
        caller1.go();
        caller2.go();
        //
        caller2.go();
        caller1.go();

    }
}
