import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.*;

public class DBSCAN {

    public static class Dot {

        int x, y, nghbrs, cl;

        Dot(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }

    public static void main(String[] args) throws java.lang.Exception {
        int x, y, cnt = 0, cl = 0;
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
                dot[cnt] = new Dot(x, y);
                dot[cnt].cl = 0;
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
        boolean f = true;
        int ng = 0;
        Dot q = new Dot(0, 0);
        while (f) {
            for (int i = 0; i < cnt; i++) {
                if (dot[i].cl == 0) {
                    dot[i].cl = ++cl;
                    q = dot[i];
                    break;
                } else if (i == cnt - 1) f = false;
            }
            ng++;
            for (int i = 0; i < cnt; i++) {
                for (int j = i + 1; j < cnt; j++) {
                    if (Math.sqrt((dot[i].x - dot[j].x) * (dot[i].x - dot[j].x) + (dot[i].y - dot[j].y) * (dot[i].y - dot[j].y)) < 20) {
                        if ((dot[i] == q) && (dot[j].cl != 0)) dot[i].cl = dot[j].cl;
                        if (ng < 2) {
                            dot[i].nghbrs++;
                            dot[j].nghbrs++;
                        }
                        if (dot[j].cl == 0 && dot[i].cl != 0) {
                            dot[j].cl = dot[i].cl;
                        } else if (dot[i].cl == 0 && dot[j].cl != 0) {
                            dot[i].cl = dot[j].cl;
                        }
                    }
                }
            }
        }
        for (int i = cnt - 1; i >= 0; i--) {
            for (int j = i - 1; j >= 0; j--) {
                if (Math.sqrt((dot[i].x - dot[j].x) * (dot[i].x - dot[j].x) + (dot[i].y - dot[j].y) * (dot[i].y - dot[j].y)) < 20)
                    if ((dot[i].cl != dot[j].cl) && (dot[i].nghbrs > 5) && (dot[j].nghbrs > 5)) dot[j].cl = dot[i].cl;
            }
        }
            BufferedImage img = new BufferedImage(510, 510, BufferedImage.TYPE_INT_RGB);
            int color = 3500000;
            for (int i = 0; i < cnt; i++) {

                if (dot[i].nghbrs == 0) {
                    img.setRGB(dot[i].x, dot[i].y, 0xffffff);
                    img.setRGB(dot[i].x + 1, dot[i].y, 0xffffff);
                    if (dot[i].x > 0) img.setRGB(dot[i].x - 1, dot[i].y, 0xffffff);
                    img.setRGB(dot[i].x, dot[i].y + 1, 0xffffff);
                    if (dot[i].y > 0) img.setRGB(dot[i].x, dot[i].y - 1, 0xffffff);
                } else if (dot[i].nghbrs <= 5) {
                    img.setRGB(dot[i].x, dot[i].y, 0x888888);
                    img.setRGB(dot[i].x + 1, dot[i].y, 0x888888);
                    if (dot[i].x > 0) img.setRGB(dot[i].x - 1, dot[i].y, 0x888888);
                    img.setRGB(dot[i].x, dot[i].y + 1, 0x888888);
                    if (dot[i].y > 0) img.setRGB(dot[i].x, dot[i].y - 1, 0x888888);
                } else {
                    img.setRGB(dot[i].x, dot[i].y, (color * dot[i].cl) % 0xffffff);
                    img.setRGB(dot[i].x + 1, dot[i].y, (color * dot[i].cl) % 0xffffff);
                    if (dot[i].x > 0) img.setRGB(dot[i].x - 1, dot[i].y, (color * dot[i].cl) % 0xffffff);
                    img.setRGB(dot[i].x, dot[i].y + 1, (color * dot[i].cl) % 0xffffff);
                    if (dot[i].y > 0) img.setRGB(dot[i].x, dot[i].y - 1, (color * dot[i].cl) % 0xffffff);
                }
            }
            ImageIO.write(img, "png", new File("mask_7.png"));
        }
    }
