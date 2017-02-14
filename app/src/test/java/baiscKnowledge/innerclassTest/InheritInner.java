package baiscKnowledge.innerclassTest;

/**
 * Created by oye on 2/14/2017.
 */

class WithInner{
    class Inner{}
}


public class InheritInner extends WithInner.Inner{
    public InheritInner(WithInner wi) {
        wi.super();
    }

    static public void test(){
        WithInner wi = new WithInner();
        InheritInner hh = new InheritInner(wi);
    }
}
