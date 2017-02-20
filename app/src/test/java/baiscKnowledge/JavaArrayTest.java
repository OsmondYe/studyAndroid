package baiscKnowledge;

import org.junit.Test;

import java.util.Arrays;


class ClassParameter<T> {
    public T[] f(T[] arg) {
        return arg;
    }
}

class MethodParameter {
    public static <T> T[] f(T[] arg) {
        return arg;
    }
}

public class JavaArrayTest {

    @Test
    public void test_basic() throws Exception {
        int a[];
        int b[] = new int[10];
        // aggregate initialization
        int[] c = new int[]{1, 2, 3, 4, 5, 6};

        System.out.println(c.length);
        c = b;
        System.out.println(c.length);

        // multi-dimensions;
        int[][] d = {
                {1, 2, 3},
                {4, 5, 6},
                {7, 8, 9},
        };
        System.out.println(d.length);
        System.out.println(d.toString());
        System.out.println(Arrays.deepToString(d));

    }

    @Test
    public void test_parameterized() throws Exception {
        Integer[] ints = {1,2,3,4,5};
        Double[] doubles = {1.1,2.2,3.3,4.4,5.5};

        Integer[] ints2 = new ClassParameter<Integer>().f(ints);

        Double[] doubles2 = new ClassParameter<Double>().f(doubles);

        ints2 = MethodParameter.f(ints);
        doubles2 = MethodParameter.f(doubles);

    }

    @Test
    public void test_ArrayUtilClass() throws Exception {


    }
}
