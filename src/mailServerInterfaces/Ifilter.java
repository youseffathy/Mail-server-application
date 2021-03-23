/**
 *
 */
package mailServerInterfaces;

import org.json.simple.JSONObject;

/**
 * @author SHIKO
 *
 */
public interface Ifilter {
	/**
	 *
	 * @param j the json object containing the filters
	 */
	public  void setFilters(JSONObject j);
	/**
	 *
	 * @param subjet email subject to compare with the filters
	 * @return the filter name (folder to be added to) or null if there's no filters.
	 */
	public String receiveEmail(String subjet, String from);

}
