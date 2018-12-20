/**
 *
 */
package ws.cccm.reg.service;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Set;

import javax.imageio.ImageIO;

import com.itextpdf.text.BadElementException;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.Barcode39;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

/**
 * @author okclip
 *
 */
public class NameTagServiceImpl implements NameTagService {

    /**
     * @param args
     */
    public static void main(String[] args) throws Exception {

    }

    public static byte[] emptyImage() {
        BufferedImage bi = new BufferedImage(900, 200, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = bi.createGraphics();
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, 900, 200);
        g.setColor(Color.WHITE);
        g.dispose();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] imageInByte = null;
        try {
            ImageIO.write(bi, "png", baos);
            baos.flush();
            imageInByte = baos.toByteArray();
            baos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return imageInByte;
    }

    public static byte[] generateBarcode(String code) {
        Barcode39 code39 = new Barcode39();
        code39.setCode(code.toUpperCase());
        java.awt.Image imageCode = code39.createAwtImage(Color.BLACK, Color.WHITE);
        java.awt.Image scaledImage = imageCode.getScaledInstance(1200, 200, java.awt.Image.SCALE_SMOOTH);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] imageInByte = null;
        try {
            ImageIO.write(BarcodeUtils.toBufferedImage(scaledImage, BufferedImage.TYPE_INT_ARGB), "png", baos);
            baos.flush();
            imageInByte = baos.toByteArray();
            baos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return imageInByte;
    }

    private PdfPTable setStartPosition(PdfPTable table, int startPosition) {
        if (startPosition < 2 || startPosition % 6 == 1) {
            return table;
        } else if (startPosition > 6) {
            startPosition = startPosition % 6;
        }
        BaseFont bfChinese = null;
        try {
            bfChinese = BaseFont.createFont("C:/Windows/fonts/MSYH.TTF", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
        } catch (DocumentException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        Font headerChinese = new Font(bfChinese, 16, Font.BOLD);
        Font graceRow = new Font(bfChinese, 16, Font.NORMAL);
        Font nameChinese = new Font(bfChinese, 34, Font.BOLD);
        Font nameEnglish = new Font(bfChinese, 12, Font.NORMAL);
        Font beforeBarcode = new Font(bfChinese, 7, Font.NORMAL);
        Font afterBarcode = new Font(bfChinese, 9, Font.BOLD);

        for (int i = 1; i < startPosition; i++) {
            PdfPCell cell = new PdfPCell();
            cell.setFixedHeight(110);
            cell.setMinimumHeight(110);
            if (i % 2 == 1) {
                cell.setPaddingRight(50);
            } else if (i % 2 == 0) {
                cell.setPaddingLeft(45);
            }
            cell.setBorder(Rectangle.NO_BORDER);
            // getConferenceName
            Paragraph p = new Paragraph(" ", headerChinese);
            p.setAlignment(Element.ALIGN_CENTER);
            cell.addElement(p);

            // grace row
            Paragraph p1 = new Paragraph(" ", graceRow);
            p1.setAlignment(Element.ALIGN_CENTER);
            cell.addElement(p1);

            // chinese name
            Paragraph p2 = new Paragraph(" ", nameChinese);
            p2.setAlignment(Element.ALIGN_CENTER);
            cell.addElement(p2);

            // english name
            Paragraph p3 = new Paragraph(" ", nameEnglish);
            p3.setAlignment(Element.ALIGN_CENTER);
            cell.addElement(p3);

            // before barcode
            {
                PdfPTable barcodeTable = new PdfPTable(3);
                barcodeTable.setTotalWidth(250);
                barcodeTable.setLockedWidth(true);
                barcodeTable.getDefaultCell().setBorder(Rectangle.NO_BORDER);
                barcodeTable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
                barcodeTable.getDefaultCell().setVerticalAlignment(Element.ALIGN_MIDDLE);

                PdfPCell chruchCell = new PdfPCell();
                chruchCell.setBorder(Rectangle.NO_BORDER);
                Paragraph chruchP = new Paragraph(" ", beforeBarcode);
                chruchP.setAlignment(Element.ALIGN_LEFT);
                chruchCell.addElement(chruchP);
                barcodeTable.addCell(chruchCell);

                PdfPCell centerCell = new PdfPCell();
                centerCell.setBorder(Rectangle.NO_BORDER);
                Paragraph centerIdP = new Paragraph(" ", beforeBarcode);
                centerIdP.setAlignment(Element.ALIGN_CENTER);
                centerCell.addElement(centerIdP);
                barcodeTable.addCell(centerCell);

                PdfPCell addressCell = new PdfPCell();
                addressCell.setBorder(Rectangle.NO_BORDER);
                Paragraph addressP = new Paragraph(" ", beforeBarcode);
                addressP.setAlignment(Element.ALIGN_RIGHT);
                addressCell.addElement(addressP);
                barcodeTable.addCell(addressCell);

                cell.addElement(barcodeTable);
            }

            try {
                cell.addElement(Image.getInstance(emptyImage()));
            } catch (BadElementException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (MalformedURLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            Paragraph p8 = new Paragraph(" ", afterBarcode);
            p8.setAlignment(Element.ALIGN_CENTER);
            p8.setExtraParagraphSpace(10);
            cell.addElement(p8);
            table.addCell(cell);
            // System.out.println("Add position holder cell in:" + i);
        }
        return table;
    }

    @Override
    public byte[] generateNameTagPrints(Set<NameTag> printRequest, int startPosition) throws Exception {
        byte[] pdf = null;
        Document document = new Document(PageSize.LETTER);
        /*
		 * public boolean setMargins(float marginLeft, float marginRight, float
		 * marginTop, float marginBottom)
         */
        document.setMargins(0, 0, 80, 0);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PdfWriter.getInstance(document, baos);
        document.open();
        // System.out.println(FontFactory.defaultEmbedding);
        // FontFactory.defaultEmbedding = true;

        // System.out.println(FontFactory.defaultEmbedding);
        // SIMSUNB
        // BaseFont bfChinese = BaseFont.createFont("STSongStd-Light",
        // "UniGB-UCS2-H", BaseFont.EMBEDDED);
        BaseFont bfChinese = BaseFont.createFont("C:/Windows/fonts/MSYH.TTF", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);

        Font headerChinese = new Font(bfChinese, 16, Font.BOLD);
        Font graceRow = new Font(bfChinese, 16, Font.NORMAL);
        Font nameChinese = new Font(bfChinese, 34, Font.BOLD);
        Font nameEnglish = new Font(bfChinese, 12, Font.NORMAL);
        Font beforeBarcode = new Font(bfChinese, 7, Font.NORMAL);
        Font afterBarcode = new Font(bfChinese, 9, Font.BOLD);

        PdfPTable table = new PdfPTable(2);
        table.getDefaultCell().setBorder(Rectangle.BOX);
        table.getDefaultCell().setFixedHeight(220);
        table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
        table.getDefaultCell().setVerticalAlignment(Element.ALIGN_MIDDLE);

        setStartPosition(table, startPosition);

        int i = startPosition;
        for (NameTag nameTag : printRequest) {
            PdfPCell cell = new PdfPCell();
            cell.setFixedHeight(220);
            cell.setMinimumHeight(220);
            if (i % 2 == 1) {
                cell.setPaddingRight(50);
            } else if (i % 2 == 0) {
                cell.setPaddingLeft(45);
            }
            cell.setBorder(Rectangle.NO_BORDER);
            // getConferenceName
            Paragraph p = new Paragraph(nameTag.getConferenceName(), headerChinese);
            p.setAlignment(Element.ALIGN_CENTER);
            cell.addElement(p);

            // grace row
            if (nameTag.isGrace()) {
                Paragraph p1 = new Paragraph(nameTag.getGraceRow(), graceRow);
                p1.setAlignment(Element.ALIGN_CENTER);
                cell.addElement(p1);

            } else {
                Paragraph p1 = new Paragraph(" ", graceRow);
                p1.setAlignment(Element.ALIGN_CENTER);
                cell.addElement(p1);
            }

            // chinese name
            String chineseName = nameTag.getChineseFullName().replaceAll(".", "$0 ");
            Paragraph p2 = new Paragraph(chineseName, nameChinese);
            p2.setAlignment(Element.ALIGN_CENTER);
            cell.addElement(p2);

            // english name
            Paragraph p3 = new Paragraph(nameTag.getEnglishFullName(), nameEnglish);
            p3.setAlignment(Element.ALIGN_CENTER);
            cell.addElement(p3);

            // before barcode
            {
                PdfPTable barcodeTable = new PdfPTable(3);
                barcodeTable.setTotalWidth(250);
                barcodeTable.setLockedWidth(true);
                barcodeTable.getDefaultCell().setBorder(Rectangle.NO_BORDER);
                barcodeTable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
                barcodeTable.getDefaultCell().setVerticalAlignment(Element.ALIGN_MIDDLE);

                PdfPCell chruchCell = new PdfPCell();
                chruchCell.setBorder(Rectangle.NO_BORDER);
                Paragraph chruchP = new Paragraph(nameTag.getChruchName(), beforeBarcode);
                chruchP.setAlignment(Element.ALIGN_LEFT);
                chruchCell.addElement(chruchP);
                barcodeTable.addCell(chruchCell);

                PdfPCell centerCell = new PdfPCell();
                centerCell.setBorder(Rectangle.NO_BORDER);
                Paragraph centerIdP = new Paragraph(nameTag.getCenterId(), beforeBarcode);
                centerIdP.setAlignment(Element.ALIGN_CENTER);
                centerCell.addElement(centerIdP);
                barcodeTable.addCell(centerCell);

                PdfPCell addressCell = new PdfPCell();
                addressCell.setBorder(Rectangle.NO_BORDER);
                Paragraph addressP = new Paragraph(nameTag.getAddress(), beforeBarcode);
                addressP.setAlignment(Element.ALIGN_RIGHT);
                addressCell.addElement(addressP);
                barcodeTable.addCell(addressCell);

                cell.addElement(barcodeTable);
            }

            //cell.addElement(Image.getInstance(generateBarcode(nameTag.getBarcodeId())));
            PdfPTable barcodeTable = new PdfPTable(1);
            barcodeTable.setTotalWidth(250);
            barcodeTable.setLockedWidth(true);
            barcodeTable.getDefaultCell().setBorder(Rectangle.NO_BORDER);
            barcodeTable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
            barcodeTable.getDefaultCell().setVerticalAlignment(Element.ALIGN_MIDDLE);

            PdfPCell barcodeCell = new PdfPCell();
            barcodeCell.setBorder(Rectangle.NO_BORDER);
            barcodeCell.addElement(Image.getInstance(generateBarcode(nameTag.getBarcodeId())));
            barcodeTable.addCell(barcodeCell);
            cell.addElement(barcodeTable);

            /*Paragraph p8 = new Paragraph(nameTag.getBarcodeId(), afterBarcode);
			p8.setAlignment(Element.ALIGN_CENTER);
			p8.setExtraParagraphSpace(10);
			cell.addElement(p8);
			table.addCell(cell);*/
            {
                PdfPTable afterBarcodeTable = new PdfPTable(3);
                afterBarcodeTable.setSpacingBefore(-8);
                afterBarcodeTable.setTotalWidth(250);
                afterBarcodeTable.setLockedWidth(true);
                afterBarcodeTable.getDefaultCell().setBorder(Rectangle.NO_BORDER);
                afterBarcodeTable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
                afterBarcodeTable.getDefaultCell().setVerticalAlignment(Element.ALIGN_TOP);

                PdfPCell leftCell = new PdfPCell();
                leftCell.setBorder(Rectangle.NO_BORDER);
                Paragraph leftP = new Paragraph(nameTag.getGroupId(), afterBarcode);
                leftP.setAlignment(Element.ALIGN_LEFT);
                leftCell.addElement(leftP);
                afterBarcodeTable.addCell(leftCell);

                PdfPCell centerCell = new PdfPCell();
                centerCell.setBorder(Rectangle.NO_BORDER);
                Paragraph centerIdP = new Paragraph(nameTag.getBarcodeId(), afterBarcode);
                centerIdP.setAlignment(Element.ALIGN_CENTER);
                centerCell.addElement(centerIdP);
                afterBarcodeTable.addCell(centerCell);

                PdfPCell rightCell = new PdfPCell();
                rightCell.setBorder(Rectangle.NO_BORDER);
                Paragraph rightP = new Paragraph(nameTag.getTopic(), afterBarcode);
                rightP.setAlignment(Element.ALIGN_RIGHT);
                rightCell.addElement(rightP);
                afterBarcodeTable.addCell(rightCell);
                cell.addElement(afterBarcodeTable);
            }

            table.addCell(cell);

            System.out.println("Added Cell in position:" + (i <= 6 ? i : i % 6));
            if (i % 6 == 0) {
                System.out.println("************************************");
            }
            i++;

            int currentP = i - startPosition;
            // System.out.println("currentP:" + currentP);
            if (currentP == printRequest.size() && currentP % 2 == 0) {
                // System.out.println(currentP);
                PdfPCell cell2 = new PdfPCell();
                cell2.setPaddingLeft(50);
                cell2.setBorder(Rectangle.NO_BORDER);
                table.addCell(cell2);
            }
        }

        document.add(table);
        document.close();
        if (baos != null && baos.size() != 0) {
            pdf = baos.toByteArray();
            baos.close();
        }
        return pdf;
    }

    @Override
    public byte[] generateNameTagPrints(Set<NameTag> printRequest) throws Exception {

        byte[] pdf = null;
        Document document = new Document(PageSize.LETTER);
        /*
		 * public boolean setMargins(float marginLeft, float marginRight, float
		 * marginTop, float marginBottom)
         */
        document.setMargins(0, 0, 80, 0);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PdfWriter.getInstance(document, baos);
        document.open();
        // System.out.println(FontFactory.defaultEmbedding);
        // FontFactory.defaultEmbedding = true;

        // System.out.println(FontFactory.defaultEmbedding);
        // SIMSUNB
        // BaseFont bfChinese = BaseFont.createFont("STSongStd-Light",
        // "UniGB-UCS2-H", BaseFont.EMBEDDED);
        BaseFont bfChinese = BaseFont.createFont("fonts/MSYH.TTF", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);

        Font headerChinese = new Font(bfChinese, 16, Font.BOLD);
        Font graceRow = new Font(bfChinese, 16, Font.NORMAL);
        Font nameChinese = new Font(bfChinese, 34, Font.BOLD);
        Font nameEnglish = new Font(bfChinese, 12, Font.NORMAL);
        Font beforeBarcode = new Font(bfChinese, 7, Font.NORMAL);
        Font afterBarcode = new Font(bfChinese, 9, Font.BOLD);

        PdfPTable table = new PdfPTable(2);
        table.getDefaultCell().setBorder(Rectangle.NO_BORDER);
        table.getDefaultCell().setBorder(Rectangle.BOX);
        table.getDefaultCell().setFixedHeight(220);
        table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
        table.getDefaultCell().setVerticalAlignment(Element.ALIGN_MIDDLE);

        int i = 0;
        for (NameTag nameTag : printRequest) {
            PdfPCell cell = new PdfPCell();
            cell.setFixedHeight(220);
            cell.setMinimumHeight(220);
            if (i % 2 == 0) {
                cell.setPaddingRight(50);
            } else if (i % 2 == 1) {
                cell.setPaddingLeft(45);
            }
            i++;
            cell.setBorder(Rectangle.NO_BORDER);
            // getConferenceName
            Paragraph p = new Paragraph(nameTag.getConferenceName(), headerChinese);
            p.setAlignment(Element.ALIGN_CENTER);
            cell.addElement(p);

            // grace row
            if (nameTag.isGrace()) {
                Paragraph p1 = new Paragraph(nameTag.getGraceRow(), graceRow);
                p1.setAlignment(Element.ALIGN_CENTER);
                cell.addElement(p1);

            } else {
                Paragraph p1 = new Paragraph(" ", graceRow);
                p1.setAlignment(Element.ALIGN_CENTER);
                cell.addElement(p1);
            }

            // chinese name
            String chineseName = nameTag.getChineseFullName().replaceAll(".", "$0 ");
            Paragraph p2 = new Paragraph(chineseName, nameChinese);
            p2.setAlignment(Element.ALIGN_CENTER);
            cell.addElement(p2);

            // english name
            Paragraph p3 = new Paragraph(nameTag.getEnglishFullName(), nameEnglish);
            p3.setAlignment(Element.ALIGN_CENTER);
            cell.addElement(p3);

            // before barcode
            {
                PdfPTable barcodeTable = new PdfPTable(3);
                barcodeTable.setTotalWidth(250);
                barcodeTable.setLockedWidth(true);
                barcodeTable.getDefaultCell().setBorder(Rectangle.NO_BORDER);
                barcodeTable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
                barcodeTable.getDefaultCell().setVerticalAlignment(Element.ALIGN_MIDDLE);

                PdfPCell chruchCell = new PdfPCell();
                chruchCell.setBorder(Rectangle.NO_BORDER);
                Paragraph chruchP = new Paragraph(nameTag.getChruchName(), beforeBarcode);
                chruchP.setAlignment(Element.ALIGN_LEFT);
                chruchCell.addElement(chruchP);
                barcodeTable.addCell(chruchCell);

                PdfPCell centerCell = new PdfPCell();
                centerCell.setBorder(Rectangle.NO_BORDER);
                Paragraph centerIdP = new Paragraph(nameTag.getCenterId(), beforeBarcode);
                centerIdP.setAlignment(Element.ALIGN_CENTER);
                centerCell.addElement(centerIdP);
                barcodeTable.addCell(centerCell);

                PdfPCell addressCell = new PdfPCell();
                addressCell.setBorder(Rectangle.NO_BORDER);
                Paragraph addressP = new Paragraph(nameTag.getAddress(), beforeBarcode);
                addressP.setAlignment(Element.ALIGN_RIGHT);
                addressCell.addElement(addressP);
                barcodeTable.addCell(addressCell);

                cell.addElement(barcodeTable);
            }

            //cell.addElement(Image.getInstance(generateBarcode(nameTag.getBarcodeId())));
            PdfPTable barcodeTable = new PdfPTable(1);
            barcodeTable.setTotalWidth(250);
            barcodeTable.setLockedWidth(true);
            barcodeTable.getDefaultCell().setBorder(Rectangle.NO_BORDER);
            barcodeTable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
            barcodeTable.getDefaultCell().setVerticalAlignment(Element.ALIGN_MIDDLE);

            PdfPCell barcodeCell = new PdfPCell();
            barcodeCell.setBorder(Rectangle.NO_BORDER);
            barcodeCell.addElement(Image.getInstance(generateBarcode(nameTag.getBarcodeId())));
            barcodeTable.addCell(barcodeCell);
            cell.addElement(barcodeTable);
            /*
			 * Paragraph p8 = new Paragraph(nameTag.getBarcodeId(),
			 * afterBarcode); p8.setAlignment(Element.ALIGN_CENTER);
			 * p8.setExtraParagraphSpace(10); cell.addElement(p8);
             */
            {
                PdfPTable afterBarcodeTable = new PdfPTable(3);
                afterBarcodeTable.setSpacingBefore(-8);
                afterBarcodeTable.setTotalWidth(250);
                afterBarcodeTable.setLockedWidth(true);
                afterBarcodeTable.getDefaultCell().setBorder(Rectangle.NO_BORDER);
                afterBarcodeTable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
                afterBarcodeTable.getDefaultCell().setVerticalAlignment(Element.ALIGN_TOP);

                PdfPCell leftCell = new PdfPCell();
                leftCell.setBorder(Rectangle.NO_BORDER);
                Paragraph leftP = new Paragraph(nameTag.getGroupId(), afterBarcode);
                leftP.setAlignment(Element.ALIGN_LEFT);
                leftCell.addElement(leftP);
                afterBarcodeTable.addCell(leftCell);

                PdfPCell centerCell = new PdfPCell();
                centerCell.setBorder(Rectangle.NO_BORDER);
                Paragraph centerIdP = new Paragraph(nameTag.getBarcodeId(), afterBarcode);
                centerIdP.setAlignment(Element.ALIGN_CENTER);
                centerCell.addElement(centerIdP);
                afterBarcodeTable.addCell(centerCell);

                PdfPCell rightCell = new PdfPCell();
                rightCell.setBorder(Rectangle.NO_BORDER);
                Paragraph rightP = new Paragraph(nameTag.getTopic(), afterBarcode);
                rightP.setAlignment(Element.ALIGN_RIGHT);
                rightCell.addElement(rightP);
                afterBarcodeTable.addCell(rightCell);

                cell.addElement(afterBarcodeTable);
            }
            table.addCell(cell);
        }

        document.add(table);
        document.close();
        if (baos != null && baos.size() != 0) {
            pdf = baos.toByteArray();
            baos.close();
        }
        return pdf;

    }

}
