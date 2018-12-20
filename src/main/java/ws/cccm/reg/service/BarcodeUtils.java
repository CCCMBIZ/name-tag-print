/**
 * 
 */
package ws.cccm.reg.service;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.awt.image.VolatileImage;
import java.io.File;

import javax.imageio.ImageIO;

import com.itextpdf.text.pdf.Barcode39;
/**
 * @author okclip
 *
 */
public class BarcodeUtils {

	public static BufferedImage generateBarcode(String code) {
		Barcode39 code39 = new Barcode39();
		code39.setCode(code);
		Image imageCode = code39.createAwtImage(Color.BLACK, Color.WHITE);
		return toBufferedImage(imageCode, BufferedImage.TYPE_INT_ARGB);
	}

	public static void main(String[] args) throws Exception {
		Barcode39 code39 = new Barcode39();
		code39.setCode("56588115");
		Image imageCode = code39.createAwtImage(Color.BLACK, Color.WHITE);
		ImageIO.write(toBufferedImage(imageCode, BufferedImage.TYPE_INT_ARGB), "png", new File("c:\\NameTagPrint\\", "rid.png"));// BufferedImage.TYPE_INT_ARGB
	}

	public static BufferedImage toBufferedImage(final Image image, final int type) {
		if (image instanceof BufferedImage)
			return (BufferedImage) image;
		if (image instanceof VolatileImage)
			return ((VolatileImage) image).getSnapshot();
		loadImage(image);
		final BufferedImage buffImg = new BufferedImage(image.getWidth(null), image.getHeight(null), type);
		final Graphics2D g2 = buffImg.createGraphics();
		g2.drawImage(image, null, null);
		g2.dispose();
		return buffImg;
	}

	private static void loadImage(final Image image) {
		class StatusObserver implements ImageObserver {
			boolean imageLoaded = false;

			public boolean imageUpdate(final Image img, final int infoflags, final int x, final int y, final int width,
					final int height) {
				if (infoflags == ALLBITS) {
					synchronized (this) {
						imageLoaded = true;
						notify();
					}
					return true;
				}
				return false;
			}
		}
		final StatusObserver imageStatus = new StatusObserver();
		synchronized (imageStatus) {
			if (image.getWidth(imageStatus) == -1 || image.getHeight(imageStatus) == -1) {
				while (!imageStatus.imageLoaded) {
					try {
						imageStatus.wait();
					} catch (InterruptedException ex) {
					}
				}
			}
		}
	}



}
