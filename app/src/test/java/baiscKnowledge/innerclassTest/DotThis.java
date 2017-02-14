package baiscKnowledge.innerclassTest;

/**
 * Created by oye on 2/14/2017.
 */

public class DotThis {
    public void f(){
        System.out.println("DotThis.f()");
    }
    public class Inner {
        public DotThis outer(){
            // return or reference to out class, ues the Outter class's name and .this
            return DotThis.this;
        }
    }
    public Inner inner(){
        return new Inner();
    }
}
