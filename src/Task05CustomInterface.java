import java.util.List;

/**
 Задание 5. Свой функциональный интерфейс.
 Дан список логинов
 Написать свой функциональный интерфейс Rule
 для проверки строк: один абстрактный метод test,
 default-методы and и negate, статическая фабрика notBlank.
 Реализовать интерфейс классом LengthRule с полями min и max.
 Создать объект LengthRule
 Собрать с помощью дефолтных методом Rule и LengthRule правил:
 1. мин длина логина 3 символа, макс длина - 12 символов.
 2. только буквы или текст
 3. не должен быть пустым
 через стрим с filter и напечатать невалидные логины.
 Ожидаемый вывод:
 всего 6, невалидных 3
 a
 ivan_2024
 max_1
 валидных 3
 */
public class Task05CustomInterface {

    public static void main(String[] args) {
        List<String> logins = List.of("anna", "a", "ivan_2024", "petr", "ekaterina99", "max_1");
        LengthRule lengthRule = new LengthRule(3, 12);
        Rule valid = Rule.notBlank()
                .and(lengthRule)
                .and(login -> login.chars().allMatch(Character::isLetterOrDigit));
        Rule invalid = valid.negate();
        List<String> invalidLogins = logins.stream()
                .filter(invalid::test)
                .toList();

        System.out.println("всего " + logins.size() + ", невалидных " + invalidLogins.size());
        invalidLogins.forEach(System.out::println);
        System.out.println("валидных " + (logins.size() - invalidLogins.size()));
    }
}

@FunctionalInterface
interface Rule {
    boolean test(String value);

    default Rule and(Rule other) {
        return value -> test(value) && other.test(value);
    }

    default Rule negate() {
        return value -> !test(value);
    }

    static Rule notBlank() {
        return value -> value != null && !value.isBlank();
    }
}

class LengthRule implements Rule {
    private final int min;
    private final int max;

    LengthRule(int min, int max) {
        this.min = min;
        this.max = max;
    }

    @Override
    public boolean test(String value) {
        return value != null && value.length() >= min && value.length() <= max;
    }
}
