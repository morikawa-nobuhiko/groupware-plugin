package jp.groupsession.v2.kintai.Knt500;

import jp.groupsession.v2.struts.AbstractGsForm;
import org.apache.struts.action.*;

//(1)FormFileインタフェースのインポート
import org.apache.struts.upload.FormFile;

/**
 * <br>[機  能] フォームクラス
 * <br>[解  説]
 * <br>[備  考]
 *
 * @author JTS
 */
public class Knt500Form extends AbstractGsForm {

	//(2)FormFileインタフェースのfileUp変数を宣言
	private FormFile fileUp;

	//(3)アップロードファイル情報に対するアクセスメソッドを宣言
	public FormFile getFileUp() { return fileUp;}
	public void setFileUp(FormFile fileUp) {this.fileUp = fileUp;}

}
