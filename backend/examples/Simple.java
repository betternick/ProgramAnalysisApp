
public class Simple {
    public static void main(String[] args) {
        System.out.println("Hello, World!");

        int a = 5;
        int b = 10;

        // Function call to calculate sum
        int sum = calculateSum(a, b);
        System.out.println("The sum of " + a + " and " + b + " is: " + sum);

        // Function call to check if a number is even
        boolean isEven = isEvenNumber(sum);
        System.out.println("Is the sum even? " + isEven);
    }

    // Function to calculate the sum of two integers
    public static int calculateSum(int num1, int num2) {
        return num1 + num2;
    }

    // Function to check if a number is even
    public static boolean isEvenNumber(int num) {
        return num % 2 == 0;
    }
}
