package baiscKnowledge.innerclassTest;

/**
 * Created by oye on 2/14/2017.
 */

public class Parcel5 {
    public Destination destination(String s){
        // define a class in a method
        class PDestination implements Destination{
            private String label;
            private PDestination(String whereTo){
                this.label = whereTo;
            }
            @Override
            public String readLabel() {
                return label;
            }
            // inner class can not have static declaration
//            static void thiste(){
//
//            }
        }

        return new PDestination(s);
    }

    public Contents contents(){
        // anonymous inner class
        return new Contents() {
            @Override
            public int value() {
                return 11;
            }
        };
    }

    public Warpping warpping(int x){
        return new Warpping(x){
            @Override
            public int value() {
                return super.value()*100;
            }
        };
    }

}
