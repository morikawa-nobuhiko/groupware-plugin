package jp.groupsession.v2.kintai.Knt500;

/**
 * <br>[機 能] 作業情報を保持するModelクラス
 * <br>[解 説]
 * <br>[備 考]
 *
 * @author JTS
 */
public class Knt500WrkModel {

	/** 作業ID */
	private int wrkId__ = -1;
	/** 作業内容 */
	private String wrkName__ = "";

	/**
	 * <p>wrkId を取得します。
	 * @return wrkId
	 */
	public int getWrkId() {
		return wrkId__;
	}

	/**
	 * <p>wrkId をセットします。
	 * @param wrkId wrkId
	 */
	public void setWrkId(int wrkId) {
		wrkId__ = wrkId;
	}

	/**
	 * <p>wrkName を取得します。
	 * @return wrkName
	 */
	public String getWrkName() {
		return wrkName__;
	}

	/**
	 * <p>wrkName をセットします。
	 * @param wrkName wrkName
	 */
	public void setWrkName(String wrkName) {
		wrkName__ = wrkName;
	}

}
