package examples;

public class IfElse {
    public static void main(String[] args) {
        int a = 5;

        if (a < 10) {
            int b = 10;
            int c = 20;
            int d = c + b;
            System.out.println("a is greater than 10, " + d);
        } else {
            System.out.println("a is less than or equal to 10");
        }

    }
}
