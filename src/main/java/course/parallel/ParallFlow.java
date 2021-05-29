package course.parallel;

public class ParallFlow implements Runnable {
    //    private int size, st, arr[][];
    private int end, st, arr1[][], arr2[][], rez[][];

    ParallFlow(int st, int end, int arr1[][], int arr2[][], int rez[][]) {
        this.end = end;
        this.st = st;
        this.arr1 = arr1;
        this.arr2 = arr2;
        this.rez = rez;
        new Thread(this).start();
    }

    @Override
    public void run() {
        for (int i = st; i < end; i++)
//            for (int j = 0; j < arr[i].length; j++)
//                arr[i][j] = j + (i * arr[i].length);

            for (int j = 0; j < arr2[0].length; j++) {
                rez[i][j] = 0;
                for (int k = 0; k < arr1[0].length; k++) {
                    rez[i][j] += arr1[i][k] * arr2[k][j];
                }
            }
    }
}