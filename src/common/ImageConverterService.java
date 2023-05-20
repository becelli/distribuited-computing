package src.common;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.imageio.ImageIO;

public class ImageConverterService {
  public static BufferedImage bytesToImage(final byte[] imageBytes) throws IOException {
    final ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(imageBytes);
    final BufferedImage image = ImageIO.read(byteArrayInputStream);
    return image;
  }

  public static byte[] imageToBytes(final BufferedImage image) throws IOException {
    final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
    ImageIO.write(image, "png", byteArrayOutputStream);
    final byte[] imageBytes = byteArrayOutputStream.toByteArray();
    return imageBytes;
  }
}
