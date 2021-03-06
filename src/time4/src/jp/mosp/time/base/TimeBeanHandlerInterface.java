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
package jp.mosp.time.base;

import java.util.Date;

import jp.mosp.framework.base.MospException;
import jp.mosp.platform.bean.file.ImportBeanInterface;
import jp.mosp.time.bean.AfterApplyAttendancesExecuteBeanInterface;
import jp.mosp.time.bean.AllowanceRegistBeanInterface;
import jp.mosp.time.bean.ApplicationRegistBeanInterface;
import jp.mosp.time.bean.AttendanceCalcBeanInterface;
import jp.mosp.time.bean.AttendanceCorrectionRegistBeanInterface;
import jp.mosp.time.bean.AttendanceListRegistBeanInterface;
import jp.mosp.time.bean.AttendanceRegistBeanInterface;
import jp.mosp.time.bean.AttendanceTransactionRegistBeanInterface;
import jp.mosp.time.bean.CutoffRegistBeanInterface;
import jp.mosp.time.bean.DifferenceRequestRegistBeanInterface;
import jp.mosp.time.bean.GoOutRegistBeanInterface;
import jp.mosp.time.bean.HolidayDataRegistBeanInterface;
import jp.mosp.time.bean.HolidayRegistBeanInterface;
import jp.mosp.time.bean.HolidayRequestRegistBeanInterface;
import jp.mosp.time.bean.LimitStandardRegistBeanInterface;
import jp.mosp.time.bean.OvertimeRequestRegistBeanInterface;
import jp.mosp.time.bean.PaidHolidayDataGrantBeanInterface;
import jp.mosp.time.bean.PaidHolidayDataRegistBeanInterface;
import jp.mosp.time.bean.PaidHolidayEntranceDateRegistBeanInterface;
import jp.mosp.time.bean.PaidHolidayFirstYearRegistBeanInterface;
import jp.mosp.time.bean.PaidHolidayGrantRegistBeanInterface;
import jp.mosp.time.bean.PaidHolidayPointDateRegistBeanInterface;
import jp.mosp.time.bean.PaidHolidayProportionallyRegistBeanInterface;
import jp.mosp.time.bean.PaidHolidayRegistBeanInterface;
import jp.mosp.time.bean.PaidHolidayTransactionRegistBeanInterface;
import jp.mosp.time.bean.RestRegistBeanInterface;
import jp.mosp.time.bean.ScheduleDateRegistBeanInterface;
import jp.mosp.time.bean.ScheduleRegistBeanInterface;
import jp.mosp.time.bean.StockHolidayDataGrantBeanInterface;
import jp.mosp.time.bean.StockHolidayDataRegistBeanInterface;
import jp.mosp.time.bean.StockHolidayRegistBeanInterface;
import jp.mosp.time.bean.StockHolidayTransactionRegistBeanInterface;
import jp.mosp.time.bean.SubHolidayRegistBeanInterface;
import jp.mosp.time.bean.SubHolidayRequestRegistBeanInterface;
import jp.mosp.time.bean.SubstituteRegistBeanInterface;
import jp.mosp.time.bean.TimeApprovalBeanInterface;
import jp.mosp.time.bean.TimeRecordBeanInterface;
import jp.mosp.time.bean.TimeSettingRegistBeanInterface;
import jp.mosp.time.bean.TotalAbsenceRegistBeanInterface;
import jp.mosp.time.bean.TotalAllowanceRegistBeanInterface;
import jp.mosp.time.bean.TotalLeaveRegistBeanInterface;
import jp.mosp.time.bean.TotalOtherVacationRegistBeanInterface;
import jp.mosp.time.bean.TotalTimeCalcBeanInterface;
import jp.mosp.time.bean.TotalTimeCorrectionRegistBeanInterface;
import jp.mosp.time.bean.TotalTimeEmployeeTransactionRegistBeanInterface;
import jp.mosp.time.bean.TotalTimeRegistBeanInterface;
import jp.mosp.time.bean.TotalTimeTransactionRegistBeanInterface;
import jp.mosp.time.bean.WorkOnHolidayRequestRegistBeanInterface;
import jp.mosp.time.bean.WorkTypeChangeRequestRegistBeanInterface;
import jp.mosp.time.bean.WorkTypeItemRegistBeanInterface;
import jp.mosp.time.bean.WorkTypePatternItemRegistBeanInterface;
import jp.mosp.time.bean.WorkTypePatternRegistBeanInterface;
import jp.mosp.time.bean.WorkTypeRegistBeanInterface;

/**
 * MosP???????????????BeanHandler???????????????????????????<br>
 */
public interface TimeBeanHandlerInterface {
	
	/**
	 * ????????????????????????????????????????????????
	 * @return ??????????????????????????????
	 * @throws MospException Bean???????????????????????????????????????????????????????????????
	 */
	AttendanceRegistBeanInterface attendanceRegist() throws MospException;
	
	/**
	 * ????????????????????????????????????????????????????????????
	 * @return ??????????????????????????????????????????
	 * @throws MospException Bean???????????????????????????????????????????????????????????????
	 */
	AttendanceCorrectionRegistBeanInterface attendanceCorrectionRegist() throws MospException;
	
	/**
	 * ????????????????????????????????????????????????????????????
	 * @return ??????????????????????????????????????????
	 * @throws MospException Bean???????????????????????????????????????????????????????????????
	 */
	RestRegistBeanInterface restRegist() throws MospException;
	
	/**
	 * ????????????????????????????????????????????????????????????
	 * @return ??????????????????????????????????????????
	 * @throws MospException Bean???????????????????????????????????????????????????????????????
	 */
	GoOutRegistBeanInterface goOutRegist() throws MospException;
	
	/**
	 * ????????????????????????????????????????????????????????????
	 * @return ??????????????????????????????????????????
	 * @throws MospException Bean???????????????????????????????????????????????????????????????
	 */
	AllowanceRegistBeanInterface allowanceRegist() throws MospException;
	
	/**
	 * ???????????????????????????????????????????????????????????????
	 * @return ?????????????????????????????????????????????
	 * @throws MospException Bean???????????????????????????????????????????????????????????????
	 */
	AttendanceTransactionRegistBeanInterface attendanceTransactionRegist() throws MospException;
	
	/**
	 * ?????????????????????????????????????????????
	 * @return ???????????????????????????
	 * @throws MospException Bean???????????????????????????????????????????????????????????????
	 */
	OvertimeRequestRegistBeanInterface overtimeRequestRegist() throws MospException;
	
	/**
	 * ?????????????????????????????????????????????
	 * @return ???????????????????????????
	 * @throws MospException Bean???????????????????????????????????????????????????????????????
	 */
	HolidayRequestRegistBeanInterface holidayRequestRegist() throws MospException;
	
	/**
	 * ???????????????????????????????????????????????????
	 * @return ?????????????????????????????????
	 * @throws MospException Bean???????????????????????????????????????????????????????????????
	 */
	WorkOnHolidayRequestRegistBeanInterface workOnHolidayRequestRegist() throws MospException;
	
	/**
	 * ????????????????????????????????????????????????
	 * @return ??????????????????????????????
	 * @throws MospException Bean???????????????????????????????????????????????????????????????
	 */
	SubHolidayRegistBeanInterface subHolidayRegist() throws MospException;
	
	/**
	 * ?????????????????????????????????????????????
	 * @return ???????????????????????????
	 * @throws MospException Bean???????????????????????????????????????????????????????????????
	 */
	SubHolidayRequestRegistBeanInterface subHolidayRequestRegist() throws MospException;
	
	/**
	 * ???????????????????????????????????????????????????
	 * @return ???????????????????????????????????????
	 * @throws MospException Bean???????????????????????????????????????????????????????????????
	 */
	WorkTypeChangeRequestRegistBeanInterface workTypeChangeRequestRegist() throws MospException;
	
	/**
	 * ???????????????????????????????????????????????????
	 * @return ?????????????????????????????????
	 * @throws MospException Bean???????????????????????????????????????????????????????????????
	 */
	DifferenceRequestRegistBeanInterface differenceRequestRegist() throws MospException;
	
	/**
	 * ???????????????????????????????????????????????????
	 * @return ?????????????????????????????????
	 * @throws MospException Bean???????????????????????????????????????????????????????????????
	 */
	TotalTimeTransactionRegistBeanInterface totalTimeTransactionRegist() throws MospException;
	
	/**
	 * ?????????????????????????????????????????????????????????
	 * @return ???????????????????????????????????????
	 * @throws MospException Bean???????????????????????????????????????????????????????????????
	 */
	TotalTimeEmployeeTransactionRegistBeanInterface totalTimeEmployeeTransactionRegist() throws MospException;
	
	/**
	 * ??????????????????????????????????????????????????????
	 * @return ????????????????????????????????????
	 * @throws MospException Bean???????????????????????????????????????????????????????????????
	 */
	TotalTimeRegistBeanInterface totalTimeRegist() throws MospException;
	
	/**
	 * ?????????????????????????????????????????????????????????
	 * @return ???????????????????????????????????????
	 * @throws MospException Bean???????????????????????????????????????????????????????????????
	 */
	TotalTimeCorrectionRegistBeanInterface totalTimeCorrectionRegist() throws MospException;
	
	/**
	 * ??????????????????????????????????????????????????????????????????
	 * @return ????????????????????????????????????????????????
	 * @throws MospException Bean???????????????????????????????????????????????????????????????
	 */
	TotalLeaveRegistBeanInterface totalLeaveRegist() throws MospException;
	
	/**
	 * ?????????????????????????????????????????????????????????????????????
	 * @return ???????????????????????????????????????????????????
	 * @throws MospException Bean???????????????????????????????????????????????????????????????
	 */
	TotalOtherVacationRegistBeanInterface totalOtherVacationRegist() throws MospException;
	
	/**
	 * ????????????????????????????????????????????????????????????
	 * @return ??????????????????????????????????????????
	 * @throws MospException Bean???????????????????????????????????????????????????????????????
	 */
	TotalAbsenceRegistBeanInterface totalAbsenceRegist() throws MospException;
	
	/**
	 * ????????????????????????????????????????????????????????????
	 * @return ??????????????????????????????????????????
	 * @throws MospException Bean???????????????????????????????????????????????????????????????
	 */
	TotalAllowanceRegistBeanInterface totalAllowanceRegist() throws MospException;
	
	/**
	 * ???????????????????????????????????????????????????
	 * @return ?????????????????????????????????
	 * @throws MospException Bean???????????????????????????????????????????????????????????????
	 */
	HolidayRegistBeanInterface holidayRegist() throws MospException;
	
	/**
	 * ????????????????????????????????????????????????
	 * @return ??????????????????????????????
	 * @throws MospException Bean???????????????????????????????????????????????????????????????
	 */
	HolidayDataRegistBeanInterface holidayDataRegist() throws MospException;
	
	/**
	 * ??????????????????????????????????????????????????????
	 * @return ????????????????????????????????????
	 * @throws MospException Bean???????????????????????????????????????????????????????????????
	 */
	PaidHolidayDataRegistBeanInterface paidHolidayDataRegist() throws MospException;
	
	/**
	 * ?????????????????????????????????????????????????????????
	 * @return ???????????????????????????????????????
	 * @throws MospException Bean???????????????????????????????????????????????????????????????
	 */
	PaidHolidayTransactionRegistBeanInterface paidHolidayTransactionRegist() throws MospException;
	
	/**
	 * ????????????????????????????????????????????????????????????
	 * @return ??????????????????????????????????????????
	 * @throws MospException Bean???????????????????????????????????????????????????????????????
	 */
	StockHolidayDataRegistBeanInterface stockHolidayDataRegist() throws MospException;
	
	/**
	 * ???????????????????????????????????????????????????????????????????????????
	 * @return ?????????????????????????????????????????????????????????
	 * @throws MospException Bean???????????????????????????????????????????????????????????????
	 */
	StockHolidayTransactionRegistBeanInterface stockHolidayTransactionRegist() throws MospException;
	
	/**
	 * ?????????????????????????????????????????????
	 * @return ???????????????????????????
	 * @throws MospException Bean???????????????????????????????????????????????????????????????
	 */
	TimeSettingRegistBeanInterface timeSettingRegist() throws MospException;
	
	/**
	 * ?????????????????????????????????????????????
	 * @return ???????????????????????????
	 * @throws MospException Bean???????????????????????????????????????????????????????????????
	 */
	LimitStandardRegistBeanInterface limitStandardRegist() throws MospException;
	
	/**
	 * ???????????????????????????????????????????????????
	 * @return ?????????????????????????????????
	 * @throws MospException Bean???????????????????????????????????????????????????????????????
	 */
	WorkTypeRegistBeanInterface workTypeRegist() throws MospException;
	
	/**
	 * ???????????????????????????????????????????????????
	 * @return ?????????????????????????????????
	 * @throws MospException Bean???????????????????????????????????????????????????????????????
	 */
	WorkTypeItemRegistBeanInterface workTypeItemRegist() throws MospException;
	
	/**
	 * ?????????????????????????????????????????????????????????
	 * @return ???????????????????????????????????????
	 * @throws MospException Bean???????????????????????????????????????????????????????????????
	 */
	WorkTypePatternRegistBeanInterface workTypePatternRegist() throws MospException;
	
	/**
	 * ???????????????????????????????????????????????????????????????
	 * @return ?????????????????????????????????????????????
	 * @throws MospException Bean???????????????????????????????????????????????????????????????
	 */
	WorkTypePatternItemRegistBeanInterface workTypePatternItemRegist() throws MospException;
	
	/**
	 * ???????????????????????????????????????????????????
	 * @return ?????????????????????????????????
	 * @throws MospException Bean???????????????????????????????????????????????????????????????
	 */
	PaidHolidayRegistBeanInterface paidHolidayRegist() throws MospException;
	
	/**
	 * ?????????????????????????????????????????????????????????
	 * @return ???????????????????????????????????????
	 * @throws MospException Bean???????????????????????????????????????????????????????????????
	 */
	PaidHolidayProportionallyRegistBeanInterface paidHolidayProportionallyRegist() throws MospException;
	
	/**
	 * ????????????????????????????????????????????????????????????
	 * @return ??????????????????????????????????????????
	 * @throws MospException Bean???????????????????????????????????????????????????????????????
	 */
	PaidHolidayFirstYearRegistBeanInterface paidHolidayFirstYearRegist() throws MospException;
	
	/**
	 * ????????????????????????(?????????)?????????????????????????????????
	 * @return ????????????????????????(?????????)???????????????
	 * @throws MospException Bean???????????????????????????????????????????????????????????????
	 */
	PaidHolidayPointDateRegistBeanInterface paidHolidayPointDateRegist() throws MospException;
	
	/**
	 * ????????????????????????(?????????)?????????????????????????????????
	 * @return ????????????????????????(?????????)???????????????
	 * @throws MospException Bean???????????????????????????????????????????????????????????????
	 */
	PaidHolidayEntranceDateRegistBeanInterface paidHolidayEntranceDateRegist() throws MospException;
	
	/**
	 * ?????????????????????????????????????????????????????????
	 * @return ???????????????????????????????????????
	 * @throws MospException Bean???????????????????????????????????????????????????????????????
	 */
	StockHolidayRegistBeanInterface stockHolidayRegist() throws MospException;
	
	/**
	 * ???????????????????????????????????????????????????
	 * @return ?????????????????????????????????
	 * @throws MospException Bean???????????????????????????????????????????????????????????????
	 */
	ScheduleRegistBeanInterface scheduleRegist() throws MospException;
	
	/**
	 * ????????????????????????????????????????????????
	 * @return ??????????????????????????????
	 * @throws MospException Bean???????????????????????????????????????????????????????????????
	 */
	ScheduleDateRegistBeanInterface scheduleDateRegist() throws MospException;
	
	/**
	 * ?????????????????????????????????????????????
	 * @return ???????????????????????????
	 * @throws MospException Bean???????????????????????????????????????????????????????????????
	 */
	CutoffRegistBeanInterface cutoffRegist() throws MospException;
	
	/**
	 * ???????????????????????????????????????????????????
	 * @return ?????????????????????????????????
	 * @throws MospException Bean???????????????????????????????????????????????????????????????
	 */
	ApplicationRegistBeanInterface applicationRegist() throws MospException;
	
	/**
	 * ???????????????????????????????????????
	 * @return ?????????????????????
	 * @throws MospException Bean???????????????????????????????????????????????????????????????
	 */
	TotalTimeCalcBeanInterface totalTimeCalc() throws MospException;
	
	/**
	 * ??????????????????????????????????????????????????????
	 * @return ????????????????????????????????????
	 * @throws MospException Bean???????????????????????????????????????????????????????????????
	 */
	SubstituteRegistBeanInterface substituteRegist() throws MospException;
	
	/**
	 * ?????????????????????????????????????????????
	 * @return ???????????????????????????
	 * @throws MospException Bean???????????????????????????????????????????????????????????????
	 */
	AttendanceListRegistBeanInterface attendanceListRegist() throws MospException;
	
	/**
	 * ?????????????????????????????????????????????
	 * @param targetDate ?????????
	 * @return ???????????????????????????
	 * @throws MospException Bean???????????????????????????????????????????????????????????????
	 */
	AttendanceListRegistBeanInterface attendanceListRegist(Date targetDate) throws MospException;
	
	/**
	 * ?????????????????????????????????????????????
	 * @return ???????????????????????????
	 * @throws MospException Bean???????????????????????????????????????????????????????????????
	 */
	AttendanceCalcBeanInterface attendanceCalc() throws MospException;
	
	/**
	 * ?????????????????????????????????????????????
	 * @param targetDate ?????????
	 * @return ???????????????????????????
	 * @throws MospException Bean???????????????????????????????????????????????????????????????
	 */
	AttendanceCalcBeanInterface attendanceCalc(Date targetDate) throws MospException;
	
	/**
	 * ???????????????????????????????????????????????????
	 * @return ?????????????????????????????????
	 * @throws MospException Bean???????????????????????????????????????????????????????????????
	 */
	TimeApprovalBeanInterface timeApproval() throws MospException;
	
	/**
	 * ???????????????????????????????????????????????????
	 * @return ?????????????????????????????????
	 * @throws MospException Bean???????????????????????????????????????????????????????????????
	 */
	PaidHolidayGrantRegistBeanInterface paidHolidayGrantRegist() throws MospException;
	
	/**
	 * ??????????????????????????????????????????????????????
	 * @return ????????????????????????????????????
	 * @throws MospException Bean???????????????????????????????????????????????????????????????
	 */
	PaidHolidayDataGrantBeanInterface paidHolidayDataGrant() throws MospException;
	
	/**
	 * ????????????????????????????????????????????????????????????
	 * @return ??????????????????????????????????????????
	 * @throws MospException Bean???????????????????????????????????????????????????????????????
	 */
	StockHolidayDataGrantBeanInterface stockHolidayDataGrant() throws MospException;
	
	/**
	 * ??????????????????????????????????????????????????????<br>
	 * @return ????????????????????????????????????
	 * @throws MospException Bean???????????????????????????????????????????????????????????????
	 */
	ImportBeanInterface holidayRequestImport() throws MospException;
	
	/**
	 * ???????????????????????????????????????????????????????????????<br>
	 * @return ?????????????????????????????????????????????
	 * @throws MospException Bean???????????????????????????????????????????????????????????????
	 */
	ImportBeanInterface workOnHolidayRequestImport() throws MospException;
	
	/**
	 * @return ????????????????????????????????????
	 * @throws MospException Bean???????????????????????????????????????????????????????????????
	 */
	AfterApplyAttendancesExecuteBeanInterface afterApplyAttendancesExecute() throws MospException;
	
	/**
	 * @return ??????????????????????????????
	 * @throws MospException Bean???????????????????????????????????????????????????????????????
	 */
	TimeRecordBeanInterface timeRecord() throws MospException;
	
}
