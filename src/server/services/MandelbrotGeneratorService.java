package src.server.services;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;

import javax.imageio.ImageIO;

import src.interfaces.Task;

public class MandelbrotGeneratorService implements Task<BufferedImage>, Serializable {
    public static void main(final String[] args) {
        final MandelbrotGeneratorService mandelbrotGenerator = new MandelbrotGeneratorService(1920, 1080, 1, 0, 0, 256);
        final BufferedImage mandelbrotImage = mandelbrotGenerator.generateMandelbrotImage();

        try {
            ImageIO.write(mandelbrotImage, "png", new File("mandelbrot.png"));
        } catch (final IOException e) {
            System.err.println("Error generating the mandelbrot image: " + e.getMessage());
        }
    }

    private final int width;
    private final int height;
    private final double zoom;
    private final double x_offset;
    private final double y_offset;
    private final int max_iter;

    public MandelbrotGeneratorService(final int width, final int height, final double zoom, final double x_offset,
            final double y_offset, final int max_iter) {
        this.width = width;
        this.height = height;
        this.zoom = zoom;
        this.x_offset = x_offset;
        this.y_offset = y_offset;
        this.max_iter = max_iter;
    }

    public MandelbrotGeneratorService(final int width, final int height, final int max_iter) {
        this.width = width;
        this.height = height;
        this.zoom = Math.random() * 10;
        this.x_offset = Math.random() * 10;
        this.y_offset = Math.random() * 10;
        this.max_iter = max_iter;
    }

    public BufferedImage execute() {
        return generateMandelbrotImage();
    }

    public BufferedImage generateMandelbrotImage() {
        final BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                double real = 0.0;
                double imaginary = 0.0;
                final double xCoordinate = calculateXCoordinate(x, width, zoom, x_offset);
                final double yCoordinate = calculateYCoordinate(y, height, zoom, y_offset);
                int iteration = max_iter;

                while (real * real + imaginary * imaginary < 4 && iteration > 1) {
                    final double temp = real * real - imaginary * imaginary + xCoordinate;
                    imaginary = 2.0 * real * imaginary + yCoordinate;
                    real = temp;
                    iteration--;
                }

                final int color = calculatePixelColor(iteration);
                image.setRGB(x, y, color);
            }
        }

        return image;
    }

    private double calculateXCoordinate(final int x, final int width, final double zoom, final double x_offset) {
        return (x - width / 2) / (width / 4.0 * zoom) - x_offset;
    }

    private double calculateYCoordinate(final int y, final int height, final double zoom, final double y_offset) {
        return (y - height / 2) / (height / 4.0 * zoom) - y_offset;
    }

    private int calculatePixelColor(final int iteration) {
        final int red = (iteration % 8 * 32) << 16;
        final int green = (iteration % 16 * 16) << 8;
        final int blue = iteration % 32 * 8;
        return red | green | blue;
    }
}
