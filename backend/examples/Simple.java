public class Simple {

    public static void main(String[] args) {
        System.out.println("Hello, World!");

        int g = 123;
        String h = "fgh";

        for (int i = 0; i < 10; i++) {
            int myNumber = 10;
            String what = "whatString";
            if (g > myNumber) {
                String gh = "fgghgh";
                // return; // Adding a return statement here
            } else {
                boolean even = isEvenNumber(g);
            }
            int waaa = 98;
        }

        int someInt = 200;
    }

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
        return num1 + num2;
        while (g > 100) {
            g--;
            int myNumber = 10;
        }
    }


    public static int sampleForLoop(int num1, int num2) {
        // nestedFunction();
        int g = 123;
        String h = "fgh";
        for (int g = 0; g < 10; g++) {
            int myNumber = 10;
            String what = "whatString";
        }
        if (h == "gh") {
            return 25;
        }
        int someInt = 200;
        int endInt = 5;
        return 6;
    }

    public static int sampleForLoopInsideIF(int num1, int num2) {
        // nestedFunction();
        int g = 123;
        String h = "fgh";
        if (h != "f") {
            for (int g = 0; g < 10; g++) {
                int myNumber = 10;
                String what = "whatString";
            }
            int afterLoopInt = 5000;
        } else {
            int someInt = 200;
            return 25;
        }

        if (h == "WHATTT") {
            int someINTman = 566;
        }
        int endInt = 5;
        return 6;
    }

    public static void nestedFunction() {
        System.out.println("This is a nested function");
    }

    // Function to check if a number is even
    public static boolean isEvenNumber(int num) {
        return num % 2 == 0;
    }

    int sampleIfProgram(int a) {
        int b = a;
        if (b > 10) {
            b++;
        } else {
            b--;
            return;
        }
        System.out.println("Yay");
    }

    int sampleForLoopProgram(int a) {
        int b = a;
        for (int count = 0; count < 10; count++) {
            b++;
        }
        System.out.println("Yay");
    }

    int sampleWhileLoopProgram(int a) {
        // int b = a;
        while (b > 10) {
            b--;
        }
        System.out.println("Yay");
    }

    int sampleWhileLoopWithBreakProgram(int a) {
        // int b = a;
        while (b > 10) {
            b--;
            break;
            b++;
        }
        System.out.println("Yay");
    }

    int sampleWhileLoopWithBreakInsideNestedIFProgram(int a) {
        int b = a;
        while (b > 10) {
            b--;
            if (2 > 1) {
                b++;
                break;
            } else {
                b = b + 2;
            }
            break;
            b = b + 5;
        }
        System.out.println("Yay");
    }

}