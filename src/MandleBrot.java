import Scheduler.Scheduler;
import Scheduler.FJAction;
import Scheduler.Worker;

import javax.swing.*;
import java.awt.image.BufferedImage;
import java.util.Random;
import java.util.concurrent.RecursiveAction;

public class MandleBrot {
    public static int[][] pxls;
    public static BufferedImage image;
    public static final int Threshold = 1000;
    private static final int Depth = 1000;
    private static final int Height = 72;

    public static void main(String[] args) throws InterruptedException{
        image = new BufferedImage(500,500,BufferedImage.TYPE_INT_RGB);
        pxls = new int[500][500];

        FJAMandelBrot mandel = new FJAMandelBrot(0,pxls.length);
        Scheduler s = new Scheduler(4);
        System.out.println("A Lone Artist Named Angelo... Starts His Masterpiece.");
        s.launch(mandel);

        draw();

        display();
        Worker[] workers = s.getAos();
        int index = 0;
        for(Worker worker: workers){
            index++;
            System.out.println("Worker ("+index+") Stole "+ worker.getNumSteals()+" Tasks");
        }
    }
    public static void draw() {
        Random rand = new Random();
        System.out.println("Image is Drawn");
        for(int i = 0; i < pxls.length; i++) {
            for(int j = 0; j < pxls[i].length; j++) {

                if(pxls[i][j] < Depth) {
                    image.setRGB(i, j, 0xFFFFFF);
                } else {
                    image.setRGB(i, j, 0x000000);
                }
            }
        }
    }
    public static void display() {
        JFrame frame = new JFrame();
        JLabel label = new JLabel(new ImageIcon(image));
        frame.getContentPane().add(label);
        frame.pack();
        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        frame.setVisible(true);
    }
    public static void serial_mandel(int pixels[][], int startRow, int endRow) {
        for(int i = startRow; i < endRow; ++i) {
            for(int j = 0; j < pixels[i].length; ++j) {


                int max = pixels.length;


                double c_re = (j - pixels.length/2.0)*4.0/pixels[i].length;
                double c_im = (i - pixels[i].length/2.0)*4.0/pixels[i].length;

                pixels[i][j] = mandel(new Complex(c_re, c_im));


            }
        }
        System.out.println("Computations Completed!");
    }
    public static int mandel(Complex c) {
        int count = 0;
        Complex z = new Complex(); //initialize to 0
        for(int k = 0; k < Depth; k++) {
            if(z.abs() >= 2.0) {
                break;
            }
            z = z.times(z).plus(c);
            count++;


        }
        return count;
    }


    public static class FJAMandelBrot extends FJAction {

        int startRow;
        int endRow;

        public FJAMandelBrot(int x, int y) {
            this.startRow = x;
            this.endRow = y;
        }

        public void compute() throws InterruptedException {
            if(endRow - startRow < Threshold) {
                serial_mandel(pxls, startRow, endRow);
            } else {
                int mid = (startRow + endRow) / 2;
                FJAMandelBrot left = new FJAMandelBrot(startRow, mid);
                left.fork();
                FJAMandelBrot right = new FJAMandelBrot(mid, endRow);
                right.compute();
                left.join();
            }
        }
    }
}





