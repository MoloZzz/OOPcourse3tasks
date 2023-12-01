package tasks;
import java.lang.reflect.*;
public class ClassDescriptionPrinter {
    public static void printClassDescription(String className) {
        try {
            ClassLoader classLoader = ClassDescriptionPrinter.class.getClassLoader();
            Class<?> loadedClass = classLoader.loadClass(className);

            Field[] fields = loadedClass.getDeclaredFields();
            System.out.println("Fields:");
            for (Field field : fields) {
                System.out.println("  " + field.toString());
            }

            Method[] methods = loadedClass.getDeclaredMethods();
            System.out.println("\nMethods:");
            for (Method method : methods) {
                System.out.println("  " + method.toString());
            }

            Constructor<?>[] constructors = loadedClass.getDeclaredConstructors();
            System.out.println("\nConstructors:");
            for (Constructor<?> constructor : constructors) {
                System.out.println("  " + constructor.toString());
            }

        } catch (ClassNotFoundException e) {
            System.err.println("Class not found: " + className);
        }
    }
}
