import java.awt.image.BufferedImage;
import java.io.*;
import javax.imageio.ImageIO;

class Cmeans {

    public static class Dot {

        Cluster cl;
        int x, y;
        double d;

        Dot(int x, int y, Cluster cl) {
            this.x = x;
            this.y = y;
            this.cl = cl;
            this.d = Math.sqrt((this.x - this.cl.x) * (this.x - this.cl.x) + (this.y - this.cl.y) * (this.y - this.cl.y));
        }
    }

    public static class Cluster {
        double x, y;
        int clr1, clr2, clr3;

        Cluster(double x, double y, int clr1, int clr2, int clr3) {
            this.x = x;
            this.y = y;
            this.clr1 = clr1;
            this.clr2 = clr2;
            this.clr3 = clr3;
        }
    }

    public static void main(String[] args) throws java.lang.Exception {
        Cluster red = new Cluster(125, 125, 255, 0, 0);
        Cluster blue = new Cluster(375, 125, 0, 0, 255);
        Cluster yellow = new Cluster(125, 375, 255, 255, 0);
        Cluster green = new Cluster(375, 375, 0, 255, 0);
        int x, y, cnt = 0;
        Dot dot[] = new Dot[10000];
        String csvFile = "src/mask_7.csv";
        BufferedReader br = null;
        String line = "";
        String cvsSplitBy = ";";
        try {
            br = new BufferedReader(new FileReader(csvFile));
            while ((line = br.readLine()) != null) {
                String[] step = line.split(cvsSplitBy);
                x = Integer.parseInt(step[0]);
                y = Integer.parseInt(step[1]);
                if ((x < 250) && (y < 250)) dot[cnt] = new Dot(x, y, red);
                if ((x >= 250) && (x < 500) && (y < 250)) dot[cnt] = new Dot(x, y, blue);
                if ((x < 250) && (y >= 250) && (y < 500)) dot[cnt] = new Dot(x, y, yellow);
                if ((x >= 250) && (x < 500) && (y >= 250) && (y < 500)) dot[cnt] = new Dot(x, y, green);
                cnt++;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        boolean f = true, f1, f2, f3, f4;
        while (f) {
            f1 = false;
            f2 = false;
            f3 = false;
            f4 = false;
            int x1 = 0, x2 = 0, x3 = 0, x4 = 0, y1 = 0, y2 = 0, y3 = 0, y4 = 0, n1 = 0, n2 = 0, n3 = 0, n4 = 0;
            for (int i = 0; i < cnt; i++) {
                if (dot[i].cl == red) {
                    x1 += dot[i].x;
                    y1 += dot[i].y;
                    n1++;
                }
                if (dot[i].cl == blue) {
                    x2 += dot[i].x;
                    y2 += dot[i].y;
                    n2++;
                }
                if (dot[i].cl == yellow) {
                    x3 += dot[i].x;
                    y3 += dot[i].y;
                    n3++;
                }
                if (dot[i].cl == green) {
                    x4 += dot[i].x;
                    y4 += dot[i].y;
                    n4++;
                }
            }
            if (((double) (x1 / n1) == red.x) && ((double) (y1 / n1) == red.y)) f1 = true;
            if (((double) (x2 / n2) == blue.x) && ((double) (y2 / n2) == blue.y)) f2 = true;
            if (((double) (x3 / n3) == yellow.x) && ((double) (y3 / n3) == yellow.y)) f3 = true;
            if (((double) (x4 / n4) == green.x) && ((double) (y4 / n4) == green.y)) f4 = true;
            if (f1 && f2 && f3 && f4 == f) f = false;
            red.x = (double) (x1 / n1);
            red.y = (double) (y1 / n1);
            blue.x = (double) (x2 / n2);
            blue.y = (double) (y2 / n2);
            yellow.x = (double) (x3 / n3);
            yellow.y = (double) (y3 / n3);
            green.x = (double) (x4 / n4);
            green.y = (double) (y4 / n4);
            double d1, d2, d3, d4, mn;
            for (int i = 0; i < cnt; i++) {
                d1 = Math.sqrt((dot[i].x - red.x) * (dot[i].x - red.x) + (dot[i].y - red.y) * (dot[i].y - red.y));
                d2 = Math.sqrt((dot[i].x - blue.x) * (dot[i].x - blue.x) + (dot[i].y - blue.y) * (dot[i].y - blue.y));
                d3 = Math.sqrt((dot[i].x - yellow.x) * (dot[i].x - yellow.x) + (dot[i].y - yellow.y) * (dot[i].y - yellow.y));
                d4 = Math.sqrt((dot[i].x - green.x) * (dot[i].x - green.x) + (dot[i].y - green.y) * (dot[i].y - green.y));
                mn = Math.min(Math.min(Math.min(d1, d2), d3), d4);
                if (mn == d1) dot[i].cl = red;
                if (mn == d2) dot[i].cl = blue;
                if (mn == d3) dot[i].cl = yellow;
                if (mn == d4) dot[i].cl = green;
            }
        }
        BufferedImage img = new BufferedImage(510, 510, BufferedImage.TYPE_INT_RGB);
        for (int i = 0; i < cnt; i++) {
            if (dot[i].cl == red) {
                img.setRGB(dot[i].x, dot[i].y, 0xff0000);
                img.setRGB(dot[i].x + 1, dot[i].y, 0xff0000);
                if (dot[i].x > 0) img.setRGB(dot[i].x - 1, dot[i].y, 0xff0000);
                img.setRGB(dot[i].x, dot[i].y + 1, 0xff0000);
                if (dot[i].y > 0) img.setRGB(dot[i].x, dot[i].y - 1, 0xff0000);
            }
            if (dot[i].cl == blue) {
                img.setRGB(dot[i].x, dot[i].y, 0x0000ff);
                img.setRGB(dot[i].x + 1, dot[i].y, 0x0000ff);
                if (dot[i].x > 0) img.setRGB(dot[i].x - 1, dot[i].y, 0x0000ff);
                img.setRGB(dot[i].x, dot[i].y + 1, 0x0000ff);
                if (dot[i].y > 0) img.setRGB(dot[i].x, dot[i].y - 1, 0x0000ff);
            }
            if (dot[i].cl == yellow) {
                img.setRGB(dot[i].x, dot[i].y, 0xffff00);
                img.setRGB(dot[i].x + 1, dot[i].y, 0xffff00);
                if (dot[i].x > 0) img.setRGB(dot[i].x - 1, dot[i].y, 0xffff00);
                img.setRGB(dot[i].x, dot[i].y + 1, 0xffff00);
                if (dot[i].y > 0) img.setRGB(dot[i].x, dot[i].y - 1, 0xffff00);
            }
            if (dot[i].cl == green) {
                img.setRGB(dot[i].x, dot[i].y, 0x00ff00);
                img.setRGB(dot[i].x + 1, dot[i].y, 0x00ff00);
                if (dot[i].x > 0) img.setRGB(dot[i].x - 1, dot[i].y, 0x00ff00);
                img.setRGB(dot[i].x, dot[i].y + 1, 0x00ff00);
                if (dot[i].y > 0) img.setRGB(dot[i].x, dot[i].y - 1, 0x00ff00);
            }
        }
        ImageIO.write(img, "png", new File("mask_7.png"));
    }
}