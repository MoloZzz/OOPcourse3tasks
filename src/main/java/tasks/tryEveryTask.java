package tasks;

public class tryEveryTask {

    public static void task1(){
        int port = 7070;
        Server.startServer(port);
    }
    public static void task2(){
        double[][] coefficients = {
                {1, -1, 0, 1},
                {0, 1, 1, 5},
                {0, 2, 1, 8}
        };

        double[] solutions = SolveSystem.solveSystem(coefficients);

        // Виведення результатів
        System.out.println("Розв'язок системи:");
        for (int i = 0; i < solutions.length; i++) {
            System.out.println("x" + (i + 1) + " = " + solutions[i]);
        }

    }
    public static void task4() {

        ClassDescriptionPrinter.printClassDescription("tasks.SerializedObject");
    }
    public static void task5(){
        LockFreeSkipList<Integer> skipList = new LockFreeSkipList<>();

        skipList.add(5);
        skipList.add(10);
        skipList.add(3);
        System.out.println("Contains 10: " + skipList.contains(10));
        System.out.println("Removed 5: " + skipList.remove(5));
        System.out.println("Contains 5: " + skipList.contains(5));

        ClassDescriptionPrinter.printClassDescription("tasks.LockFreeSkipList");

    }

}
