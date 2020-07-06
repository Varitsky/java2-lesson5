package ru.geekbrains;

import java.util.Arrays;
public class Main {

    static final int size = 10000000;
    static final int h = size / 2;

    public static void main(String[] args) {
        float[] arrayOfAMillionOnes = new float[size];
        Arrays.fill(arrayOfAMillionOnes, 1);

        oneThread(arrayOfAMillionOnes);
        twoThread(arrayOfAMillionOnes);

    }


    private static void oneThread(float[] arr) {
        long startTime = System.currentTimeMillis();
        for (int i = 0; i < arr.length; i++) {
            arr[i] = (float)(arr[i] * Math.sin(0.2f + i / 5) * Math.cos(0.2f + i / 5) * Math.cos(0.4f + i / 2));
        }
        System.out.println("Время расчёта в один поток: " + (System.currentTimeMillis() - startTime));
    }


    private static void twoThread(float[] arr) {
        long startBreakingTime = System.currentTimeMillis();
        float[] arrOne = new float[h];
        float[] arrTwo = new float[h];
        System.arraycopy(arr, 0, arrOne, 0, h);
        System.arraycopy(arr, h, arrTwo, 0, h);

        long part1 = System.currentTimeMillis() - startBreakingTime;
        System.out.println("Время разбивки массива на два: " + part1);

        long startTime = System.currentTimeMillis();
        Thread firstThread = new Thread(new Runnable() {
            @Override
            public void run() {
                long threadOneTime = System.currentTimeMillis();
                for (int i = 0; i < arrOne.length; i++) {
                    arrOne[i] = (float)(arrOne[i] * Math.sin(0.2f + i / 5) * Math.cos(0.2f + i / 5) * Math.cos(0.4f + i / 2));
                }
                System.out.println("Первый массив (1/2): " + (System.currentTimeMillis()-threadOneTime));
            }
        });

//        long threadOneTime = System.currentTimeMillis();
        firstThread.start();
//        try {
//            firstThread.join();
//            System.out.println("Первый поток: " + (System.currentTimeMillis()-threadOneTime));
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }


        Thread secondThread = new Thread(new Runnable() {
            @Override
            public void run() {
                long threadTwoTime = System.currentTimeMillis();
                for (int i = 0; i < arrTwo.length; i++) {
                    arrTwo[i] = (float) (arrTwo[i] * Math.sin(0.2f + (i) / 5) * Math.cos(0.2f + (i) / 5) * Math.cos(0.4f + (i) / 2));
                }
                System.out.println("Второй массив (2/2): " + (System.currentTimeMillis()-threadTwoTime));
            }

        });
        secondThread.start();


        try {
            firstThread.join();
            secondThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        long part2 = System.currentTimeMillis() - startTime;
        System.out.println("Время расчёта в два потока: " + part2);

        long unionTime = System.currentTimeMillis();
        System.arraycopy(arrOne, 0, arr, 0, h);
        System.arraycopy(arrTwo, 0, arr, h, h);

        long part3 = System.currentTimeMillis() - unionTime;
        System.out.println("Склейка обратно: " + part3);

        System.out.println();
        System.out.println("Общее время: " + (part1+part2+part3));
    }


}