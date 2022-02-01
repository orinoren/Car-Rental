import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Main {
    public static void main(String[] args) {
        List<Integer> list1 = Stream.of(1, 3, 2, 657, 567, 2, 5, 3, 77, 44, 77).collect(Collectors.toList());
        List<Integer> list2 = Stream.of(12, 34, 23, 3657, 5637, 32, 55, 33, 677, 544, 777).collect(Collectors.toList());

        BiFunction<List<Integer>, List<Integer>, List<Integer>> biFunction = (l1, l2) ->
                Stream.of(l1, l2)
                        .flatMap(List::stream)
                        .distinct()
                        .collect(Collectors.toList());

        Function<List<Integer>, List<Integer>> function = (l) ->
                l.stream().sorted().collect(Collectors.toList());

        System.out.println(biFunction.andThen(function).apply(list1,list2));


    }

}
