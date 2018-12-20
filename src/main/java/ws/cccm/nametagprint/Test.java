/**
 * 
 */
package ws.cccm.nametagprint;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.LinkedHashSet;
import java.util.Set;
import ws.cccm.reg.service.NameTag;
import ws.cccm.reg.service.NameTagService;
import ws.cccm.reg.service.NameTagServiceImpl;

/**
 * @author okclip
 *
 */
public class Test {
	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		Set<NameTag> printRequest = new LinkedHashSet<NameTag>();
		
		for (int i=1; i<= 8; i++){
			printRequest.add(generateTag(i));
		}
		/*for (int i=1; i<= 6; i++){
			printRequest.add(generateGraceTag(i));
		}*/
		
		//printRequest.add(generateTag(1));
		
		NameTagService nameTagService = new NameTagServiceImpl();
		byte[] pdf = nameTagService.generateNameTagPrints(printRequest, 8);
		try {
			Files.delete(Paths.get("C:\\Temp\\nametag.pdf"));
		} catch (Exception e) {
		}
		Files.write(Paths.get("C:\\Temp\\nametag.pdf"), pdf, StandardOpenOption.CREATE_NEW);
		System.out.println("Done");
	}
	
	private static NameTag generateGraceTag(int seq){
		NameTag testTag2 = new NameTag();
		testTag2.setChineseFullName("周優群"+seq);
		testTag2.setEnglishFullName("Youqun Zhou");
		testTag2.setChruchName("真光堂");
		testTag2.setCenterId("1234567891");
		testTag2.setAddress("Streamwood, IL");
		testTag2.setBarcodeId("1888");
		testTag2.setGrace(true);
		return testTag2;
	}
	
	private static NameTag generateTag(int seq){
		NameTag testTag = new NameTag();
		testTag.setChineseFullName("陳為"+seq);
		testTag.setEnglishFullName("Wei Chen");
		testTag.setChruchName("真光堂");
		testTag.setCenterId("1234567890");
		testTag.setAddress("Streamwood, IL");
		testTag.setBarcodeId("1887");
		return testTag;
	}
}
