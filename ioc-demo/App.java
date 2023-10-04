import java.lang.reflect.InvocationTargetException;

public class App {
    public static void main(String[] args) throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        ClassLoader systemClassLoader = ClassLoader.getSystemClassLoader();

        Class<?> container = systemClassLoader.loadClass("Container");

        Object o = container.getDeclaredConstructor().newInstance();

        System.out.println(o);
    }
}
