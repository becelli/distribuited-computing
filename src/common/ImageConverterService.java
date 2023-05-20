package src.common;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.imageio.ImageIO;

public class ImageConverterService {
  public static BufferedImage bytesToImage(byte[] imageBytes) throws IOException {
    ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(imageBytes);
    BufferedImage image = ImageIO.read(byteArrayInputStream);
    return image;
  }

  public static byte[] imageToBytes(BufferedImage image) throws IOException {
    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
    ImageIO.write(image, "png", byteArrayOutputStream);
    byte[] imageBytes = byteArrayOutputStream.toByteArray();
    return imageBytes;
  }
}
