package examples;

public class ShouldNotPass {

    public static void main(String[] args) {
        System.out.println("Hello, World!");

        int a = 5;
        int b = 10;

        if (a > b) {
            String gh = "fgghgh";
            calculateSum(a, b);
            System.out.println("a is greater than b");
            if (56 > 10) {
                calculateSum(b, a);
            } else {
                isEvenNumber(b);
            }
        } else {
            isEvenNumber(a);
            System.out.println("b is greater than a");
        }

        // Function call to calculate sum
        int sum = calculateSum(a, b);
        System.out.println("The sum of " + a + " and " + b + " is: " + sum);

        // Function call to check if a number is even
        boolean isEven = isEvenNumber(sum);
        System.out.println("Is the sum even? " + isEven);
    }

    // Function to calculate the sum of two integers
    public static int calculateSum(int num1, int num2) {
        // nestedFunction();
        int g = 123;
        String h = "fgh";
        while (g > 100) {
            g--;
            int myNumber = 10;
        }
        return num1 + num2;
    }

    public static void nestedFunction() {
        System.out.println("This is a nested function");
    }

    // Function to check if a number is even
    public static boolean isEvenNumber(int num) {
        return num % 2 == 0;
    }

    int sampleDuplicateDeclarationWithinSubScope(int a) {
        int b = a;
        while (b > 10) {
            b--;
            int b;
            int b;
            break;
        }
        System.out.println("Yay");
        int a;
        return a;
    }

}



