/**
 * 
 */
package ws.cccm.reg.service;

import java.util.Set;

/**
 * @author okclip
 *
 */
public interface NameTagService {
	
	public byte[] generateNameTagPrints(Set<NameTag> printRequest) throws Exception;
	
	public byte[] generateNameTagPrints(Set<NameTag> printRequest, int startPosition) throws Exception;

}
