package com.promote.common.utils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfCopy;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfSmartCopy;
import com.itextpdf.text.pdf.PdfStamper;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.draw.VerticalPositionMark;

/**
 * PDF 報表工具類
 */
public class PdfUtil {

	private static final Logger logger = LogManager.getLogger(PdfUtil.class);

	private Document pdfDoc = null;
	private PdfWriter writer = null;
	private String kaiuFontPath = null;
	private BaseFont kaiuBaseFont = null;
	private Font kaiuFont = null;
	private PdfUtil instance = null;
	private ByteArrayOutputStream baos = null;
	private byte[] pdfBytes = null;

	/**
	 * 報表版面初始化設定.
	 * 
	 * @param rectangle 版面設定
	 * @param left 左邊邊界
	 * @param right 右邊邊界
	 * @param top 上邊邊界
	 * @param bottom 下邊邊界
	 * @throws DocumentException
	 * @throws IOException 
	 */
	public void init(Rectangle rectangle, float left, float right, float top, float bottom) throws DocumentException, IOException {
		this.baos = new ByteArrayOutputStream();
		instance = this;

		Resource kaiuResource = new ClassPathResource("fonts/kaiu.ttf");
		kaiuFontPath = kaiuResource.getFile().getPath();
		kaiuBaseFont = BaseFont.createFont(kaiuFontPath, BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
		kaiuFont = new Font(kaiuBaseFont, 10, Font.NORMAL);
		pdfDoc = new Document(rectangle, left, right, top, bottom);
		writer = PdfWriter.getInstance(pdfDoc, baos);
		pdfDoc.open();
	}

	/**
	 * 報表版面預設A4直式.
	 * 
	 * @throws DocumentException
	 * @throws IOException 
	 */
	public PdfUtil() throws DocumentException, IOException {
		init(PageSize.A4, 20, 20, 80, 20);
	}

	/**
	 * 添加報表文字.
	 * 
	 * @param text 文字內容
	 * @throws DocumentException
	 */
	public PdfUtil txt(String text) throws DocumentException {
		// pdfDoc.add(new Paragraph(text, kaiuFont));
		
		PdfPTable table = new PdfPTable(1);
		table.setWidthPercentage(100);

		PdfPCell cell = new PdfPCell(new Phrase(text, kaiuFont));
		cell.setPadding(0);
		cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
		cell.setBorder(PdfPCell.NO_BORDER);

		table.addCell(cell);
		pdfDoc.add(table);
		return instance;
	}

	/**
	 * 添加報表文字(直接靠右).
	 * 
	 * @param text 文字內容
	 * @throws DocumentException
	 */
	public PdfUtil txtR(String text) throws DocumentException {
		Paragraph p = new Paragraph(text, kaiuFont);
		p.setAlignment(Element.ALIGN_RIGHT);
		pdfDoc.add(p);
		return instance;
	}

	/**
	 * 添加報表文字(靠左、靠右類型).
	 * 
	 * @param leftText 靠左文字
	 * @param rightText 靠右文字
	 * @throws DocumentException
	 */
	public PdfUtil txtLR(String leftText, String rightText) throws DocumentException {
		Chunk glue = new Chunk(new VerticalPositionMark());
		Paragraph p = new Paragraph(leftText, kaiuFont);
		p.add(new Chunk(glue));
		p.add(rightText);
		pdfDoc.add(p);
		return instance;
	}
	
	public PdfUtil txtC(String centerText) throws DocumentException {
		PdfPTable table = new PdfPTable(3);
		table.setWidthPercentage(100);

		PdfPCell leftCell = new PdfPCell(new Phrase("", kaiuFont));
		leftCell.setPadding(0);
		leftCell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
		leftCell.setBorder(PdfPCell.NO_BORDER);
		
		PdfPCell centerCell = new PdfPCell(new Phrase(centerText, kaiuFont));
		centerCell.setPadding(0);
		centerCell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
		centerCell.setBorder(PdfPCell.NO_BORDER);

		PdfPCell rightCell = new PdfPCell(new Phrase("", kaiuFont));
		rightCell.setPadding(0);
		rightCell.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);
		rightCell.setBorder(PdfPCell.NO_BORDER);

		table.addCell(leftCell);
		table.addCell(centerCell);
		table.addCell(rightCell);
		pdfDoc.add(table);
		return instance;
	}
	
	/**
	 * 添加報表文字(靠左、置中、靠右類型).
	 * 
	 * @param leftText 靠左文字
	 * @param rightText 靠右文字
	 * @throws DocumentException
	 */
	public PdfUtil tableLR(String leftText, String rightText) throws DocumentException {
		PdfPTable table = new PdfPTable(2);
		table.setWidthPercentage(100);

		PdfPCell leftCell = new PdfPCell(new Phrase(leftText, kaiuFont));
		leftCell.setPadding(0);
		leftCell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
		leftCell.setBorder(PdfPCell.NO_BORDER);

		PdfPCell rightCell = new PdfPCell(new Phrase(rightText, kaiuFont));
		rightCell.setPadding(0);
		rightCell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
		rightCell.setBorder(PdfPCell.NO_BORDER);

		table.addCell(leftCell);
		table.addCell(rightCell);
		pdfDoc.add(table);
		return instance;
	}

	/**
	 * 添加報表文字(靠左、置中、靠右類型).
	 * 
	 * @param leftText 靠左文字
	 * @param centerText 置中文字
	 * @param rightText 靠右文字
	 * @throws DocumentException
	 */
	public PdfUtil txtLCR(String leftText, String centerText, String rightText) throws DocumentException {
		PdfPTable table = new PdfPTable(3);
		table.setWidthPercentage(100);

		PdfPCell leftCell = new PdfPCell(new Phrase(leftText, kaiuFont));
		leftCell.setPadding(0);
		leftCell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
		leftCell.setBorder(PdfPCell.NO_BORDER);

		PdfPCell centerCell = new PdfPCell(new Phrase(centerText, kaiuFont));
		centerCell.setPadding(0);
		centerCell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
		centerCell.setBorder(PdfPCell.NO_BORDER);

		PdfPCell rightCell = new PdfPCell(new Phrase(rightText, kaiuFont));
		rightCell.setPadding(0);
		rightCell.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);
		rightCell.setBorder(PdfPCell.NO_BORDER);

		table.addCell(leftCell);
		table.addCell(centerCell);
		table.addCell(rightCell);
		pdfDoc.add(table);
		return instance;
	}
	
	/**
	 * 斷行.
	 * 
	 * @throws DocumentException
	 */
	public PdfUtil newLine() throws DocumentException {
		pdfDoc.add(new Paragraph(Chunk.NEWLINE));
		return instance;
	}
	/**
	 * 添加元素.
	 * 
	 * @throws DocumentException
	 */
	public PdfUtil element(Element element) throws DocumentException {
		pdfDoc.add(element);
		return instance;
	}
	
	/**
	 * 添加頁首/頁尾資訊.
	 * 
	 * @return 回傳添加完頁首/頁尾資訊的PDF byte[]
	 * @throws DocumentException
	 * @throws IOException
	 */
	public byte[] pageHF() throws DocumentException, IOException {
		PdfReader pr = new PdfReader(pdfBytes);
		int totalPages = pr.getNumberOfPages();

		ByteArrayOutputStream baosStamper = null;
		PdfStamper stamper = null;
		PdfContentByte cb = null;
		byte[] pdfByte = null;
		try {
			baosStamper = new ByteArrayOutputStream();
			stamper = new PdfStamper(pr, baosStamper);

			for (int i = 1; i <= totalPages; i++) {
				cb = stamper.getUnderContent(i);
				cb.beginText();
				cb.setFontAndSize(kaiuBaseFont, 9);
				cb.showTextAligned(PdfContentByte.ALIGN_RIGHT, "頁數：" + i, pdfDoc.right(), pdfDoc.top() + 20, 0);
				cb.endText();
				
				/*Resource logImageResource = new ClassPathResource("ftl/logo.png");
				Image img = Image.getInstance(logImageResource.getFile().getPath());
				img.setAbsolutePosition(pdfDoc.left(), pdfDoc.top() + 20);
				img.scalePercent(60);
				cb.addImage(img, false);*/
			}
		} catch (Exception e) {
			logger.error(e);
		} finally {
			if (stamper != null) stamper.close();
			if (pr != null) pr.close();
			if (baosStamper != null) {
				pdfByte = baosStamper.toByteArray();
			}
			IOUtils.closeQuietly(baos);
			IOUtils.closeQuietly(baosStamper);
		}
		return pdfByte;
	}

	/**
	 * 增加表格.
	 * 
	 * @param headerMap 表頭資訊
	 * @param rowList 表格資料集合
	 * @param size 字型大小
	 * @throws DocumentException 
	 */
	@SuppressWarnings("unchecked")
	public PdfUtil table(Map<String, Object> headerMap, List<Map<String, Object>> rowList, float size) throws DocumentException {
		int columnSize = (int) headerMap.get("COLUMN_SIZE");
		List<Map<String, Object>> cellList = (List<Map<String, Object>>) headerMap.get("CELLS");

		PdfPTable table = new PdfPTable(columnSize);
		table.setWidthPercentage(100f);

		int headerRows = 1;																							// 設定第一列為表頭
		if (headerMap.get("HEADER_ROWS") != null) {
			headerRows = (int) headerMap.get("HEADER_ROWS");
		}

		if (rowList != null && rowList.size() != 0) {
			table.setHeaderRows(headerRows);
		}

		// 設定表頭寬度
		float[] widths = null;
		if (headerMap.get("WIDTHS") != null) {
			widths = (float[]) headerMap.get("WIDTHS");
		} else {
			widths = new float[columnSize];
			for (int i = 0; i < widths.length; i++) {
				if (i == 0 || i == 1) {
					widths[i] = 1;
				} else {
					widths[i] = 2;
				}
			}			
		}
		table.setWidths(widths);

		// 繪製表頭
		for (Map<String, Object> cellMap : cellList) {
			PdfPCell cell = new PdfPCell();
			cell.setVerticalAlignment(Element.ALIGN_TOP);
			cell.setBorder(PdfPCell.NO_BORDER);

			if (cellMap.get("ROWSPAN") != null) {
				cell.setRowspan((int) cellMap.get("ROWSPAN"));
			}

			if (cellMap.get("COLSPAN") != null) {
				cell.setColspan((int) cellMap.get("COLSPAN"));
			}

			if (cellMap.get("BACKGROUND_COLOR") != null) {
				String hexColor = (String) cellMap.get("BACKGROUND_COLOR");
				int red = Integer.valueOf(hexColor.substring(1, 3), 16);
				int green = Integer.valueOf(hexColor.substring(3, 5), 16);
				int blue = Integer.valueOf(hexColor.substring(5, 7), 16);

				cell.setBackgroundColor(new BaseColor(red, green, blue));
			}

			String colText = (String) cellMap.get("TEXT");
			colText = colText.replace("\\n", "\n");

			Font headerFont= new Font(kaiuBaseFont, size);
			if (cellMap.get("FONT_COLOR") != null) {
				String hexColor = (String) cellMap.get("FONT_COLOR");
				int red = Integer.valueOf(hexColor.substring(1, 3), 16);
				int green = Integer.valueOf(hexColor.substring(3, 5), 16);
				int blue = Integer.valueOf(hexColor.substring(5, 7), 16);

				headerFont.setColor(new BaseColor(red, green, blue));
			}

			Paragraph p = new Paragraph(colText, headerFont);
			p.setAlignment(Element.ALIGN_CENTER);

			Float fontSize = p.getFont().getSize();
			Float capHeight = p.getFont().getBaseFont().getFontDescriptor(BaseFont.CAPHEIGHT, fontSize);

			cell.setPaddingBottom(4 * (fontSize - capHeight));
			cell.addElement(p);

			table.addCell(cell);
		}

		// 添加資料
		if (rowList != null) {
			for (Map<String, Object> rowMap : rowList) {
				//logger.debug("report data row = " + rowMap);

				for (Map<String, Object> cellMap : cellList) {
					if (cellMap.get("KEY") == null) continue;
					
					String columnKey = (String) cellMap.get("KEY");
					String txtAlign = (String) cellMap.get("TXT_ALIGN");

					PdfPCell cell = new PdfPCell();

					int align = Element.ALIGN_RIGHT;
					if ("LEFT".equals(txtAlign)) align = Element.ALIGN_LEFT;
					if ("CENTER".equals(txtAlign)) align = Element.ALIGN_CENTER;
					if ("RIGHT".equals(txtAlign)) align = Element.ALIGN_RIGHT;

					Font kaiuFontData = new Font(kaiuBaseFont, size);
					String cellValue = (rowMap.get(columnKey) == null ? "" : rowMap.get(columnKey).toString());

					Paragraph p = new Paragraph(cellValue, kaiuFontData);
					p.setAlignment(align);

					Float fontSize = p.getFont().getSize();
					Float capHeight = p.getFont().getBaseFont().getFontDescriptor(BaseFont.CAPHEIGHT, fontSize);

					cell.setPaddingBottom(3 * (fontSize - capHeight));
					cell.addElement(p);

					table.addCell(cell);
				}
			}
		}
		

		pdfDoc.add(table);
		return instance;
	}

	/**
	 * 合併PDF.
	 * 
	 * @param sourcePdf 來源PDF byte陣列
	 * @param copyPdf 要合併的PDF byte陣列
	 * @return 回傳合併完的PDF byte陣列
	 * @throws DocumentException
	 * @throws IOException
	 */
	public static byte[] appendPdf(byte[] sourcePdf, byte[] copyPdf) throws DocumentException, IOException {
		ByteArrayOutputStream copyBaos = null;
		byte[] pdfByte = null;
		try {
			Document document = new Document();
			copyBaos = new ByteArrayOutputStream();
			PdfCopy copy = new PdfSmartCopy(document, copyBaos);
			document.open();

			PdfReader sourceReader = new PdfReader(sourcePdf);
			copy.addDocument(sourceReader);
			sourceReader.close();

			PdfReader copyReader = new PdfReader(copyPdf);
			copy.addDocument(copyReader);
			copyReader.close();

			document.close();

			pdfByte = copyBaos.toByteArray();
		} catch (Exception e) {
			logger.error(e);
		} finally {
			IOUtils.closeQuietly(copyBaos);
		}
		return pdfByte;
	}

	/**
	 * 開啟新的頁面
	 */
	public PdfUtil newPage() {
		pdfDoc.newPage();
		return instance;
	}

	/**
	 * 取得PDF byte[].
	 */
	public byte[] toPdfByte() {
		return pdfBytes;
	}

	/**
	 * 關閉Document.
	 */
	public PdfUtil closeDoc() {
		if (pdfDoc != null && pdfDoc.isOpen()) {
			try {
				txt(" "); // 確保PDF有內容，才能順利關閉
			} catch (DocumentException e) {
				logger.info("PdfUtil.closeDoc {}", e.getMessage());
			} finally{
				pdfDoc.close();
				pdfBytes = baos.toByteArray();
			} 
		}
		return instance;
	}
	
	/**
	 * 對 PDF 進行加密
	 * @param sourcePdf
	 * @param applicantId
	 * @return
	 * @throws DocumentException
	 * @throws IOException
	 */
	public byte[] doEncryption(byte[] sourcePdf, String applicantId) throws DocumentException, IOException {
		byte[] encryptionPdf = null;
		try {
			PdfReader srcReader = new PdfReader(sourcePdf);
			ByteArrayOutputStream encryptionBaos = new ByteArrayOutputStream(sourcePdf.length);
			PdfStamper encryptionStamper = new PdfStamper(srcReader, encryptionBaos);
			
			encryptionStamper.setEncryption(applicantId.getBytes(), applicantId.getBytes(), PdfWriter.ALLOW_PRINTING, PdfWriter.ENCRYPTION_AES_128);
			
			encryptionStamper.close();
			encryptionPdf = encryptionBaos.toByteArray();
			srcReader.close();
			encryptionBaos.close();
		} catch(DocumentException|IOException e) {
			logger.info("PdfUtil.doEncryption {}", e.getMessage());
			throw e;
		} finally {
		}
		return encryptionPdf;
	}
	
	public static void main(String[] args) {
		try {
			// test data
//			AcctValuePdfVo vo = new AcctValuePdfVo();
//			vo.setPolicyNo("UC50000017");
//			vo.setProductName("金采人生變額萬能壽險");
//			vo.setEffectivedate(new Date());
//			vo.setCustomerName("吳安國");
//			vo.setInsuredName("吳佩怡");
//			vo.setPolicyType("乙型");
//			vo.setMainAmount(new BigDecimal("10000000"));
//			vo.setLoanAmount(new BigDecimal("0"));
//			vo.setIntAmount(new BigDecimal("0"));
//			vo.setDeathInsuranceAmount(new BigDecimal("1123263"));

//			PdfUtil pdfUtil = new PdfUtil();
//			DecimalFormat df = new DecimalFormat("#,###");

			// page1
			// 表頭
//			pdfUtil.txtC(String.format("%s帳戶價值通知單", "111"));
//			pdfUtil.newLine();
			// 保單資訊
//			String[] effectivedate = DateUtil.getRocDate(vo.getEffectivedate()).split("/");
////
//			pdfUtil.tableLR(
//					String.format("保單號碼：%s", vo.getPolicyNo()),
//					String.format("投保始期：%s", String.format("%s年%s月%s日", effectivedate[0], effectivedate[1], effectivedate[2])));
//			pdfUtil.tableLR(
//					String.format("要 保 人：%s", vo.getCustomerName()),
//					String.format("被 保 人：%s", vo.getInsuredName()));
//			pdfUtil.tableLR(
//					String.format("保險型態：%s", vo.getPolicyType()),
//					String.format("保險金額：%s", df.format(vo.getMainAmount()) + "元"));
//			pdfUtil.tableLR(
//					String.format("保單借款本金：%s", StringUtils.leftPad(df.format(vo.getLoanAmount()) + "元", 11, " ")),
//					String.format("保單借款利息累計：%s", StringUtils.leftPad(df.format(vo.getIntAmount()) + "元", 11, " ")));
//			pdfUtil.tableLR(
//					String.format("身故保險金額：%s", StringUtils.leftPad(df.format(vo.getDeathInsuranceAmount()) + "元", 11, " ")), "");
/*
			byte[] pdfByte = pdfUtil.closeDoc().pageHF();
			pdfByte = pdfUtil.doEncryption(pdfByte, "1234");
			System.out.println(222);
			FileOutputStream fos = new FileOutputStream(new File("tmp/accountValue.pdf"));
			System.out.println(444);
			IOUtils.write(pdfByte, fos);
			IOUtils.closeQuietly(fos);
			System.out.println(333);
			String str = new String("峯".getBytes("BIG5"), "UTF-8");
			if(str.equals("?")){
		       System.out.println("峯" + "非BIG5字集");
		    }
		    System.out.println(StringEscapeUtils.escapeJava("鑫尹"));
		    System.out.println(new String("峯".getBytes("BIG5"), "UTF-16"));*/
		} catch (Exception e) {
//			e.printStackTrace();
		}
	}
}