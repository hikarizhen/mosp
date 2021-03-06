/*
 * MosP - Mind Open Source Project    http://www.mosp.jp/
 * Copyright (C) MIND Co., Ltd.       http://www.e-mind.co.jp/
 * 
 * This program is free software: you can redistribute it and/or
 * modify it under the terms of the GNU Affero General Public License
 * as published by the Free Software Foundation, either version 3
 * of the License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 * 
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package jp.mosp.time.bean.impl;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jp.mosp.framework.base.MospException;
import jp.mosp.framework.base.MospParams;
import jp.mosp.framework.constant.MospConst;
import jp.mosp.framework.utils.DateUtility;
import jp.mosp.framework.utils.MospUtility;
import jp.mosp.platform.base.PlatformBean;
import jp.mosp.platform.bean.workflow.WorkflowIntegrateBeanInterface;
import jp.mosp.platform.constant.PlatformConst;
import jp.mosp.platform.constant.PlatformMessageConst;
import jp.mosp.platform.dao.file.ImportDaoInterface;
import jp.mosp.platform.dao.file.ImportFieldDaoInterface;
import jp.mosp.platform.dao.human.HumanDaoInterface;
import jp.mosp.platform.dao.human.impl.PfmHumanDao;
import jp.mosp.platform.dao.workflow.WorkflowDaoInterface;
import jp.mosp.platform.dto.file.ImportDtoInterface;
import jp.mosp.platform.dto.file.ImportFieldDtoInterface;
import jp.mosp.platform.dto.human.HumanDtoInterface;
import jp.mosp.platform.dto.workflow.WorkflowDtoInterface;
import jp.mosp.platform.utils.InputCheckUtility;
import jp.mosp.platform.utils.MonthUtility;
import jp.mosp.time.bean.ApplicationReferenceBeanInterface;
import jp.mosp.time.bean.ImportTableReferenceBeanInterface;
import jp.mosp.time.bean.PaidHolidayReferenceBeanInterface;
import jp.mosp.time.bean.TotalTimeTransactionReferenceBeanInterface;
import jp.mosp.time.bean.WorkTypeImportAddonBeanInterface;
import jp.mosp.time.bean.WorkTypeItemReferenceBeanInterface;
import jp.mosp.time.bean.WorkTypeItemRegistBeanInterface;
import jp.mosp.time.constant.TimeConst;
import jp.mosp.time.constant.TimeFileConst;
import jp.mosp.time.constant.TimeMessageConst;
import jp.mosp.time.dao.settings.AttendanceDaoInterface;
import jp.mosp.time.dao.settings.CutoffDaoInterface;
import jp.mosp.time.dao.settings.HolidayDaoInterface;
import jp.mosp.time.dao.settings.HolidayDataDaoInterface;
import jp.mosp.time.dao.settings.PaidHolidayDataDaoInterface;
import jp.mosp.time.dao.settings.StockHolidayDataDaoInterface;
import jp.mosp.time.dao.settings.TimeSettingDaoInterface;
import jp.mosp.time.dao.settings.TotalTimeDataDaoInterface;
import jp.mosp.time.dao.settings.WorkTypeDaoInterface;
import jp.mosp.time.dao.settings.impl.TmdAttendanceDao;
import jp.mosp.time.dao.settings.impl.TmdHolidayDataDao;
import jp.mosp.time.dao.settings.impl.TmdPaidHolidayDao;
import jp.mosp.time.dao.settings.impl.TmdStockHolidayDao;
import jp.mosp.time.dao.settings.impl.TmdTotalTimeDao;
import jp.mosp.time.dao.settings.impl.TmmWorkTypeDao;
import jp.mosp.time.dto.settings.ApplicationDtoInterface;
import jp.mosp.time.dto.settings.AttendanceDtoInterface;
import jp.mosp.time.dto.settings.CutoffDtoInterface;
import jp.mosp.time.dto.settings.HolidayDataDtoInterface;
import jp.mosp.time.dto.settings.HolidayDtoInterface;
import jp.mosp.time.dto.settings.PaidHolidayDataDtoInterface;
import jp.mosp.time.dto.settings.PaidHolidayDtoInterface;
import jp.mosp.time.dto.settings.StockHolidayDataDtoInterface;
import jp.mosp.time.dto.settings.TimeSettingDtoInterface;
import jp.mosp.time.dto.settings.TotalTimeDataDtoInterface;
import jp.mosp.time.dto.settings.TotalTimeDtoInterface;
import jp.mosp.time.dto.settings.WorkTypeDtoInterface;
import jp.mosp.time.dto.settings.WorkTypeItemDtoInterface;
import jp.mosp.time.dto.settings.impl.TmdAttendanceDto;
import jp.mosp.time.dto.settings.impl.TmdHolidayDataDto;
import jp.mosp.time.dto.settings.impl.TmdPaidHolidayDataDto;
import jp.mosp.time.dto.settings.impl.TmdStockHolidayDto;
import jp.mosp.time.dto.settings.impl.TmdTotalTimeDataDto;
import jp.mosp.time.dto.settings.impl.TmmWorkTypeDto;
import jp.mosp.time.entity.WorkTypeEntity;
import jp.mosp.time.utils.TimeUtility;

/**
 * ?????????????????????????????????????????????
 */
public class ImportTableReferenceBean extends PlatformBean implements ImportTableReferenceBeanInterface {
	
	/**
	 * ????????????????????????????????????<br>
	 */
	public static final int								MAX_LENGTH_TIME_COMMENT		= 50;
	
	/**
	 * ???????????????(?????????????????????????????????????????????)???<br>
	 */
	protected static final String						CODE_KEY_WORKTYPE_ADDONS	= "WorkTypeImportAddons";
	
	/**
	 * ???????????????????????????DAO???
	 */
	protected ImportDaoInterface						importDao;
	
	/**
	 * ??????????????????????????????????????????DAO???
	 */
	protected ImportFieldDaoInterface					importFieldDao;
	
	/**
	 * ???????????????DAO???
	 */
	protected HumanDaoInterface							humanDao;
	
	/**
	 * ???????????????DAO???
	 */
	private AttendanceDaoInterface						attendanceDao;
	
	/**
	 * ?????????????????????DAO???
	 */
	private TotalTimeDataDaoInterface					totalTimeDataDao;
	
	/**
	 * ?????????????????????DAO???
	 */
	private HolidayDaoInterface							holidayDao;
	
	/**
	 * ???????????????DAO???
	 */
	private HolidayDataDaoInterface						holidayDataDao;
	
	/**
	 * ?????????????????????DAO???
	 */
	private PaidHolidayDataDaoInterface					paidHolidayDataDao;
	
	/**
	 * ???????????????????????????DAO???
	 */
	private StockHolidayDataDaoInterface				stockHolidayDataDao;
	
	/**
	 * ?????????????????????DAO???<br>
	 */
	protected WorkTypeDaoInterface						workTypeDao;
	
	/**
	 * ??????????????????DAO???<br>
	 */
	private TimeSettingDaoInterface						timeSettingDao;
	
	/**
	 * ????????????DAO????????????<br>
	 */
	private CutoffDaoInterface							cutoffDao;
	
	/**
	 * ??????????????????DAO???<br>
	 */
	private WorkflowDaoInterface						workflowDao;
	
	/**
	 * ??????????????????????????????<br>
	 */
	private ApplicationReferenceBeanInterface			application;
	
	/**
	 * ????????????????????????????????????<br>
	 */
	protected PaidHolidayReferenceBeanInterface			paidHolidayReference;
	
	/**
	 * ????????????????????????????????????<br>
	 */
	private TotalTimeTransactionReferenceBeanInterface	totalTimeTransaction;
	
	/**
	 * ????????????????????????????????????<br>
	 */
	protected WorkflowIntegrateBeanInterface			workflowIntegrate;
	
	/**
	 * ????????????????????????????????????<br>
	 */
	protected WorkTypeItemRegistBeanInterface			workTypeItemRegist;
	
	/**
	 * ????????????????????????????????????<br>
	 */
	protected WorkTypeItemReferenceBeanInterface		workTypeItemRefer;
	
	/**
	 * ????????????????????????????????????????????????
	 */
	protected List<WorkTypeImportAddonBeanInterface>	workTypeAddonBeans;
	
	
	/**
	 * {@link PlatformBean#PlatformBean()}??????????????????<br>
	 */
	public ImportTableReferenceBean() {
		super();
	}
	
	/**
	 * {@link PlatformBean#PlatformBean(MospParams, Connection)}??????????????????<br>
	 * @param mospParams MosP????????????????????????
	 * @param connection DB??????????????????
	 */
	public ImportTableReferenceBean(MospParams mospParams, Connection connection) {
		super(mospParams, connection);
	}
	
	@Override
	public void initBean() throws MospException {
		importDao = (ImportDaoInterface)createDao(ImportDaoInterface.class);
		importFieldDao = (ImportFieldDaoInterface)createDao(ImportFieldDaoInterface.class);
		humanDao = (HumanDaoInterface)createDao(HumanDaoInterface.class);
		attendanceDao = (AttendanceDaoInterface)createDao(AttendanceDaoInterface.class);
		totalTimeDataDao = (TotalTimeDataDaoInterface)createDao(TotalTimeDataDaoInterface.class);
		holidayDao = (HolidayDaoInterface)createDao(HolidayDaoInterface.class);
		holidayDataDao = (HolidayDataDaoInterface)createDao(HolidayDataDaoInterface.class);
		paidHolidayDataDao = (PaidHolidayDataDaoInterface)createDao(PaidHolidayDataDaoInterface.class);
		stockHolidayDataDao = (StockHolidayDataDaoInterface)createDao(StockHolidayDataDaoInterface.class);
		workTypeDao = (WorkTypeDaoInterface)createDao(WorkTypeDaoInterface.class);
		timeSettingDao = (TimeSettingDaoInterface)createDao(TimeSettingDaoInterface.class);
		cutoffDao = (CutoffDaoInterface)createDao(CutoffDaoInterface.class);
		workflowDao = (WorkflowDaoInterface)createDao(WorkflowDaoInterface.class);
		application = (ApplicationReferenceBeanInterface)createBean(ApplicationReferenceBeanInterface.class);
		paidHolidayReference = (PaidHolidayReferenceBeanInterface)createBean(PaidHolidayReferenceBeanInterface.class);
		totalTimeTransaction = (TotalTimeTransactionReferenceBeanInterface)createBean(
				TotalTimeTransactionReferenceBeanInterface.class);
		workflowIntegrate = (WorkflowIntegrateBeanInterface)createBean(WorkflowIntegrateBeanInterface.class);
		workTypeItemRegist = (WorkTypeItemRegistBeanInterface)createBean(WorkTypeItemRegistBeanInterface.class);
		workTypeItemRefer = (WorkTypeItemReferenceBeanInterface)createBean(WorkTypeItemReferenceBeanInterface.class);
		
		workTypeAddonBeans = null;
	}
	
	@Override
	public List<AttendanceDtoInterface> getAttendanceList(String importCode, List<String[]> list) throws MospException {
		ImportDtoInterface importDto = importDao.findForKey(importCode);
		if (importDto == null) {
			return null;
		}
		List<ImportFieldDtoInterface> importFieldDtoList = importFieldDao.findForList(importCode);
		if (importFieldDtoList == null || importFieldDtoList.isEmpty()) {
			return null;
		}
		List<AttendanceDtoInterface> attendanceList = new ArrayList<AttendanceDtoInterface>();
		int i = 0;
		for (String[] csvArray : list) {
			if (importDto.getHeader() == 1 && i == 0) {
				// ???????????????????????????
				if (!checkHeader(importDto, importFieldDtoList, csvArray)) {
					// ????????????????????????????????????
					addInvalidHeaderErrorMessage();
					return null;
				}
			} else {
				boolean hasError = false;
				String employeeCode = "";
				// ?????????
				AttendanceDtoInterface dto = new TmdAttendanceDto();
				dto.setTimesWork(1);
				dto.setLateReason("");
				dto.setLateCertificate("");
				dto.setLateComment("");
				dto.setLeaveEarlyReason("");
				dto.setLeaveEarlyCertificate("");
				dto.setLeaveEarlyComment("");
				dto.setTimeComment("");
				dto.setRemarks("");
				for (ImportFieldDtoInterface importFieldDto : importFieldDtoList) {
					int fieldOrder = importFieldDto.getFieldOrder();
					if (csvArray.length > fieldOrder - 1) {
						String value = csvArray[fieldOrder - 1];
						String fieldName = importFieldDto.getFieldName();
						if (fieldName.equals(PfmHumanDao.COL_EMPLOYEE_CODE)) {
							// ???????????????
							employeeCode = value;
						} else if (fieldName.equals(TmdAttendanceDao.COL_WORK_DATE)) {
							// ?????????
							Date workDate = getDate(value);
							if (workDate == null) {
								hasError = true;
								break;
							}
							dto.setWorkDate(workDate);
						} else if (fieldName.equals(TmdAttendanceDao.COL_TIMES_WORK)) {
							// ????????????
							int timesWork = 0;
							try {
								timesWork = Integer.parseInt(value);
							} catch (NumberFormatException e) {
								hasError = true;
								break;
							}
//							if (timesWork <= 0) {
							if (timesWork != 1) {
								hasError = true;
								break;
							}
							dto.setTimesWork(timesWork);
						} else if (fieldName.equals(TmdAttendanceDao.COL_WORK_TYPE_CODE)) {
							// ?????????????????????
							dto.setWorkTypeCode(value);
						} else if (fieldName.equals(TmdAttendanceDao.COL_DIRECT_START)) {
							// ??????
							int directStart = 0;
							try {
								directStart = Integer.parseInt(value);
							} catch (NumberFormatException e) {
								hasError = true;
								break;
							}
							if (directStart != 0 && directStart != 1) {
								hasError = true;
								break;
							}
							dto.setDirectStart(directStart);
						} else if (fieldName.equals(TmdAttendanceDao.COL_DIRECT_END)) {
							// ??????
							int directEnd = 0;
							try {
								directEnd = Integer.parseInt(value);
							} catch (NumberFormatException e) {
								hasError = true;
								break;
							}
							if (directEnd != 0 && directEnd != 1) {
								hasError = true;
								break;
							}
							dto.setDirectEnd(directEnd);
						} else if (fieldName.equals(TmdAttendanceDao.COL_START_TIME)) {
							// ????????????
							dto.setStartTime(getTimestamp(value));
						} else if (fieldName.equals(TmdAttendanceDao.COL_ACTUAL_START_TIME)) {
							// ???????????????
							dto.setActualStartTime(getTimestamp(value));
						} else if (fieldName.equals(TmdAttendanceDao.COL_END_TIME)) {
							// ????????????
							dto.setEndTime(getTimestamp(value));
						} else if (fieldName.equals(TmdAttendanceDao.COL_ACTUAL_END_TIME)) {
							// ???????????????
							dto.setActualEndTime(getTimestamp(value));
						} else if (fieldName.equals(TmdAttendanceDao.COL_LATE_DAYS)) {
							// ????????????
							int lateDays = 0;
							try {
								lateDays = Integer.parseInt(value);
							} catch (NumberFormatException e) {
								hasError = true;
								break;
							}
							if (lateDays < 0) {
								hasError = true;
								break;
							}
							dto.setLateDays(lateDays);
						} else if (fieldName.equals(TmdAttendanceDao.COL_LATE_THIRTY_MINUTES_OR_MORE)) {
							// ??????30???????????????
							int lateThirtyMinutesOrMore = 0;
							try {
								lateThirtyMinutesOrMore = Integer.parseInt(value);
							} catch (NumberFormatException e) {
								hasError = true;
								break;
							}
							if (lateThirtyMinutesOrMore < 0) {
								hasError = true;
								break;
							}
							dto.setLateThirtyMinutesOrMore(lateThirtyMinutesOrMore);
						} else if (fieldName.equals(TmdAttendanceDao.COL_LATE_LESS_THAN_THIRTY_MINUTES)) {
							// ??????30???????????????
							int lateLessThanThirtyMinutes = 0;
							try {
								lateLessThanThirtyMinutes = Integer.parseInt(value);
							} catch (NumberFormatException e) {
								hasError = true;
								break;
							}
							if (lateLessThanThirtyMinutes < 0) {
								hasError = true;
								break;
							}
							dto.setLateLessThanThirtyMinutes(lateLessThanThirtyMinutes);
						} else if (fieldName.equals(TmdAttendanceDao.COL_LATE_TIME)) {
							// ????????????
							int lateTime = 0;
							try {
								lateTime = Integer.parseInt(value);
							} catch (NumberFormatException e) {
								hasError = true;
								break;
							}
							if (lateTime < 0) {
								hasError = true;
								break;
							}
							dto.setLateTime(lateTime);
						} else if (fieldName.equals(TmdAttendanceDao.COL_LATE_THIRTY_MINUTES_OR_MORE_TIME)) {
							// ??????30???????????????
							int lateThirtyMinutesOrMoreTime = 0;
							try {
								lateThirtyMinutesOrMoreTime = Integer.parseInt(value);
							} catch (NumberFormatException e) {
								hasError = true;
								break;
							}
							if (lateThirtyMinutesOrMoreTime < 0) {
								hasError = true;
								break;
							}
							dto.setLateThirtyMinutesOrMoreTime(lateThirtyMinutesOrMoreTime);
						} else if (fieldName.equals(TmdAttendanceDao.COL_LATE_LESS_THAN_THIRTY_MINUTES_TIME)) {
							// ??????30???????????????
							int lateLessThanThirtyMinutesTime = 0;
							try {
								lateLessThanThirtyMinutesTime = Integer.parseInt(value);
							} catch (NumberFormatException e) {
								hasError = true;
								break;
							}
							if (lateLessThanThirtyMinutesTime < 0) {
								hasError = true;
								break;
							}
							dto.setLateLessThanThirtyMinutesTime(lateLessThanThirtyMinutesTime);
						} else if (fieldName.equals(TmdAttendanceDao.COL_LATE_REASON)) {
							// ????????????
							if (value.isEmpty()) {
								dto.setLateReason("");
							} else if (TimeConst.CODE_TARDINESS_WHY_INDIVIDU.equals(value)
									|| TimeConst.CODE_TARDINESS_WHY_BAD_HEALTH.equals(value)
									|| TimeConst.CODE_TARDINESS_WHY_OTHERS.equals(value)
									|| TimeConst.CODE_TARDINESS_WHY_TRAIN.equals(value)
									|| TimeConst.CODE_TARDINESS_WHY_COMPANY.equals(value)) {
								// ?????????????????????????????????????????????????????????????????????????????????
								dto.setLateReason(value);
							} else {
								hasError = true;
								break;
							}
						} else if (fieldName.equals(TmdAttendanceDao.COL_LATE_CERTIFICATE)) {
							// ???????????????
							if (value.isEmpty()) {
								dto.setLateCertificate("");
							} else if ("0".equals(value) || "1".equals(value)) {
								dto.setLateCertificate(value);
							} else {
								hasError = true;
								break;
							}
						} else if (fieldName.equals(TmdAttendanceDao.COL_LATE_COMMENT)) {
							// ??????????????????
							checkLength(value, MAX_LENGTH_TIME_COMMENT, "??????????????????", fieldOrder);
							if (mospParams.hasErrorMessage()) {
								hasError = true;
								break;
							}
							dto.setLateComment(value);
						} else if (fieldName.equals(TmdAttendanceDao.COL_LEAVE_EARLY_DAYS)) {
							// ????????????
							int leaveEarlyDays = 0;
							try {
								leaveEarlyDays = Integer.parseInt(value);
							} catch (NumberFormatException e) {
								hasError = true;
								break;
							}
							if (leaveEarlyDays < 0) {
								hasError = true;
								break;
							}
							dto.setLeaveEarlyDays(leaveEarlyDays);
						} else if (fieldName.equals(TmdAttendanceDao.COL_LEAVE_EARLY_THIRTY_MINUTES_OR_MORE)) {
							// ??????30???????????????
							int leaveEarlyThirtyMinutesOrMore = 0;
							try {
								leaveEarlyThirtyMinutesOrMore = Integer.parseInt(value);
							} catch (NumberFormatException e) {
								hasError = true;
								break;
							}
							if (leaveEarlyThirtyMinutesOrMore < 0) {
								hasError = true;
								break;
							}
							dto.setLeaveEarlyThirtyMinutesOrMore(leaveEarlyThirtyMinutesOrMore);
						} else if (fieldName.equals(TmdAttendanceDao.COL_LEAVE_EARLY_LESS_THAN_THIRTY_MINUTES)) {
							// ??????30???????????????
							int leaveEarlyLessThanThirtyMinutes = 0;
							try {
								leaveEarlyLessThanThirtyMinutes = Integer.parseInt(value);
							} catch (NumberFormatException e) {
								hasError = true;
								break;
							}
							if (leaveEarlyLessThanThirtyMinutes < 0) {
								hasError = true;
								break;
							}
							dto.setLeaveEarlyLessThanThirtyMinutes(leaveEarlyLessThanThirtyMinutes);
						} else if (fieldName.equals(TmdAttendanceDao.COL_LEAVE_EARLY_TIME)) {
							// ????????????
							int leaveEarlyTime = 0;
							try {
								leaveEarlyTime = Integer.parseInt(value);
							} catch (NumberFormatException e) {
								hasError = true;
								break;
							}
							if (leaveEarlyTime < 0) {
								hasError = true;
								break;
							}
							dto.setLeaveEarlyTime(leaveEarlyTime);
						} else if (fieldName.equals(TmdAttendanceDao.COL_LEAVE_EARLY_THIRTY_MINUTES_OR_MORE_TIME)) {
							// ??????30???????????????
							int leaveEarlyThirtyMinutesOrMoreTime = 0;
							try {
								leaveEarlyThirtyMinutesOrMoreTime = Integer.parseInt(value);
							} catch (NumberFormatException e) {
								hasError = true;
								break;
							}
							if (leaveEarlyThirtyMinutesOrMoreTime < 0) {
								hasError = true;
								break;
							}
							dto.setLeaveEarlyThirtyMinutesOrMoreTime(leaveEarlyThirtyMinutesOrMoreTime);
						} else if (fieldName.equals(TmdAttendanceDao.COL_LEAVE_EARLY_LESS_THAN_THIRTY_MINUTES_TIME)) {
							// ??????30???????????????
							int leaveEarlyLessThanThirtyMinutesTime = 0;
							try {
								leaveEarlyLessThanThirtyMinutesTime = Integer.parseInt(value);
							} catch (NumberFormatException e) {
								hasError = true;
								break;
							}
							if (leaveEarlyLessThanThirtyMinutesTime < 0) {
								hasError = true;
								break;
							}
							dto.setLeaveEarlyLessThanThirtyMinutesTime(leaveEarlyLessThanThirtyMinutesTime);
						} else if (fieldName.equals(TmdAttendanceDao.COL_LEAVE_EARLY_REASON)) {
							// ????????????
							if (value.isEmpty()) {
								dto.setLeaveEarlyReason("");
							} else if (TimeConst.CODE_LEAVEEARLY_WHY_INDIVIDU.equals(value)
									|| TimeConst.CODE_LEAVEEARLY_WHY_BAD_HEALTH.equals(value)
									|| TimeConst.CODE_LEAVEEARLY_WHY_OTHERS.equals(value)
									|| TimeConst.CODE_LEAVEEARLY_WHY_COMPANY.equals(value)) {
								// ??????????????????????????????????????????????????????????????????
								dto.setLeaveEarlyReason(value);
							} else {
								hasError = true;
								break;
							}
						} else if (fieldName.equals(TmdAttendanceDao.COL_LEAVE_EARLY_CERTIFICATE)) {
							// ???????????????
							if (value.isEmpty()) {
								dto.setLeaveEarlyCertificate("");
							} else if ("0".equals(value) || "1".equals(value)) {
								dto.setLeaveEarlyCertificate(value);
							} else {
								hasError = true;
								break;
							}
						} else if (fieldName.equals(TmdAttendanceDao.COL_LEAVE_EARLY_COMMENT)) {
							// ??????????????????
							checkLength(value, MAX_LENGTH_TIME_COMMENT, "??????????????????", fieldOrder);
							if (mospParams.hasErrorMessage()) {
								hasError = true;
								break;
							}
							dto.setLeaveEarlyComment(value);
						} else if (fieldName.equals(TmdAttendanceDao.COL_WORK_TIME)) {
							// ????????????
							int workTime = 0;
							try {
								workTime = Integer.parseInt(value);
							} catch (NumberFormatException e) {
								hasError = true;
								break;
							}
							if (workTime < 0) {
								hasError = true;
								break;
							}
							dto.setWorkTime(workTime);
						} else if (fieldName.equals(TmdAttendanceDao.COL_GENERAL_WORK_TIME)) {
							// ??????????????????
							int generalWorkTime = 0;
							try {
								generalWorkTime = Integer.parseInt(value);
							} catch (NumberFormatException e) {
								hasError = true;
								break;
							}
							if (generalWorkTime < 0) {
								hasError = true;
								break;
							}
							dto.setGeneralWorkTime(generalWorkTime);
						} else if (fieldName.equals(TmdAttendanceDao.COL_WORK_TIME_WITHIN_PRESCRIBED_WORK_TIME)) {
							// ?????????????????????????????????
							int workTimeWithinPrescribedWorkTime = 0;
							try {
								workTimeWithinPrescribedWorkTime = Integer.parseInt(value);
							} catch (NumberFormatException e) {
								hasError = true;
								break;
							}
							if (workTimeWithinPrescribedWorkTime < 0) {
								hasError = true;
								break;
							}
							dto.setWorkTimeWithinPrescribedWorkTime(workTimeWithinPrescribedWorkTime);
						} else if (fieldName.equals(TmdAttendanceDao.COL_CONTRACT_WORK_TIME)) {
							// ??????????????????
							int contractWorkTime = 0;
							try {
								contractWorkTime = Integer.parseInt(value);
							} catch (NumberFormatException e) {
								hasError = true;
								break;
							}
							if (contractWorkTime < 0) {
								hasError = true;
								break;
							}
							dto.setContractWorkTime(contractWorkTime);
						} else if (fieldName.equals(TmdAttendanceDao.COL_SHORT_UNPAID)) {
							// ??????????????????
							int shortUnpaid = 0;
							try {
								shortUnpaid = Integer.parseInt(value);
							} catch (NumberFormatException e) {
								hasError = true;
								break;
							}
							if (shortUnpaid < 0) {
								hasError = true;
								break;
							}
							dto.setShortUnpaid(shortUnpaid);
						} else if (fieldName.equals(TmdAttendanceDao.COL_REST_TIME)) {
							// ????????????
							int restTime = 0;
							try {
								restTime = Integer.parseInt(value);
							} catch (NumberFormatException e) {
								hasError = true;
								break;
							}
							if (restTime < 0) {
								hasError = true;
								break;
							}
							dto.setRestTime(restTime);
						} else if (fieldName.equals(TmdAttendanceDao.COL_OVER_REST_TIME)) {
							// ?????????????????????
							int overRestTime = 0;
							try {
								overRestTime = Integer.parseInt(value);
							} catch (NumberFormatException e) {
								hasError = true;
								break;
							}
							if (overRestTime < 0) {
								hasError = true;
								break;
							}
							dto.setOverRestTime(overRestTime);
						} else if (fieldName.equals(TmdAttendanceDao.COL_NIGHT_REST_TIME)) {
							// ??????????????????
							int nightRestTime = 0;
							try {
								nightRestTime = Integer.parseInt(value);
							} catch (NumberFormatException e) {
								hasError = true;
								break;
							}
							if (nightRestTime < 0) {
								hasError = true;
								break;
							}
							dto.setNightRestTime(nightRestTime);
						} else if (fieldName.equals(TmdAttendanceDao.COL_LEGAL_HOLIDAY_REST_TIME)) {
							// ????????????????????????
							int legalHolidayRestTime = 0;
							try {
								legalHolidayRestTime = Integer.parseInt(value);
							} catch (NumberFormatException e) {
								hasError = true;
								break;
							}
							if (legalHolidayRestTime < 0) {
								hasError = true;
								break;
							}
							dto.setLegalHolidayRestTime(legalHolidayRestTime);
						} else if (fieldName.equals(TmdAttendanceDao.COL_PRESCRIBED_HOLIDAY_REST_TIME)) {
							// ????????????????????????
							int prescribedHolidayRestTime = 0;
							try {
								prescribedHolidayRestTime = Integer.parseInt(value);
							} catch (NumberFormatException e) {
								hasError = true;
								break;
							}
							if (prescribedHolidayRestTime < 0) {
								hasError = true;
								break;
							}
							dto.setPrescribedHolidayRestTime(prescribedHolidayRestTime);
						} else if (fieldName.equals(TmdAttendanceDao.COL_PUBLIC_TIME)) {
							// ??????????????????
							int publicTime = 0;
							try {
								publicTime = Integer.parseInt(value);
							} catch (NumberFormatException e) {
								hasError = true;
								break;
							}
							if (publicTime < 0) {
								hasError = true;
								break;
							}
							dto.setPublicTime(publicTime);
						} else if (fieldName.equals(TmdAttendanceDao.COL_PRIVATE_TIME)) {
							// ??????????????????
							int privateTime = 0;
							try {
								privateTime = Integer.parseInt(value);
							} catch (NumberFormatException e) {
								hasError = true;
								break;
							}
							if (privateTime < 0) {
								hasError = true;
								break;
							}
							dto.setPrivateTime(privateTime);
						} else if (fieldName.equals(TmdAttendanceDao.COL_TIMES_OVERTIME)) {
							// ????????????
							int timesOvertime = 0;
							try {
								timesOvertime = Integer.parseInt(value);
							} catch (NumberFormatException e) {
								hasError = true;
								break;
							}
							if (timesOvertime < 0) {
								hasError = true;
								break;
							}
							dto.setTimesOvertime(timesOvertime);
						} else if (fieldName.equals(TmdAttendanceDao.COL_OVERTIME)) {
							// ????????????
							int overtime = 0;
							try {
								overtime = Integer.parseInt(value);
							} catch (NumberFormatException e) {
								hasError = true;
								break;
							}
							if (overtime < 0) {
								hasError = true;
								break;
							}
							dto.setOvertime(overtime);
						} else if (TmdAttendanceDao.COL_OVERTIME_BEFORE.equals(fieldName)) {
							// ???????????????
							int overtimeBefore = 0;
							try {
								overtimeBefore = Integer.parseInt(value);
							} catch (NumberFormatException e) {
								hasError = true;
								break;
							}
							if (overtimeBefore < 0) {
								hasError = true;
								break;
							}
							dto.setOvertimeBefore(overtimeBefore);
						} else if (TmdAttendanceDao.COL_OVERTIME_AFTER.equals(fieldName)) {
							// ???????????????
							int overtimeAfter = 0;
							try {
								overtimeAfter = Integer.parseInt(value);
							} catch (NumberFormatException e) {
								hasError = true;
								break;
							}
							if (overtimeAfter < 0) {
								hasError = true;
								break;
							}
							dto.setOvertimeAfter(overtimeAfter);
						} else if (fieldName.equals(TmdAttendanceDao.COL_OVERTIME_IN)) {
							// ?????????????????????
							int overtimeIn = 0;
							try {
								overtimeIn = Integer.parseInt(value);
							} catch (NumberFormatException e) {
								hasError = true;
								break;
							}
							if (overtimeIn < 0) {
								hasError = true;
								break;
							}
							dto.setOvertimeIn(overtimeIn);
						} else if (fieldName.equals(TmdAttendanceDao.COL_OVERTIME_OUT)) {
							// ?????????????????????
							int overtimeOut = 0;
							try {
								overtimeOut = Integer.parseInt(value);
							} catch (NumberFormatException e) {
								hasError = true;
								break;
							}
							if (overtimeOut < 0) {
								hasError = true;
								break;
							}
							dto.setOvertimeOut(overtimeOut);
						} else if (fieldName.equals(TmdAttendanceDao.COL_WORKDAY_OVERTIME_IN)) {
							// ?????????????????????????????????
							int workdayOvertimeIn = 0;
							try {
								workdayOvertimeIn = Integer.parseInt(value);
							} catch (NumberFormatException e) {
								hasError = true;
								break;
							}
							if (workdayOvertimeIn < 0) {
								hasError = true;
								break;
							}
							dto.setWorkdayOvertimeIn(workdayOvertimeIn);
						} else if (fieldName.equals(TmdAttendanceDao.COL_WORKDAY_OVERTIME_OUT)) {
							// ?????????????????????????????????
							int workdayOvertimeOut = 0;
							try {
								workdayOvertimeOut = Integer.parseInt(value);
							} catch (NumberFormatException e) {
								hasError = true;
								break;
							}
							if (workdayOvertimeOut < 0) {
								hasError = true;
								break;
							}
							dto.setWorkdayOvertimeOut(workdayOvertimeOut);
						} else if (fieldName.equals(TmdAttendanceDao.COL_PRESCRIBED_HOLIDAY_OVERTIME_IN)) {
							// ???????????????????????????????????????
							int prescribedHolidayOvertimeIn = 0;
							try {
								prescribedHolidayOvertimeIn = Integer.parseInt(value);
							} catch (NumberFormatException e) {
								hasError = true;
								break;
							}
							if (prescribedHolidayOvertimeIn < 0) {
								hasError = true;
								break;
							}
							dto.setPrescribedHolidayOvertimeIn(prescribedHolidayOvertimeIn);
						} else if (fieldName.equals(TmdAttendanceDao.COL_PRESCRIBED_HOLIDAY_OVERTIME_OUT)) {
							// ???????????????????????????????????????
							int prescribedHolidayOvertimeOut = 0;
							try {
								prescribedHolidayOvertimeOut = Integer.parseInt(value);
							} catch (NumberFormatException e) {
								hasError = true;
								break;
							}
							if (prescribedHolidayOvertimeOut < 0) {
								hasError = true;
								break;
							}
							dto.setPrescribedHolidayOvertimeOut(prescribedHolidayOvertimeOut);
						} else if (fieldName.equals(TmdAttendanceDao.COL_LATE_NIGHT_TIME)) {
							// ??????????????????
							int lateNightTime = 0;
							try {
								lateNightTime = Integer.parseInt(value);
							} catch (NumberFormatException e) {
								hasError = true;
								break;
							}
							if (lateNightTime < 0) {
								hasError = true;
								break;
							}
							dto.setLateNightTime(lateNightTime);
						} else if (fieldName.equals(TmdAttendanceDao.COL_NIGHT_WORK_WITHIN_PRESCRIBED_WORK)) {
							// ?????????????????????????????????
							int nightWorkWithinPrescribedWork = 0;
							try {
								nightWorkWithinPrescribedWork = Integer.parseInt(value);
							} catch (NumberFormatException e) {
								hasError = true;
								break;
							}
							if (nightWorkWithinPrescribedWork < 0) {
								hasError = true;
								break;
							}
							dto.setNightWorkWithinPrescribedWork(nightWorkWithinPrescribedWork);
						} else if (fieldName.equals(TmdAttendanceDao.COL_NIGHT_OVERTIME_WORK)) {
							// ?????????????????????
							int nightOvertimeWork = 0;
							try {
								nightOvertimeWork = Integer.parseInt(value);
							} catch (NumberFormatException e) {
								hasError = true;
								break;
							}
							if (nightOvertimeWork < 0) {
								hasError = true;
								break;
							}
							dto.setNightOvertimeWork(nightOvertimeWork);
						} else if (fieldName.equals(TmdAttendanceDao.COL_NIGHT_WORK_ON_HOLIDAY)) {
							// ????????????????????????
							int nightWorkOnHoliday = 0;
							try {
								nightWorkOnHoliday = Integer.parseInt(value);
							} catch (NumberFormatException e) {
								hasError = true;
								break;
							}
							if (nightWorkOnHoliday < 0) {
								hasError = true;
								break;
							}
							dto.setNightWorkOnHoliday(nightWorkOnHoliday);
						} else if (fieldName.equals(TmdAttendanceDao.COL_SPECIFIC_WORK_TIME)) {
							// ????????????????????????
							int specificWorkTime = 0;
							try {
								specificWorkTime = Integer.parseInt(value);
							} catch (NumberFormatException e) {
								hasError = true;
								break;
							}
							if (specificWorkTime < 0) {
								hasError = true;
								break;
							}
							dto.setSpecificWorkTime(specificWorkTime);
						} else if (fieldName.equals(TmdAttendanceDao.COL_LEGAL_WORK_TIME)) {
							// ????????????????????????
							int legalWorkTime = 0;
							try {
								legalWorkTime = Integer.parseInt(value);
							} catch (NumberFormatException e) {
								hasError = true;
								break;
							}
							if (legalWorkTime < 0) {
								hasError = true;
								break;
							}
							dto.setLegalWorkTime(legalWorkTime);
						} else if (fieldName.equals(TmdAttendanceDao.COL_DECREASE_TIME)) {
							// ??????????????????
							int decreaseTime = 0;
							try {
								decreaseTime = Integer.parseInt(value);
							} catch (NumberFormatException e) {
								hasError = true;
								break;
							}
							if (decreaseTime < 0) {
								hasError = true;
								break;
							}
							dto.setDecreaseTime(decreaseTime);
						} else if (fieldName.equals(TmdAttendanceDao.COL_TIME_COMMENT)) {
							// ??????????????????
							dto.setTimeComment(value);
						} else if (fieldName.equals(TmdAttendanceDao.COL_WORK_DAYS)) {
							// ????????????
							double workDays = 0;
							try {
								workDays = Double.parseDouble(value);
							} catch (NumberFormatException e) {
								hasError = true;
								break;
							}
							if (workDays < 0) {
								hasError = true;
								break;
							}
							dto.setWorkDays(workDays);
						} else if (fieldName.equals(TmdAttendanceDao.COL_WORK_DAYS_FOR_PAID_LEAVE)) {
							// ???????????????????????????
							int workDaysForPaidLeave = 0;
							try {
								workDaysForPaidLeave = Integer.parseInt(value);
							} catch (NumberFormatException e) {
								hasError = true;
								break;
							}
							if (workDaysForPaidLeave < 0) {
								hasError = true;
								break;
							}
							dto.setWorkDaysForPaidLeave(workDaysForPaidLeave);
						} else if (fieldName.equals(TmdAttendanceDao.COL_TOTAL_WORK_DAYS_FOR_PAID_LEAVE)) {
							// ???????????????????????????
							int totalWorkDaysForPaidLeave = 0;
							try {
								totalWorkDaysForPaidLeave = Integer.parseInt(value);
							} catch (NumberFormatException e) {
								hasError = true;
								break;
							}
							if (totalWorkDaysForPaidLeave < 0) {
								hasError = true;
								break;
							}
							dto.setTotalWorkDaysForPaidLeave(totalWorkDaysForPaidLeave);
						} else if (fieldName.equals(TmdAttendanceDao.COL_TIMES_HOLIDAY_WORK)) {
							// ??????????????????
							int timesHolidayWork = 0;
							try {
								timesHolidayWork = Integer.parseInt(value);
							} catch (NumberFormatException e) {
								hasError = true;
								break;
							}
							if (timesHolidayWork < 0) {
								hasError = true;
								break;
							}
							dto.setTimesHolidayWork(timesHolidayWork);
						} else if (fieldName.equals(TmdAttendanceDao.COL_TIMES_LEGAL_HOLIDAY_WORK)) {
							// ????????????????????????
							int timesLegalHolidayWork = 0;
							try {
								timesLegalHolidayWork = Integer.parseInt(value);
							} catch (NumberFormatException e) {
								hasError = true;
								break;
							}
							if (timesLegalHolidayWork < 0) {
								hasError = true;
								break;
							}
							dto.setTimesLegalHolidayWork(timesLegalHolidayWork);
						} else if (fieldName.equals(TmdAttendanceDao.COL_TIMES_PRESCRIBED_HOLIDAY_WORK)) {
							// ????????????????????????
							int timesPrescribedHolidayWork = 0;
							try {
								timesPrescribedHolidayWork = Integer.parseInt(value);
							} catch (NumberFormatException e) {
								hasError = true;
								break;
							}
							if (timesPrescribedHolidayWork < 0) {
								hasError = true;
								break;
							}
							dto.setTimesPrescribedHolidayWork(timesPrescribedHolidayWork);
						} else if (fieldName.equals(TmdAttendanceDao.COL_PAID_LEAVE_DAYS)) {
							// ??????????????????
							double paidLeaveDays = 0;
							try {
								paidLeaveDays = Double.parseDouble(value);
							} catch (NumberFormatException e) {
								hasError = true;
								break;
							}
							if (paidLeaveDays < 0) {
								hasError = true;
								break;
							}
							dto.setPaidLeaveDays(paidLeaveDays);
						} else if (fieldName.equals(TmdAttendanceDao.COL_PAID_LEAVE_HOURS)) {
							// ?????????????????????
							int paidLeaveHours = 0;
							try {
								paidLeaveHours = Integer.parseInt(value);
							} catch (NumberFormatException e) {
								hasError = true;
								break;
							}
							if (paidLeaveHours < 0) {
								hasError = true;
								break;
							}
							dto.setPaidLeaveHours(paidLeaveHours);
						} else if (fieldName.equals(TmdAttendanceDao.COL_STOCK_LEAVE_DAYS)) {
							// ????????????????????????
							double stockLeaveDays = 0;
							try {
								stockLeaveDays = Double.parseDouble(value);
							} catch (NumberFormatException e) {
								hasError = true;
								break;
							}
							if (stockLeaveDays < 0) {
								hasError = true;
								break;
							}
							dto.setStockLeaveDays(stockLeaveDays);
						} else if (fieldName.equals(TmdAttendanceDao.COL_COMPENSATION_DAYS)) {
							// ????????????
							double compensationDays = 0;
							try {
								compensationDays = Double.parseDouble(value);
							} catch (NumberFormatException e) {
								hasError = true;
								break;
							}
							if (compensationDays < 0) {
								hasError = true;
								break;
							}
							dto.setCompensationDays(compensationDays);
						} else if (fieldName.equals(TmdAttendanceDao.COL_LEGAL_COMPENSATION_DAYS)) {
							// ??????????????????
							double legalCompensationDays = 0;
							try {
								legalCompensationDays = Double.parseDouble(value);
							} catch (NumberFormatException e) {
								hasError = true;
								break;
							}
							if (legalCompensationDays < 0) {
								hasError = true;
								break;
							}
							dto.setLegalCompensationDays(legalCompensationDays);
						} else if (fieldName.equals(TmdAttendanceDao.COL_PRESCRIBED_COMPENSATION_DAYS)) {
							// ??????????????????
							double prescribedCompensationDays = 0;
							try {
								prescribedCompensationDays = Double.parseDouble(value);
							} catch (NumberFormatException e) {
								hasError = true;
								break;
							}
							if (prescribedCompensationDays < 0) {
								hasError = true;
								break;
							}
							dto.setPrescribedCompensationDays(prescribedCompensationDays);
						} else if (fieldName.equals(TmdAttendanceDao.COL_NIGHT_COMPENSATION_DAYS)) {
							// ??????????????????
							double nightCompensationDays = 0;
							try {
								nightCompensationDays = Double.parseDouble(value);
							} catch (NumberFormatException e) {
								hasError = true;
								break;
							}
							if (nightCompensationDays < 0) {
								hasError = true;
								break;
							}
							dto.setNightCompensationDays(nightCompensationDays);
						} else if (fieldName.equals(TmdAttendanceDao.COL_SPECIAL_LEAVE_DAYS)) {
							// ??????????????????
							double specialLeaveDays = 0;
							try {
								specialLeaveDays = Double.parseDouble(value);
							} catch (NumberFormatException e) {
								hasError = true;
								break;
							}
							if (specialLeaveDays < 0) {
								hasError = true;
								break;
							}
							dto.setSpecialLeaveDays(specialLeaveDays);
						} else if (fieldName.equals(TmdAttendanceDao.COL_SPECIAL_LEAVE_HOURS)) {
							// ?????????????????????
							int specialLeaveHours = 0;
							try {
								specialLeaveHours = Integer.parseInt(value);
							} catch (NumberFormatException e) {
								hasError = true;
								break;
							}
							if (specialLeaveHours < 0) {
								hasError = true;
								break;
							}
							dto.setSpecialLeaveHours(specialLeaveHours);
						} else if (fieldName.equals(TmdAttendanceDao.COL_OTHER_LEAVE_DAYS)) {
							// ?????????????????????
							double otherLeaveDays = 0;
							try {
								otherLeaveDays = Double.parseDouble(value);
							} catch (NumberFormatException e) {
								hasError = true;
								break;
							}
							if (otherLeaveDays < 0) {
								hasError = true;
								break;
							}
							dto.setOtherLeaveDays(otherLeaveDays);
						} else if (fieldName.equals(TmdAttendanceDao.COL_OTHER_LEAVE_HOURS)) {
							// ????????????????????????
							int otherLeaveHours = 0;
							try {
								otherLeaveHours = Integer.parseInt(value);
							} catch (NumberFormatException e) {
								hasError = true;
								break;
							}
							if (otherLeaveHours < 0) {
								hasError = true;
								break;
							}
							dto.setOtherLeaveHours(otherLeaveHours);
						} else if (fieldName.equals(TmdAttendanceDao.COL_ABSENCE_DAYS)) {
							// ????????????
							double absenceDays = 0;
							try {
								absenceDays = Double.parseDouble(value);
							} catch (NumberFormatException e) {
								hasError = true;
								break;
							}
							if (absenceDays < 0) {
								hasError = true;
								break;
							}
							dto.setAbsenceDays(absenceDays);
						} else if (fieldName.equals(TmdAttendanceDao.COL_ABSENCE_HOURS)) {
							// ???????????????
							int absenceHours = 0;
							try {
								absenceHours = Integer.parseInt(value);
							} catch (NumberFormatException e) {
								hasError = true;
								break;
							}
							if (absenceHours < 0) {
								hasError = true;
								break;
							}
							dto.setAbsenceHours(absenceHours);
						} else if (fieldName.equals(TmdAttendanceDao.COL_GRANTED_LEGAL_COMPENSATION_DAYS)) {
							// ????????????????????????
							double grantedLegalCompensationDays = 0;
							try {
								grantedLegalCompensationDays = Double.parseDouble(value);
							} catch (NumberFormatException e) {
								hasError = true;
								break;
							}
							if (grantedLegalCompensationDays < 0) {
								hasError = true;
								break;
							}
							dto.setGrantedLegalCompensationDays(grantedLegalCompensationDays);
						} else if (fieldName.equals(TmdAttendanceDao.COL_GRANTED_PRESCRIBED_COMPENSATION_DAYS)) {
							// ????????????????????????
							double grantedPrescribedCompensationDays = 0;
							try {
								grantedPrescribedCompensationDays = Double.parseDouble(value);
							} catch (NumberFormatException e) {
								hasError = true;
								break;
							}
							if (grantedPrescribedCompensationDays < 0) {
								hasError = true;
								break;
							}
							dto.setGrantedPrescribedCompensationDays(grantedPrescribedCompensationDays);
						} else if (fieldName.equals(TmdAttendanceDao.COL_GRANTED_NIGHT_COMPENSATION_DAYS)) {
							// ????????????????????????
							double grantedNightCompensationDays = 0;
							try {
								grantedNightCompensationDays = Double.parseDouble(value);
							} catch (NumberFormatException e) {
								hasError = true;
								break;
							}
							if (grantedNightCompensationDays < 0) {
								hasError = true;
								break;
							}
							dto.setGrantedNightCompensationDays(grantedNightCompensationDays);
						} else if (fieldName
							.equals(TmdAttendanceDao.COL_LEGAL_HOLIDAY_WORK_TIME_WITH_COMPENSATION_DAY)) {
							// ??????????????????(????????????)
							int legalHolidayWorkTimeWithCompensationDay = 0;
							try {
								legalHolidayWorkTimeWithCompensationDay = Integer.parseInt(value);
							} catch (NumberFormatException e) {
								hasError = true;
								break;
							}
							if (legalHolidayWorkTimeWithCompensationDay < 0) {
								hasError = true;
								break;
							}
							dto.setLegalHolidayWorkTimeWithCompensationDay(legalHolidayWorkTimeWithCompensationDay);
						} else if (fieldName
							.equals(TmdAttendanceDao.COL_LEGAL_HOLIDAY_WORK_TIME_WITHOUT_COMPENSATION_DAY)) {
							// ??????????????????(????????????)
							int legalHolidayWorkTimeWithoutCompensationDay = 0;
							try {
								legalHolidayWorkTimeWithoutCompensationDay = Integer.parseInt(value);
							} catch (NumberFormatException e) {
								hasError = true;
								break;
							}
							if (legalHolidayWorkTimeWithoutCompensationDay < 0) {
								hasError = true;
								break;
							}
							dto.setLegalHolidayWorkTimeWithoutCompensationDay(
									legalHolidayWorkTimeWithoutCompensationDay);
						} else if (fieldName
							.equals(TmdAttendanceDao.COL_PRESCRIBED_HOLIDAY_WORK_TIME_WITH_COMPENSATION_DAY)) {
							// ??????????????????(????????????)
							int prescribedHolidayWorkTimeWithCompensationDay = 0;
							try {
								prescribedHolidayWorkTimeWithCompensationDay = Integer.parseInt(value);
							} catch (NumberFormatException e) {
								hasError = true;
								break;
							}
							if (prescribedHolidayWorkTimeWithCompensationDay < 0) {
								hasError = true;
								break;
							}
							dto.setPrescribedHolidayWorkTimeWithCompensationDay(
									prescribedHolidayWorkTimeWithCompensationDay);
						} else if (fieldName
							.equals(TmdAttendanceDao.COL_PRESCRIBED_HOLIDAY_WORK_TIME_WITHOUT_COMPENSATION_DAY)) {
							// ??????????????????(????????????)
							int prescribedHolidayWorkTimeWithoutCompensationDay = 0;
							try {
								prescribedHolidayWorkTimeWithoutCompensationDay = Integer.parseInt(value);
							} catch (NumberFormatException e) {
								hasError = true;
								break;
							}
							if (prescribedHolidayWorkTimeWithoutCompensationDay < 0) {
								hasError = true;
								break;
							}
							dto.setPrescribedHolidayWorkTimeWithoutCompensationDay(
									prescribedHolidayWorkTimeWithoutCompensationDay);
						} else if (fieldName.equals(TmdAttendanceDao.COL_OVERTIME_IN_WITH_COMPENSATION_DAY)) {
							// ?????????????????????????????????(????????????)
							int overtimeInWithCompensationDay = 0;
							try {
								overtimeInWithCompensationDay = Integer.parseInt(value);
							} catch (NumberFormatException e) {
								hasError = true;
								break;
							}
							if (overtimeInWithCompensationDay < 0) {
								hasError = true;
								break;
							}
							dto.setOvertimeInWithCompensationDay(overtimeInWithCompensationDay);
						} else if (fieldName.equals(TmdAttendanceDao.COL_OVERTIME_IN_WITHOUT_COMPENSATION_DAY)) {
							// ?????????????????????????????????(????????????)
							int overtimeInWithoutCompensationDay = 0;
							try {
								overtimeInWithoutCompensationDay = Integer.parseInt(value);
							} catch (NumberFormatException e) {
								hasError = true;
								break;
							}
							if (overtimeInWithoutCompensationDay < 0) {
								hasError = true;
								break;
							}
							dto.setOvertimeInWithoutCompensationDay(overtimeInWithoutCompensationDay);
						} else if (fieldName.equals(TmdAttendanceDao.COL_OVERTIME_OUT_WITH_COMPENSATION_DAY)) {
							// ?????????????????????????????????(????????????)
							int overtimeOutWithCompensationDay = 0;
							try {
								overtimeOutWithCompensationDay = Integer.parseInt(value);
							} catch (NumberFormatException e) {
								hasError = true;
								break;
							}
							if (overtimeOutWithCompensationDay < 0) {
								hasError = true;
								break;
							}
							dto.setOvertimeOutWithCompensationDay(overtimeOutWithCompensationDay);
						} else if (fieldName.equals(TmdAttendanceDao.COL_OVERTIME_OUT_WITHOUT_COMPENSATION_DAY)) {
							// ?????????????????????????????????(????????????)
							int overtimeOutWithoutCompensationDay = 0;
							try {
								overtimeOutWithoutCompensationDay = Integer.parseInt(value);
							} catch (NumberFormatException e) {
								hasError = true;
								break;
							}
							if (overtimeOutWithoutCompensationDay < 0) {
								hasError = true;
								break;
							}
							dto.setOvertimeOutWithoutCompensationDay(overtimeOutWithoutCompensationDay);
						} else if (fieldName.equals(TmdAttendanceDao.COL_STATUTORY_HOLIDAY_WORK_TIME_IN)) {
							// ?????????????????????????????????????????????
							int statutoryHolidayWorkTimeIn = 0;
							try {
								statutoryHolidayWorkTimeIn = Integer.parseInt(value);
							} catch (NumberFormatException e) {
								hasError = true;
								break;
							}
							if (statutoryHolidayWorkTimeIn < 0) {
								hasError = true;
								break;
							}
							dto.setStatutoryHolidayWorkTimeIn(statutoryHolidayWorkTimeIn);
						} else if (fieldName.equals(TmdAttendanceDao.COL_STATUTORY_HOLIDAY_WORK_TIME_OUT)) {
							// ?????????????????????????????????????????????
							int statutoryHolidayWorkTimeOut = 0;
							try {
								statutoryHolidayWorkTimeOut = Integer.parseInt(value);
							} catch (NumberFormatException e) {
								hasError = true;
								break;
							}
							if (statutoryHolidayWorkTimeOut < 0) {
								hasError = true;
								break;
							}
							dto.setStatutoryHolidayWorkTimeOut(statutoryHolidayWorkTimeOut);
						} else if (fieldName.equals(TmdAttendanceDao.COL_PRESCRIBED_HOLIDAY_WORK_TIME_IN)) {
							// ?????????????????????????????????????????????
							int prescribedHolidayWorkTimeIn = 0;
							try {
								prescribedHolidayWorkTimeIn = Integer.parseInt(value);
							} catch (NumberFormatException e) {
								hasError = true;
								break;
							}
							if (prescribedHolidayWorkTimeIn < 0) {
								hasError = true;
								break;
							}
							dto.setPrescribedHolidayWorkTimeIn(prescribedHolidayWorkTimeIn);
						} else if (fieldName.equals(TmdAttendanceDao.COL_PRESCRIBED_HOLIDAY_WORK_TIME_OUT)) {
							// ?????????????????????????????????????????????
							int prescribedHolidayWorkTimeOut = 0;
							try {
								prescribedHolidayWorkTimeOut = Integer.parseInt(value);
							} catch (NumberFormatException e) {
								hasError = true;
								break;
							}
							if (prescribedHolidayWorkTimeOut < 0) {
								hasError = true;
								break;
							}
							dto.setPrescribedHolidayWorkTimeOut(prescribedHolidayWorkTimeOut);
						}
					}
				}
				if (!hasError && employeeCode.isEmpty()) {
					hasError = true;
				}
				if (!hasError && dto.getWorkDate() == null) {
					hasError = true;
				}
				if (!hasError) {
					HumanDtoInterface humanDto = humanDao.findForEmployeeCode(employeeCode, dto.getWorkDate());
					if (humanDto == null || humanDto.getPersonalId() == null || humanDto.getPersonalId().isEmpty()) {
						hasError = true;
					} else {
						// ??????ID
						dto.setPersonalId(humanDto.getPersonalId());
					}
				}
				if (!hasError) {
					if (dto.getWorkTypeCode() == null || dto.getWorkTypeCode().isEmpty()) {
						hasError = true;
					} else {
						WorkTypeDtoInterface workTypeDto = workTypeDao.findForInfo(dto.getWorkTypeCode(),
								dto.getWorkDate());
						if (workTypeDto == null) {
							hasError = true;
						}
					}
				}
				if (hasError) {
					addInvalidDataErrorMessage(i);
				} else {
					// ?????????????????????????????????
					for (AttendanceDtoInterface attendanceDto : attendanceList) {
						if (attendanceDto.getPersonalId().equals(dto.getPersonalId())
								&& attendanceDto.getWorkDate().equals(dto.getWorkDate())
								&& attendanceDto.getTimesWork() == dto.getTimesWork()) {
							addDuplicateDataErrorMessage(i);
							hasError = true;
							break;
						}
					}
				}
				if (!hasError) {
					AttendanceDtoInterface attendanceDto = attendanceDao.findForKey(dto.getPersonalId(),
							dto.getWorkDate(), dto.getTimesWork());
					if (attendanceDto != null) {
						WorkflowDtoInterface workflowDto = workflowDao.findForKey(attendanceDto.getWorkflow());
						if (workflowDto != null) {
							if (PlatformConst.CODE_STATUS_DRAFT.equals(workflowDto.getWorkflowStatus())) {
								// ???????????????
								dto.setTmdAttendanceId(attendanceDto.getTmdAttendanceId());
								dto.setWorkflow(attendanceDto.getWorkflow());
							} else if (PlatformConst.CODE_STATUS_APPLY.equals(workflowDto.getWorkflowStatus())
									|| PlatformConst.CODE_STATUS_APPROVED.equals(workflowDto.getWorkflowStatus())
									|| PlatformConst.CODE_STATUS_REVERT.equals(workflowDto.getWorkflowStatus())
									|| PlatformConst.CODE_STATUS_CANCEL.equals(workflowDto.getWorkflowStatus())
									|| PlatformConst.CODE_STATUS_COMPLETE.equals(workflowDto.getWorkflowStatus())
									|| workflowIntegrate.isCancelApprovable(workflowDto)) {
								// ??????????????????????????????????????????????????????????????????????????????
								addAlreadyRegisteredDataErrorMessage(i);
								hasError = true;
								break;
							}
						}
					}
				}
				if (!hasError) {
					attendanceList.add(dto);
				}
			}
			i++;
		}
		return attendanceList;
	}
	
	@Override
	public List<TotalTimeDataDtoInterface> getTotalTimeList(String importCode, List<String[]> list)
			throws MospException {
		ImportDtoInterface importDto = importDao.findForKey(importCode);
		if (importDto == null) {
			return null;
		}
		List<ImportFieldDtoInterface> importFieldDtoList = importFieldDao.findForList(importCode);
		if (importFieldDtoList == null || importFieldDtoList.isEmpty()) {
			return null;
		}
		List<TotalTimeDataDtoInterface> totaltimeList = new ArrayList<TotalTimeDataDtoInterface>();
		int i = 0;
		for (String[] csvArray : list) {
			if (importDto.getHeader() == 1 && i == 0) {
				// ????????????????????????
				if (!checkHeader(importDto, importFieldDtoList, csvArray)) {
					// ????????????????????????????????????
					addInvalidHeaderErrorMessage();
					return null;
				}
			} else {
				boolean hasError = false;
				String employeeCode = "";
				TotalTimeDataDtoInterface dto = new TmdTotalTimeDataDto();
				for (ImportFieldDtoInterface importFieldDto : importFieldDtoList) {
					int fieldOrder = importFieldDto.getFieldOrder();
					if (csvArray.length > fieldOrder - 1) {
						String value = csvArray[fieldOrder - 1];
						String fieldName = importFieldDto.getFieldName();
						if (fieldName.equals(PfmHumanDao.COL_EMPLOYEE_CODE)) {
							// ???????????????
							employeeCode = value;
						} else if (fieldName.equals(TmdTotalTimeDao.COL_CALCULATION_YEAR)) {
							// ???
							InputCheckUtility.checkYear(mospParams, value, fieldName);
							if (mospParams.hasErrorMessage()) {
								hasError = true;
								break;
							}
							dto.setCalculationYear(getInteger(value));
						} else if (fieldName.equals(TmdTotalTimeDao.COL_CALCULATION_MONTH)) {
							// ???
							InputCheckUtility.checkMonth(mospParams, value, fieldName);
							if (mospParams.hasErrorMessage()) {
								hasError = true;
								break;
							}
							dto.setCalculationMonth(getInteger(value));
						} else if (fieldName.equals(TmdTotalTimeDao.COL_CALCULATION_DATE)) {
							// ?????????
							Date calculationDate = getDate(value);
							if (calculationDate == null) {
								hasError = true;
								break;
							}
							dto.setCalculationDate(calculationDate);
						} else if (fieldName.equals(TmdTotalTimeDao.COL_WORK_TIME)) {
							// ????????????
							int workTime = 0;
							try {
								workTime = Integer.parseInt(value);
							} catch (NumberFormatException e) {
								hasError = true;
								break;
							}
							if (workTime < 0) {
								hasError = true;
								break;
							}
							dto.setWorkTime(workTime);
						} else if (TmdTotalTimeDao.COL_SPECIFIC_WORK_TIME.equals(fieldName)) {
							// ??????????????????
							int specificWorkTime = 0;
							try {
								specificWorkTime = Integer.parseInt(value);
							} catch (NumberFormatException e) {
								hasError = true;
								break;
							}
							if (specificWorkTime < 0) {
								hasError = true;
								break;
							}
							dto.setSpecificWorkTime(specificWorkTime);
						} else if (TmdTotalTimeDao.COL_SHORT_UNPAID.equals(fieldName)) {
							// ??????????????????
							int shortUnpaid = 0;
							try {
								shortUnpaid = Integer.parseInt(value);
							} catch (NumberFormatException e) {
								hasError = true;
								break;
							}
							if (shortUnpaid < 0) {
								hasError = true;
								break;
							}
							dto.setShortUnpaid(shortUnpaid);
						} else if (fieldName.equals(TmdTotalTimeDao.COL_TIMES_WORK_DATE)) {
							// ????????????
							double timesWorkDate = 0;
							try {
								timesWorkDate = Double.parseDouble(value);
							} catch (NumberFormatException e) {
								hasError = true;
								break;
							}
							if (timesWorkDate < 0) {
								hasError = true;
								break;
							}
							dto.setTimesWorkDate(timesWorkDate);
						} else if (fieldName.equals(TmdTotalTimeDao.COL_TIMES_WORK)) {
							// ????????????
							int timesWork = 0;
							try {
								timesWork = Integer.parseInt(value);
							} catch (NumberFormatException e) {
								hasError = true;
								break;
							}
							if (timesWork < 0) {
								hasError = true;
								break;
							}
							dto.setTimesWork(timesWork);
						} else if (fieldName.equals(TmdTotalTimeDao.COL_LEGAL_WORK_ON_HOLIDAY)) {
							// ????????????????????????
							double legalWorkOnHoliday = 0;
							try {
								legalWorkOnHoliday = Double.parseDouble(value);
							} catch (NumberFormatException e) {
								hasError = true;
								break;
							}
							if (legalWorkOnHoliday < 0) {
								hasError = true;
								break;
							}
							dto.setLegalWorkOnHoliday(legalWorkOnHoliday);
						} else if (fieldName.equals(TmdTotalTimeDao.COL_SPECIFIC_WORK_ON_HOLIDAY)) {
							// ????????????????????????
							double specificWorkOnHoliday = 0;
							try {
								specificWorkOnHoliday = Double.parseDouble(value);
							} catch (NumberFormatException e) {
								hasError = true;
								break;
							}
							if (specificWorkOnHoliday < 0) {
								hasError = true;
								break;
							}
							dto.setSpecificWorkOnHoliday(specificWorkOnHoliday);
						} else if (fieldName.equals(TmdTotalTimeDao.COL_TIMES_ACHIEVEMENT)) {
							// ??????????????????
							int timesAchievement = 0;
							try {
								timesAchievement = Integer.parseInt(value);
							} catch (NumberFormatException e) {
								hasError = true;
								break;
							}
							if (timesAchievement < 0) {
								hasError = true;
								break;
							}
							dto.setTimesAchievement(timesAchievement);
						} else if (fieldName.equals(TmdTotalTimeDao.COL_TIMES_TOTAL_WORK_DATE)) {
							// ??????????????????
							int timesTotalWorkDate = 0;
							try {
								timesTotalWorkDate = Integer.parseInt(value);
							} catch (NumberFormatException e) {
								hasError = true;
								break;
							}
							if (timesTotalWorkDate < 0) {
								hasError = true;
								break;
							}
							dto.setTimesTotalWorkDate(timesTotalWorkDate);
						} else if (fieldName.equals(TmdTotalTimeDao.COL_DIRECT_START)) {
							// ????????????
							int directStart = 0;
							try {
								directStart = Integer.parseInt(value);
							} catch (NumberFormatException e) {
								hasError = true;
								break;
							}
							if (directStart < 0) {
								hasError = true;
								break;
							}
							dto.setDirectStart(directStart);
						} else if (fieldName.equals(TmdTotalTimeDao.COL_DIRECT_END)) {
							// ????????????
							int directEnd = 0;
							try {
								directEnd = Integer.parseInt(value);
							} catch (NumberFormatException e) {
								hasError = true;
								break;
							}
							if (directEnd < 0) {
								hasError = true;
								break;
							}
							dto.setDirectEnd(directEnd);
						} else if (fieldName.equals(TmdTotalTimeDao.COL_REST_TIME)) {
							// ????????????
							int restTime = 0;
							try {
								restTime = Integer.parseInt(value);
							} catch (NumberFormatException e) {
								hasError = true;
								break;
							}
							if (restTime < 0) {
								hasError = true;
								break;
							}
							dto.setRestTime(restTime);
						} else if (fieldName.equals(TmdTotalTimeDao.COL_REST_LATE_NIGHT)) {
							// ??????????????????
							int restLateNight = 0;
							try {
								restLateNight = Integer.parseInt(value);
							} catch (NumberFormatException e) {
								hasError = true;
								break;
							}
							if (restLateNight < 0) {
								hasError = true;
								break;
							}
							dto.setRestLateNight(restLateNight);
						} else if (fieldName.equals(TmdTotalTimeDao.COL_REST_WORK_ON_SPECIFIC_HOLIDAY)) {
							// ????????????????????????
							int restWorkOnSpecificHoliday = 0;
							try {
								restWorkOnSpecificHoliday = Integer.parseInt(value);
							} catch (NumberFormatException e) {
								hasError = true;
								break;
							}
							if (restWorkOnSpecificHoliday < 0) {
								hasError = true;
								break;
							}
							dto.setRestWorkOnSpecificHoliday(restWorkOnSpecificHoliday);
						} else if (fieldName.equals(TmdTotalTimeDao.COL_REST_WORK_ON_HOLIDAY)) {
							// ????????????????????????
							int restWorkOnHoliday = 0;
							try {
								restWorkOnHoliday = Integer.parseInt(value);
							} catch (NumberFormatException e) {
								hasError = true;
								break;
							}
							if (restWorkOnHoliday < 0) {
								hasError = true;
								break;
							}
							dto.setRestWorkOnHoliday(restWorkOnHoliday);
						} else if (fieldName.equals(TmdTotalTimeDao.COL_PUBLIC_TIME)) {
							// ??????????????????
							int publicTime = 0;
							try {
								publicTime = Integer.parseInt(value);
							} catch (NumberFormatException e) {
								hasError = true;
								break;
							}
							if (publicTime < 0) {
								hasError = true;
								break;
							}
							dto.setPublicTime(publicTime);
						} else if (fieldName.equals(TmdTotalTimeDao.COL_PRIVATE_TIME)) {
							// ??????????????????
							int privateTime = 0;
							try {
								privateTime = Integer.parseInt(value);
							} catch (NumberFormatException e) {
								hasError = true;
								break;
							}
							if (privateTime < 0) {
								hasError = true;
								break;
							}
							dto.setPrivateTime(privateTime);
						} else if (TmdTotalTimeDao.COL_OVERTIME.equals(fieldName)) {
							// ????????????
							int overtime = 0;
							try {
								overtime = Integer.parseInt(value);
							} catch (NumberFormatException e) {
								hasError = true;
								break;
							}
							if (overtime < 0) {
								hasError = true;
								break;
							}
							dto.setOvertime(overtime);
						} else if (TmdTotalTimeDao.COL_OVERTIME_IN.equals(fieldName)) {
							// ?????????????????????
							int overtimeIn = 0;
							try {
								overtimeIn = Integer.parseInt(value);
							} catch (NumberFormatException e) {
								hasError = true;
								break;
							}
							if (overtimeIn < 0) {
								hasError = true;
								break;
							}
							dto.setOvertimeIn(overtimeIn);
						} else if (fieldName.equals(TmdTotalTimeDao.COL_OVERTIME_OUT)) {
							// ?????????????????????
							int overtimeOut = 0;
							try {
								overtimeOut = Integer.parseInt(value);
							} catch (NumberFormatException e) {
								hasError = true;
								break;
							}
							if (overtimeOut < 0) {
								hasError = true;
								break;
							}
							dto.setOvertimeOut(overtimeOut);
						} else if (fieldName.equals(TmdTotalTimeDao.COL_LATE_NIGHT)) {
							// ????????????
							int lateNight = 0;
							try {
								lateNight = Integer.parseInt(value);
							} catch (NumberFormatException e) {
								hasError = true;
								break;
							}
							if (lateNight < 0) {
								hasError = true;
								break;
							}
							dto.setLateNight(lateNight);
						} else if (fieldName.equals(TmdTotalTimeDao.COL_NIGHT_WORK_WITHIN_PRESCRIBED_WORK)) {
							// ?????????????????????????????????
							int nightWorkWithinPrescribedWork = 0;
							try {
								nightWorkWithinPrescribedWork = Integer.parseInt(value);
							} catch (NumberFormatException e) {
								hasError = true;
								break;
							}
							if (nightWorkWithinPrescribedWork < 0) {
								hasError = true;
								break;
							}
							dto.setNightWorkWithinPrescribedWork(nightWorkWithinPrescribedWork);
						} else if (fieldName.equals(TmdTotalTimeDao.COL_NIGHT_OVERTIME_WORK)) {
							// ?????????????????????
							int nightOvertimeWork = 0;
							try {
								nightOvertimeWork = Integer.parseInt(value);
							} catch (NumberFormatException e) {
								hasError = true;
								break;
							}
							if (nightOvertimeWork < 0) {
								hasError = true;
								break;
							}
							dto.setNightOvertimeWork(nightOvertimeWork);
						} else if (fieldName.equals(TmdTotalTimeDao.COL_NIGHT_WORK_ON_HOLIDAY)) {
							// ????????????????????????
							int nightWorkOnHoliday = 0;
							try {
								nightWorkOnHoliday = Integer.parseInt(value);
							} catch (NumberFormatException e) {
								hasError = true;
								break;
							}
							if (nightWorkOnHoliday < 0) {
								hasError = true;
								break;
							}
							dto.setNightWorkOnHoliday(nightWorkOnHoliday);
						} else if (fieldName.equals(TmdTotalTimeDao.COL_WORK_ON_SPECIFIC_HOLIDAY)) {
							// ??????????????????
							int workOnSpecificHoliday = 0;
							try {
								workOnSpecificHoliday = Integer.parseInt(value);
							} catch (NumberFormatException e) {
								hasError = true;
								break;
							}
							if (workOnSpecificHoliday < 0) {
								hasError = true;
								break;
							}
							dto.setWorkOnSpecificHoliday(workOnSpecificHoliday);
						} else if (fieldName.equals(TmdTotalTimeDao.COL_WORK_ON_HOLIDAY)) {
							// ??????????????????
							int workOnHoliday = 0;
							try {
								workOnHoliday = Integer.parseInt(value);
							} catch (NumberFormatException e) {
								hasError = true;
								break;
							}
							if (workOnHoliday < 0) {
								hasError = true;
								break;
							}
							dto.setWorkOnHoliday(workOnHoliday);
						} else if (fieldName.equals(TmdTotalTimeDao.COL_DECREASE_TIME)) {
							// ??????????????????
							int decreaseTime = 0;
							try {
								decreaseTime = Integer.parseInt(value);
							} catch (NumberFormatException e) {
								hasError = true;
								break;
							}
							if (decreaseTime < 0) {
								hasError = true;
								break;
							}
							dto.setDecreaseTime(decreaseTime);
						} else if (fieldName.equals(TmdTotalTimeDao.COL_FORTY_FIVE_HOUR_OVERTIME)) {
							// 45?????????????????????
							int fortyFiveHourOvertime = 0;
							try {
								fortyFiveHourOvertime = Integer.parseInt(value);
							} catch (NumberFormatException e) {
								hasError = true;
								break;
							}
							if (fortyFiveHourOvertime < 0) {
								hasError = true;
								break;
							}
							dto.setFortyFiveHourOvertime(fortyFiveHourOvertime);
						} else if (fieldName.equals(TmdTotalTimeDao.COL_TIMES_OVERTIME)) {
							// ????????????
							int timesOvertime = 0;
							try {
								timesOvertime = Integer.parseInt(value);
							} catch (NumberFormatException e) {
								hasError = true;
								break;
							}
							if (timesOvertime < 0) {
								hasError = true;
								break;
							}
							dto.setTimesOvertime(timesOvertime);
						} else if (fieldName.equals(TmdTotalTimeDao.COL_TIMES_WORKING_HOLIDAY)) {
							// ??????????????????
							int timesWorkingHoliday = 0;
							try {
								timesWorkingHoliday = Integer.parseInt(value);
							} catch (NumberFormatException e) {
								hasError = true;
								break;
							}
							if (timesWorkingHoliday < 0) {
								hasError = true;
								break;
							}
							dto.setTimesWorkingHoliday(timesWorkingHoliday);
						} else if (TmdTotalTimeDao.COL_LATE_DAYS.equals(fieldName)) {
							// ??????????????????
							int lateDays = 0;
							try {
								lateDays = Integer.parseInt(value);
							} catch (NumberFormatException e) {
								hasError = true;
								break;
							}
							if (lateDays < 0) {
								hasError = true;
								break;
							}
							dto.setLateDays(lateDays);
						} else if (TmdTotalTimeDao.COL_LATE_THIRTY_MINUTES_OR_MORE.equals(fieldName)) {
							// ??????30???????????????
							int lateThirtyMinutesOrMore = 0;
							try {
								lateThirtyMinutesOrMore = Integer.parseInt(value);
							} catch (NumberFormatException e) {
								hasError = true;
								break;
							}
							if (lateThirtyMinutesOrMore < 0) {
								hasError = true;
								break;
							}
							dto.setLateThirtyMinutesOrMore(lateThirtyMinutesOrMore);
						} else if (TmdTotalTimeDao.COL_LATE_LESS_THAN_THIRTY_MINUTES.equals(fieldName)) {
							// ??????30???????????????
							int lateLessThanThirtyMinutes = 0;
							try {
								lateLessThanThirtyMinutes = Integer.parseInt(value);
							} catch (NumberFormatException e) {
								hasError = true;
								break;
							}
							if (lateLessThanThirtyMinutes < 0) {
								hasError = true;
								break;
							}
							dto.setLateLessThanThirtyMinutes(lateLessThanThirtyMinutes);
						} else if (TmdTotalTimeDao.COL_LATE_TIME.equals(fieldName)) {
							// ??????????????????
							int lateTime = 0;
							try {
								lateTime = Integer.parseInt(value);
							} catch (NumberFormatException e) {
								hasError = true;
								break;
							}
							if (lateTime < 0) {
								hasError = true;
								break;
							}
							dto.setLateTime(lateTime);
						} else if (TmdTotalTimeDao.COL_LATE_THIRTY_MINUTES_OR_MORE_TIME.equals(fieldName)) {
							// ??????30???????????????
							int lateThirtyMinutesOrMoreTime = 0;
							try {
								lateThirtyMinutesOrMoreTime = Integer.parseInt(value);
							} catch (NumberFormatException e) {
								hasError = true;
								break;
							}
							if (lateThirtyMinutesOrMoreTime < 0) {
								hasError = true;
								break;
							}
							dto.setLateThirtyMinutesOrMoreTime(lateThirtyMinutesOrMoreTime);
						} else if (TmdTotalTimeDao.COL_LATE_LESS_THAN_THIRTY_MINUTES_TIME.equals(fieldName)) {
							// ??????30???????????????
							int lateLessThanThirtyMinutesTime = 0;
							try {
								lateLessThanThirtyMinutesTime = Integer.parseInt(value);
							} catch (NumberFormatException e) {
								hasError = true;
								break;
							}
							if (lateLessThanThirtyMinutesTime < 0) {
								hasError = true;
								break;
							}
							dto.setLateLessThanThirtyMinutesTime(lateLessThanThirtyMinutesTime);
						} else if (TmdTotalTimeDao.COL_TIMES_LATE.equals(fieldName)) {
							// ??????????????????
							int timesLate = 0;
							try {
								timesLate = Integer.parseInt(value);
							} catch (NumberFormatException e) {
								hasError = true;
								break;
							}
							if (timesLate < 0) {
								hasError = true;
								break;
							}
							dto.setTimesLate(timesLate);
						} else if (TmdTotalTimeDao.COL_LEAVE_EARLY_DAYS.equals(fieldName)) {
							// ??????????????????
							int leaveEarlyDays = 0;
							try {
								leaveEarlyDays = Integer.parseInt(value);
							} catch (NumberFormatException e) {
								hasError = true;
								break;
							}
							if (leaveEarlyDays < 0) {
								hasError = true;
								break;
							}
							dto.setLeaveEarlyDays(leaveEarlyDays);
						} else if (TmdTotalTimeDao.COL_LEAVE_EARLY_THIRTY_MINUTES_OR_MORE.equals(fieldName)) {
							//  ??????30???????????????
							int leaveEarlyThirtyMinutesOrMore = 0;
							try {
								leaveEarlyThirtyMinutesOrMore = Integer.parseInt(value);
							} catch (NumberFormatException e) {
								hasError = true;
								break;
							}
							if (leaveEarlyThirtyMinutesOrMore < 0) {
								hasError = true;
								break;
							}
							dto.setLeaveEarlyThirtyMinutesOrMore(leaveEarlyThirtyMinutesOrMore);
						} else if (TmdTotalTimeDao.COL_LEAVE_EARLY_LESS_THAN_THIRTY_MINUTES.equals(fieldName)) {
							// ??????30???????????????
							int leaveEarlyLessThanThirtyMinutes = 0;
							try {
								leaveEarlyLessThanThirtyMinutes = Integer.parseInt(value);
							} catch (NumberFormatException e) {
								hasError = true;
								break;
							}
							if (leaveEarlyLessThanThirtyMinutes < 0) {
								hasError = true;
								break;
							}
							dto.setLeaveEarlyLessThanThirtyMinutes(leaveEarlyLessThanThirtyMinutes);
						} else if (TmdTotalTimeDao.COL_LEAVE_EARLY_TIME.equals(fieldName)) {
							// ??????????????????
							int leaveEarlyTime = 0;
							try {
								leaveEarlyTime = Integer.parseInt(value);
							} catch (NumberFormatException e) {
								hasError = true;
								break;
							}
							if (leaveEarlyTime < 0) {
								hasError = true;
								break;
							}
							dto.setLeaveEarlyTime(leaveEarlyTime);
						} else if (TmdTotalTimeDao.COL_LEAVE_EARLY_THIRTY_MINUTES_OR_MORE_TIME.equals(fieldName)) {
							// ??????30???????????????
							int leaveEarlyThirtyMinutesOrMoreTime = 0;
							try {
								leaveEarlyThirtyMinutesOrMoreTime = Integer.parseInt(value);
							} catch (NumberFormatException e) {
								hasError = true;
								break;
							}
							if (leaveEarlyThirtyMinutesOrMoreTime < 0) {
								hasError = true;
								break;
							}
							dto.setLeaveEarlyThirtyMinutesOrMoreTime(leaveEarlyThirtyMinutesOrMoreTime);
						} else if (TmdTotalTimeDao.COL_LEAVE_EARLY_LESS_THAN_THIRTY_MINUTES_TIME.equals(fieldName)) {
							// ??????30???????????????
							int leaveEarlyLessThanThirtyMinutesTime = 0;
							try {
								leaveEarlyLessThanThirtyMinutesTime = Integer.parseInt(value);
							} catch (NumberFormatException e) {
								hasError = true;
								break;
							}
							if (leaveEarlyLessThanThirtyMinutesTime < 0) {
								hasError = true;
								break;
							}
							dto.setLeaveEarlyLessThanThirtyMinutesTime(leaveEarlyLessThanThirtyMinutesTime);
						} else if (TmdTotalTimeDao.COL_TIMES_LEAVE_EARLY.equals(fieldName)) {
							// ??????????????????
							int timesLeaveEarly = 0;
							try {
								timesLeaveEarly = Integer.parseInt(value);
							} catch (NumberFormatException e) {
								hasError = true;
								break;
							}
							if (timesLeaveEarly < 0) {
								hasError = true;
								break;
							}
							dto.setTimesLeaveEarly(timesLeaveEarly);
						} else if (fieldName.equals(TmdTotalTimeDao.COL_TIMES_HOLIDAY)) {
							// ????????????
							int timesHoliday = 0;
							try {
								timesHoliday = Integer.parseInt(value);
							} catch (NumberFormatException e) {
								hasError = true;
								break;
							}
							if (timesHoliday < 0) {
								hasError = true;
								break;
							}
							dto.setTimesHoliday(timesHoliday);
						} else if (fieldName.equals(TmdTotalTimeDao.COL_TIMES_LEGAL_HOLIDAY)) {
							// ??????????????????
							int timesLegalHoliday = 0;
							try {
								timesLegalHoliday = Integer.parseInt(value);
							} catch (NumberFormatException e) {
								hasError = true;
								break;
							}
							if (timesLegalHoliday < 0) {
								hasError = true;
								break;
							}
							dto.setTimesLegalHoliday(timesLegalHoliday);
						} else if (fieldName.equals(TmdTotalTimeDao.COL_TIMES_SPECIFIC_HOLIDAY)) {
							// ??????????????????
							int timesSpecificHoliday = 0;
							try {
								timesSpecificHoliday = Integer.parseInt(value);
							} catch (NumberFormatException e) {
								hasError = true;
								break;
							}
							if (timesSpecificHoliday < 0) {
								hasError = true;
								break;
							}
							dto.setTimesSpecificHoliday(timesSpecificHoliday);
						} else if (fieldName.equals(TmdTotalTimeDao.COL_TIMES_PAID_HOLIDAY)) {
							// ??????????????????
							double timesPaidHoliday = 0;
							try {
								timesPaidHoliday = Double.parseDouble(value);
							} catch (NumberFormatException e) {
								hasError = true;
								break;
							}
							if (timesPaidHoliday < 0) {
								hasError = true;
								break;
							}
							dto.setTimesPaidHoliday(timesPaidHoliday);
						} else if (fieldName.equals(TmdTotalTimeDao.COL_PAID_HOLIDAY_HOUR)) {
							// ??????????????????
							int paidHolidayHour = 0;
							try {
								paidHolidayHour = Integer.parseInt(value);
							} catch (NumberFormatException e) {
								hasError = true;
								break;
							}
							if (paidHolidayHour < 0) {
								hasError = true;
								break;
							}
							dto.setPaidHolidayHour(paidHolidayHour);
						} else if (fieldName.equals(TmdTotalTimeDao.COL_TIMES_STOCK_HOLIDAY)) {
							// ????????????????????????
							double timesStockHoliday = 0;
							try {
								timesStockHoliday = Double.parseDouble(value);
							} catch (NumberFormatException e) {
								hasError = true;
								break;
							}
							if (timesStockHoliday < 0) {
								hasError = true;
								break;
							}
							dto.setTimesStockHoliday(timesStockHoliday);
						} else if (fieldName.equals(TmdTotalTimeDao.COL_TIMES_COMPENSATION)) {
							// ????????????
							double timesCompensation = 0;
							try {
								timesCompensation = Double.parseDouble(value);
							} catch (NumberFormatException e) {
								hasError = true;
								break;
							}
							if (timesCompensation < 0) {
								hasError = true;
								break;
							}
							dto.setTimesCompensation(timesCompensation);
						} else if (fieldName.equals(TmdTotalTimeDao.COL_TIMES_LEGAL_COMPENSATION)) {
							// ??????????????????
							double timesLegalCompensation = 0;
							try {
								timesLegalCompensation = Double.parseDouble(value);
							} catch (NumberFormatException e) {
								hasError = true;
								break;
							}
							if (timesLegalCompensation < 0) {
								hasError = true;
								break;
							}
							dto.setTimesLegalCompensation(timesLegalCompensation);
						} else if (fieldName.equals(TmdTotalTimeDao.COL_TIMES_SPECIFIC_COMPENSATION)) {
							// ??????????????????
							double timesSpecificCompensation = 0;
							try {
								timesSpecificCompensation = Double.parseDouble(value);
							} catch (NumberFormatException e) {
								hasError = true;
								break;
							}
							if (timesSpecificCompensation < 0) {
								hasError = true;
								break;
							}
							dto.setTimesSpecificCompensation(timesSpecificCompensation);
						} else if (fieldName.equals(TmdTotalTimeDao.COL_TIMES_LATE_COMPENSATION)) {
							// ??????????????????
							double timesLateCompensation = 0;
							try {
								timesLateCompensation = Double.parseDouble(value);
							} catch (NumberFormatException e) {
								hasError = true;
								break;
							}
							if (timesLateCompensation < 0) {
								hasError = true;
								break;
							}
							dto.setTimesLateCompensation(timesLateCompensation);
						} else if (fieldName.equals(TmdTotalTimeDao.COL_TIMES_HOLIDAY_SUBSTITUTE)) {
							// ??????????????????
							double timesHolidaySubstitute = 0;
							try {
								timesHolidaySubstitute = Double.parseDouble(value);
							} catch (NumberFormatException e) {
								hasError = true;
								break;
							}
							if (timesHolidaySubstitute < 0) {
								hasError = true;
								break;
							}
							dto.setTimesHolidaySubstitute(timesHolidaySubstitute);
						} else if (fieldName.equals(TmdTotalTimeDao.COL_TIMES_LEGAL_HOLIDAY_SUBSTITUTE)) {
							//  ????????????????????????
							double timesLegalHolidaySubstitute = 0;
							try {
								timesLegalHolidaySubstitute = Double.parseDouble(value);
							} catch (NumberFormatException e) {
								hasError = true;
								break;
							}
							if (timesLegalHolidaySubstitute < 0) {
								hasError = true;
								break;
							}
							dto.setTimesLegalHolidaySubstitute(timesLegalHolidaySubstitute);
						} else if (fieldName.equals(TmdTotalTimeDao.COL_TIMES_SPECIFIC_HOLIDAY_SUBSTITUTE)) {
							// ????????????????????????
							double timesSpecificHolidaySubstitute = 0;
							try {
								timesSpecificHolidaySubstitute = Double.parseDouble(value);
							} catch (NumberFormatException e) {
								hasError = true;
								break;
							}
							if (timesSpecificHolidaySubstitute < 0) {
								hasError = true;
								break;
							}
							dto.setTimesSpecificHolidaySubstitute(timesSpecificHolidaySubstitute);
						} else if (fieldName.equals(TmdTotalTimeDao.COL_TOTAL_SPECIAL_HOLIDAY)) {
							// ????????????????????????
							double totalSpecialHoliday = 0;
							try {
								totalSpecialHoliday = Double.parseDouble(value);
							} catch (NumberFormatException e) {
								hasError = true;
								break;
							}
							if (totalSpecialHoliday < 0) {
								hasError = true;
								break;
							}
							dto.setTotalSpecialHoliday(totalSpecialHoliday);
						} else if (fieldName.equals(TmdTotalTimeDao.COL_SPECIAL_HOLIDAY_HOUR)) {
							// ??????????????????
							int specialHolidayHour = 0;
							try {
								specialHolidayHour = Integer.parseInt(value);
							} catch (NumberFormatException e) {
								hasError = true;
								break;
							}
							if (specialHolidayHour < 0) {
								hasError = true;
								break;
							}
							dto.setSpecialHolidayHour(specialHolidayHour);
						} else if (fieldName.equals(TmdTotalTimeDao.COL_TOTAL_OTHER_HOLIDAY)) {
							// ???????????????????????????
							double totalOtherHoliday = 0;
							try {
								totalOtherHoliday = Double.parseDouble(value);
							} catch (NumberFormatException e) {
								hasError = true;
								break;
							}
							if (totalOtherHoliday < 0) {
								hasError = true;
								break;
							}
							dto.setTotalOtherHoliday(totalOtherHoliday);
						} else if (fieldName.equals(TmdTotalTimeDao.COL_OTHER_HOLIDAY_HOUR)) {
							// ?????????????????????
							int otherHolidayHour = 0;
							try {
								otherHolidayHour = Integer.parseInt(value);
							} catch (NumberFormatException e) {
								hasError = true;
								break;
							}
							if (otherHolidayHour < 0) {
								hasError = true;
								break;
							}
							dto.setOtherHolidayHour(otherHolidayHour);
						} else if (fieldName.equals(TmdTotalTimeDao.COL_TOTAL_ABSENCE)) {
							// ??????????????????
							double totalAbsence = 0;
							try {
								totalAbsence = Double.parseDouble(value);
							} catch (NumberFormatException e) {
								hasError = true;
								break;
							}
							if (totalAbsence < 0) {
								hasError = true;
								break;
							}
							dto.setTotalAbsence(totalAbsence);
						} else if (fieldName.equals(TmdTotalTimeDao.COL_ABSENCE_HOUR)) {
							// ????????????
							int absenceHolidayHour = 0;
							try {
								absenceHolidayHour = Integer.parseInt(value);
							} catch (NumberFormatException e) {
								hasError = true;
								break;
							}
							if (absenceHolidayHour < 0) {
								hasError = true;
								break;
							}
							dto.setAbsenceHour(absenceHolidayHour);
						} else if (fieldName.equals(TmdTotalTimeDao.COL_TOTAL_ALLOWANCE)) {
							// ????????????
							int totalAllowance = 0;
							try {
								totalAllowance = Integer.parseInt(value);
							} catch (NumberFormatException e) {
								hasError = true;
								break;
							}
							if (totalAllowance < 0) {
								hasError = true;
								break;
							}
							dto.setTotalAllowance(totalAllowance);
						} else if (fieldName.equals(TmdTotalTimeDao.COL_SIXTY_HOUR_OVERTIME)) {
							// 60?????????????????????
							int sixtyHourOvertime = 0;
							try {
								sixtyHourOvertime = Integer.parseInt(value);
							} catch (NumberFormatException e) {
								hasError = true;
								break;
							}
							if (sixtyHourOvertime < 0) {
								hasError = true;
								break;
							}
							dto.setSixtyHourOvertime(sixtyHourOvertime);
						} else if (fieldName.equals(TmdTotalTimeDao.COL_WEEK_DAY_OVERTIME)) {
							// ?????????????????????
							int weekDayOvertime = 0;
							try {
								weekDayOvertime = Integer.parseInt(value);
							} catch (NumberFormatException e) {
								hasError = true;
								break;
							}
							if (weekDayOvertime < 0) {
								hasError = true;
								break;
							}
							dto.setWeekDayOvertime(weekDayOvertime);
						} else if (fieldName.equals(TmdTotalTimeDao.COL_SPECIFIC_OVERTIME)) {
							// ???????????????????????????
							int specificOvertime = 0;
							try {
								specificOvertime = Integer.parseInt(value);
							} catch (NumberFormatException e) {
								hasError = true;
								break;
							}
							if (specificOvertime < 0) {
								hasError = true;
								break;
							}
							dto.setSpecificOvertime(specificOvertime);
						} else if (fieldName.equals(TmdTotalTimeDao.COL_TIMES_ALTERNATIVE)) {
							// ??????????????????
							double timesAlternative = 0;
							try {
								timesAlternative = Double.parseDouble(value);
							} catch (NumberFormatException e) {
								hasError = true;
								break;
							}
							if (timesAlternative < 0) {
								hasError = true;
								break;
							}
							dto.setTimesAlternative(timesAlternative);
						} else if (TmdTotalTimeDao.COL_LEGAL_COMPENSATION_UNUSED.equals(fieldName)) {
							// ???????????????????????????
							double legalCompensationUnused = 0;
							try {
								legalCompensationUnused = Double.parseDouble(value);
							} catch (NumberFormatException e) {
								hasError = true;
								break;
							}
							if (legalCompensationUnused < 0) {
								hasError = true;
								break;
							}
							dto.setLegalCompensationUnused(legalCompensationUnused);
						} else if (TmdTotalTimeDao.COL_SPECIFIC_COMPENSATION_UNUSED.equals(fieldName)) {
							// ???????????????????????????
							double specificCompensationUnused = 0;
							try {
								specificCompensationUnused = Double.parseDouble(value);
							} catch (NumberFormatException e) {
								hasError = true;
								break;
							}
							if (specificCompensationUnused < 0) {
								hasError = true;
								break;
							}
							dto.setSpecificCompensationUnused(specificCompensationUnused);
						} else if (TmdTotalTimeDao.COL_LATE_COMPENSATION_UNUSED.equals(fieldName)) {
							// ???????????????????????????
							double lateCompensationUnused = 0;
							try {
								lateCompensationUnused = Double.parseDouble(value);
							} catch (NumberFormatException e) {
								hasError = true;
								break;
							}
							if (lateCompensationUnused < 0) {
								hasError = true;
								break;
							}
							dto.setLateCompensationUnused(lateCompensationUnused);
						} else if (TmdTotalTimeDao.COL_STATUTORY_HOLIDAY_WORK_TIME_IN.equals(fieldName)) {
							// ?????????????????????????????????????????????
							int statutoryHolidayWorkTimeIn = 0;
							try {
								statutoryHolidayWorkTimeIn = Integer.parseInt(value);
							} catch (NumberFormatException e) {
								hasError = true;
								break;
							}
							if (statutoryHolidayWorkTimeIn < 0) {
								hasError = true;
								break;
							}
							dto.setStatutoryHolidayWorkTimeIn(statutoryHolidayWorkTimeIn);
						} else if (TmdTotalTimeDao.COL_STATUTORY_HOLIDAY_WORK_TIME_OUT.equals(fieldName)) {
							// ?????????????????????????????????????????????
							int statutoryHolidayWorkTimeOut = 0;
							try {
								statutoryHolidayWorkTimeOut = Integer.parseInt(value);
							} catch (NumberFormatException e) {
								hasError = true;
								break;
							}
							if (statutoryHolidayWorkTimeOut < 0) {
								hasError = true;
								break;
							}
							dto.setStatutoryHolidayWorkTimeOut(statutoryHolidayWorkTimeOut);
						} else if (TmdTotalTimeDao.COL_PRESCRIBED_HOLIDAY_WORK_TIME_IN.equals(fieldName)) {
							// ?????????????????????????????????????????????
							int prescribedHolidayWorkTimeIn = 0;
							try {
								prescribedHolidayWorkTimeIn = Integer.parseInt(value);
							} catch (NumberFormatException e) {
								hasError = true;
								break;
							}
							if (prescribedHolidayWorkTimeIn < 0) {
								hasError = true;
								break;
							}
							dto.setPrescribedHolidayWorkTimeIn(prescribedHolidayWorkTimeIn);
						} else if (TmdTotalTimeDao.COL_PRESCRIBED_HOLIDAY_WORK_TIME_OUT.equals(fieldName)) {
							// ?????????????????????????????????????????????
							int prescribedHolidayWorkTimeOut = 0;
							try {
								prescribedHolidayWorkTimeOut = Integer.parseInt(value);
							} catch (NumberFormatException e) {
								hasError = true;
								break;
							}
							if (prescribedHolidayWorkTimeOut < 0) {
								hasError = true;
								break;
							}
							dto.setPrescribedHolidayWorkTimeOut(prescribedHolidayWorkTimeOut);
						} else if (fieldName.equals(TmdTotalTimeDao.COL_WEEKLY_OVER_FORTY_HOUR_WORK_TIME)) {
							// ???40?????????????????????
							int weeklyOverFortyHourWorkTime = 0;
							try {
								weeklyOverFortyHourWorkTime = Integer.parseInt(value);
							} catch (NumberFormatException e) {
								hasError = true;
								break;
							}
							if (weeklyOverFortyHourWorkTime < 0) {
								hasError = true;
								break;
							}
							dto.setWeeklyOverFortyHourWorkTime(weeklyOverFortyHourWorkTime);
						} else if (fieldName.equals(TmdTotalTimeDao.COL_OVERTIME_IN_NO_WEEKLY_FORTY)) {
							// ?????????????????????(???40???????????????)
							int overtimeInNoWeeklyForty = 0;
							try {
								overtimeInNoWeeklyForty = Integer.parseInt(value);
							} catch (NumberFormatException e) {
								hasError = true;
								break;
							}
							if (overtimeInNoWeeklyForty < 0) {
								hasError = true;
								break;
							}
							dto.setOvertimeInNoWeeklyForty(overtimeInNoWeeklyForty);
						} else if (fieldName.equals(TmdTotalTimeDao.COL_OVERTIME_OUT_NO_WEEKLY_FORTY)) {
							// ?????????????????????(???40???????????????)
							int overtimeOutNoWeeklyForty = 0;
							try {
								overtimeOutNoWeeklyForty = Integer.parseInt(value);
							} catch (NumberFormatException e) {
								hasError = true;
								break;
							}
							if (overtimeOutNoWeeklyForty < 0) {
								hasError = true;
								break;
							}
							dto.setOvertimeOutNoWeeklyForty(overtimeOutNoWeeklyForty);
						} else if (fieldName.equals(TmdTotalTimeDao.COL_WEEK_DAY_OVERTIME_TOTAL)) {
							// ????????????????????????
							int weekDayOvertimeTotal = 0;
							try {
								weekDayOvertimeTotal = Integer.parseInt(value);
							} catch (NumberFormatException e) {
								hasError = true;
								break;
							}
							if (weekDayOvertimeTotal < 0) {
								hasError = true;
								break;
							}
							dto.setWeekDayOvertimeTotal(weekDayOvertimeTotal);
						} else if (fieldName.equals(TmdTotalTimeDao.COL_WEEK_DAY_OVERTIME_IN_NO_WEEKLY_FORTY)) {
							// ?????????????????????(???40???????????????)
							int weekDayOvertimeInNoWeeklyForty = 0;
							try {
								weekDayOvertimeInNoWeeklyForty = Integer.parseInt(value);
							} catch (NumberFormatException e) {
								hasError = true;
								break;
							}
							if (weekDayOvertimeInNoWeeklyForty < 0) {
								hasError = true;
								break;
							}
							dto.setWeekDayOvertimeInNoWeeklyForty(weekDayOvertimeInNoWeeklyForty);
						} else if (fieldName.equals(TmdTotalTimeDao.COL_WEEK_DAY_OVERTIME_OUT_NO_WEEKLY_FORTY)) {
							// ?????????????????????(???40???????????????)
							int weekDayOvertimeOutNoWeeklyForty = 0;
							try {
								weekDayOvertimeOutNoWeeklyForty = Integer.parseInt(value);
							} catch (NumberFormatException e) {
								hasError = true;
								break;
							}
							if (weekDayOvertimeOutNoWeeklyForty < 0) {
								hasError = true;
								break;
							}
							dto.setWeekDayOvertimeOutNoWeeklyForty(weekDayOvertimeOutNoWeeklyForty);
						} else if (fieldName.equals(TmdTotalTimeDao.COL_WEEK_DAY_OVERTIME_IN)) {
							// ?????????????????????
							int weekDayOvertimeIn = 0;
							try {
								weekDayOvertimeIn = Integer.parseInt(value);
							} catch (NumberFormatException e) {
								hasError = true;
								break;
							}
							if (weekDayOvertimeIn < 0) {
								hasError = true;
								break;
							}
							dto.setWeekDayOvertimeIn(weekDayOvertimeIn);
						}
					}
				}
				if (!hasError && employeeCode.isEmpty()) {
					hasError = true;
				}
				if (!hasError && dto.getCalculationYear() < 1) {
					hasError = true;
				}
				if (!hasError && (dto.getCalculationMonth() < 1 || dto.getCalculationMonth() > 12)) {
					hasError = true;
				}
				if (!hasError && dto.getCalculationDate() == null) {
					hasError = true;
				}
				if (!hasError) {
					HumanDtoInterface humanDto = humanDao.findForEmployeeCode(employeeCode, dto.getCalculationDate());
					if (humanDto == null || humanDto.getPersonalId() == null || humanDto.getPersonalId().isEmpty()) {
						hasError = true;
					} else {
						// ??????ID
						dto.setPersonalId(humanDto.getPersonalId());
					}
				}
				String cutoffCode = "";
				if (!hasError) {
					ApplicationDtoInterface applicationDto = application.findForPerson(dto.getPersonalId(),
							dto.getCalculationDate());
					if (applicationDto == null) {
						hasError = true;
					} else {
						TimeSettingDtoInterface timeSettingDto = timeSettingDao
							.findForInfo(applicationDto.getWorkSettingCode(), dto.getCalculationDate());
						if (timeSettingDto == null) {
							hasError = true;
						} else {
							CutoffDtoInterface cutoffDto = cutoffDao.findForInfo(timeSettingDto.getCutoffCode(),
									dto.getCalculationDate());
							if (cutoffDto == null) {
								hasError = true;
							} else {
								// ???????????????????????????
								Date cutoffDate = TimeUtility.getCutoffLastDate(cutoffDto.getCutoffDate(),
										dto.getCalculationYear(), dto.getCalculationMonth());
								// ??????????????????????????????
								if (cutoffDto.getCutoffDate() != 0) {
									// ???????????????????????????????????????
									cutoffDate = DateUtility.getDate(dto.getCalculationYear(),
											dto.getCalculationMonth(), cutoffDto.getCutoffDate());
								}
								if (cutoffDate.equals(dto.getCalculationDate())) {
									cutoffCode = cutoffDto.getCutoffCode();
								} else {
									hasError = true;
								}
							}
						}
					}
				}
				if (hasError) {
					addInvalidDataErrorMessage(i);
				} else {
					// ?????????????????????????????????
					for (TotalTimeDataDtoInterface totalTimeDataDto : totaltimeList) {
						if (totalTimeDataDto.getPersonalId().equals(dto.getPersonalId())
								&& totalTimeDataDto.getCalculationYear() == dto.getCalculationYear()
								&& totalTimeDataDto.getCalculationMonth() == dto.getCalculationMonth()) {
							addDuplicateDataErrorMessage(i);
							hasError = true;
							break;
						}
					}
				}
				if (!hasError) {
					TotalTimeDataDtoInterface totalTimeDataDto = totalTimeDataDao.findForKey(dto.getPersonalId(),
							dto.getCalculationYear(), dto.getCalculationMonth());
					if (totalTimeDataDto != null && cutoffCode != null && !cutoffCode.isEmpty()) {
						TotalTimeDtoInterface totalTimeDto = totalTimeTransaction.findForKey(dto.getCalculationYear(),
								dto.getCalculationMonth(), cutoffCode);
						if (totalTimeDto == null) {
							dto.setTmdTotalTimeId(totalTimeDataDto.getTmdTotalTimeId());
						} else {
							int state = totalTimeDto.getCutoffState();
							if (state == TimeConst.CODE_CUTOFF_STATE_NOT_TIGHT
									|| state == TimeConst.CODE_CUTOFF_STATE_TEMP_TIGHT) {
								// ????????????????????????
								dto.setTmdTotalTimeId(totalTimeDataDto.getTmdTotalTimeId());
							} else if (state == TimeConst.CODE_CUTOFF_STATE_TIGHTENED) {
								// ???????????????
								addAlreadyRegisteredDataErrorMessage(i);
								hasError = true;
								break;
							}
						}
					}
				}
				if (!hasError) {
					totaltimeList.add(dto);
				}
			}
			i++;
		}
		return totaltimeList;
	}
	
	@Override
	public List<PaidHolidayDataDtoInterface> getPaidHolidayList(String importCode, List<String[]> list)
			throws MospException {
		Date systemDate = getSystemDate();
		ImportDtoInterface importDto = importDao.findForKey(importCode);
		if (importDto == null) {
			return null;
		}
		List<ImportFieldDtoInterface> importFieldDtoList = importFieldDao.findForList(importCode);
		if (importFieldDtoList == null || importFieldDtoList.isEmpty()) {
			return null;
		}
		List<PaidHolidayDataDtoInterface> paidHolidayDataList = new ArrayList<PaidHolidayDataDtoInterface>();
		int i = 0;
		for (String[] csvArray : list) {
			if (importDto.getHeader() == 1 && i == 0) {
				// ????????????????????????
				if (!checkHeader(importDto, importFieldDtoList, csvArray)) {
					// ????????????????????????????????????
					addInvalidHeaderErrorMessage();
					return null;
				}
			} else {
				boolean hasError = false;
				String employeeCode = "";
				PaidHolidayDataDtoInterface dto = new TmdPaidHolidayDataDto();
				for (ImportFieldDtoInterface importFieldDto : importFieldDtoList) {
					int fieldOrder = importFieldDto.getFieldOrder();
					if (csvArray.length > fieldOrder - 1) {
						String value = csvArray[fieldOrder - 1];
						String fieldName = importFieldDto.getFieldName();
						if (fieldName.equals(PfmHumanDao.COL_EMPLOYEE_CODE)) {
							// ???????????????
							employeeCode = value;
						} else if (fieldName.equals(TmdPaidHolidayDao.COL_ACTIVATE_DATE)) {
							// ?????????
							Date activateDate = getDate(value);
							if (activateDate == null) {
								hasError = true;
								break;
							}
							dto.setActivateDate(activateDate);
						} else if (fieldName.equals(TmdPaidHolidayDao.COL_ACQUISITION_DATE)) {
							// ?????????
							Date acquisitionDate = getDate(value);
							if (acquisitionDate == null) {
								hasError = true;
								break;
							}
							dto.setAcquisitionDate(acquisitionDate);
						} else if (fieldName.equals(TmdPaidHolidayDao.COL_LIMIT_DATE)) {
							// ?????????
							Date limitDate = getDate(value);
							if (limitDate == null) {
								hasError = true;
								break;
							}
							dto.setLimitDate(limitDate);
						} else if (fieldName.equals(TmdPaidHolidayDao.COL_HOLD_DAY)) {
							// ????????????
							double holdDay = 0;
							try {
								holdDay = Double.parseDouble(value);
							} catch (NumberFormatException e) {
								hasError = true;
								break;
							}
							if (holdDay < 0) {
								hasError = true;
								break;
							}
							dto.setHoldDay(holdDay);
						} else if (fieldName.equals(TmdPaidHolidayDao.COL_HOLD_HOUR)) {
							// ???????????????
							int holdHour = 0;
							try {
								holdHour = Integer.parseInt(value);
							} catch (NumberFormatException e) {
								hasError = true;
								break;
							}
							if (holdHour < 0) {
								hasError = true;
								break;
							}
							dto.setHoldHour(holdHour);
						} else if (fieldName.equals(TmdPaidHolidayDao.COL_GIVING_DAY)) {
							// ????????????
							double givingDay = 0;
							try {
								givingDay = Double.parseDouble(value);
							} catch (NumberFormatException e) {
								hasError = true;
								break;
							}
							if (givingDay < 0) {
								hasError = true;
								break;
							}
							dto.setGivingDay(givingDay);
						} else if (fieldName.equals(TmdPaidHolidayDao.COL_GIVING_HOUR)) {
							// ???????????????
							int givingHour = 0;
							try {
								givingHour = Integer.parseInt(value);
							} catch (NumberFormatException e) {
								hasError = true;
								break;
							}
							if (givingHour < 0) {
								hasError = true;
								break;
							}
							dto.setGivingHour(givingHour);
						} else if (fieldName.equals(TmdPaidHolidayDao.COL_CANCEL_DAY)) {
							// ????????????
							double cancelDay = 0;
							try {
								cancelDay = Double.parseDouble(value);
							} catch (NumberFormatException e) {
								hasError = true;
								break;
							}
							if (cancelDay < 0) {
								hasError = true;
								break;
							}
							dto.setCancelDay(cancelDay);
						} else if (fieldName.equals(TmdPaidHolidayDao.COL_CANCEL_HOUR)) {
							// ???????????????
							int cancelHour = 0;
							try {
								cancelHour = Integer.parseInt(value);
							} catch (NumberFormatException e) {
								hasError = true;
								break;
							}
							if (cancelHour < 0) {
								hasError = true;
								break;
							}
							dto.setCancelHour(cancelHour);
						} else if (fieldName.equals(TmdPaidHolidayDao.COL_USE_DAY)) {
							// ????????????
							double useDay = 0;
							try {
								useDay = Double.parseDouble(value);
							} catch (NumberFormatException e) {
								hasError = true;
								break;
							}
							if (useDay < 0) {
								hasError = true;
								break;
							}
							dto.setUseDay(useDay);
						} else if (fieldName.equals(TmdPaidHolidayDao.COL_USE_HOUR)) {
							// ???????????????
							int useHour = 0;
							try {
								useHour = Integer.parseInt(value);
							} catch (NumberFormatException e) {
								hasError = true;
								break;
							}
							if (useHour < 0) {
								hasError = true;
								break;
							}
							dto.setUseHour(useHour);
						}
					}
				}
				if (!hasError && employeeCode.isEmpty()) {
					hasError = true;
				}
				if (!hasError && dto.getActivateDate() == null) {
					hasError = true;
				}
				if (!hasError) {
					HumanDtoInterface humanDto = humanDao.findForEmployeeCode(employeeCode, dto.getActivateDate());
					if (humanDto == null || humanDto.getPersonalId() == null || humanDto.getPersonalId().isEmpty()) {
						hasError = true;
					} else {
						// ??????ID
						dto.setPersonalId(humanDto.getPersonalId());
					}
				}
				if (!hasError) {
					// ?????????????????????????????????
					ApplicationDtoInterface applicationDto = application.findForPerson(dto.getPersonalId(), systemDate);
					if (applicationDto != null) {
						// ?????????????????????????????????
						PaidHolidayDtoInterface paidHolidayDto = paidHolidayReference
							.getPaidHolidayInfo(applicationDto.getPaidHolidayCode(), systemDate);
						if (paidHolidayDto != null) {
							dto.setDenominatorDayHour(paidHolidayDto.getTimeAcquisitionLimitTimes());
						}
					}
				}
				// ????????????????????????
				if (dto.getAcquisitionDate() == null || dto.getLimitDate() == null) {
					hasError = true;
				}
				if (hasError) {
					addInvalidDataErrorMessage(i);
				} else {
					// ?????????????????????????????????
					for (PaidHolidayDataDtoInterface paidHolidayDataDto : paidHolidayDataList) {
						if (paidHolidayDataDto.getPersonalId().equals(dto.getPersonalId())
								&& paidHolidayDataDto.getActivateDate().equals(dto.getActivateDate())
								&& paidHolidayDataDto.getAcquisitionDate().equals(dto.getAcquisitionDate())) {
							addDuplicateDataErrorMessage(i);
							hasError = true;
							break;
						}
					}
				}
				if (!hasError) {
					PaidHolidayDataDtoInterface paidHolidayDataDto = paidHolidayDataDao.findForKey(dto.getPersonalId(),
							dto.getActivateDate(), dto.getAcquisitionDate());
					if (paidHolidayDataDto != null) {
						dto.setTmdPaidHolidayId(paidHolidayDataDto.getTmdPaidHolidayId());
					}
				}
				if (!hasError) {
					paidHolidayDataList.add(dto);
				}
			}
			i++;
		}
		return paidHolidayDataList;
	}
	
	@Override
	public List<StockHolidayDataDtoInterface> getStockHolidayList(String importCode, List<String[]> list)
			throws MospException {
		ImportDtoInterface importDto = importDao.findForKey(importCode);
		if (importDto == null) {
			return null;
		}
		List<ImportFieldDtoInterface> importFieldDtoList = importFieldDao.findForList(importCode);
		if (importFieldDtoList == null || importFieldDtoList.isEmpty()) {
			return null;
		}
		List<StockHolidayDataDtoInterface> stockHolidayDataList = new ArrayList<StockHolidayDataDtoInterface>();
		int i = 0;
		for (String[] csvArray : list) {
			if (importDto.getHeader() == 1 && i == 0) {
				// ????????????????????????
				if (!checkHeader(importDto, importFieldDtoList, csvArray)) {
					// ????????????????????????????????????
					addInvalidHeaderErrorMessage();
					return null;
				}
			} else {
				boolean hasError = false;
				String employeeCode = "";
				StockHolidayDataDtoInterface dto = new TmdStockHolidayDto();
				for (ImportFieldDtoInterface importFieldDto : importFieldDtoList) {
					int fieldOrder = importFieldDto.getFieldOrder();
					if (csvArray.length > fieldOrder - 1) {
						String value = csvArray[fieldOrder - 1];
						String fieldName = importFieldDto.getFieldName();
						if (fieldName.equals(PfmHumanDao.COL_EMPLOYEE_CODE)) {
							// ???????????????
							employeeCode = value;
						} else if (fieldName.equals(TmdStockHolidayDao.COL_ACTIVATE_DATE)) {
							// ?????????
							Date activateDate = getDate(value);
							if (activateDate == null) {
								hasError = true;
								break;
							}
							dto.setActivateDate(activateDate);
						} else if (fieldName.equals(TmdStockHolidayDao.COL_ACQUISITION_DATE)) {
							// ?????????
							Date acquisitionDate = getDate(value);
							if (acquisitionDate == null) {
								hasError = true;
								break;
							}
							dto.setAcquisitionDate(acquisitionDate);
						} else if (fieldName.equals(TmdStockHolidayDao.COL_LIMIT_DATE)) {
							// ?????????
							Date limitDate = getDate(value);
							if (limitDate == null) {
								hasError = true;
								break;
							}
							dto.setLimitDate(getDate(value));
						} else if (fieldName.equals(TmdStockHolidayDao.COL_HOLD_DAY)) {
							// ????????????
							double holdDay = 0;
							try {
								holdDay = Double.parseDouble(value);
							} catch (NumberFormatException e) {
								hasError = true;
								break;
							}
							if (holdDay < 0) {
								hasError = true;
								break;
							}
							dto.setHoldDay(holdDay);
						} else if (fieldName.equals(TmdStockHolidayDao.COL_GIVING_DAY)) {
							// ????????????
							double givingDay = 0;
							try {
								givingDay = Double.parseDouble(value);
							} catch (NumberFormatException e) {
								hasError = true;
								break;
							}
							if (givingDay < 0) {
								hasError = true;
								break;
							}
							dto.setGivingDay(givingDay);
						} else if (fieldName.equals(TmdStockHolidayDao.COL_CANCEL_DAY)) {
							// ????????????
							double cancelDay = 0;
							try {
								cancelDay = Double.parseDouble(value);
							} catch (NumberFormatException e) {
								hasError = true;
								break;
							}
							if (cancelDay < 0) {
								hasError = true;
								break;
							}
							dto.setCancelDay(cancelDay);
						} else if (fieldName.equals(TmdStockHolidayDao.COL_USE_DAY)) {
							// ????????????
							double useDay = 0;
							try {
								useDay = Double.parseDouble(value);
							} catch (NumberFormatException e) {
								hasError = true;
								break;
							}
							if (useDay < 0) {
								hasError = true;
								break;
							}
							dto.setUseDay(useDay);
						}
					}
				}
				if (!hasError && employeeCode.isEmpty()) {
					hasError = true;
				}
				if (!hasError && dto.getActivateDate() == null) {
					hasError = true;
				}
				if (!hasError) {
					HumanDtoInterface humanDto = humanDao.findForEmployeeCode(employeeCode, dto.getActivateDate());
					if (humanDto == null || humanDto.getPersonalId() == null || humanDto.getPersonalId().isEmpty()) {
						hasError = true;
					} else {
						// ??????ID
						dto.setPersonalId(humanDto.getPersonalId());
					}
				}
				//
				if (dto.getAcquisitionDate() == null || dto.getLimitDate() == null) {
					hasError = true;
				}
				
				if (hasError) {
					addInvalidDataErrorMessage(i);
				} else {
					// ?????????????????????????????????
					for (StockHolidayDataDtoInterface stockHolidayDataDto : stockHolidayDataList) {
						if (stockHolidayDataDto.getPersonalId().equals(dto.getPersonalId())
								&& stockHolidayDataDto.getActivateDate().equals(dto.getActivateDate())
								&& stockHolidayDataDto.getAcquisitionDate().equals(dto.getAcquisitionDate())) {
							addDuplicateDataErrorMessage(i);
							hasError = true;
							break;
						}
					}
				}
				if (!hasError) {
					StockHolidayDataDtoInterface stockHolidayDataDto = stockHolidayDataDao
						.findForKey(dto.getPersonalId(), dto.getActivateDate(), dto.getAcquisitionDate());
					if (stockHolidayDataDto != null) {
						dto.setTmdStockHolidayId(stockHolidayDataDto.getTmdStockHolidayId());
					}
				}
				if (!hasError) {
					stockHolidayDataList.add(dto);
				}
			}
			i++;
		}
		return stockHolidayDataList;
	}
	
	@Override
	public List<HolidayDataDtoInterface> getHolidayDataList(String importCode, List<String[]> list)
			throws MospException {
		ImportDtoInterface importDto = importDao.findForKey(importCode);
		if (importDto == null) {
			return null;
		}
		List<ImportFieldDtoInterface> importFieldDtoList = importFieldDao.findForList(importCode);
		if (importFieldDtoList == null || importFieldDtoList.isEmpty()) {
			return null;
		}
		List<HolidayDataDtoInterface> holidayDataList = new ArrayList<HolidayDataDtoInterface>();
		int i = 0;
		for (String[] csvArray : list) {
			if (importDto.getHeader() == 1 && i == 0) {
				// ????????????????????????
				if (!checkHeader(importDto, importFieldDtoList, csvArray)) {
					// ????????????????????????????????????
					addInvalidHeaderErrorMessage();
					return null;
				}
			} else {
				boolean hasError = false;
				String employeeCode = "";
				HolidayDataDtoInterface dto = new TmdHolidayDataDto();
				for (ImportFieldDtoInterface importFieldDto : importFieldDtoList) {
					int fieldOrder = importFieldDto.getFieldOrder();
					if (csvArray.length > fieldOrder - 1) {
						String value = csvArray[fieldOrder - 1];
						String fieldName = importFieldDto.getFieldName();
						if (PfmHumanDao.COL_EMPLOYEE_CODE.equals(fieldName)) {
							// ???????????????
							employeeCode = value;
						} else if (TmdHolidayDataDao.COL_ACTIVATE_DATE.equals(fieldName)) {
							// ?????????
							Date activateDate = getDate(value);
							if (activateDate == null) {
								hasError = true;
								break;
							}
							dto.setActivateDate(activateDate);
						} else if (TmdHolidayDataDao.COL_HOLIDAY_CODE.equals(fieldName)) {
							// ???????????????
							dto.setHolidayCode(value);
						} else if (TmdHolidayDataDao.COL_HOLIDAY_TYPE.equals(fieldName)) {
							// ????????????
							int holidayType = 0;
							try {
								holidayType = Integer.parseInt(value);
							} catch (NumberFormatException e) {
								hasError = true;
								break;
							}
							if (holidayType != 2 && holidayType != 3 && holidayType != 4) {
								hasError = true;
								break;
							}
							dto.setHolidayType(holidayType);
						} else if (TmdHolidayDataDao.COL_GIVING_DAY.equals(fieldName)) {
							// ????????????
							double givingDay = 0;
							try {
								givingDay = Double.parseDouble(value);
							} catch (NumberFormatException e) {
								hasError = true;
								break;
							}
							if (givingDay < 0) {
								hasError = true;
								break;
							}
							dto.setGivingDay(givingDay);
						} else if (TmdHolidayDataDao.COL_GIVING_HOUR.equals(fieldName)) {
							// ????????????
							int givingHour = 0;
							try {
								givingHour = Integer.parseInt(value);
							} catch (NumberFormatException e) {
								hasError = true;
								break;
							}
							if (givingHour < 0) {
								hasError = true;
								break;
							}
							dto.setGivingHour(givingHour);
						} else if (TmdHolidayDataDao.COL_CANCEL_DAY.equals(fieldName)) {
							// ????????????
							double cancelDay = 0;
							try {
								cancelDay = Double.parseDouble(value);
							} catch (NumberFormatException e) {
								hasError = true;
								break;
							}
							if (cancelDay < 0) {
								hasError = true;
								break;
							}
							dto.setCancelDay(cancelDay);
						} else if (TmdHolidayDataDao.COL_CANCEL_HOUR.equals(fieldName)) {
							// ????????????
							int cancelHour = 0;
							try {
								cancelHour = Integer.parseInt(value);
							} catch (NumberFormatException e) {
								hasError = true;
								break;
							}
							if (cancelHour < 0) {
								hasError = true;
								break;
							}
							dto.setCancelHour(cancelHour);
						} else if (TmdHolidayDataDao.COL_HOLIDAY_LIMIT_DATE.equals(fieldName)) {
							// ????????????
							Date holidayLimitDate = getDate(value);
							if (holidayLimitDate == null) {
								hasError = true;
								break;
							}
							dto.setHolidayLimitDate(holidayLimitDate);
						} else if (TmdHolidayDataDao.COL_HOLIDAY_LIMIT_MONTH.equals(fieldName)) {
							// ????????????(???)
							int holidayLimitMonth = 0;
							try {
								holidayLimitMonth = Integer.parseInt(value);
							} catch (NumberFormatException e) {
								hasError = true;
								break;
							}
							if (holidayLimitMonth < 0) {
								hasError = true;
								break;
							}
							dto.setHolidayLimitMonth(holidayLimitMonth);
						} else if (TmdHolidayDataDao.COL_HOLIDAY_LIMIT_DAY.equals(fieldName)) {
							// ????????????(???)
							int holidayLimitDay = 0;
							try {
								holidayLimitDay = Integer.parseInt(value);
							} catch (NumberFormatException e) {
								hasError = true;
								break;
							}
							if (holidayLimitDay < 0) {
								hasError = true;
								break;
							}
							dto.setHolidayLimitDay(holidayLimitDay);
						}
					}
				}
				if (!hasError && employeeCode.isEmpty()) {
					hasError = true;
				}
				if (!hasError && dto.getActivateDate() == null) {
					hasError = true;
				}
				if (!hasError) {
					HumanDtoInterface humanDto = humanDao.findForEmployeeCode(employeeCode, dto.getActivateDate());
					if (humanDto == null || humanDto.getPersonalId() == null || humanDto.getPersonalId().isEmpty()) {
						hasError = true;
					} else {
						// ??????ID
						dto.setPersonalId(humanDto.getPersonalId());
					}
				}
				if (!hasError) {
					if (dto.getHolidayCode() == null || dto.getHolidayCode().isEmpty()) {
						hasError = true;
					} else {
						HolidayDtoInterface holidayDto = holidayDao.findForInfo(dto.getHolidayCode(),
								dto.getActivateDate(), dto.getHolidayType());
						if (holidayDto == null) {
							hasError = true;
						}
					}
				}
				if (!hasError) {
					if (dto.getHolidayLimitMonth() == 0 && dto.getHolidayLimitDay() == 0) {
						// 0???0??????????????????????????????5874897???12???31????????????
						dto.setHolidayLimitDate(TimeUtility.getUnlimitedDate());
					} else {
						dto.setHolidayLimitDate(DateUtility.addDay(
								DateUtility.addMonth(dto.getActivateDate(), dto.getHolidayLimitMonth()),
								dto.getHolidayLimitDay() - 1));
					}
				}
				if (hasError) {
					addInvalidDataErrorMessage(i);
				} else {
					// ?????????????????????????????????
					for (HolidayDataDtoInterface holidayDataDto : holidayDataList) {
						if (holidayDataDto.getPersonalId().equals(dto.getPersonalId())
								&& holidayDataDto.getActivateDate().equals(dto.getActivateDate())
								&& holidayDataDto.getHolidayCode().equals(dto.getHolidayCode())
								&& holidayDataDto.getHolidayType() == dto.getHolidayType()) {
							addDuplicateDataErrorMessage(i);
							hasError = true;
							break;
						}
					}
				}
				if (!hasError) {
					HolidayDataDtoInterface holidayDataDto = holidayDataDao.findForKey(dto.getPersonalId(),
							dto.getActivateDate(), dto.getHolidayCode(), dto.getHolidayType());
					if (holidayDataDto != null) {
						dto.setTmdHolidayId(holidayDataDto.getTmdHolidayId());
					}
				}
				if (!hasError) {
					holidayDataList.add(dto);
				}
			}
			i++;
		}
		return holidayDataList;
	}
	
	@Override
	public Map<WorkTypeDtoInterface, Map<String, WorkTypeItemDtoInterface>> getWorkType(String importCode,
			List<String[]> list) throws MospException {
		// ???????????? ????????????
		// ???????????????????????????
		ImportDtoInterface importDto = importDao.findForKey(importCode);
		if (importDto == null) {
			// ????????????????????????null?????????
			return null;
		}
		// ??????????????????????????????????????????
		List<ImportFieldDtoInterface> importFieldDtoList = importFieldDao.findForList(importCode);
		if (importFieldDtoList == null || importFieldDtoList.isEmpty()) {
			// ???????????????????????????????????????null????????????????????????
			return null;
		}
		// ???????????????
		Map<WorkTypeDtoInterface, Map<String, WorkTypeItemDtoInterface>> targetMap = new HashMap<WorkTypeDtoInterface, Map<String, WorkTypeItemDtoInterface>>();
		// ????????????????????????
		for (WorkTypeImportAddonBeanInterface addon : getWorkTypeAddonBeans()) {
			// ?????????????????????????????????
			addon.initAddonBean(importDto, importFieldDtoList, list);
		}
		// ????????????????????????????????????????????????????????????
		int i = 0;
		// ???????????????????????????
		for (String[] csvArray : list) {
			if (importDto.getHeader() == 1 && i == 0) {
				// ?????????????????????????????????i???0?????????
				if (!checkHeader(importDto, importFieldDtoList, csvArray)) {
					// ????????????????????????????????????
					addInvalidHeaderErrorMessage();
					return null;
				}
			} else {
				// ?????????????????????
				WorkTypeDtoInterface dto = new TmmWorkTypeDto();
				// ??????????????????????????????
				List<String> workTypeCodeList = new ArrayList<String>();
				// ?????????????????????????????????
				Map<String, WorkTypeItemDtoInterface> itemMap = new HashMap<String, WorkTypeItemDtoInterface>();
				// ????????????
				String year = "";
				String month = "";
				boolean isActivateDate = false;
				// ????????? ???????????????
				dto.setInactivateFlag(0);
				// ??????????????????????????????????????????
				for (ImportFieldDtoInterface importFieldDto : importFieldDtoList) {
					// ?????????????????????????????????????????????
					int fieldOrder = importFieldDto.getFieldOrder();
					if (csvArray.length > fieldOrder - 1) {
						// ??????????????????????????????????????????????????????????????????
						// ?????????????????????????????????????????????
						String value = csvArray[fieldOrder - 1];
						// ??????????????????????????????????????????????????????
						String fieldName = importFieldDto.getFieldName();
						// ?????????????????????????????????
						if (!isWorkTypeInfo(fieldName)) {
							continue;
						}
						// ?????????(????????????)
						if (fieldName.equals(TimeFileConst.ACTIVATE_DATE_YEAR)) {
							// ?????????(???)
							year = value;
						} else if (fieldName.equals(TimeFileConst.ACTIVATE_DATE_MONTH)) {
							// ?????????(???)
							month = value;
						}
						if (!isActivateDate && !year.isEmpty() && !month.isEmpty()) {
							// ???????????????
							String[] rep = { "?????????(???)", "?????????(???)", "" };
							InputCheckUtility.checkDateGeneral(mospParams, year, month, "1", rep);
							if (mospParams.hasErrorMessage()) {
								return null;
							}
							Date activateDate = MonthUtility.getYearMonthDate(Integer.parseInt(year),
									Integer.parseInt(month));
							dto.setActivateDate(activateDate);
							isActivateDate = true;
							continue;
						}
						// ????????????????????????
						setWorkTypeDto(dto, fieldName, value);
					}
				}
				// ???????????????????????????????????????????????????????????????
				if (workTypeCodeList.contains(dto.getWorkTypeCode())) {
					// ???????????????
					addDuplicateDataErrorMessage(i);
					return null;
				} else {
					// ???????????????????????????
					workTypeCodeList.add(dto.getWorkTypeCode());
				}
				if (mospParams.hasErrorMessage()) {
					return null;
				}
				// ??????????????????????????????????????????
				for (ImportFieldDtoInterface importFieldDto : importFieldDtoList) {
					// ?????????????????????????????????????????????
					int fieldOrder = importFieldDto.getFieldOrder();
					if (csvArray.length > fieldOrder - 1) {
						// ???????????????????????????????????????????????????????????????????????????????????????????????????????????????
						String value = csvArray[fieldOrder - 1];
						// ??????????????????????????????????????????????????????
						String fieldName = importFieldDto.getFieldName();
						// ???????????????????????????????????????
						if (!isWorkTypeItemInfo(fieldName)) {
							continue;
						}
						// ?????????????????????????????????
						WorkTypeItemDtoInterface itemDto = setWorktypeItemDto(fieldName, fieldOrder, value, dto, i);
						// ??????????????????????????????
						if (itemDto == null) {
							return null;
						}
						// ????????????(????????????)?????????????????????
						if (isWorkTypeItemShort(itemDto)) {
							// ????????????(????????????)????????????????????????
							itemDto = setWorkTypeItemShort(fieldName, value, itemMap, itemDto);
						}
						// ?????????????????????
						itemMap.put(itemDto.getWorkTypeItemCode(), itemDto);
					}
				}
				// ??????????????????
				itemMap.put(TimeConst.CODE_RESTTIME, getWoryTypeRestTimedto(dto, itemMap));
				// ??????????????????
				itemMap.put(TimeConst.CODE_WORKTIME, getWoryTypeWorkTimedto(dto, itemMap));
				// ???????????????
				targetMap.put(dto, itemMap);
			}
			
			i++;
		}
		
		return targetMap;
	}
	
	/**
	 * ???????????????????????????????????????????????????<br>
	 * @param workTypeDto ???????????????????????????
	 * @param itemMap ???????????????
	 * @return ?????????????????????????????????
	 * @throws MospException ???????????????????????????????????????SQL???????????????????????????
	 */
	protected WorkTypeItemDtoInterface getWoryTypeRestTimedto(WorkTypeDtoInterface workTypeDto,
			Map<String, WorkTypeItemDtoInterface> itemMap) throws MospException {
		// ?????????????????????
		WorkTypeItemDtoInterface rest1StartDto = itemMap.get(TimeConst.CODE_RESTSTART1);
		WorkTypeItemDtoInterface rest1EndDto = itemMap.get(TimeConst.CODE_RESTEND1);
		WorkTypeItemDtoInterface rest2StartDto = itemMap.get(TimeConst.CODE_RESTSTART2);
		WorkTypeItemDtoInterface rest2EndDto = itemMap.get(TimeConst.CODE_RESTEND2);
		WorkTypeItemDtoInterface rest3StartDto = itemMap.get(TimeConst.CODE_RESTSTART3);
		WorkTypeItemDtoInterface rest3EndDto = itemMap.get(TimeConst.CODE_RESTEND3);
		WorkTypeItemDtoInterface rest4StartDto = itemMap.get(TimeConst.CODE_RESTSTART4);
		WorkTypeItemDtoInterface rest4EndDto = itemMap.get(TimeConst.CODE_RESTEND4);
		// ?????????????????????
		int rest1 = 0;
		if (rest1StartDto != null && rest1EndDto != null) {
			rest1 = TimeUtility.getDifferenceMinutes(rest1StartDto.getWorkTypeItemValue(),
					rest1EndDto.getWorkTypeItemValue());
		}
		int rest2 = 0;
		if (rest2StartDto != null && rest2EndDto != null) {
			rest2 = TimeUtility.getDifferenceMinutes(rest2StartDto.getWorkTypeItemValue(),
					rest2EndDto.getWorkTypeItemValue());
		}
		int rest3 = 0;
		if (rest3StartDto != null && rest3EndDto != null) {
			rest3 = TimeUtility.getDifferenceMinutes(rest3StartDto.getWorkTypeItemValue(),
					rest3EndDto.getWorkTypeItemValue());
		}
		int rest4 = 0;
		if (rest4StartDto != null && rest4EndDto != null) {
			rest4 = TimeUtility.getDifferenceMinutes(rest4StartDto.getWorkTypeItemValue(),
					rest4EndDto.getWorkTypeItemValue());
		}
		// ??????????????????
		int rest = workTypeItemRefer.getRestTime(rest1, rest2, rest3, rest4);
		// DTO??????
		WorkTypeItemDtoInterface itemDto = workTypeItemRegist.getInitDto();
		// ???????????????????????????
		itemDto.setWorkTypeCode(workTypeDto.getWorkTypeCode());
		itemDto.setActivateDate(workTypeDto.getActivateDate());
		itemDto.setInactivateFlag(workTypeDto.getInactivateFlag());
		itemDto.setWorkTypeItemCode(TimeConst.CODE_RESTTIME);
		itemDto.setWorkTypeItemValue(DateUtility.addMinute(DateUtility.getDefaultTime(), rest));
		itemDto.setPreliminary("");
		
		return itemDto;
	}
	
	/**
	 * ???????????????????????????????????????????????????<br>
	 * @param workTypeDto ?????????????????????
	 * @param itemMap ???????????????
	 * @return ?????????????????????????????????
	 * @throws MospException ???????????????????????????????????????SQL???????????????????????????
	 */
	protected WorkTypeItemDtoInterface getWoryTypeWorkTimedto(WorkTypeDtoInterface workTypeDto,
			Map<String, WorkTypeItemDtoInterface> itemMap) throws MospException {
		WorkTypeItemDtoInterface startDto = itemMap.get(TimeConst.CODE_WORKSTART);
		WorkTypeItemDtoInterface endDto = itemMap.get(TimeConst.CODE_WORKEND);
		WorkTypeItemDtoInterface restDto = itemMap.get(TimeConst.CODE_RESTTIME);
		// ???????????????????????????
		Date startTime = startDto != null ? startDto.getWorkTypeItemValue() : null;
		// ???????????????????????????
		Date endTime = endDto != null ? endDto.getWorkTypeItemValue() : null;
		// ???????????????????????????
		int restTime = 0;
		if (restDto != null) {
			restTime = TimeUtility.getDifferenceMinutes(DateUtility.getDefaultTime(), restDto.getWorkTypeItemValue());
		}
		// ??????????????????????????????
		WorkTypeItemDtoInterface itemDto = workTypeItemRegist.getInitDto();
		// ???????????????????????????
		itemDto.setWorkTypeCode(workTypeDto.getWorkTypeCode());
		itemDto.setActivateDate(workTypeDto.getActivateDate());
		itemDto.setInactivateFlag(workTypeDto.getInactivateFlag());
		itemDto.setWorkTypeItemCode(TimeConst.CODE_WORKTIME);
		itemDto.setWorkTypeItemValue(DateUtility.addMinute(DateUtility.getDefaultTime(),
				workTypeItemRefer.getWorkTime(startTime, endTime, restTime)));
		itemDto.setPreliminary("");
		return itemDto;
	}
	
	/**
	 * ????????????????????????????????????<br>
	 * @param dto ??????????????????
	 * @param fieldName ?????????
	 * @param value ?????????
	 * @throws MospException ???????????????????????????????????????SQL???????????????????????????
	 */
	protected void setWorkTypeDto(WorkTypeDtoInterface dto, String fieldName, String value) throws MospException {
		// ?????????????????????
		if (fieldName.equals(TmmWorkTypeDao.COL_WORK_TYPE_CODE)) {
			// ?????????????????????
			dto.setWorkTypeCode(value);
		} else if (fieldName.equals(TmmWorkTypeDao.COL_WORK_TYPE_NAME)) {
			// ??????????????????(????????????)
			dto.setWorkTypeName(value);
		} else if (fieldName.equals(TmmWorkTypeDao.COL_WORK_TYPE_ABBR)) {
			// ??????????????????(????????????)
			dto.setWorkTypeAbbr(value);
		} else if (fieldName.equals(TmmWorkTypeDao.COL_INACTIVATE_FLAG)) {
			// ??????/????????????
			checkRequired(value, getCodeName(fieldName, TimeFileConst.CODE_IMPORT_TYPE_TMD_WORK_TYPE), null);
			if (mospParams.hasErrorMessage()) {
				return;
			}
			checkTypeNumber(value, getCodeName(fieldName, TimeFileConst.CODE_IMPORT_TYPE_TMD_WORK_TYPE), null);
			if (mospParams.hasErrorMessage()) {
				return;
			}
			checkFlag(getInteger(value), getCodeName(fieldName, TimeFileConst.CODE_IMPORT_TYPE_TMD_WORK_TYPE), null);
			if (mospParams.hasErrorMessage()) {
				return;
			}
			dto.setInactivateFlag(MospUtility.getInt(value));
		}
	}
	
	/**
	 * ??????????????????????????????????????????????????????<br>
	 * @param fieldName ?????????
	 * @param i ??????????????????
	 * @param value ?????????
	 * @param worktypeDto ??????????????????
	 * @param index ????????????
	 * @return ????????????????????????
	 * @throws MospException ???????????????????????????????????????SQL???????????????????????????
	 */
	protected WorkTypeItemDtoInterface setWorktypeItemDto(String fieldName, Integer i, String value,
			WorkTypeDtoInterface worktypeDto, int index) throws MospException {
		// ??????????????????????????????
		WorkTypeItemDtoInterface dto = workTypeItemRegist.getInitDto();
		// ???????????????????????????
		dto.setActivateDate(worktypeDto.getActivateDate());
		dto.setWorkTypeCode(worktypeDto.getWorkTypeCode());
		dto.setWorkTypeItemValue(DateUtility.getDefaultTime());
		dto.setPreliminary("");
		dto.setInactivateFlag(worktypeDto.getInactivateFlag());
		if (fieldName.equals(TimeFileConst.WORK_START_TIME)) {
			// ??????????????????
			dto.setWorkTypeItemCode(TimeConst.CODE_WORKSTART);
			dto.setWorkTypeItemValue(getDefaltTimestamp(value, fieldName));
			return dto;
		} else if (fieldName.equals(TimeFileConst.WORK_END_TIME)) {
			// ??????????????????
			dto.setWorkTypeItemCode(TimeConst.CODE_WORKEND);
			dto.setWorkTypeItemValue(getDefaltTimestamp(value, fieldName));
			return dto;
		} else if (fieldName.equals(TimeFileConst.REST1_START_TIME)) {
			// ??????1(????????????)
			dto.setWorkTypeItemCode(TimeConst.CODE_RESTSTART1);
			dto.setWorkTypeItemValue(getDefaltTimestamp(value, fieldName));
			return dto;
		} else if (fieldName.equals(TimeFileConst.REST1_END_TIME)) {
			// ??????1(????????????)
			dto.setWorkTypeItemCode(TimeConst.CODE_RESTEND1);
			dto.setWorkTypeItemValue(getDefaltTimestamp(value, fieldName));
			return dto;
		} else if (fieldName.equals(TimeFileConst.REST2_START_TIME)) {
			// ??????2(????????????)
			dto.setWorkTypeItemCode(TimeConst.CODE_RESTSTART2);
			dto.setWorkTypeItemValue(getDefaltTimestamp(value, fieldName));
			return dto;
		} else if (fieldName.equals(TimeFileConst.REST2_END_TIME)) {
			// ??????2(????????????)
			dto.setWorkTypeItemCode(TimeConst.CODE_RESTEND2);
			dto.setWorkTypeItemValue(getDefaltTimestamp(value, fieldName));
			return dto;
		} else if (fieldName.equals(TimeFileConst.REST3_START_TIME)) {
			// ??????3(????????????)
			dto.setWorkTypeItemCode(TimeConst.CODE_RESTSTART3);
			dto.setWorkTypeItemValue(getDefaltTimestamp(value, fieldName));
			return dto;
		} else if (fieldName.equals(TimeFileConst.REST3_END_TIME)) {
			// ??????3(????????????)
			dto.setWorkTypeItemCode(TimeConst.CODE_RESTEND3);
			dto.setWorkTypeItemValue(getDefaltTimestamp(value, fieldName));
			return dto;
		} else if (fieldName.equals(TimeFileConst.REST4_START_TIME)) {
			// ??????4(????????????)
			dto.setWorkTypeItemCode(TimeConst.CODE_RESTSTART4);
			dto.setWorkTypeItemValue(getDefaltTimestamp(value, fieldName));
			return dto;
		} else if (fieldName.equals(TimeFileConst.REST4_END_TIME)) {
			// ??????4(????????????)
			dto.setWorkTypeItemCode(TimeConst.CODE_RESTEND4);
			dto.setWorkTypeItemValue(getDefaltTimestamp(value, fieldName));
			return dto;
		} else if (fieldName.equals(TimeFileConst.FRONT_START_TIME)) {
			// ?????????(????????????)(????????????)
			dto.setWorkTypeItemCode(TimeConst.CODE_FRONTSTART);
			dto.setWorkTypeItemValue(getDefaltTimestamp(value, fieldName));
			return dto;
		} else if (fieldName.equals(TimeFileConst.FRONT_END_TIME)) {
			// ?????????(????????????)(????????????)
			dto.setWorkTypeItemCode(TimeConst.CODE_FRONTEND);
			dto.setWorkTypeItemValue(getDefaltTimestamp(value, fieldName));
			return dto;
		} else if (fieldName.equals(TimeFileConst.BACK_START_TIME)) {
			// ?????????(????????????)(????????????)
			dto.setWorkTypeItemCode(TimeConst.CODE_BACKSTART);
			dto.setWorkTypeItemValue(getDefaltTimestamp(value, fieldName));
			return dto;
		} else if (fieldName.equals(TimeFileConst.BACK_END_TIME)) {
			// ?????????(????????????)(????????????)
			dto.setWorkTypeItemCode(TimeConst.CODE_BACKEND);
			dto.setWorkTypeItemValue(getDefaltTimestamp(value, fieldName));
			return dto;
		} else if (fieldName.equals(TimeFileConst.OVER_PER)) {
			// ??????????????????[???](????????????)
			dto.setWorkTypeItemCode(TimeConst.CODE_OVERPER);
			dto.setWorkTypeItemValue(getDefaltTimestamp(value, fieldName));
			return dto;
		} else if (fieldName.equals(TimeFileConst.OVER_REST)) {
			// ??????????????????(????????????)
			dto.setWorkTypeItemCode(TimeConst.CODE_OVERREST);
			dto.setWorkTypeItemValue(getDefaltTimestamp(value, fieldName));
			return dto;
		} else if (fieldName.equals(TimeFileConst.OVER_BEFORE)) {
			// ????????????(??????)(????????????)
			dto.setWorkTypeItemCode(TimeConst.CODE_OVERBEFORE);
			dto.setWorkTypeItemValue(getDefaltTimestamp(value, fieldName));
			return dto;
		} else if (fieldName.equals(TimeFileConst.BEFORE_OVERTIME)) {
			// ?????????????????????????????????
			if (value == null || value.isEmpty()) {
				// ?????????????????????????????????
				value = String.valueOf(MospConst.INACTIVATE_FLAG_ON);
			}
			checkRequired(value, getCodeName(fieldName, TimeFileConst.CODE_IMPORT_TYPE_TMD_WORK_TYPE), index);
			if (mospParams.hasErrorMessage()) {
				return null;
			}
			checkTypeNumber(value, getCodeName(fieldName, TimeFileConst.CODE_IMPORT_TYPE_TMD_WORK_TYPE), index);
			if (mospParams.hasErrorMessage()) {
				return null;
			}
			checkFlag(getInteger(value), getCodeName(fieldName, TimeFileConst.CODE_IMPORT_TYPE_TMD_WORK_TYPE), index);
			if (mospParams.hasErrorMessage()) {
				return null;
			}
			dto.setWorkTypeItemCode(TimeConst.CODE_AUTO_BEFORE_OVERWORK);
			dto.setPreliminary(value);
			return dto;
		} else if (fieldName.equals(TimeFileConst.HALF_REST_WORK_TIME)) {
			// ??????????????????(?????????????????????????????????)
			dto.setWorkTypeItemCode(TimeConst.CODE_HALFREST);
			dto.setWorkTypeItemValue(getDefaltTimestamp(value, fieldName));
			return dto;
		} else if (fieldName.equals(TimeFileConst.HALF_REST_START_TIME)) {
			// ??????????????????(??????????????????)
			dto.setWorkTypeItemCode(TimeConst.CODE_HALFRESTSTART);
			dto.setWorkTypeItemValue(getDefaltTimestamp(value, fieldName));
			return dto;
		} else if (fieldName.equals(TimeFileConst.HALF_REST_END_TIME)) {
			// ??????????????????(??????????????????)
			dto.setWorkTypeItemCode(TimeConst.CODE_HALFRESTEND);
			dto.setWorkTypeItemValue(getDefaltTimestamp(value, fieldName));
			return dto;
		} else if (fieldName.equals(TimeFileConst.DIRECT_START)) {
			// ??????
			if (value == null || value.isEmpty()) {
				// ??????????????????????????????????????????????????????(OFF)??????
				value = String.valueOf(MospConst.CHECKBOX_OFF);
			}
			checkTypeNumber(value, getCodeName(fieldName, TimeFileConst.CODE_IMPORT_TYPE_TMD_WORK_TYPE), index);
			if (mospParams.hasErrorMessage()) {
				return null;
			}
			checkFlag(getInteger(value), getCodeName(fieldName, TimeFileConst.CODE_IMPORT_TYPE_TMD_WORK_TYPE), index);
			if (mospParams.hasErrorMessage()) {
				return null;
			}
			dto.setWorkTypeItemCode(TimeConst.CODE_WORK_TYPE_ITEM_DIRECT_START);
			dto.setPreliminary(value);
			return dto;
		} else if (fieldName.equals(TimeFileConst.DIRECT_END)) {
			// ??????
			checkTypeNumber(value, getCodeName(fieldName, TimeFileConst.CODE_IMPORT_TYPE_TMD_WORK_TYPE), index);
			if (mospParams.hasErrorMessage()) {
				return null;
			}
			if (MospUtility.isEmpty(value)) {
				// ??????????????????????????????????????????????????????(OFF)??????
				value = String.valueOf(MospConst.CHECKBOX_OFF);
			}
			checkFlag(getInteger(value), getCodeName(fieldName, TimeFileConst.CODE_IMPORT_TYPE_TMD_WORK_TYPE), index);
			if (mospParams.hasErrorMessage()) {
				return null;
			}
			dto.setWorkTypeItemCode(TimeConst.CODE_WORK_TYPE_ITEM_DIRECT_END);
			dto.setPreliminary(value);
			return dto;
		} else if (fieldName.equals(TimeFileConst.EXCLUDE_NIGHT_REST)) {
			// ??????????????????
			if (value == null || value.isEmpty()) {
				// ????????????????????????????????????
				value = String.valueOf(MospConst.INACTIVATE_FLAG_ON);
			}
			checkTypeNumber(value, getCodeName(fieldName, TimeFileConst.CODE_IMPORT_TYPE_TMD_WORK_TYPE), index);
			if (mospParams.hasErrorMessage()) {
				return null;
			}
			checkFlag(getInteger(value), getCodeName(fieldName, TimeFileConst.CODE_IMPORT_TYPE_TMD_WORK_TYPE), index);
			if (mospParams.hasErrorMessage()) {
				return null;
			}
			dto.setWorkTypeItemCode(TimeConst.CODE_WORK_TYPE_ITEM_EXCLUDE_NIGHT_REST);
			dto.setPreliminary(value);
			return dto;
		} else if (fieldName.equals(TimeFileConst.SHORT1_START)) {
			// ????????????1(????????????)
			dto.setWorkTypeItemCode(TimeConst.CODE_WORK_TYPE_ITEM_SHORT1_START);
			dto.setWorkTypeItemValue(getDefaltTimestamp(value, fieldName));
			return dto;
		} else if (fieldName.equals(TimeFileConst.SHORT1_END)) {
			// ????????????1(????????????)
			dto.setWorkTypeItemCode(TimeConst.CODE_WORK_TYPE_ITEM_SHORT1_END);
			dto.setWorkTypeItemValue(getDefaltTimestamp(value, fieldName));
			return dto;
		} else if (fieldName.equals(TimeFileConst.SHORT1_TYPE)) {
			// ????????????1(??????)
			if (value == null || value.isEmpty()) {
				// ???????????????????????????(??????)?????????
				value = WorkTypeEntity.CODE_PAY_TYPE_PAY;
			}
			checkTypeNumber(value, getCodeName(fieldName, TimeFileConst.CODE_IMPORT_TYPE_TMD_WORK_TYPE), index);
			if (mospParams.hasErrorMessage()) {
				return null;
			}
			checkFlag(getInteger(value), getCodeName(fieldName, TimeFileConst.CODE_IMPORT_TYPE_TMD_WORK_TYPE), index);
			if (mospParams.hasErrorMessage()) {
				return null;
			}
			dto.setWorkTypeItemCode(TimeConst.CODE_WORK_TYPE_ITEM_SHORT1_START);
			dto.setPreliminary(value);
			return dto;
		} else if (fieldName.equals(TimeFileConst.SHORT2_START)) {
			// ????????????2(????????????)
			dto.setWorkTypeItemCode(TimeConst.CODE_WORK_TYPE_ITEM_SHORT2_START);
			dto.setWorkTypeItemValue(getDefaltTimestamp(value, fieldName));
			return dto;
		} else if (fieldName.equals(TimeFileConst.SHORT2_END)) {
			// ????????????2(????????????)
			dto.setWorkTypeItemCode(TimeConst.CODE_WORK_TYPE_ITEM_SHORT2_END);
			dto.setWorkTypeItemValue(getDefaltTimestamp(value, fieldName));
			return dto;
		} else if (fieldName.equals(TimeFileConst.SHORT2_TYPE)) {
			// ????????????2(??????)
			if (value == null || value.isEmpty()) {
				// ???????????????????????????(??????)?????????
				value = WorkTypeEntity.CODE_PAY_TYPE_PAY;
			}
			checkTypeNumber(value, getCodeName(fieldName, TimeFileConst.CODE_IMPORT_TYPE_TMD_WORK_TYPE), index);
			if (mospParams.hasErrorMessage()) {
				return null;
			}
			checkFlag(getInteger(value), getCodeName(fieldName, TimeFileConst.CODE_IMPORT_TYPE_TMD_WORK_TYPE), index);
			if (mospParams.hasErrorMessage()) {
				return null;
			}
			dto.setWorkTypeItemCode(TimeConst.CODE_WORK_TYPE_ITEM_SHORT2_START);
			dto.setPreliminary(value);
			return dto;
		} else {
			// ????????????????????????
			for (WorkTypeImportAddonBeanInterface addon : getWorkTypeAddonBeans()) {
				// ?????????????????????????????????????????????
				dto = addon.setWorktypeItemDto(fieldName, i, value, worktypeDto, index);
				// ?????????????????????
				if (dto != null) {
					// ??????????????????????????????
					return dto;
				}
			}
		}
		// ????????????????????????????????????
		addInvalidDataErrorMessage(index);
		return null;
	}
	
	/**
	 * ????????????(????????????)??????????????????<BR>
	 * @param itemMap ????????????????????????
	 * @param dto ??????????????????
	 * @param fieldName ?????????
	 * @param value ?????????
	 * @return ????????????????????????
	 * @throws MospException ???????????????????????????????????????SQL???????????????????????????
	 */
	protected WorkTypeItemDtoInterface setWorkTypeItemShort(String fieldName, String value,
			Map<String, WorkTypeItemDtoInterface> itemMap, WorkTypeItemDtoInterface dto) throws MospException {
		// ??????????????????????????????????????????????????????????????????????????????
		if (itemMap.get(dto.getWorkTypeItemCode()) == null) {
			// ??????????????????????????????????????????
			return dto;
		}
		// ????????????????????????????????????????????????????????????
		dto = itemMap.get(dto.getWorkTypeItemCode());
		// ????????????(????????????)?????????
		if (fieldName.equals(TimeFileConst.SHORT1_START) || fieldName.equals(TimeFileConst.SHORT2_START)) {
			// ??????????????????
			dto.setWorkTypeItemValue(getDefaltTimestamp(value, fieldName));
		}
		// ????????????(??????)?????????
		if (fieldName.equals(TimeFileConst.SHORT1_TYPE) || fieldName.equals(TimeFileConst.SHORT2_TYPE)) {
			// ?????????(??????)?????????
			dto.setPreliminary(value);
		}
		// ??????????????????????????????????????????????????????????????????????????????
		return dto;
	}
	
	/**
	 * ??????????????????????????????<br>
	 * @param dto ????????????????????????
	 * @return ???????????????true????????????false?????????????????????
	 * @throws MospException ???????????????????????????????????????SQL???????????????????????????
	 */
	protected boolean isWorkTypeItemShort(WorkTypeItemDtoInterface dto) throws MospException {
		if (dto.getWorkTypeItemCode().equals(TimeConst.CODE_WORK_TYPE_ITEM_SHORT1_START)
				|| dto.getWorkTypeItemCode().equals(TimeConst.CODE_WORK_TYPE_ITEM_SHORT2_START)) {
			// ????????????????????????
			return true;
		}
		return false;
	}
	
	/**
	 * ??????????????????????????????????????????<br>
	 * @param fieldName ?????????
	 * @return ???????????????true????????????false?????????????????????
	 */
	protected boolean isWorkTypeInfo(String fieldName) {
		String[] item = { TimeFileConst.ACTIVATE_DATE_YEAR, TimeFileConst.ACTIVATE_DATE_MONTH,
			TmmWorkTypeDao.COL_WORK_TYPE_CODE, TmmWorkTypeDao.COL_WORK_TYPE_NAME, TmmWorkTypeDao.COL_WORK_TYPE_ABBR,
			TmmWorkTypeDao.COL_INACTIVATE_FLAG };
		List<String> list = Arrays.asList(item);
		return list.contains(fieldName);
	}
	
	/**
	 * ??????????????????????????????????????????<br>
	 * @param fieldName ?????????
	 * @return ???????????????true????????????false?????????????????????
	 * @throws MospException ??????????????????????????????
	 */
	protected boolean isWorkTypeItemInfo(String fieldName) throws MospException {
		String[] item = { TimeFileConst.WORK_START_TIME, TimeFileConst.WORK_END_TIME, TimeFileConst.REST1_START_TIME,
			TimeFileConst.REST1_END_TIME, TimeFileConst.REST2_START_TIME, TimeFileConst.REST2_END_TIME,
			TimeFileConst.REST3_START_TIME, TimeFileConst.REST3_END_TIME, TimeFileConst.REST4_START_TIME,
			TimeFileConst.REST4_END_TIME, TimeFileConst.FRONT_START_TIME, TimeFileConst.FRONT_END_TIME,
			TimeFileConst.BACK_START_TIME, TimeFileConst.BACK_END_TIME, TimeFileConst.OVER_PER, TimeFileConst.OVER_REST,
			TimeFileConst.OVER_BEFORE, TimeFileConst.BEFORE_OVERTIME, TimeFileConst.HALF_REST_WORK_TIME,
			TimeFileConst.HALF_REST_START_TIME, TimeFileConst.HALF_REST_END_TIME, TimeFileConst.DIRECT_START,
			TimeFileConst.DIRECT_END, TimeFileConst.EXCLUDE_NIGHT_REST, TimeFileConst.SHORT1_START,
			TimeFileConst.SHORT1_TYPE, TimeFileConst.SHORT1_END, TimeFileConst.SHORT2_START, TimeFileConst.SHORT2_TYPE,
			TimeFileConst.SHORT2_END };
		List<String> list = Arrays.asList(item);
		
		boolean containFlag = list.contains(fieldName);
		if (containFlag == false) {
			for (WorkTypeImportAddonBeanInterface addonBean : getWorkTypeAddonBeans()) {
				containFlag = addonBean.isWorkTypeItemInfo(fieldName);
				if (containFlag) {
					break;
				}
			}
		}
		return containFlag;
	}
	
	/**
	 * ??????????????????????????????????????????????????????????????????<br>
	 * @return ????????????????????????????????????????????????
	 * @throws MospException Bean???????????????????????????????????????????????????????????????
	 */
	protected List<WorkTypeImportAddonBeanInterface> getWorkTypeAddonBeans() throws MospException {
		// ??????????????????????????????????????????
		if (workTypeAddonBeans == null) {
			// ??????????????????????????????
			workTypeAddonBeans = new ArrayList<WorkTypeImportAddonBeanInterface>();
			// ??????????????????????????????
			for (String[] addon : mospParams.getProperties().getCodeArray(CODE_KEY_WORKTYPE_ADDONS, false)) {
				// ?????????????????????????????????????????????
				if (addon == null || MospUtility.isEmpty(addon)) {
					// ?????????????????????
					continue;
				}
				// ?????????????????????
				String addonBean = addon[0];
				// ?????????????????????????????????????????????
				if (addonBean == null || addonBean.isEmpty()) {
					// ?????????????????????
					continue;
				}
				// ?????????????????????
				WorkTypeImportAddonBeanInterface bean = (WorkTypeImportAddonBeanInterface)createBean(addonBean);
				// ??????????????????????????????
				workTypeAddonBeans.add(bean);
			}
		}
		// ?????????????????????????????????????????????????????????
		return workTypeAddonBeans;
	}
	
	/**
	 * ????????????????????????
	 * @param importDto ???????????????DTO
	 * @param list ??????????????????????????????DTO
	 * @param array ???????????????
	 * @return true?????????????????????false??????????????????
	 */
	protected boolean checkHeader(ImportDtoInterface importDto, List<ImportFieldDtoInterface> list, String[] array) {
		if (importDto == null) {
			return false;
		}
		if (list == null || list.isEmpty()) {
			return false;
		}
		String[] headerArray = new String[list.size()];
		int i = 0;
		for (ImportFieldDtoInterface importFieldDto : list) {
			headerArray[i] = mospParams.getProperties().getCodeItemName(importDto.getImportTable(),
					importFieldDto.getFieldName());
			i++;
		}
		if (Arrays.equals(headerArray, array)) {
			return true;
		} else {
			addHeaderErrorMessage(headerArray, array);
			return false;
		}
	}
	
	/**
	 * ????????????????????????????????????????????????????????????Date??????????????????
	 * @param date ?????????????????????
	 * @return ??????????????????
	 */
	protected Date getDate(String date) {
		if (date.indexOf("/") == -1) {
			return DateUtility.getDate(date, "yyyyMMdd");
		}
		return DateUtility.getDate(date);
	}
	
	/**
	 * ??????????????????????????????????????????????????????????????????Date??????????????????
	 * @param timestamp ???????????????????????????
	 * @return ??????????????????
	 */
	protected Date getTimestamp(String timestamp) {
		if (timestamp.indexOf("/") == -1) {
			return DateUtility.getDate(timestamp, "yyyyMMdd H:m");
		}
		return DateUtility.getDate(timestamp, "y/M/d H:m");
	}
	
	/**
	 * ???????????????????????????????????????????????????????????????
	 * ??????????????????????????????????????????????????????????????????Date??????????????????
	 * @param timestamp ????????????????????????HH:MM???
	 * @param fieldName ?????????
	 * @return ??????????????????
	 * @throws MospException ???????????????????????????????????????SQL???????????????????????????
	 */
	protected Date getDefaltTimestamp(String timestamp, String fieldName) throws MospException {
		if (timestamp.isEmpty()) {
			timestamp = "0:0";
		}
		// ????????????????????????
		String[] rep = { getCodeName(fieldName, TimeFileConst.CODE_IMPORT_TYPE_TMD_WORK_TYPE), "HH:MM" };
		String[] target = MospUtility.split(timestamp, ":");
		if (target.length != 2) {
			mospParams.addErrorMessage(PlatformMessageConst.MSG_INPUT_DATE, rep);
			return null;
		}
		try {
			Integer.parseInt(target[0]);
			Integer.parseInt(target[1]);
		} catch (NumberFormatException e) {
			// int??????????????????????????????????????????
			mospParams.addErrorMessage(PlatformMessageConst.MSG_INPUT_DATE, rep);
			return null;
		}
		if (Integer.parseInt(target[0]) > 47) {
			mospParams.addErrorMessage("PFW0129", getCodeName(fieldName, TimeFileConst.CODE_IMPORT_TYPE_TMD_WORK_TYPE),
					"47");
			return null;
		}
		if (Integer.parseInt(target[1]) > 59) {
			mospParams.addErrorMessage("PFW0129", getCodeName(fieldName, TimeFileConst.CODE_IMPORT_TYPE_TMD_WORK_TYPE),
					"59");
			return null;
		}
		return workTypeItemRegist.getDefaultTime(target[0], target[1]);
	}
	
	/**
	 * ????????????????????????Date??????????????????<br>
	 * @param time ?????????????????????
	 * @return ?????????????????????????????????????????????null
	 * @throws MospException ?????????
	 */
	protected Date checkTime(String time) throws MospException {
		if (time.isEmpty()) {
			// ???????????????
			// ??????(???????????????)??????
			return DateUtility.getDefaultTime();
		}
		if (time.indexOf(":") != -1) {
			// :??????????????????????????????
			String[] times = time.split(":", 0);
			try {
				Integer.parseInt(times[0]);
				Integer.parseInt(times[1]);
			} catch (NumberFormatException e) {
				// int??????????????????????????????????????????
				return null;
			}
			// ????????????
			return DateUtility.getTime(times[0], times[1]);
		}
		// ?????????????????????
		return null;
	}
	
	/**
	 * ??????????????????????????????Date??????????????????<br>
	 * @param time ?????????????????????
	 * @param name ?????????
	 * @return ??????????????????????????????????????????????????????null
	 * @throws MospException ?????????
	 */
	protected Date checkRequiredTime(String time, String name) throws MospException {
		if (time.indexOf(":") != -1) {
			// :??????????????????????????????
			String[] times = time.split(":", 0);
			try {
				Integer.parseInt(times[0]);
				Integer.parseInt(times[1]);
			} catch (NumberFormatException e) {
				// int??????????????????????????????????????????
				// ??????????????????????????????
				addIntCastErrorMessage(name);
				return null;
			}
			// ????????????
			return DateUtility.getTime(times[0], times[1]);
		}
		// ?????????????????????
		// ??????????????????????????????
		addInjusticeDataErrorMessage(name);
		return null;
	}
	
	/**
	 * int?????????????????????????????????<br>
	 * @param value ??????????????????
	 * @param name ?????????
	 * @param flag true:?????????????????????????????????false:??????????????????????????????
	 * @return ????????????????????? ????????????????????????????????????-1
	 */
	protected int checkIntCast(String value, String name, boolean flag) {
		int num = 0;
		if (value.isEmpty()) {
			return -2;
		}
		try {
			num = Integer.parseInt(value);
		} catch (NumberFormatException e) {
			// int??????????????????????????????????????????
			if (flag) {
				// ??????????????????????????????
				addIntCastErrorMessage(name);
			}
			return -1;
		}
		
		return num;
	}
	
	/**
	 * ??????????????????????????????????????????????????????
	 */
	protected void addInvalidHeaderErrorMessage() {
		mospParams.addErrorMessage(TimeMessageConst.MSG_FORM_INJUSTICE, mospParams.getName("Header"));
	}
	
	/**
	 * ??????????????????????????????????????????????????????(??????)???<br>
	 * @param header1 ???????????????
	 * @param header2 ?????????????????????
	 * ????????????????????????:????????????[]???????????????????????????????????????
	 */
	protected void addHeaderErrorMessage(String[] header1, String[] header2) {
		if (header1.length != header2.length) {
			return;
		}
		for (int i = 0; i < header2.length; i++) {
			if (!header2[i].equals(header1[i])) {
				String rep = mospParams.getName("Header") + "???" + header1[i];
				addNotInputErrorMessage(rep);
				break;
			}
		}
	}
	
	/**
	 * ???????????????????????????????????????????????????
	 * @param i ?????????????????????
	 */
	protected void addInvalidDataErrorMessage(int i) {
		String rep = ++i + mospParams.getName("TheLine", "Of", "Data");
		mospParams.addErrorMessage(TimeMessageConst.MSG_FORM_INJUSTICE, rep);
	}
	
	/**
	 * ????????????????????????????????????
	 * @param i ?????????????????????
	 */
	protected void addDuplicateDataErrorMessage(int i) {
		String rep = ++i + mospParams.getName("TheLine", "Of", "Data");
		mospParams.addErrorMessage(TimeMessageConst.MSG_FILE_REPETITION, rep);
	}
	
	/**
	 * ??????????????????????????????????????????
	 * @param i ?????????????????????
	 */
	protected void addAlreadyRegisteredDataErrorMessage(int i) {
		String rep = ++i + mospParams.getName("TheLine", "Of", "Data");
		mospParams.addErrorMessage(TimeMessageConst.MSG_ALREADY_EXIST, rep);
	}
	
	/**
	 * ???????????????????????????????????????????????????(??????)???<br>
	 * @param name ?????????
	 * ????????????????????????:[?????????]?????????????????????????????????????????????????????????
	 */
	protected void addInjusticeDataErrorMessage(String name) {
		mospParams.addErrorMessage(PlatformMessageConst.MSG_INPUT_FORM_AMP, name);
	}
	
	/**
	 * int????????????????????????????????????????????????<br>
	 * @param name ?????????
	 * ????????????????????????:[?????????]??????????????????????????????????????????
	 */
	protected void addIntCastErrorMessage(String name) {
		mospParams.addErrorMessage(PlatformMessageConst.MSG_NUMBER_CHECK_AMP, name);
	}
	
	/**
	 * ??????(???)1000??????????????????????????????????????????<br>
	 * ????????????????????????:??????1000????????????????????????????????????
	 */
	protected void addYearErrorMessage() {
		String rep = mospParams.getName("No1", "No0", "No0", "No0");
		mospParams.addErrorMessage(PlatformMessageConst.MSG_YEAR_CHECK, rep);
	}
	
	/**
	 * ??????(???)1???12??????????????????????????????????????????<br>
	 * ????????????????????????:??????1--12??????????????????????????????
	 */
	protected void addMonthErrorMessage() {
		mospParams.addErrorMessage(PlatformMessageConst.MSG_MONTH_CHECK);
	}
	
	/**
	 * ?????????????????????????????????????????????<br>
	 * @param name1 ?????????A
	 * @param name2 ?????????B
	 * ????????????????????????:[?????????A]???[?????????B]????????????????????????????????????????????????
	 */
	protected void addTimeComparisonErrorMessage(String name1, String name2) {
		mospParams.addErrorMessage(PlatformMessageConst.MSG_INVALID_ORDER, name1, name2);
	}
	
	/**
	 * ??????????????????????????????????????????<br>
	 * @param name ?????????
	 * ????????????????????????:??????????????????[?????????]??????????????????????????????[?????????]?????????????????????????????????????????????????????????
	 */
	protected void addTimeOutErrorMessage(String name) {
		mospParams.addErrorMessage(TimeMessageConst.MSG_WORK_TIME_OUT_CHECK, name);
	}
	
	/**
	 * ???????????????????????????????????????????????????<br>
	 * @param name1 ?????????A
	 * @param name2 ?????????B
	 * ????????????????????????:[?????????A]??????[?????????B]????????????????????????????????????????????????
	 */
	protected void addDiscordanceErrorMessage(String name1, String name2) {
		String rep1 = name1;
		String rep2 = name2 + "????????????" + mospParams.getName("Moment");
		mospParams.addErrorMessage(PlatformMessageConst.MSG_CHR_TYPE, rep1, rep2);
	}
	
	/**
	 * ?????????????????????????????????????????????????????????<br>
	 * @param name ?????????
	 * ????????????????????????:[?????????]???????????????????????????????????????
	 */
	protected void addNotInputErrorMessage(String name) {
		mospParams.addErrorMessage(PlatformMessageConst.MSG_REQUIRED, name);
	}
}
