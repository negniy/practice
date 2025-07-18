import java.util.Random;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.annotation.*;

interface Printable {
    void print();
}

class HeavyBox {
    private double weight;

    public HeavyBox(double weight) {
        this.weight = weight;
    }

    public double getWeight() {
        return weight;
    }
}

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
@interface DeprecatedEx {
    String message();
}

@DeprecatedEx(message = "Use NewClass instead")
class OldClass {
    @DeprecatedEx(message = "Use newMethod instead")
    public void oldMethod() {
    }
}

class NewClass {
}

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@interface JsonField {
    String name();
}

class JsonSerializer {
    public String serialize(Object obj) throws IllegalAccessException {
        StringBuilder json = new StringBuilder("{");
        Field[] fields = obj.getClass().getDeclaredFields();
        boolean first = true;
        for (Field field : fields) {
            if (field.isAnnotationPresent(JsonField.class)) {
                if (!first) json.append(", ");
                field.setAccessible(true);
                json.append("\"").append(field.getAnnotation(JsonField.class).name()).append("\": \"").append(field.get(obj)).append("\"");
                first = false;
            }
        }
        json.append("}");
        return json.toString();
    }
}

class ReflectionHandler {
    public void checkDeprecated(Class<?> clazz) {
        if (clazz.isAnnotationPresent(DeprecatedEx.class)) {
            System.out.println("Внимание: класс '" + clazz.getSimpleName() + "' устарел. Альтернатива: " + clazz.getAnnotation(DeprecatedEx.class).message());
        }
        for (Method method : clazz.getDeclaredMethods()) {
            if (method.isAnnotationPresent(DeprecatedEx.class)) {
                System.out.println("Внимание: метод '" + method.getName() + "' устарел. Альтернатива: " + method.getAnnotation(DeprecatedEx.class).message());
            }
        }
    }
}

public class Main {
    public static void main(String[] args) {
        Printable printable = () -> System.out.println("Hello from Printable");
        printable.print();

        Predicate<String> isNotNull = str -> str != null;
        Predicate<String> isNotEmpty = str -> !str.isEmpty();
        Predicate<String> isValid = isNotNull.and(isNotEmpty);
        System.out.println("isValid('test'): " + isValid.test("test"));
        System.out.println("isValid(''): " + isValid.test(""));
        System.out.println("isValid(null): " + isValid.test(null));

        Predicate<String> startsWithJNEndsWithA = str -> (str != null && (str.startsWith("J") || str.startsWith("N"))) && str.endsWith("A");
        System.out.println("startsWithJNEndsWithA('Java'): " + startsWithJNEndsWithA.test("Java"));
        System.out.println("startsWithJNEndsWithA('NetA'): " + startsWithJNEndsWithA.test("NetA"));
        System.out.println("startsWithJNEndsWithA('Test'): " + startsWithJNEndsWithA.test("Test"));

        Consumer<HeavyBox> unloadBox = box -> System.out.println("Отгрузили ящик с весом " + box.getWeight());
        Consumer<HeavyBox> sendBox = box -> System.out.println("Отправляем ящик с весом " + box.getWeight());
        HeavyBox box = new HeavyBox(10.5);
        unloadBox.andThen(sendBox).accept(box);

        Function<Integer, String> numberType = num -> {
            if (num > 0) return "Положительное число";
            else if (num < 0) return "Отрицательное число";
            else return "Ноль";
        };
        System.out.println("numberType(5): " + numberType.apply(5));
        System.out.println("numberType(-3): " + numberType.apply(-3));
        System.out.println("numberType(0): " + numberType.apply(0));

        Supplier<Integer> randomNumber = () -> new Random().nextInt(11);
        System.out.println("Random number: " + randomNumber.get());

        ReflectionHandler handler = new ReflectionHandler();
        handler.checkDeprecated(OldClass.class);

        class TestObject {
            @JsonField(name = "userName")
            private String name = "John";
            @JsonField(name = "userAge")
            private int age = 25;
            private String ignored = "Ignore";
        }

        JsonSerializer serializer = new JsonSerializer();
        try {
            System.out.println("JSON: " + serializer.serialize(new TestObject()));
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
