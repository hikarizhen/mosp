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

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import jp.mosp.framework.base.MospException;
import jp.mosp.framework.base.MospParams;
import jp.mosp.framework.utils.DateUtility;
import jp.mosp.framework.utils.MospUtility;
import jp.mosp.platform.bean.human.EntranceReferenceBeanInterface;
import jp.mosp.platform.bean.human.HumanSearchBeanInterface;
import jp.mosp.platform.constant.PlatformConst;
import jp.mosp.platform.dto.human.HumanDtoInterface;
import jp.mosp.time.base.TimeApplicationBean;
import jp.mosp.time.bean.AttendanceTransactionReferenceBeanInterface;
import jp.mosp.time.bean.PaidHolidayDataGrantBeanInterface;
import jp.mosp.time.bean.PaidHolidayDataReferenceBeanInterface;
import jp.mosp.time.bean.PaidHolidayDataSearchBeanInterface;
import jp.mosp.time.bean.PaidHolidayGrantReferenceBeanInterface;
import jp.mosp.time.bean.PaidHolidayGrantRegistBeanInterface;
import jp.mosp.time.bean.ScheduleUtilBeanInterface;
import jp.mosp.time.constant.TimeConst;
import jp.mosp.time.dto.settings.AttendanceTransactionDtoInterface;
import jp.mosp.time.dto.settings.PaidHolidayDataDtoInterface;
import jp.mosp.time.dto.settings.PaidHolidayDataGrantListDtoInterface;
import jp.mosp.time.dto.settings.PaidHolidayGrantDtoInterface;
import jp.mosp.time.dto.settings.impl.PaidHolidayDataGrantListDto;
import jp.mosp.time.utils.TimeUtility;

/**
 * ???????????????????????????????????????
 */
public class PaidHolidayDataSearchBean extends TimeApplicationBean implements PaidHolidayDataSearchBeanInterface {
	
	/**
	 * ??????????????????????????????
	 */
	protected HumanSearchBeanInterface						humanSearch;
	
	/**
	 * ????????????????????????????????????
	 */
	protected EntranceReferenceBeanInterface				entranceReference;
	
	/**
	 * ????????????????????????????????????
	 */
	protected PaidHolidayGrantReferenceBeanInterface		paidHolidayGrantReference;
	
	/**
	 * ????????????????????????????????????
	 */
	protected PaidHolidayGrantRegistBeanInterface			paidHolidayGrantRegist;
	
	/**
	 * ???????????????????????????????????????
	 */
	protected PaidHolidayDataReferenceBeanInterface			paidHolidayDataReference;
	
	/**
	 * ????????????????????????????????????<br>
	 */
	protected ScheduleUtilBeanInterface						scheduleUtil;
	
	/**
	 * ???????????????????????????????????????
	 */
	protected PaidHolidayDataGrantBeanInterface				paidHolidayDataGrant;
	
	/**
	 * ????????????????????????????????????????????????
	 */
	protected AttendanceTransactionReferenceBeanInterface	attendanceTransactionReference;
	
	/**
	 * ????????????
	 */
	protected Date											activateDate;
	private Date											entranceFromDate;
	private Date											entranceToDate;
	private String											employeeCode;
	private String											employeeName;
	private String											workPlaceCode;
	private String											employmentCode;
	private String											sectionCode;
	private String											positionCode;
	private String											paidHolidayCode;
	private String											grant;
	/**
	 * ??????????????????
	 */
	protected boolean										calcAttendanceRate;
	private Set<String>										personalIdSet;
	
	/**
	 * ????????????<br>
	 */
	public static final int									NOT_CALCULATED			= 1;
	
	/**
	 * ?????????(?????????)???<br>
	 */
	public static final int									NOT_GRANTED_BUT_CALC	= 2;
	
	/**
	 * ?????????(???????????????)???<br>
	 */
	public static final int									NOT_GRANTED				= 0;
	
	/**
	 * ????????????<br>
	 */
	public static final int									GRANTED					= 3;
	
	
	/**
	 * ????????????????????????
	 */
	public PaidHolidayDataSearchBean() {
		super();
	}
	
	/**
	 * ????????????????????????
	 * @param mospParams MosP???????????????????????????
	 * @param connection DB?????????????????????
	 */
	public PaidHolidayDataSearchBean(MospParams mospParams, Connection connection) {
		super(mospParams, connection);
	}
	
	@Override
	public void initBean() throws MospException {
		super.initBean();
		humanSearch = (HumanSearchBeanInterface)createBean(HumanSearchBeanInterface.class);
		entranceReference = (EntranceReferenceBeanInterface)createBean(EntranceReferenceBeanInterface.class);
		paidHolidayGrantReference = (PaidHolidayGrantReferenceBeanInterface)createBean(
				PaidHolidayGrantReferenceBeanInterface.class);
		paidHolidayGrantRegist = (PaidHolidayGrantRegistBeanInterface)createBean(
				PaidHolidayGrantRegistBeanInterface.class);
		paidHolidayDataReference = (PaidHolidayDataReferenceBeanInterface)createBean(
				PaidHolidayDataReferenceBeanInterface.class);
		scheduleUtil = createBeanInstance(ScheduleUtilBeanInterface.class);
		paidHolidayDataGrant = (PaidHolidayDataGrantBeanInterface)createBean(PaidHolidayDataGrantBeanInterface.class);
		attendanceTransactionReference = (AttendanceTransactionReferenceBeanInterface)createBean(
				AttendanceTransactionReferenceBeanInterface.class);
	}
	
	@Override
	public List<PaidHolidayDataGrantListDtoInterface> getSearchList() throws MospException {
		// ?????????????????????????????????
		List<PaidHolidayDataGrantListDtoInterface> list = new ArrayList<PaidHolidayDataGrantListDtoInterface>();
		// ?????????????????????????????????
		List<HumanDtoInterface> humanList = getHumanList();
		// ????????????????????????????????????
		for (HumanDtoInterface humanDto : humanList) {
			PaidHolidayDataGrantListDtoInterface dto = getDto(humanDto);
			if (dto == null) {
				continue;
			}
			// ????????????????????????????????????
			list.add(dto);
			paidHolidayGrantRegist(dto);
		}
		return list;
	}
	
	/**
	 * ??????????????????????????????????????????<br>
	 * @return ????????????????????????
	 * @throws MospException ??????????????????????????????SQL??????????????????????????????????????????
	 */
	protected List<HumanDtoInterface> getHumanList() throws MospException {
		// ???????????????????????????????????????????????????
		humanSearch.setTargetDate(activateDate);
		humanSearch.setEmployeeCode(employeeCode);
		humanSearch.setEmployeeCodeType(PlatformConst.SEARCH_FORWARD_MATCH);
		humanSearch.setEmployeeName(employeeName);
		humanSearch.setWorkPlaceCode(workPlaceCode);
		humanSearch.setEmploymentContractCode(employmentCode);
		humanSearch.setPositionCode(positionCode);
		humanSearch.setSectionCode(sectionCode);
		humanSearch.setStateType(PlatformConst.EMPLOYEE_STATE_PRESENCE);
		return humanSearch.search();
	}
	
	/**
	 * ???????????????????????????DTO??????????????????<br>
	 * @param dto ??????DTO
	 * @return ???????????????????????????DTO
	 * @throws MospException ??????????????????????????????SQL??????????????????????????????????????????
	 */
	protected PaidHolidayDataGrantListDtoInterface getDto(HumanDtoInterface dto) throws MospException {
		// ??????ID
		if (!searchPersonalId(dto)) {
			return null;
		}
		// ?????????
		if (!searchEntranceDate(dto)) {
			return null;
		}
		// ???????????????????????????
		if (!hasPaidHolidaySettings(dto.getPersonalId(), activateDate)) {
			return null;
		}
		// ??????????????????????????????????????????
		if (isPaidHolidayTypeNot()) {
			return null;
		}
		// ??????????????????
		if (!searchPaidHoliday()) {
			return null;
		}
		// ?????????????????????????????????
		int grantTimes = paidHolidayDataGrant.getGrantTimes(dto.getPersonalId(), activateDate);
		// ??????????????????????????????
		Date grantDate = paidHolidayDataGrant.getGrantDate(dto.getPersonalId(), activateDate, grantTimes);
		// ????????????????????????????????????????????????
		PaidHolidayDataDtoInterface paidHolidayDataDto = paidHolidayDataReference.findForKey(dto.getPersonalId(),
				grantDate, grantDate);
		// ??????????????????
		if (!searchGrant(paidHolidayDataDto, dto.getPersonalId(), grantDate)) {
			return null;
		}
		return getDto(dto, paidHolidayDataDto, grantDate, grantTimes);
	}
	
	/**
	 * ???????????????????????????DTO??????????????????<br>
	 * @param dto ??????DTO
	 * @param paidHolidayDataDto ?????????????????????DTO
	 * @param grantDate ?????????
	 * @param grantTimes ????????????????????????
	 * @return ???????????????????????????DTO
	 * @throws MospException ??????????????????????????????SQL??????????????????????????????????????????
	 */
	protected PaidHolidayDataGrantListDtoInterface getDto(HumanDtoInterface dto,
			PaidHolidayDataDtoInterface paidHolidayDataDto, Date grantDate, int grantTimes) throws MospException {
		// ???????????????????????????????????????
		PaidHolidayDataGrantListDtoInterface paidHolidayDataGrantListDto = new PaidHolidayDataGrantListDto();
		// ??????????????????
		Date startDate = getStartDate(dto, grantTimes, grantDate);
		// ??????????????????
		Date endDate = getEndDate(startDate, grantTimes);
		// ???????????????
		setAttendanceRate(paidHolidayDataGrantListDto, dto, grantDate, startDate, endDate);
		// ???????????????????????????????????????
		setDto(paidHolidayDataGrantListDto, dto, paidHolidayDataDto, grantDate, startDate, endDate);
		// ????????????????????????????????????
		return paidHolidayDataGrantListDto;
	}
	
	/**
	 * ??????ID?????????
	 * @param dto ??????DTO
	 * @return ?????????????????????????????????true????????????????????????false
	 */
	protected boolean searchPersonalId(HumanDtoInterface dto) {
		return searchPersonalId(dto.getPersonalId());
	}
	
	/**
	 * ??????ID?????????
	 * @param personalId ??????ID
	 * @return ?????????????????????????????????true????????????????????????false
	 */
	protected boolean searchPersonalId(String personalId) {
		if (personalIdSet == null) {
			return true;
		}
		return personalIdSet.contains(personalId);
	}
	
	/**
	 * ??????????????????
	 * @param dto ??????DTO
	 * @return ?????????????????????????????????true????????????????????????false
	 * @throws MospException ???????????????
	 */
	protected boolean searchEntranceDate(HumanDtoInterface dto) throws MospException {
		return searchEntranceDate(dto.getPersonalId());
	}
	
	/**
	 * ??????????????????
	 * @param personalId ??????ID
	 * @return ?????????????????????????????????true????????????????????????false
	 * @throws MospException ???????????????
	 */
	protected boolean searchEntranceDate(String personalId) throws MospException {
		// ???????????????????????????
		if (entranceFromDate == null) {
			return true;
		}
		// ???????????????
		Date date = entranceReference.getEntranceDate(personalId);
		if (date == null) {
			return false;
		}
		// ??????????????????????????????????????????
		return DateUtility.isTermContain(date, entranceFromDate, entranceToDate);
	}
	
	/**
	 * ??????????????????????????????????????????????????????<br>
	 * @return ????????????(true????????????????????????false?????????????????????)
	 */
	protected boolean isPaidHolidayTypeNot() {
		return paidHolidayDto.getPaidHolidayType() == TimeConst.CODE_PAID_HOLIDAY_TYPE_NOT;
	}
	
	/**
	 * ???????????????????????????<br>
	 * @return ?????????????????????????????????true????????????????????????false
	 */
	protected boolean searchPaidHoliday() {
		if (paidHolidayCode.isEmpty()) {
			return true;
		}
		return paidHolidayCode.equals(paidHolidayDto.getPaidHolidayCode());
	}
	
	/**
	 * ?????????????????????????????????
	 * @param dto ??????DTO
	 * @param personalId ??????ID
	 * @param grantDate ?????????
	 * @return ?????????????????????????????????true????????????????????????false
	 * @throws MospException ???????????????
	 */
	protected boolean searchGrant(PaidHolidayDataDtoInterface dto, String personalId, Date grantDate)
			throws MospException {
		// ?????????????????????????????????????????????
		if (MospUtility.isEmpty(grant)) {
			// ????????????????????????????????????
			return true;
		}
		// ?????????????????????????????????
		PaidHolidayGrantDtoInterface paidHolidayGrantDto = paidHolidayGrantReference.findForKey(personalId, grantDate);
		// ????????????(????????????)?????????
		int intGrant = MospUtility.getInt(grant);
		// ????????????(????????????)?????????????????????
		if (intGrant == NOT_CALCULATED) {
			// ?????????????????????????????????????????????
			return isNotCalclated(dto, paidHolidayGrantDto);
		}
		// ???????????????(????????????)?????????(?????????)?????????
		if (intGrant == NOT_GRANTED_BUT_CALC) {
			// ????????????????????????(?????????)?????????????????????
			return isNotGrantedButCalc(dto, paidHolidayGrantDto);
		}
		// ???????????????(????????????)??????????????????
		if (intGrant == NOT_GRANTED) {
			// ?????????????????????????????????????????????
			return isNotGranted(dto, paidHolidayGrantDto);
		}
		// ????????????(????????????)?????????????????????
		if (intGrant == GRANTED) {
			// ?????????????????????????????????????????????
			return isGranted(dto, paidHolidayGrantDto);
		}
		return false;
	}
	
	/**
	 * ??????????????????????????????????????????????????????<br>
	 * @param dto      ??????????????????
	 * @param grantDto ????????????????????????
	 * @return ????????????(true???????????????????????????????????????false??????????????????)
	 */
	protected boolean isNotCalclated(PaidHolidayDataDtoInterface dto, PaidHolidayGrantDtoInterface grantDto) {
		// ?????????????????????????????????
		if (dto != null) {
			// ??????????????????????????????????????????
			return false;
		}
		// ???????????????????????????????????????
		if (grantDto == null) {
			// ??????????????????????????????????????????
			return true;
		}
		// ???????????????????????????????????????????????????(1)??????????????????????????????
		return grantDto.getGrantStatus() == NOT_CALCULATED;
	}
	
	/**
	 * ????????????????????????(?????????)??????????????????????????????<br>
	 * @param dto      ??????????????????
	 * @param grantDto ????????????????????????
	 * @return ????????????(true???????????????????????????(?????????)????????????false??????????????????)
	 */
	protected boolean isNotGrantedButCalc(PaidHolidayDataDtoInterface dto, PaidHolidayGrantDtoInterface grantDto) {
		// ?????????????????????????????????
		if (dto != null) {
			// ????????????????????????(?????????)??????????????????
			return false;
		}
		// ???????????????????????????????????????
		if (grantDto == null) {
			// ????????????????????????(?????????)??????????????????
			return false;
		}
		// ???????????????????????????????????????????????????(2)??????????????????????????????
		return grantDto.getGrantStatus() == NOT_GRANTED_BUT_CALC;
	}
	
	/**
	 * ??????????????????????????????????????????????????????<br>
	 * @param dto      ??????????????????
	 * @param grantDto ????????????????????????
	 * @return ????????????(true???????????????????????????????????????false??????????????????)
	 */
	protected boolean isGranted(PaidHolidayDataDtoInterface dto, PaidHolidayGrantDtoInterface grantDto) {
		// ?????????????????????????????????
		if (dto != null) {
			// ??????????????????????????????????????????
			return true;
		}
		// ???????????????????????????????????????
		if (grantDto == null) {
			// ??????????????????????????????????????????
			return false;
		}
		// ?????????????????????????????????????????????????????????????????????????????????
		return grantDto.getGrantStatus() == GRANTED;
	}
	
	/**
	 * ??????????????????????????????????????????????????????<br>
	 * @param dto      ??????????????????
	 * @param grantDto ????????????????????????
	 * @return ????????????(true???????????????????????????????????????false??????????????????)
	 */
	protected boolean isNotGranted(PaidHolidayDataDtoInterface dto, PaidHolidayGrantDtoInterface grantDto) {
		// ?????????????????????????????????????????????
		return isGranted(dto, grantDto) == false;
	}
	
	/**
	 * DTO????????????????????????
	 * @param dto ??????DTO
	 * @param humanDto ???????????????DTO
	 * @param paidHolidayDataDto ?????????????????????DTO
	 * @param grantDate ????????????
	 * @param firstDate ????????????
	 * @param lastDate ????????????
	 */
	protected void setDto(PaidHolidayDataGrantListDtoInterface dto, HumanDtoInterface humanDto,
			PaidHolidayDataDtoInterface paidHolidayDataDto, Date grantDate, Date firstDate, Date lastDate) {
		dto.setPersonalId(humanDto.getPersonalId());
		dto.setEmployeeCode(humanDto.getEmployeeCode());
		dto.setLastName(humanDto.getLastName());
		dto.setFirstName(humanDto.getFirstName());
		dto.setGrantDate(grantDate);
		dto.setFirstDate(firstDate);
		dto.setLastDate(lastDate);
		dto.setAccomplish(mospParams.getName("Ram", "Accomplish"));
		if (isAccomplished(dto.getAttendanceRate())) {
			// ???????????????
			dto.setAccomplish(mospParams.getName("Accomplish"));
		}
		if (grantDate == null) {
			dto.setAccomplish(mospParams.getName("Hyphen"));
		}
		if (dto.getAttendanceRate() == null) {
			dto.setAccomplish(mospParams.getName("Hyphen"));
		}
		if (!dto.getError().isEmpty()) {
			dto.setAccomplish(mospParams.getName("Hyphen"));
		}
		dto.setGrant(mospParams.getName("Ram", "Giving"));
		dto.setActivateDate(null);
		dto.setGrantDays(null);
		if (paidHolidayDataDto != null) {
			// ??????????????????
			dto.setGrant(mospParams.getName("Giving", "Finish"));
			dto.setActivateDate(paidHolidayDataDto.getActivateDate());
			dto.setGrantDays(paidHolidayDataDto.getHoldDay());
		}
	}
	
	/**
	 * ????????????????????????????????????????????????????????????<br>
	 * @param attendanceRate ?????????
	 * @return ????????????????????????true????????????????????????false
	 */
	protected boolean isAccomplished(Double attendanceRate) {
		if (paidHolidayDto.getWorkRatio() <= 0) {
			// ?????????????????????????????????0?????????????????????
			return true;
		}
		// ?????????????????????????????????0?????????????????????
		if (attendanceRate == null) {
			return false;
		}
		return Double.compare(attendanceRate.doubleValue() * 100, paidHolidayDto.getWorkRatio()) >= 0;
	}
	
	/**
	 * ??????????????????????????????????????????
	 * @param dto ??????DTO
	 * @throws MospException ???????????????
	 */
	protected void paidHolidayGrantRegist(PaidHolidayDataGrantListDtoInterface dto) throws MospException {
		if (dto.getAttendanceRate() == null) {
			return;
		}
		// ???????????????????????????????????????
		paidHolidayGrantRegist(dto.getPersonalId(), dto.getGrantDate());
	}
	
	/**
	 * ??????????????????????????????????????????
	 * @param personalId ??????ID
	 * @param grantDate ?????????
	 * @throws MospException ???????????????
	 */
	protected void paidHolidayGrantRegist(String personalId, Date grantDate) throws MospException {
		PaidHolidayGrantDtoInterface dto = paidHolidayGrantReference.findForKey(personalId, grantDate);
		if (dto != null && dto.getGrantStatus() == GRANTED) {
			// ??????????????????
			return;
		}
		if (dto == null) {
			dto = paidHolidayGrantRegist.getInitDto();
			dto.setPersonalId(personalId);
			dto.setGrantDate(grantDate);
		}
		dto.setGrantStatus(NOT_GRANTED_BUT_CALC);
		paidHolidayGrantRegist.regist(dto);
	}
	
	/**
	 * ???????????????????????????<br>
	 * @param dto ??????DTO
	 * @param grantTimes ????????????????????????
	 * @param grantDate ?????????
	 * @return ?????????
	 * @throws MospException ???????????????
	 */
	protected Date getStartDate(HumanDtoInterface dto, int grantTimes, Date grantDate) throws MospException {
		return getStartDate(dto.getPersonalId(), grantTimes, grantDate);
	}
	
	@Override
	public Date getStartDate(String personalId, int grantTimes, Date grantDate) throws MospException {
		Date entranceDate = entranceReference.getEntranceDate(personalId);
		if (grantTimes <= 0) {
			// 0???????????????
			return null;
		} else if (grantTimes == 1) {
			// 1??????????????????????????????
			return entranceDate;
		}
		// 1?????????????????????
		Date startDate = paidHolidayDataGrant.getGrantDate(personalId, activateDate, grantTimes - 1, entranceDate);
		if (grantTimes >= 3) {
			// 3???????????????
			return startDate;
		}
		// 3?????????????????????
		if (startDate != null) {
			return startDate;
		}
		Date targetDate = DateUtility.addYear(grantDate, -1);
		if (targetDate.after(entranceDate)) {
			// ???????????????????????????
			return targetDate;
		}
		// ????????????????????????
		return entranceDate;
	}
	
	@Override
	public Date getEndDate(Date startDate, int grantTimes) {
		if (startDate == null) {
			return null;
		}
		if (grantTimes <= 0) {
			// 0???????????????
			return null;
		} else if (grantTimes == 1) {
			// 1????????????6???????????????????????????
			return addDay(DateUtility.addMonth(startDate, 6), -1);
		}
		// 1????????????????????????1????????????????????????
		return addDay(DateUtility.addYear(startDate, 1), -1);
	}
	
	/**
	 * ???????????????????????????
	 * @param dto ??????DTO
	 * @param humanDto ???????????????DTO
	 * @param grantDate ?????????
	 * @param startDate ?????????
	 * @param endDate ?????????
	 * @throws MospException ????????????????????????????????????SQL???????????????????????????
	 */
	protected void setAttendanceRate(PaidHolidayDataGrantListDtoInterface dto, HumanDtoInterface humanDto,
			Date grantDate, Date startDate, Date endDate) throws MospException {
		setAttendanceRate(dto, humanDto.getPersonalId(), grantDate, startDate, endDate);
	}
	
	/**
	 * ???????????????????????????
	 * @param dto ??????DTO
	 * @param personalId ??????ID
	 * @param grantDate ?????????
	 * @param startDate ?????????
	 * @param endDate ?????????
	 * @throws MospException ????????????????????????????????????SQL???????????????????????????
	 */
	protected void setAttendanceRate(PaidHolidayDataGrantListDtoInterface dto, String personalId, Date grantDate,
			Date startDate, Date endDate) throws MospException {
		dto.setWorkDays(null);
		dto.setTotalWorkDays(null);
		dto.setAttendanceRate(null);
		dto.setError("");
		if (startDate == null) {
			return;
		}
		if (!calcAttendanceRate) {
			return;
		}
		int attendanceDays = 0;
		int totalWorkDays = 0;
		Date firstDate = startDate;
		Date lastDate = addDay(grantDate, -1);
		Set<Long> set = attendanceTransactionReference.findForMilliseconds(personalId, firstDate, lastDate);
		if (getNumberOfDays(firstDate, lastDate) != set.size()) {
			// ????????????????????????????????????????????????
			int milliseconds = 1000 * 60 * TimeConst.CODE_DEFINITION_HOUR * TimeConst.TIME_DAY_ALL_HOUR;
			long firstTime = firstDate.getTime();
			long lastTime = lastDate.getTime();
			for (long i = firstTime; i <= lastTime; i += milliseconds) {
				if (set.contains(i)) {
					continue;
				}
				Date date = new Date(i);
				
				// ??????????????????????????????????????????????????????????????????
				String workTypeCode = scheduleUtil.getScheduledWorkTypeCodeNoMessage(personalId, date);
				// ??????????????????????????????????????????????????????????????????????????????????????????
				if (MospUtility.isEmpty(workTypeCode)) {
					// ????????????
					continue;
				}
				// ?????????????????????????????????????????????????????????????????????????????????????????????????????????
				if (TimeUtility.isHoliday(workTypeCode)) {
					// ????????????
					continue;
				}
				totalWorkDays++;
			}
		}
		// ?????????????????????????????????????????????
		AttendanceTransactionDtoInterface attendanceTransactionDto = attendanceTransactionReference.sum(personalId,
				firstDate, lastDate);
		// ???????????????????????????????????????????????????
		if (attendanceTransactionDto != null) {
			attendanceDays += attendanceTransactionDto.getNumerator();
			totalWorkDays += attendanceTransactionDto.getDenominator();
		}
		// ????????????????????????????????????????????????
		int days = getNumberOfDays(grantDate, endDate);
		attendanceDays += days;
		totalWorkDays += days;
		dto.setWorkDays(attendanceDays);
		dto.setTotalWorkDays(totalWorkDays);
		dto.setAttendanceRate(getAttendanceRate(attendanceDays, totalWorkDays));
	}
	
	/**
	 * ???????????????????????????
	 * @param workDays ????????????
	 * @param totalWorkDays ???????????????
	 * @return ?????????
	 */
	protected double getAttendanceRate(int workDays, int totalWorkDays) {
		if (totalWorkDays <= 0) {
			// ??????????????????0???????????????
			return 0;
		}
		BigDecimal dividend = new BigDecimal(workDays);
		BigDecimal divisor = new BigDecimal(totalWorkDays);
		BigDecimal quotient = dividend.divide(divisor, 3, BigDecimal.ROUND_FLOOR);
		return quotient.doubleValue();
	}
	
	/**
	 * ????????????????????????<br>
	 * @param firstDate ??????
	 * @param lastDate ??????
	 * @return ??????
	 */
	protected int getNumberOfDays(Date firstDate, Date lastDate) {
		long difference = lastDate.getTime() - firstDate.getTime();
		if (difference < 0) {
			// 0?????????????????????
			return 0;
		}
		long quotient = difference / (1000 * 60 * TimeConst.CODE_DEFINITION_HOUR * TimeConst.TIME_DAY_ALL_HOUR);
		return (int)quotient + 1;
	}
	
	@Override
	public void setActivateDate(Date activateDate) {
		this.activateDate = getDateClone(activateDate);
	}
	
	@Override
	public void setEntranceFromDate(Date entranceFromDate) {
		this.entranceFromDate = getDateClone(entranceFromDate);
	}
	
	@Override
	public void setEntranceToDate(Date entranceToDate) {
		this.entranceToDate = getDateClone(entranceToDate);
	}
	
	@Override
	public void setEmployeeCode(String employeeCode) {
		this.employeeCode = employeeCode;
	}
	
	@Override
	public void setEmployeeName(String employeeName) {
		this.employeeName = employeeName;
	}
	
	@Override
	public void setWorkPlaceCode(String workPlaceCode) {
		this.workPlaceCode = workPlaceCode;
	}
	
	@Override
	public void setEmploymentCode(String employmentCode) {
		this.employmentCode = employmentCode;
	}
	
	@Override
	public void setSectionCode(String sectionCode) {
		this.sectionCode = sectionCode;
	}
	
	@Override
	public void setPositionCode(String positionCode) {
		this.positionCode = positionCode;
	}
	
	@Override
	public void setPaidHolidayCode(String paidHolidayCode) {
		this.paidHolidayCode = paidHolidayCode;
	}
	
	@Override
	public void setGrant(String grant) {
		this.grant = grant;
	}
	
	@Override
	public void setCalcAttendanceRate(boolean calcAttendanceRate) {
		this.calcAttendanceRate = calcAttendanceRate;
	}
	
	@Override
	public void setPersonalIdSet(Set<String> personalIdSet) {
		this.personalIdSet = personalIdSet;
	}
	
}
