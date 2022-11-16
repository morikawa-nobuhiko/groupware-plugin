package jp.groupsession.v2.kintai.Knt100;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Date;
import java.util.Calendar;
import java.util.Objects;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import jp.co.sjts.util.NullDefault;
import jp.co.sjts.util.date.UDate;
import jp.co.sjts.util.jdbc.JDBCUtil;
import jp.co.sjts.util.jdbc.SqlBuffer;
import jp.co.sjts.util.dao.AbstractDao;
import jp.groupsession.v2.struts.AbstractGsAction;
import jp.groupsession.v2.cmn.GSConst;
import jp.groupsession.v2.cmn.GSConstSchedule;
import jp.groupsession.v2.cmn.config.PluginConfig;
import jp.groupsession.v2.cmn.dao.GroupModel;
import jp.groupsession.v2.cmn.dao.BaseUserModel;
import jp.groupsession.v2.cmn.dao.UsidSelectGrpNameDao;
import jp.groupsession.v2.cmn.dao.BaseUserModel;
import jp.groupsession.v2.cmn.model.RequestModel;
import jp.groupsession.v2.kintai.Knt100.Knt100GroupModel;
import jp.groupsession.v2.kintai.Knt002.Knt002Dao;

public class Knt100Dao extends AbstractDao {

	/** Logging インスタンス */
	private static Log log__ = LogFactory.getLog(Knt100Dao.class);

	/**
	 * <p>グループ一覧の取得
	 * @param con DB Connection
	 * @throws SQLException SQL実行例外
	 * @return ArrayList<Knt100UserModel>
	 */
	public ArrayList<Knt100GroupModel> selectGroups(Connection con) throws SQLException {

		PreparedStatement pstmt = null;
		ResultSet rs = null;
		ArrayList<Knt100GroupModel> ret = new ArrayList<Knt100GroupModel>();

		try {

			//SQL文
			SqlBuffer sql = new SqlBuffer();
			sql.addSql(" select");
			sql.addSql("   *");
			sql.addSql(" from");
			sql.addSql("   CMN_GROUPM");
			sql.addSql(" where");
			sql.addSql("   GRP_SID >= 2");
//			sql.addSql(" and");
//			sql.addSql("   GRP_SID <= 7");

			pstmt = con.prepareStatement(sql.toSqlString());
			log__.info(sql.toLogString());

			sql.setParameter(pstmt);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				Knt100GroupModel model = new Knt100GroupModel();
				model.setGrpId(rs.getInt("GRP_SID"));
				model.setGrpName(rs.getString("GRP_NAME"));
				ret.add(model);
			}

		} catch (SQLException e) {
			throw e;
		} finally {
			JDBCUtil.closeResultSet(rs);
			JDBCUtil.closeStatement(pstmt);
		}
		return ret;
	}

	/**
	 * <p>グループ＋日付でユーザー情報一覧を取得
	 * @param grpSid   グループID
	 * @param loginSid 除外するユーザーID
	 * @param y        年
	 * @param m        月
	 * @param d        日
	 * @param userId ユーザーID
	 * @param con DB Connection
	 * @throws SQLException SQL実行例外
	 * @return 作業実績合計時間
	 */
	public ArrayList<Knt100UserModel> selectGroupUsersInfo(int grpSid, int loginSid, int y, int m, int d, Connection con) throws SQLException {

		PreparedStatement pstmt = null;
		ResultSet rs = null;
		ArrayList<Knt100UserModel> ret = new ArrayList<Knt100UserModel>();
		Knt002Dao dao002 = new Knt002Dao();

		try {

			//SQL文
			SqlBuffer sql = new SqlBuffer();
			sql.addSql(" select");
			sql.addSql("   USR_SID");
			sql.addSql(" from");
			sql.addSql("   CMN_BELONGM");
			sql.addSql(" where");
			sql.addSql("   GRP_SID = ?");
			sql.addSql(" and");
			sql.addSql("   USR_SID != ?");

			pstmt = con.prepareStatement(sql.toSqlString());
			sql.addIntValue(grpSid);
			sql.addIntValue(loginSid);
			log__.info(sql.toLogString());

			sql.setParameter(pstmt);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				int usrSid = rs.getInt("USR_SID");
				log__.debug("GRP_ID=" + grpSid);
				log__.debug("USR_ID=" + usrSid);
				Knt100UserModel model = new Knt100UserModel();
				// ID＋氏名を取得
				Knt100UserModel userInfo = selectUserInfo(usrSid,con);
				// 打刻の作業実績合計時間（日）を取得
				double stpTime = selectDayUserStampTotalTime(usrSid, y, m, d, con);
				// 中抜け合計時間（2:時間内退勤 3:時間内出勤）
				double stpNonWorkTime = selectDayUserStampNonWorkTime(usrSid, y, m, d, 2, 3, con);
				stpTime = stpTime - stpNonWorkTime;	// 中抜け時間
				if (1 <= stpTime){
					stpTime = stpTime - 1;			// 昼休み
				}
				// 申請実績合計時間（日）を取得
				double usrTime = dao002.selectTotalTimeDay(usrSid, y, m, d, con);
				// 承認情報（日）を取得
				boolean userSyn = selectUserApproval(usrSid, y, m, d, con);
				// 打刻の出退勤時間間隔を取得
				String userInOut = selectDayUserStump(usrSid, y, m, d, con);
				// モデルに反映
				model.setUsrSid(userInfo.getUsrSid());
				model.setUsrName(userInfo.getUsrName());
				model.setStpTime(stpTime);
				model.setStpJsk(userInOut);
				model.setUsrTime(usrTime);
				model.setUsrSyn(userSyn);
				ret.add(model);
			}

		} catch (SQLException e) {
			throw e;
		} finally {
			JDBCUtil.closeResultSet(rs);
			JDBCUtil.closeStatement(pstmt);
		}

		return ret;

	}

	/**
	 * <p>パラメーターによるグループに所属するユーザー基本情報を取得
	 * @param grpId  グループID
	 * @param userId ユーザーID
	 * @param con DB Connection
	 * @throws SQLException SQL実行例外
	 * @return 作業実績合計時間
	 */
	public Knt100UserModel selectUserInfo(int usrSid, Connection con) throws SQLException {

		PreparedStatement pstmt = null;
		ResultSet rs = null;
		Knt100UserModel ret = new Knt100UserModel();

		try {

			//SQL文
			SqlBuffer sql = new SqlBuffer();
			sql.addSql(" select");
			sql.addSql("   *");
			sql.addSql(" from");
			sql.addSql("   CMN_USRM_INF");
			sql.addSql(" where");
			sql.addSql("   USR_SID = ?");

			pstmt = con.prepareStatement(sql.toSqlString());
			sql.addIntValue(usrSid);
			log__.info(sql.toLogString());

			sql.setParameter(pstmt);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				Knt100UserModel model = new Knt100UserModel();
				model.setUsrSid(rs.getInt("USR_SID"));
				model.setUsrName(rs.getString("USI_SEI") + " " + rs.getString("USI_MEI"));
				ret = model;
			}

		} catch (SQLException e) {
			throw e;
		} finally {
			JDBCUtil.closeResultSet(rs);
			JDBCUtil.closeStatement(pstmt);
		}

		return ret;

	}

	/**
	 * <p>パラメーターによるユーザーの作業実績合計時間（日別）を取得
	 * @param grpId  グループID
	 * @param userId ユーザーID
	 * @param con DB Connection
	 * @throws SQLException SQL実行例外
	 * @return 作業実績合計時間
	 */
	public double selectUserDayTime(int usrSid, int yyyy, int mm, int dd, Connection con) throws SQLException {

		PreparedStatement pstmt = null;
		ResultSet rs = null;
		double ret = 0;

		try {

			// TIMESTAMPの書式
			String STPD = yyyy + "-" + mm + "-" + dd + " " + "00:00:00";

			//SQL文
			SqlBuffer sql = new SqlBuffer();
			sql.addSql(" select");
			sql.addSql("   sum(JSK_WRK_TIME) as JSK_WRK_TIME");
			sql.addSql(" from");
			sql.addSql("   KNT_JSK");
			sql.addSql(" where");
			sql.addSql("   JSK_USR_SID = ?");
			sql.addSql(" and");
			sql.addSql("   JSK_YMD = ?");

			pstmt = con.prepareStatement(sql.toSqlString());
			sql.addIntValue(usrSid);
			sql.addStrValue(STPD);
			log__.info(sql.toLogString());

			sql.setParameter(pstmt);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				ret = rs.getDouble("JSK_WRK_TIME");
			}

		} catch (SQLException e) {
			throw e;
		} finally {
			JDBCUtil.closeResultSet(rs);
			JDBCUtil.closeStatement(pstmt);
		}

		return ret;

	}

	/**
	 * <p>パラメーターによるユーザーの承認（日別）を取得
	 * @param grpId  グループID
	 * @param userId ユーザーID
	 * @param con DB Connection
	 * @throws SQLException SQL実行例外
	 * @return 作業実績合計時間
	 */
	public boolean selectUserApproval(int usrSid, int yyyy, int mm, int dd, Connection con) throws SQLException {

		PreparedStatement pstmt = null;
		ResultSet rs = null;
		boolean ret = false;

		try {

			// TIMESTAMPの書式
			String STPD = yyyy + "-" + mm + "-" + dd + " " + "00:00:00";

			//SQL文
			SqlBuffer sql = new SqlBuffer();
			sql.addSql(" select");
			sql.addSql("   SYN_USR_SID");
			sql.addSql(" from");
			sql.addSql("   KNT_SYN");
			sql.addSql(" where");
			sql.addSql("   SYN_USR_SID = ?");
			sql.addSql(" and");
			sql.addSql("   SYN_YMD = ?");

			pstmt = con.prepareStatement(sql.toSqlString());
			sql.addIntValue(usrSid);
			sql.addStrValue(STPD);
			log__.info(sql.toLogString());

			sql.setParameter(pstmt);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				ret = true;
				break;
			}

		} catch (SQLException e) {
			throw e;
		} finally {
			JDBCUtil.closeResultSet(rs);
			JDBCUtil.closeStatement(pstmt);
		}

		return ret;

	}

	/**
	 * <p>パラメーターによるユーザーの出社退社時刻（日別）を取得
	 * @param userId ユーザーID
	 * @param yyyy   年
	 * @param mm     月
	 * @param dd     日
	 * @param con DB Connection
	 * @throws SQLException SQL実行例外
	 * @return 作業実績合計時間
	 */
	public String selectDayUserStump(int usrSid, int yyyy, int mm, int dd, Connection con) throws SQLException {

		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String ret = "";

		try {

			// TIMESTAMPの書式
			String IN  = yyyy + "-" + mm + "-" + dd + " 06:00:00";
			String OUT = yyyy + "-" + mm + "-" + dd + " 23:59:59";

			//SQL文
			SqlBuffer sql = new SqlBuffer();
			sql.addSql(" SELECT");
			sql.addSql("   MIN(STP_TIME) AS IT,");
			sql.addSql("   MAX(STP_TIME) AS OT");
			sql.addSql(" FROM");
			sql.addSql("   KNT_STP");
			sql.addSql(" WHERE");
			sql.addSql("   STP_USR_SID = ?");
			sql.addSql(" AND");
			sql.addSql("   STP_TIME >= ?");
			sql.addSql(" AND");
			sql.addSql("   STP_TIME <= ?");

			pstmt = con.prepareStatement(sql.toSqlString());
			sql.addIntValue(usrSid);
			sql.addStrValue(IN);
			sql.addStrValue(OUT);
			log__.info(sql.toLogString());

			sql.setParameter(pstmt);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				Timestamp inTime = rs.getTimestamp("IT");
				Timestamp outTime = rs.getTimestamp("OT");
				if (inTime==null || outTime==null) {
					break;
				}
				SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
				String strInTime = sdf.format(inTime);
				String strOutTime = sdf.format(outTime);
				ret = strInTime + "～" + strOutTime;
				break;
			}

		} catch (SQLException e) {
			throw e;
		} finally {
			JDBCUtil.closeResultSet(rs);
			JDBCUtil.closeStatement(pstmt);
		}

		return ret;

	}

	/**
	 * <p>パラメーターによるユーザーの作業実績合計時間（日別）を取得
	 * @param userId ユーザーID
	 * @param yyyy   年
	 * @param mm     月
	 * @param dd     日
	 * @param con DB Connection
	 * @throws SQLException SQL実行例外
	 * @return 作業実績合計時間
	 */
	public double selectDayUserStampTotalTime(int usrSid, int yyyy, int mm, int dd, Connection con) throws SQLException {

		PreparedStatement pstmt = null;
		ResultSet rs = null;
		double ret = 0;
		
		try {

			// TIMESTAMPの書式
			String DAY_START = yyyy + "-" + mm + "-" + dd + " 06:00:00";
			String DAY_END   = yyyy + "-" + mm + "-" + dd + " 23:59:59";

			//SQL文
			SqlBuffer sql = new SqlBuffer();
			sql.addSql(" SELECT");
			sql.addSql("   TIMESTAMPDIFF(MINUTE, IN, OUT) AS TIME");
			sql.addSql(" FROM");
//			sql.addSql("   (SELECT MIN(STP_TIME) as IN, MAX(STP_TIME) as OUT FROM KNT_STP WHERE STP_USR_SID = ? AND STP_TIME >= ? AND STP_TIME <= ?)");
			sql.addSql("   (SELECT MIN(STP_TIME) as IN, MAX(STP_TIME) as OUT FROM KNT_STP");
			sql.addSql("   WHERE");
			sql.addSql("    STP_USR_SID = ?");
			sql.addSql("   AND");
			sql.addSql("    STP_TIME >= ?");
			sql.addSql("   AND");
			sql.addSql("    STP_TIME <= ?)");

			pstmt = con.prepareStatement(sql.toSqlString());
			sql.addIntValue(usrSid);
			sql.addStrValue(DAY_START);
			sql.addStrValue(DAY_END);
			log__.info(sql.toLogString());

			sql.setParameter(pstmt);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				ret = rs.getDouble("TIME");
				ret = ret / 60;
				break;
			}

		} catch (SQLException e) {
			throw e;
		} finally {
			JDBCUtil.closeResultSet(rs);
			JDBCUtil.closeStatement(pstmt);
		}

		return ret;

	}

	/**
	 * <p>昼休みと中抜けを考慮したユーザーの実績合計時間（日別）を取得
	 * @param userId ユーザーID
	 * @param yyyy   年
	 * @param mm     月
	 * @param dd     日
	 * @param con DB Connection
	 * @throws SQLException SQL実行例外
	 * @return 作業実績合計時間
	 */
	public double selectDayUserStampTotalTimeEx(int usrSid, int yyyy, int mm, int dd, Connection con) throws SQLException {

		PreparedStatement pstmt = null;
		ResultSet rs = null;
		double ret = 0;
		Knt002Dao dao002 = new Knt002Dao();

		try {

			// 次の日付を取得する
			Calendar cal = Calendar.getInstance();
			cal.set(yyyy, mm - 1, dd);
			cal.add(Calendar.DAY_OF_MONTH, 1);
			int toY = cal.get(Calendar.YEAR);
			int toM = cal.get(Calendar.MONTH) + 1;
			int toD = cal.get(Calendar.DATE);

			// TIMESTAMPの書式
			String WRK_START = yyyy + "-" + mm  + "-" + dd  + " 08:30:00";
			String DAY_START = yyyy + "-" + mm  + "-" + dd  + " 06:00:00";
//			String DAY_END   = yyyy + "-" + mm  + "-" + (dd + 1) + " 04:00:00";
			String DAY_END   = toY  + "-" + toM + "-" + toD + " 04:00:00";
			log__.debug("DAY_END=" + DAY_END);

			//SQL文
			SqlBuffer sql = new SqlBuffer();
			sql.addSql(" SELECT");
//			sql.addSql("   TIMESTAMPDIFF(MINUTE, IN, OUT) AS TIME");
			sql.addSql("   TIMESTAMPDIFF(MINUTE, ?, OUT) AS TIME");
			sql.addSql(" FROM");
			sql.addSql("   (SELECT MIN(STP_TIME) as IN, MAX(STP_TIME) as OUT FROM KNT_STP");
			sql.addSql(" WHERE");
			sql.addSql("   STP_USR_SID = ?");
			sql.addSql(" AND");
			sql.addSql("   STP_TIME >= ?");
			sql.addSql(" AND");
			sql.addSql("   STP_TIME <= ?)");

			pstmt = con.prepareStatement(sql.toSqlString());
			sql.addStrValue(WRK_START);
			sql.addIntValue(usrSid);
			sql.addStrValue(DAY_START);
			sql.addStrValue(DAY_END);
			log__.info(sql.toLogString());

			sql.setParameter(pstmt);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				ret = rs.getDouble("TIME");
				ret = ret / 60;
				break;
			}

			// 出退勤時間
			Timestamp firstInTime = dao002.selectInOutTime(usrSid, yyyy, mm, dd, 0, con);
			Timestamp lastOutTime = dao002.selectInOutTime(usrSid, yyyy, mm, dd, 1, con);
			Timestamp outTime = dao002.selectInOutTime(usrSid, yyyy, mm, dd, 2, con);
			Timestamp inTime  = dao002.selectInOutTime(usrSid, yyyy, mm, dd, 3, con);

			// 出勤または退勤なし
			if (Objects.isNull(firstInTime) || Objects.isNull(lastOutTime)) {
				return 0;
			}

			// 勤怠実績時間
			double stpTime = ret;

			// 休憩時間
			double restTime = 0;

			// 昼休み時間帯
			LocalDateTime dt1;
			LocalDateTime dt2;
			Timestamp startLunchTime = Timestamp.valueOf(yyyy + "-" + mm + "-" + dd + " 12:00:00");
			Timestamp endLunchTime   = Timestamp.valueOf(yyyy + "-" + mm + "-" + dd + " 13:00:00");

			// 昼休みパターン判定
			if (Objects.isNull(firstInTime)) {
				log__.debug("昼休みパターン：出勤忘れ");
				stpTime = 0;
			} else if (Objects.isNull(lastOutTime)) {
				log__.debug("昼休みパターン：退勤忘れ");
				stpTime = 0;
			} else if (lastOutTime.getTime() <= startLunchTime.getTime()) {
				log__.debug("昼休みパターン：AM勤務＆AM退勤");
			} else if (firstInTime.getTime() <= startLunchTime.getTime() && lastOutTime.getTime() <= endLunchTime.getTime()) {
				log__.debug("昼休みパターン：AM勤務＆昼休み中退勤");
				int h = toTimestampFormat(lastOutTime, "hh");
				int m = toTimestampFormat(lastOutTime, "mm");
				int d = toTimestampFormat(lastOutTime, "dd");
				dt1 = LocalDateTime.of(yyyy, mm, dd, 13, 00, 00);
				dt2 = LocalDateTime.of(yyyy, mm, dd, h, m, 00);
				// 昼休み開始時間と退勤時間の差分を取得
				long diffMinutes = ChronoUnit.MINUTES.between(dt2, dt1);
				if (0 < diffMinutes) {
					restTime = 60 - diffMinutes;
					restTime = restTime / 60;
				}
				log__.debug("dt1=" + dt1);
				log__.debug("dt2=" + dt2);
				log__.debug("diffMinutes=" + diffMinutes);
			} else if (endLunchTime.getTime() <= firstInTime.getTime() && endLunchTime.getTime() <= lastOutTime.getTime()) {
				log__.debug("昼休みパターン：PM勤務");
			} else {
				log__.debug("フルタイム:");
				restTime = 1;
			}
			log__.debug("restTime=" + restTime);

			// 中抜け合計時間（2:時間内退勤 3:時間内出勤）
			double stpNonWorkTime = selectDayUserStampNonWorkTime(usrSid, yyyy, mm, dd, 2, 3, con);

			// 中抜け時間
			double nonWorkTime = 0;
			if (Objects.isNull(outTime) && Objects.isNull(inTime)) {
				log__.debug("中抜パターン：出退勤忘れまたは中抜けなし");
			} else if (Objects.isNull(outTime) && Objects.nonNull(inTime)) {
				stpTime = 0;
				restTime = 0;
				log__.debug("中抜パターン：退勤忘れ");
			} else if (Objects.nonNull(outTime) && Objects.isNull(inTime)) {
				stpTime = 0;
				restTime = 0;
				log__.debug("中抜パターン：出勤忘れ");
			} else if (outTime.getTime() <= startLunchTime.getTime() && inTime.getTime() <= startLunchTime.getTime()) {
				nonWorkTime = stpNonWorkTime;
				log__.debug("中抜パターン：AM中抜け");
			} else if (endLunchTime.getTime() <= outTime.getTime() && endLunchTime.getTime() <= inTime.getTime()) {
				nonWorkTime = stpNonWorkTime;
				log__.debug("中抜パターン：PM中抜け");
			} else if (outTime.getTime() < startLunchTime.getTime() && endLunchTime.getTime() < inTime.getTime()) {
				nonWorkTime = stpNonWorkTime;
				// 外出中で昼休みは減算しない
				restTime = 0;
				log__.debug("中抜パターン：AM中抜＆PM戻り");
			} else if (outTime.getTime() <= startLunchTime.getTime() && endLunchTime.getTime() <= inTime.getTime()) {
				log__.debug("中抜パターン：12:00-13:00中抜");
			}
			log__.debug("nonWorkTime=" + nonWorkTime);

			// 17:30-18:00 休憩
			Timestamp start1730Time = Timestamp.valueOf(yyyy + "-" + mm + "-" + dd + " 17:30:00");
			Timestamp end1730Time   = Timestamp.valueOf(yyyy + "-" + mm + "-" + dd + " 18:00:00");
			if (!Objects.isNull(outTime) && !Objects.isNull(inTime) && outTime.getTime() <= start1730Time.getTime() && end1730Time.getTime() <= inTime.getTime()) {
				log__.debug("休憩パターン：中抜け中");
			} else if (firstInTime.getTime() <= start1730Time.getTime() && end1730Time.getTime() <= lastOutTime.getTime()) {
				log__.debug("休憩パターン：出勤17:30以前 退勤18:00以降");
				restTime += 0.5;
			}

			// 22:00-22:30 休憩
			Timestamp start2200Time = Timestamp.valueOf(yyyy + "-" + mm + "-" + dd + " 22:00:00");
			Timestamp end2200Time   = Timestamp.valueOf(yyyy + "-" + mm + "-" + dd + " 22:30:00");
			if (!Objects.isNull(outTime) && !Objects.isNull(inTime) && outTime.getTime() <= start2200Time.getTime() && end2200Time.getTime() <= inTime.getTime()) {
				log__.debug("休憩パターン：中抜け中");
			} else if (firstInTime.getTime() <= start2200Time.getTime() && end2200Time.getTime() <= lastOutTime.getTime()) {
				log__.debug("休憩パターン：出勤22:00以前 退勤22:30以降");
				restTime += 0.5;
			}

			// 勤務時間 - 昼休み - 中抜け - 休憩（17:30-18:00 or 22:00-22:30）
			ret = stpTime - restTime - nonWorkTime;

		} catch (SQLException e) {
			throw e;
		} finally {
			JDBCUtil.closeResultSet(rs);
			JDBCUtil.closeStatement(pstmt);
		}

		return ret;

	}

	/**
	 * <br>[機  能] 指定した時間の期間内判定
	 * <br>[解  説]
	 * <br>[備  考]
	 * @param  ts      判定する時間
	 * @param  ts1     開始時間
	 * @param  ts2     終了時間
	 * @return boolean 判定
	 */
	private boolean between(Timestamp ts, Timestamp ts1, Timestamp ts2) {
		return !(ts1.after(ts) || ts2.before(ts));
	}

	/**
	 * <p>Timestampから指定した書式を返す
	 * @param ts Timestamp
	 * @return int
	 */
	public int toTimestampFormat(Timestamp ts, String fmt){
		SimpleDateFormat sdf = new SimpleDateFormat(fmt);
		String str = sdf.format(ts);
		int ret = Integer.parseInt(str);
		return ret;
	}

	/**
	 * <p>中抜けの作業実績合計時間（日別）を取得
	 * @param userId ユーザーID
	 * @param yyyy   年
	 * @param mm     月
	 * @param dd     日
	 * @param con DB Connection
	 * @throws SQLException SQL実行例外
	 * @return 作業実績合計時間
	 */
	public double selectDayUserStampNonWorkTime(int usrSid, int yyyy, int mm, int dd, int out, int in, Connection con) throws SQLException {

		PreparedStatement pstmt = null;
		ResultSet rs = null;
		double ret = 0;
		
		try {

			// 次の日付を取得する
			Calendar cal = Calendar.getInstance();
			cal.set(yyyy, mm - 1, dd);
			cal.add(Calendar.DAY_OF_MONTH, 1);
			int toY = cal.get(Calendar.YEAR);
			int toM = cal.get(Calendar.MONTH) + 1;
			int toD = cal.get(Calendar.DATE);

			// TIMESTAMPの書式
			String DAY_START = yyyy + "-" + mm  + "-" + dd  + " 06:00:00";
			String DAY_END   = toY  + "-" + toM + "-" + toD + " 04:00:00";

			//SQL文
			SqlBuffer sql = new SqlBuffer();
			sql.addSql(" SELECT");
			sql.addSql("   *,");
			sql.addSql("   TIMESTAMPDIFF(MINUTE, IN, OUT) AS TIME");
			sql.addSql(" FROM");
//			sql.addSql("   (SELECT DISTINCT MIN(STP_TIME) as IN, MAX(STP_TIME) as OUT FROM KNT_STP WHERE STP_USR_SID = ? AND (STP_TIME >= ? AND STP_TIME <= ?) AND (STP_IOM_ID = ? OR STP_IOM_ID = ?))");
			sql.addSql("   (SELECT DISTINCT MIN(STP_TIME) as IN, MAX(STP_TIME) as OUT FROM KNT_STP");
			sql.addSql("   WHERE");
			sql.addSql("    STP_USR_SID = ?");
			sql.addSql("   AND");
			sql.addSql("   (STP_TIME >= ?");
			sql.addSql("   AND");
			sql.addSql("    STP_TIME <= ?)");
			sql.addSql("   AND");
			sql.addSql("   (STP_IOM_ID = ?");
			sql.addSql("    OR");
			sql.addSql("    STP_IOM_ID = ?)");
			sql.addSql("   )");

			pstmt = con.prepareStatement(sql.toSqlString());
			sql.addIntValue(usrSid);
			sql.addStrValue(DAY_START);
			sql.addStrValue(DAY_END);
			sql.addIntValue(out);
			sql.addIntValue(in);
			log__.info(sql.toLogString());

			sql.setParameter(pstmt);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				ret = rs.getDouble("TIME");
				ret = ret / 60;
				break;
			}

		} catch (SQLException e) {
			throw e;
		} finally {
			JDBCUtil.closeResultSet(rs);
			JDBCUtil.closeStatement(pstmt);
		}

		return ret;

	}

	/**
	 * <p>承認ユーザーの存在チェック
	 * @param grpId  グループID
	 * @param userId ユーザーID
	 * @param con DB Connection
	 * @throws SQLException SQL実行例外
	 * @return 作業実績合計時間
	 */
	public Boolean containsApprovedUser(int usrSid, int y, int m, int d, Connection con) throws SQLException {

		PreparedStatement pstmt = null;
		ResultSet rs = null;
		Boolean ret = false;
		Boolean isApproved = false;

		try {

			// TIMESTAMPの書式
			String STPD = y + "-" + m + "-" + d + " " + "00:00:00";

			//SQL文
			SqlBuffer sql = new SqlBuffer();
			sql.addSql(" select");
			sql.addSql("   *");
			sql.addSql(" from");
			sql.addSql("   KNT_SYN");
			sql.addSql(" where");
			sql.addSql("   SYN_USR_SID = ?");
			sql.addSql(" and");
			sql.addSql("   SYN_YMD = ?");

			pstmt = con.prepareStatement(sql.toSqlString());
			sql.addIntValue(usrSid);
			sql.addStrValue(STPD);
			log__.info(sql.toLogString());

			sql.setParameter(pstmt);
			rs = pstmt.executeQuery();
			ret = rs.next();

			if (ret){
				// 存在する
				isApproved = true;
			} else {
				// 存在しない
				isApproved = false;
			}

		} catch (SQLException e) {
			throw e;
		} finally {
			JDBCUtil.closeResultSet(rs);
			JDBCUtil.closeStatement(pstmt);
		}

		return isApproved;

	}

	/**
	* <br>[機  能] 承認情報を登録する
	* <br>[解  説]
	* <br>[備  考]
	* @param usrSid ユーザーID
	* @param y     年
	* @param m     月
	* @param d     日
	* @param con   コネクション
	* @throws SQLException SQL実行時例外
	*/
	public void insertApprovedUser(int usrSid, int y, int m, int d, Connection con) throws SQLException {

		PreparedStatement pstmt = null;

		try {

			// TIMESTAMPの書式
			String STPD = y + "-" + m + "-" + d + " " + "00:00:00";

			//SQL文
			SqlBuffer sql = new SqlBuffer();
			sql.addSql(" insert ");
			sql.addSql(" into ");
			sql.addSql(" KNT_SYN(");
			sql.addSql("   SYN_USR_SID,");
			sql.addSql("   SYN_YMD,");
			sql.addSql(" )");
			sql.addSql(" values");
			sql.addSql(" (");
			sql.addSql("   ?,");
			sql.addSql("   ?");
			sql.addSql(" )");

			pstmt = con.prepareStatement(sql.toSqlString());
			sql.addIntValue(usrSid);
			sql.addStrValue(STPD);
			log__.info(sql.toLogString());
			sql.setParameter(pstmt);
			pstmt.executeUpdate();

		} catch (SQLException e) {
			throw e;
		} finally {
			JDBCUtil.closeStatement(pstmt);
		}
	}

	/**
	* <br>[機  能] 承認情報を削除する
	* <br>[解  説]
	* <br>[備  考]
	* @param usrSid ユーザーID
	* @param y     年
	* @param m     月
	* @param d     日
	* @param con   コネクション
	* @throws SQLException SQL実行時例外
	*/
	public void deleteApprovedUser(int usrSid, int y, int m, int d, Connection con) throws SQLException {

		PreparedStatement pstmt = null;

		try {

			// TIMESTAMPの書式
			String STPD = y + "-" + m + "-" + d + " " + "00:00:00";

			//SQL文
			SqlBuffer sql = new SqlBuffer();
			sql.addSql(" delete");
			sql.addSql("   KNT_SYN");
			sql.addSql(" where");
			sql.addSql("   SYN_USR_SID = ?");
			sql.addSql(" and");
			sql.addSql("   SYN_YMD = ?");

			pstmt = con.prepareStatement(sql.toSqlString());
			sql.addIntValue(usrSid);
			sql.addStrValue(STPD);
			log__.info(sql.toLogString());
			sql.setParameter(pstmt);
			pstmt.executeUpdate();

		} catch (SQLException e) {
			throw e;
		} finally {
			JDBCUtil.closeStatement(pstmt);
		}
	}

}
