package baiscKnowledge.innerclassTest;

/**
 * Created by oye on 2/14/2017.
 */

public interface ClassInInterface {
    void howy();
    // class in interface is auto:   public static
    class Test implements ClassInInterface{
        @Override
        public void howy() {
            System.out.println("ClassInInterface.howy()");
        }
    }
}
