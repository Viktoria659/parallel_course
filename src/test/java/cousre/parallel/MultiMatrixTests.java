package cousre.parallel;

import course.parallel.MultiMatrix;
import org.junit.*;
import org.junit.contrib.java.lang.system.TextFromStandardInputStream;
import org.junit.runners.MethodSorters;

import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;

import static org.junit.Assert.assertEquals;
import static org.junit.contrib.java.lang.system.TextFromStandardInputStream.emptyStandardInputStream;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class MultiMatrixTests {
    @Rule
    public final TextFromStandardInputStream systemInMock = emptyStandardInputStream();
    private MultiMatrix m;
    private static FileWriter fw;
    private static BigDecimal normVal[][];
    private static int numPotok = -1, step, size, start, val[];

    @Before
    public void before() {
        m = new MultiMatrix();
    }

    @After
    public void after() {
        m = null;
    }

    @Test
    public void testMet_1_check() {
        systemInMock.provideLines("0", "kssd", "-789", "1000000",
                "1000000", "100000000000", "", " ", "\n", "2", "3");
        int a[][] = m.check();
        assertEquals(a.length, new int[2][3].length);
        assertEquals(a[0].length, (new int[2][3])[0].length);
        systemInMock.provideLines("5", "ksdfsd", "-789", "100000000000",
                "", " ", "\n", "3", "4");
        int b[][] = m.check();
        assertEquals(b.length, new int[3][4].length);
        assertEquals(b[0].length, (new int[3][4])[0].length);

        /*long st, en;
        systemInMock.provideLines("10000", "10000", "10000", "10000");
        st = System.nanoTime();
        m.multiply(0);
        en = System.nanoTime();
        System.out.println("Время вычисления: " + (en - st));

        systemInMock.provideLines("7000", "7000", "7000", "7000");
        st = System.nanoTime();
        m.multiply(2);
        en = System.nanoTime();
        System.out.println("Время вычисления: " + (en - st));

        systemInMock.provideLines("7000", "7000", "7000", "7000");
        st = System.nanoTime();
        m.multiply(4);
        en = System.nanoTime();
        System.out.println("Время вычисления: " + (en - st));

        systemInMock.provideLines("7000", "7000", "7000", "7000");
        st = System.nanoTime();
        m.multiply(8);
        en = System.nanoTime();
        System.out.println("Время вычисления: " + (en - st));*/
    }


    @Test
    public void testMet_2_size() {
        assertEquals(m.size(new int[5][2], new int[2][6]).length, new int[5][6].length);
        assertEquals(m.size(new int[6][2], new int[2][6])[0].length, (new int[6][6])[0].length);
        assertEquals(m.size(new int[3][4], new int[4][3]).length, new int[3][3].length);
        assertEquals(m.size(new int[3][4], new int[4][3])[0].length, (new int[3][3])[0].length);
    }


    @Test
    public void testMet_3_filling() {
        int arr[][] = {{0, 1}, {2, 3}, {4, 5}};
        systemInMock.provideLines("3", "2");
        int arr2[][] = m.filling();
        for (int i = 0; i < arr.length; i++) {
            for (int j = 0; j < arr[i].length; j++) {
                assertEquals(arr[i][j], arr2[i][j]);
            }
        }

        int arr3[][] = {{0, 1, 2}, {3, 4, 5}};
        systemInMock.provideLines("2", "3");
        int arr4[][] = m.filling();
        for (int i = 0; i < arr3.length; i++) {
            for (int j = 0; j < arr[i].length; j++) {
                assertEquals(arr3[i][j], arr4[i][j]);
            }
        }
    }


    @Test
    public void testMet_4_multiply_1() {
        int[][] rez;
        systemInMock.provideLines("3", "4", "4", "5");
        int arr[][] = {{70, 76, 82, 88, 94}, {190, 212, 234, 256, 278}, {310, 348, 386, 424, 462}};
        for (int i = 0; i < arr.length; i++) {
            for (int j = 0; j < arr[i].length; j++) {
            }
        }
    }

    @Test
    public void testMet_4_multiply_2() {
        systemInMock.provideLines("2", "3", "3", "4");
        int arr2[][] = {{20, 23, 26, 29}, {56, 68, 80, 92}};

        for (int i = 0; i < arr2.length; i++) {
            for (int j = 0; j < arr2[i].length; j++) {
            }
        }
    }


    @Test
    public void testMet_5_multiply() throws IOException {
        start = 0;
        size = 8;
        step = 2;
        fw = new FileWriter("test1 - вторые 25.txt");
        val = new int[]{10, 100, 1000};
        normVal = new BigDecimal[(size - start + step) / step][3];
        long st, en;
        System.out.println("Для определения эффективности разного количества потоков при вычислении матриц, будет " + "проведено " +
                (size - start) / step + " иттераций: ");
        for (int i = start, h = 0; i < size + step; i += step, h++) {
            st = System.nanoTime();
            fw.write(i + " поток:\n");
            m = new MultiMatrix();
            multiflow(i);
            m = null;
            en = System.nanoTime();
            System.out.format("%s%-2d%s%-3d%s%-2d%s%-4.1f%s", " ", h, " итерация (", i, " поток) завершена. " +
                    "Осталось - ", (size - start) / step - h, " (примерно ", (((double) (en - st) / 1000000000) *
                    ((size - start) / step - h)), " сек)\n");
        }
        end();
        fw.close();
    }


    private void multiflow(int ind) throws IOException {
        numPotok++;
        long st, en;
        double re, sum = 0;
        int countIter = 25;
        for (int i = 0; i < val.length; i++, sum = 0) {
//            fw.write("Матрица: " + val[i] + "x" + val[i] + "\n");
            System.out.println("Матрица: " + val[i] + "x" + val[i]);
            for (int j = 0; j < countIter; j++) {
                System.out.println(j);
                systemInMock.provideLines(String.valueOf(val[i]), String.valueOf(val[i]),
                        String.valueOf(val[i]), String.valueOf(val[i]));
                st = System.nanoTime();
                m.multiply(ind);
                en = System.nanoTime();
                re = (double) (en - st) / 1000000000;
                sum += re;
                fw.write(BigDecimal.valueOf(re).setScale(10,
                        BigDecimal.ROUND_HALF_UP) + "\n");
            }
            normVal[numPotok][i] = BigDecimal.valueOf(sum / countIter);
            fw.write("Среднее зеначение: " + (BigDecimal.valueOf(sum
                    / countIter).setScale(10, BigDecimal.ROUND_HALF_UP)) + " сек\n\n");
        }
    }


    private void end() throws IOException {
        String[] matr = {"10x10", " 100x100 ", " 1000x1000 "};
        fw.write("\nЛучшие результаты вычисления матриц:\n");
        System.out.println("\nЛучшие результаты вычисления матриц (" + start + "-"
                + size + " потоков с шагом +" + step + "):\n");
        for (int i = 0; i < normVal[0].length; i++) {
            BigDecimal rez = normVal[0][i];
            int num = 0;
            for (int j = 1; j < normVal.length; j++)
                if (rez.compareTo(normVal[j][i]) == 1) {
                    num = j;
                    rez = normVal[j][i];
                }
            fw.write(matr[i] + " - " + ((num * step) + start) + " поток - " +
                    rez.setScale(10, BigDecimal.ROUND_HALF_UP) + " сек\n");
            System.out.format("%s%s%-3d%s%.8f%s", matr[i], " - ", (num * step) +
                    start, " поток - ", rez, " сек\n");
        }
    }
}
