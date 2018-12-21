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
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.util.EnumMap;
import java.util.Map;

import javax.imageio.ImageIO;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.itextpdf.text.pdf.Barcode39;

/**
 * @author okclip
 *
 */
public class BarcodeUtils {

	static final String charset = "UTF-8";

	public static BufferedImage generateBarcode(String code) {
		Barcode39 code39 = new Barcode39();
		code39.setCode(code);
		Image imageCode = code39.createAwtImage(Color.BLACK, Color.WHITE);
		return toBufferedImage(imageCode, BufferedImage.TYPE_INT_ARGB);
	}

	public static void main1(String[] args) throws Exception {
		Barcode39 code39 = new Barcode39();
		code39.setCode("56588115");
		Image imageCode = code39.createAwtImage(Color.BLACK, Color.WHITE);
		ImageIO.write(toBufferedImage(imageCode, BufferedImage.TYPE_INT_ARGB), "png",
				new File("c:\\NameTagPrint\\", "rid.png"));// BufferedImage.TYPE_INT_ARGB
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
	
	public static void main(String[] args) throws Exception {
		InputStream in = new ByteArrayInputStream(generateQRcode("12345", 100, 100));
		BufferedImage bImageFromConvert = ImageIO.read(in);
		ImageIO.write(bImageFromConvert, "png", new File("c:\\NameTagPrint\\", "qrprint.png"));// BufferedImage.TYPE_INT_ARGB
	}

	public static byte[] generateQRcode(String qrInfo, int width, int height) throws Exception {
		Map<EncodeHintType, Object> hintMap = new EnumMap<EncodeHintType, Object>(EncodeHintType.class);
		hintMap.put(EncodeHintType.CHARACTER_SET, charset);
		hintMap.put(EncodeHintType.MARGIN, 0); /* default = 4 */
		hintMap.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);

		QRCodeWriter qrCodeWriter = new QRCodeWriter();
		BitMatrix byteMatrix = qrCodeWriter.encode(qrInfo, BarcodeFormat.QR_CODE, width, height, hintMap);
		int CrunchifyWidth = byteMatrix.getWidth();
		BufferedImage image = new BufferedImage(CrunchifyWidth, CrunchifyWidth, BufferedImage.TYPE_INT_RGB);
		image.createGraphics();

		Graphics2D graphics = (Graphics2D) image.getGraphics();
		graphics.setColor(Color.WHITE);
		graphics.fillRect(0, 0, CrunchifyWidth, CrunchifyWidth);
		graphics.setColor(Color.BLACK);

		for (int i = 0; i < CrunchifyWidth; i++) {
			for (int j = 0; j < CrunchifyWidth; j++) {
				if (byteMatrix.get(i, j)) {
					graphics.fillRect(i, j, 1, 1);
				}
			}
		}

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		byte[] imageInByte = null;
		try {
			ImageIO.write(image, "png", baos);
			baos.flush();
			imageInByte = baos.toByteArray();
			baos.close();
		} finally {
			baos.close();
		}
		return imageInByte;
	}

}
