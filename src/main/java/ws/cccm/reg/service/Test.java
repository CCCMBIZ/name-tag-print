/**
 * 
 */
package ws.cccm.reg.service;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.LinkedHashSet;
import java.util.Set;

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
		
		for (int i=1; i<= 15; i++){
			printRequest.add(generateTag(i));
		}
		for (int i=1; i<= 15; i++){
			printRequest.add(generateGraceTag(i));
		}
		
		//printRequest.add(generateTag(2));
		
		NameTagService nameTagService = new NameTagServiceImpl();
		byte[] pdf = nameTagService.generateNameTagPrints(printRequest, 3);
		try {
			Files.delete(Paths.get("C:\\NameTagPrint\\nametag.pdf"));
		} catch (Exception e) {
		}
		Files.write(Paths.get("C:\\NameTagPrint\\nametag.pdf"), pdf, StandardOpenOption.CREATE_NEW);
		System.out.println("Done");
	}
	
	private static NameTag generateGraceTag(int seq){
		NameTag testTag2 = new NameTag();
		testTag2.setChineseFullName("丁海");
		testTag2.setEnglishFullName("Hai Din");
		testTag2.setChruchName("Pittsburgh Chinese Church at Oakland");
		testTag2.setCenterId("1234567891");
		testTag2.setAddress("Streamwood, IL");
		testTag2.setGroupId("100");
		testTag2.setBarcodeId("1888");
		testTag2.setGrace(true);
		testTag2.setTopic("#1 #2 #3");
		return testTag2;
	}
	
	private static NameTag generateTag(int seq){
		NameTag testTag = new NameTag();
		testTag.setChineseFullName("陳為");
		testTag.setTopic("#1 #2 #3 #4");
		testTag.setEnglishFullName("Wei Chen");
		testTag.setChruchName("真光堂");
		testTag.setCenterId("1234567890");
		testTag.setAddress("Streamwood, IL");
		testTag.setGroupId("99");
		testTag.setBarcodeId("1887");
		return testTag;
	}
}
