/**
 * 
 */
package ws.cccm.nametagprint;

import ws.cccm.reg.service.NameTag;
import ws.cccm.reg.service.NameTagService;
import ws.cccm.reg.service.NameTagServiceImpl;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Date;
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
		Date now = new Date();
		String caption = now.toString();
		System.out.println(caption);
		for (int i=1; i<= 6; i++){
			printRequest.add(generateTag(i));
		}
		for (int i=1; i<= 6; i++){
			printRequest.add(generateGraceTag(i));
		}
		
		//printRequest.add(generateTag(2));
		
		NameTagService nameTagService = new NameTagServiceImpl();
		byte[] pdf = nameTagService.generateNameTagPrints(printRequest, 1);
		try {
			Files.delete(Paths.get("d:\\Windows\\nametag.pdf"));
		} catch (Exception e) {
		}
		Files.write(Paths.get("D:\\Windows\\nametag.pdf"), pdf, StandardOpenOption.CREATE_NEW);
		System.out.println("Done");
	}
	
	private static NameTag generateGraceTag(int seq){
		NameTag testTag2 = new NameTag();
		testTag2.setConferenceName("二零一八基督徒大會");
		testTag2.setChineseFullName("丁海");
		testTag2.setEnglishFullName("Hai Din");
		testTag2.setChruchName("Asian Campus Church  - UIC Chinese Christian Fellowship");
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
		testTag.setConferenceName("二零一八基督徒大會");
		testTag.setChineseFullName("陳為");
		testTag.setTopic("#1 #2 #3 #4");
		testTag.setEnglishFullName("Wei Chen");
		testTag.setChruchName("華人基督徒聯合會北郊堂活泉青年團契");
		testTag.setCenterId("1234567890");
		testTag.setAddress("Streamwood, IL");
		testTag.setGroupId("99");
		testTag.setBarcodeId("1887");
		return testTag;
	}
}
