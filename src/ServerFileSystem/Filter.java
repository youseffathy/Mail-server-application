/**
 *
 */
package ServerFileSystem;

import org.json.simple.JSONObject;

import mailServerInterfaces.Ifilter;

/**
 * @author SHIKO
 *
 */
public class Filter implements Ifilter {
	/**
	 * filters json object
	 */

	private JSONObject filters = null;

	@Override
	public void setFilters(JSONObject j) {
		filters = j;
	}

	@Override
	public String receiveEmail(String subjet, String from) {
		int n = Integer.valueOf((String) filters.get("filtersnum"));
		for (int i = 1; i <= n; i++) {
			String[] arr = {((String) filters.get(String.valueOf(i))), subjet };
			if (isSimilar(arr)) {
				return ((String) filters.get(String.valueOf(i)));
			}
		}
		if (isSpam(subjet, from)) {
			return "Spam";
		}
		return null;
	}

	/**
	 * @param subject email sub
	 * @param user author
	 * @return is spam
	 */
	private boolean isSpam(String subject, String user) {
		String spamFilters = (String) filters.get("spamFilters");
		String[] spams = spamFilters.split(", ");
		for (int i = 0; i < spams.length; i++) {
			String[] arr = {subject, spams[i] };
			if (isSimilar(arr)) {
				return true;
			}
		}
		String spamUsers = (String) filters.get("spamUsers");
		spams = spamUsers.split(", ");
		for (int i = 1; i < spams.length; i++) {
			if (user.equals(spams[i])) {
				return true;
			}
		}
		return false;

	}

	/**
	 * @param text array of two texts to compare
	 * @return boolean if the two strings are similar
	 */
	public boolean isSimilar(String[] text) {
		char[] t1 = text[0].toCharArray();
		char[] t2 = text[1].toCharArray();
		double pre = prefix(t1, t2);
		int[] suf = suffix(t1, t2);
		double lcs = longest_common_sequence_seg_words(t1, t2);
		double l1 = t1.length;
		double l2 = t2.length;
		double minlen = Math.min(l1, l2);
		double maxlen = Math.max(l1, l2);
		double x = pre + suf[0];
		double l = (0.5) * (l1 + l2);
		double pct = (1.0 / 3.0) * (lcs / l + lcs / l1 + lcs / l2)
		+ (0.5) * 0.01 * (x / l1 + x / l2) + 0.2 * (minlen / maxlen);

		return (pct > 0.6);
	}

	/**
	 * @param t1 text 1
     * @param t2 text 2
     * @return number of lcs
	 */
	private int longest_common_sequence_seg_words(char[] t1, char[] t2) {
		int x = t1.length;
		int y = t2.length;
		int[][] L = new int[x + 1][y + 1];
		for (int i = x; i >= 0; i--) {
			for (int j = y; j >= 0; j--) {
				if (i == x || j == y) {
					L[i][j] = 0;
				} else if (Character.toLowerCase(t1[i]) == Character.toLowerCase(t2[j])) {
					// console.log(t1[i] + " : " + c[t1[i]]);
					L[i][j] = 1 + L[i + 1][j + 1];
				} else {
					L[i][j] = Math.max(L[i + 1][j], L[i][j + 1]);
				}
			}
		}
		return L[0][0];
	}

	/**
	 * @param t1 text 1
     * @param t2 text 2
     * @return number of prefixs
	 */
	private int prefix(char[] t1, char[] t2) {
		int i = 0;
		int minlen = Math.min(t1.length, t2.length);
		while (i < minlen && (Character.toLowerCase(t1[i]) == Character.toLowerCase(t2[i]))) {
			i++;
		}
		return i;
	}

	/**
	 * @param t1 text 1
	 * @param t2 text 2
	 * @return number of suffixs
	 */
	private int[] suffix(char[] t1, char[] t2) {
		int i = t1.length - 1;
		int j = t2.length - 1;
		int s = 0;
		while (i >= 0 && j >= 0 && (Character.toLowerCase(t1[i]) == Character.toLowerCase(t2[j]))) {
			s++;
			i--;
			j--;
		}
		int[] a = {s, i, j };
		return a;
	}
}
