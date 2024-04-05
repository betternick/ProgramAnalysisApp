package src.main.resources;

public class Simple {
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

    public static int sampleForLoop(int num1, int num2) {
        // nestedFunction();
        int g = 123;
        String h = "fgh";
        for (g = 0; g < 10; g++) {
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
            for (g = 0; g < 10; g++) {
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

    public static void singleLineFunction() {
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
            return;
        } else {
            b--;

        }
        System.out.println("Yay");
        int why = 6;
    }


    int sampleForLoopProgram(int a) {
        int b = a;
        for (int count = 0; count < 10; count++) {
            b++;
            return;
        }
        System.out.println("Yay");
        return b;
        String what = "this shouldnt be reached";
    }


    int sampleWhileLoopWithBreakInsideNestedIFProgram(int a) {
        int b = a;
        while (b > 10) {
            b--;
            if (2 > 1) {
                b++;
                break;
                int alsowontaffect = 50;
            } else {
                b = b + 2;
            }
            break;
            int wontaffect = 67;
        }
        System.out.println("Yay");
        return b;
        int impoissible = 45;
    }


    int Ifstatementafterbreak(int a) {
        int b = a;
        while (b > 10) {
            b--;
            break;
            if (1 > 0) {
                int why = 9;
            }
        }
        System.out.println("Yay");
        return a;
    }


    int returnInOuterLoop(int a) {
        int b = a;
        while (b > 10) {
            b--;
            while (1 > 0) {
                int why = 9;
                //   return;
                int y = 999;
            }
            b++;
            return;
        }
        System.out.println("Yay");
        return a;
    }

    int returnInInnerLoop(int a) {
        int b = a;
        while (b > 10) {
            b--;
            return;
            while (1 > 0) {
                int why = 9;
                return;
                int y = 999;
            }
            b++;
            //   return;
        }
        System.out.println("Yay");
        return a;
    }

    int simplewhileReturn(int a) {
        int b = a;
        while (b > 10) {
            b--;
            return;
        }
        System.out.println("Yay");
        int y = 67;
        return a;
    }


    int returnInBothBranches(int a) {
        int b = a;
        if (1>6) {
            int h = 90;
            return;
        } else {
            int y = 60;
            return;
            int check = 5;
        }
        System.out.println("Yay");
        return a;
    }

    int returnInIfElseBranch(int a) {
        int b = a;
        if (1>6) {
            int h = 90;
        } else {
            return;
            int y = 60;
        }
        System.out.println("Yay");
        return a;
    }




    public static void aSimpleTest(String[] var0) {
        byte var1 = 5;
        byte var2 = 10;
        System.out.println(var3);
    }




    int AssigmentWithoutDeclaration(int a) {
        int b = 10;

        if (a > 5) {
            int c = a + b;
            z = b + 5;
            System.out.println(c);
        } else {
            int d = a - b;
            System.out.println(d);
        }
        return z;
    }

    int checkingIfElse(int a) {
        int g = 56;
        if (g < 56) {
            String y = "er";
        } else if (g == 10) {
            String h = "rty";
        } else {
            String u = "qw";
        }
        int ty = 8;
    }

    int sampleDeclarationWithNoUse(int a) {
        // int unusedVariableA;
        String d;
        while (b > 10) {
            b--;
            String d;
            break;
        }
        System.out.println("Yay");
        return a;
    }
//////////////// TESTS NOT ADDED FOR THE BELOW //////////////////////

    public static int calculateSum(int var0, int var1) {
        int g = 10;
        return var0 + var1;
        System.out.println("This code should be unreachable");
        int y = 12;
        int f = 45;
    }

    int fixedCodeThatCompiles(int a) {
        int b = 11;
        String d;
        while (b > 10) {
            b--;
            break;
        }
        System.out.println("Yay");
        return a;
    }

        public static void main2(String[] var0) {
        System.out.println("Hello, World!");
        byte var1 = 5;
        byte var2 = 10;
        if (var1 > var2) {
            calculateSum(var1, var2);
            System.out.println("a is greater than b");
        } else {
            isEvenNumber(var1);
            System.out.println("b is greater than a");
        }
        int y = 10;
    }


        int returnInIfThenBranch(int a) {
        int b = a;
        if (1>6) {
            int h = 90;
            return;
        } else {
            int y = 60;
        }
        System.out.println("Yay");
        return a;
    }


    int Loopstatementafterbreak(int a) {
        int b = a;
        while (b > 10) {
            b--;
            break;
            while (1 > 0) {
                int why = 9;
            }
        }
        System.out.println("Yay");
        return a;
    }

    int breakinInnerLoop(int a) {
        int b = a;
        while (b > 10) {
            b--;
            while (1 > 0) {
                int why = 9;
                break;
                int y = 999;
            }
            b++;
        }
        System.out.println("Yay");
        return a;
    }


    int sampleWhileLoopProgram(int a) {
        int b = a;
        while (b > 10) {
            b--;
        }
        System.out.println("Yay");
        return b;
    }

    int sampleWhileLoopWithBreakProgram(int a) {
        int b = a;
        while (b > 10) {
            b--;
            break;
        }
        System.out.println("Yay");
        return a;
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

int otherStatementafterbreak(int a) {
    int b = a;
    while (b > 10) {
        b--;
        break;
        int yyy = 34;
        if (1 > 0) {
        }
    }
    System.out.println("Yay");
    return a;
}


}
