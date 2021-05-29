package course.parallel;

import mpi.MPI;

import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;

//-jar ${MPJ_HOME}/lib/starter.jar -np 2

public class ParallelApplication {
    private static FileWriter fw;

    public static void main(String[] args) throws IOException {
        long time = 0;
        int count = 1;
        int[] n = {5};
        int potok = 2;
        fw = new FileWriter("test_MPI_22222" + potok + ".txt");
        fw.write(potok + " поток:\n");
        potok++;
        for (int m = 0; m < n.length; m++) {
            fw.write("Матрица: " + n[m] + "x" + n[m] + "\n");
            System.out.println("Матрица: " + n[m] + "x" + n[m] + "\n");
            for (int k = 0; k < count; k++) {
                int size1 = n[m];
                int size2 = n[m];
                int[][] arr = new int[size1][size2];
                for (int i = 0; i < arr.length; i++) {
                    for (int j = 0; j < arr[i].length; j++) {
                        arr[i][j] = j + (i * arr[i].length);
                    }
                }


                MPI.Init(args);
                int rank = MPI.COMM_WORLD.Rank();
                int size = MPI.COMM_WORLD.Size();
                int unitSize = size1, root = 0;
                int[] sendbuf = new int[unitSize * unitSize];

                int step = size1 / (size);
                int[] recvbuf = new int[unitSize * unitSize];
                int[] newRecvBuf = new int[unitSize * (unitSize)];

                long st = System.nanoTime();

                MPI.COMM_WORLD.Scatter(
                        sendbuf,
                        0,
                        unitSize * step,
                        MPI.INT,
                        recvbuf,
                        0,
                        unitSize * step, MPI.INT, root);


                if (rank != root) {
                    int start = (rank - 1) * step;
                    int end = rank * step;
                    int c = start * unitSize;

                    for (int i = start; i < end; i++) {
                        for (int j = 0; j < arr[0].length; j++) {
                            recvbuf[c] = 0;
                            for (int l = 0; l < arr[0].length; l++) {
                                recvbuf[c] += arr[i][l] * arr[l][j];
                            }
                            c++;
                        }
                    }
                }

                MPI.COMM_WORLD.Gather(recvbuf, 0, unitSize * step,
                        MPI.INT, newRecvBuf, 0, unitSize * step, MPI.INT, root);

                if (rank == root) {

                    if (unitSize % size != 0) {

                    int start = unitSize / size * (size - 1);
                    int c = (start + 1) * unitSize;

                    for (int i = start; i < unitSize; i++) {
                        for (int j = 0; j < arr[0].length; j++) {
                            if (c == newRecvBuf.length) break;
                            newRecvBuf[c] = 0;
                            for (int l = 0; l < arr[0].length; l++) {
                                newRecvBuf[c] += arr[i][l] * arr[l][j];
                            }
                            c++;
                        }
                    }

                    }

                    for (int i = 0; i < newRecvBuf.length; i++) {
                        System.out.format("%-7d", newRecvBuf[i]);
                        if ((i + 1) % (unitSize) == 0) System.out.println();
                    }
                    System.out.println();
                    long en = System.nanoTime();
                    time += (en - st);
                    long buftime[] = new long[size];
                    long timerez[] = new long[size];

                    buftime[rank] = time;
                    System.out.println(BigDecimal.valueOf(
                            (double) ((en - st) / 1000000000 / potok))
                            .setScale(10, BigDecimal.ROUND_HALF_UP));
                    fw.write(BigDecimal.valueOf(
                            (double) ((en - st) / 1000000000 / potok))
                            .setScale(10, BigDecimal.ROUND_HALF_UP) + "\n");

                }


                MPI.Finalize();
            }
            long rez = time / count;
            System.out.println(BigDecimal.valueOf(
                    (double) rez / 1000000000 / potok).setScale(10, BigDecimal.ROUND_HALF_UP));
            fw.write("среднее: matrx " + n[m] + "X" + n[m] + " - " + BigDecimal.valueOf(
                    (double) rez / 1000000000 / potok).setScale(10, BigDecimal.ROUND_HALF_UP) + "\n");
        }
        fw.close();
    }
}
