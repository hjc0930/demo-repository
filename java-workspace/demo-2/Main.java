import java.sql.Array;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        List<Integer> listArr = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
        int page = 2;
        int pageSize = 3;

        List<Integer> list = listArr.stream().skip((page - 1) * pageSize).limit(pageSize).toList();
        System.out.println(list);
    }
}
