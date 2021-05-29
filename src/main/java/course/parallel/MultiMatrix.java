package course.parallel;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MultiMatrix {
    private static int n = 0;
    private static int in;
    private static int m = 0;

    public static int[][] check() {
        n++;
        BufferedReader buf = new BufferedReader(new InputStreamReader(System.in));
        int maxInt = 2147483645;
        for (int f = 0, x = 0, y = 0; ; ) {
            System.out.println("\nВведите " + (f + 1) + " размер " + n + " массива:");
            try {
                int c = Integer.parseInt(buf.readLine());
                if (c <= 0) throw new Throwable();
                if (f == 0) {
                    x = c;
                    if (n % 2 == 0 && x != in) {
                        System.out.println("Матрицы должны быть согласованы!\n" +
                                "2 размер 1 матрицы должен быть равен 1 размеру 2 метрицы!");
                        f = -1;
                    }
                }
                if (f == 1) {
                    y = c;
                    if (n % 2 == 1) in = c;
                }
                f++;
                if (f > 1) {
                    if ((long) x * (long) y > maxInt) {
                        System.out.println("Размер полученного массива выходит за допустимые рамки: " +
                                "arr[x][y], где x*y < maxInt");
                        f = 0;
                    } else {
                        if ((f + 1) % 2 == 0) in = 0;
                        System.out.println("Вы ввели массив размера [" + x + "][" + y + "]");
                        return new int[x][y];
                    }
                }
            } catch (Throwable e) {
                System.out.println("Нужно вводить целое число > 0");
            }
        }
    }


    public static int[][] filling() {
        int[][] arr = check();
        for (int i = 0; i < arr.length; i++) {
            for (int j = 0; j < arr[i].length; j++) {
                arr[i][j] = j + (i * arr[i].length);
//                System.out.format("%-7d", arr[i][j]);
//                if (j == arr[0].length - 1) System.out.println();
            }
        }
        return arr;
    }


    public static int[][] size(int[][] x, int[][] y) {
        return new int[x.length][y[0].length];
    }

    public static int[][] multiply(int potoks) { //Задание 7. Умножение двух матриц.
        m++;
        long st, en;
        int arr1[][] = filling();
        int arr2[][] = filling();

        System.out.println("\nПроизведение массивов: " + m);
        int[][] rez = size(arr1, arr2);
        System.out.println("arr[" + arr1.length + "][" + arr1[0].length + "] * " + "arr2[" + arr2.length + "][" +
                arr2[0].length + "] = rez[" + rez.length + "][" +
                rez[0].length + "]");
        st = System.nanoTime();
        if (potoks == 0) {
            for (int i = 0; i < arr1.length; i++)
                for (int j = 0; j < arr2[0].length; j++) {
                    rez[i][j] = 0;
                    for (int k = 0; k < arr1[0].length; k++) {
                        rez[i][j] += arr1[i][k] * arr2[k][j];
                    }
//                    System.out.format("%-7d", rez[i][j]);
//                    if (j == rez[0].length - 1) System.out.println();
                }
        } else if (potoks >= rez.length) {
            for (int k = 0; k < rez.length; k++)
                new ParallFlow(k, k + 1, arr1, arr2, rez);
        } else {
            int step = rez.length / potoks;
            for (int k = 0; k < rez.length; k += step)
                if (k + step <= rez.length) new ParallFlow(k, k + step, arr1, arr2, rez);
                else new ParallFlow(k, rez.length, arr1, arr2, rez);
        }
        en = System.nanoTime();
        System.out.println("Время произведения матриц составило: " +
                BigDecimal.valueOf(((double) (en - st) / 1000000000)).setScale(10,
                        BigDecimal.ROUND_HALF_UP) + " сек\n\n");
        return rez;
    }
}

