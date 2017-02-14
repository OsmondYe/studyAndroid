package baiscKnowledge.innerclassTest;

/**
 * Created by oye on 2/14/2017.
 */

public class ParcelNestedClass {
    private static class ParcelContents implements Contents {
        @Override
        public int value() {
            return 11;
        }
    }

    protected static class ParcelDestination implements Destination {
        static int x = 10;
        String label;

        public ParcelDestination(String label) {
            this.label = label;
        }

        public static void f() {
        }

        @Override
        public String readLabel() {
            return label;
        }

        static class AnotherLevel {
            static int x = 10;

            public static void f() {

            }
        }
    }

    public static Destination destination(){
        return new ParcelDestination("china");
    }

    public static Contents contents() {
        return  new ParcelContents();
    }

}
