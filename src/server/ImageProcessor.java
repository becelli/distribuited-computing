package src.server;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;

import javax.imageio.ImageIO;

import src.interfaces.Task;

public class ImageProcessor implements Task<BufferedImage>, Serializable {
  public static void main(final String[] args) {
    final String inputImagePath = "mandelbrot.png";

    try {
      final BufferedImage inputImage = ImageIO.read(new File(inputImagePath));

      final ImageProcessor imageProcessor = new ImageProcessor("grayscale", inputImage);
      final BufferedImage grayscaleImage = imageProcessor.convertToGrayscale();
      final BufferedImage blurredImage = imageProcessor.blur();

      ImageIO.write(grayscaleImage, "jpg", new File("output_grayscale.jpg"));
      ImageIO.write(blurredImage, "jpg", new File("output_blurred.jpg"));
    } catch (final IOException e) {
      System.err.println("Error converting the image to grayscale: " + e.getMessage());
    }
  }

  private final String operation;

  private BufferedImage inputImage;

  public ImageProcessor(final String operation, final BufferedImage inputImage) {
    this.operation = operation;
    this.inputImage = inputImage;
  }

  public BufferedImage execute() {
    return switch (operation) {
      case "grayscale" -> convertToGrayscale();
      case "blur" -> blur();
      default -> throw new IllegalArgumentException("Unknown operation: " + operation);
    };
  }

  public BufferedImage convertToGrayscale() {
    final int width = inputImage.getWidth();
    final int height = inputImage.getHeight();

    final BufferedImage grayscaleImage = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_GRAY);

    for (int x = 0; x < width; x++) {
      for (int y = 0; y < height; y++) {
        final int rgb = inputImage.getRGB(x, y);
        final int alpha = (rgb >> 24) & 0xFF;
        final int red = (rgb >> 16) & 0xFF;
        final int green = (rgb >> 8) & 0xFF;
        final int blue = rgb & 0xFF;

        final int gray = (red + green + blue) / 3;
        final int grayscaleRGB = (alpha << 24) | (gray << 16) | (gray << 8) | gray;

        grayscaleImage.setRGB(x, y, grayscaleRGB);
      }
    }

    return grayscaleImage;
  }

  public BufferedImage blur() {
    final int width = inputImage.getWidth();
    final int height = inputImage.getHeight();

    final BufferedImage blurredImage = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_GRAY);

    final float[] kernel = new float[] {
        1 / 16f, 1 / 8f, 1 / 16f,
        1 / 8f, 1 / 4f, 1 / 8f,
        1 / 16f, 1 / 8f, 1 / 16f,
    };

    for (int x = 1; x < width - 1; x++) {
      for (int y = 1; y < height - 1; y++) {
        float sum = 0;

        for (int i = -1; i <= 1; i++) {
          for (int j = -1; j <= 1; j++) {
            final int rgb = inputImage.getRGB(x + i, y + j);
            final int red = (rgb >> 16) & 0xFF;
            final int green = (rgb >> 8) & 0xFF;
            final int blue = rgb & 0xFF;

            final int gray = (red + green + blue) / 3;
            final float weight = kernel[(i + 1) * 3 + (j + 1)];
            sum += gray * weight;
          }
        }

        final int blurredRGB = (255 << 24) | ((int) sum << 16) | ((int) sum << 8) | (int) sum;
        blurredImage.setRGB(x, y, blurredRGB);
      }
    }

    return blurredImage;
  }
}
