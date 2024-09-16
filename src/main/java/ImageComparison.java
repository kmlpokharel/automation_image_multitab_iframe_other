import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.apache.commons.io.FileUtils;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;
public class ImageComparison {
    static String inputLogo = "src/main/resources/input/input.png";
    static String outputLogo = "src/main/resources/output/output.png";
    static String comparisonResult = "src/main/resources/output/result.png";

        public static void clearResult() {
            File folder = new File("src/main/resources/output/");
            File[] files = folder.listFiles();
            if (files != null) {
                Arrays.stream(files).forEach(File::delete);
            }
        }

        private static   void captureImage() {
            WebDriverManager.chromedriver().setup();
            WebDriver driver = new ChromeDriver();
            driver.manage().window().maximize();
            driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
            driver.get("https://google.com");
            driver.manage().window().maximize();
            try {
                File logoFile = driver.findElement(By.xpath("//img[@class='lnXdpd']")).getScreenshotAs(OutputType.FILE);
                FileUtils.copyFile(logoFile, new File(outputLogo));
            } catch (IOException e) {
                e.printStackTrace();
            }
            String result = compareImage(inputLogo, outputLogo);
            System.out.println(result);
            driver.quit();
        }

        private static String compareImage(String inputLogoPath, String outputLogoPath) {
            try {
                BufferedImage img1 = ImageIO.read(new File(inputLogoPath));
                BufferedImage img2 = ImageIO.read(new File(outputLogoPath));
                double isDifferent = getDifferencePercentage(img1, img2);
                subtractImages(img1, img2);
                String result;
                if(isDifferent>1)
                {
                    result = "FAIL with Diff%:"+isDifferent;
                }
                else
                    result = "PASS with Diff%:"+isDifferent;

                return result;
            } catch (IOException e) {
                e.printStackTrace();
                return "ERROR";
            }
        }


        public static double getDifferencePercentage(BufferedImage img1, BufferedImage img2) {
            if (img1.getWidth() != img2.getWidth() || img1.getHeight() != img2.getHeight()) {
                throw new IllegalArgumentException("Images must have the same dimensions");
            }
            int width = img1.getWidth();
            int height = img1.getHeight();
            int totalPixels = width * height;
            int differingPixels = 0;
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    if (img1.getRGB(x, y) != img2.getRGB(x, y)) {
                        differingPixels++;
                    }
                }
            }
            double diffPercentage = (double) differingPixels / totalPixels * 100;
            return diffPercentage;
        }

    private static void subtractImages(BufferedImage image1, BufferedImage image2) throws IOException {
        BufferedImage image3 = new BufferedImage(image1.getWidth(), image1.getHeight(), image1.getType());
        int color;
        for(int x = 0; x < image1.getWidth(); x++)
            for(int y = 0; y < image1.getHeight(); y++) {
                color = Math.abs(image2.getRGB(x, y) - image1.getRGB(x, y));
                image3.setRGB(x, y, color);
            }
        ImageIO.write(image3, "png",  new File(comparisonResult));
    }


    public static void main(String[] args) {
        clearResult();
        captureImage();
    }
    }


