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
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import jp.mosp.framework.base.MospException;
import jp.mosp.framework.base.MospParams;
import jp.mosp.framework.utils.DateUtility;
import jp.mosp.platform.bean.human.HumanSearchBeanInterface;
import jp.mosp.platform.constant.PlatformConst;
import jp.mosp.platform.constant.PlatformMessageConst;
import jp.mosp.platform.dto.human.HumanDtoInterface;
import jp.mosp.platform.utils.MonthUtility;
import jp.mosp.time.base.TimeApplicationBean;
import jp.mosp.time.base.TimeBean;
import jp.mosp.time.bean.CutoffUtilBeanInterface;
import jp.mosp.time.bean.TimeMasterBeanInterface;
import jp.mosp.time.bean.TotalTimeEmployeeTransactionReferenceBeanInterface;
import jp.mosp.time.constant.TimeConst;
import jp.mosp.time.dto.settings.ApplicationDtoInterface;
import jp.mosp.time.dto.settings.CutoffDtoInterface;
import jp.mosp.time.dto.settings.TimeSettingDtoInterface;
import jp.mosp.time.entity.ApplicationEntity;
import jp.mosp.time.utils.TimeMessageUtility;
import jp.mosp.time.utils.TimeUtility;

/**
 * ???????????????????????????????????????<br>
 *
 */
public class CutoffUtilBean extends TimeApplicationBean implements CutoffUtilBeanInterface {
	
	/**
	 * ??????????????????????????????
	 */
	HumanSearchBeanInterface							humanSearch;
	
	/**
	 * ??????????????????????????????????????????
	 */
	TotalTimeEmployeeTransactionReferenceBeanInterface	totalTimeEmployeeRefer;
	
	/**
	 * ???????????????????????????????????????<br>
	 */
	protected TimeMasterBeanInterface					timeMaster;
	
	
	/**
	 * {@link TimeBean#TimeBean()}??????????????????<br>
	 */
	public CutoffUtilBean() {
		super();
	}
	
	/**
	 * {@link TimeBean#TimeBean(MospParams, Connection)}??????????????????<br>
	 * @param mospParams MosP????????????
	 * @param connection DB??????????????????
	 */
	protected CutoffUtilBean(MospParams mospParams, Connection connection) {
		super(mospParams, connection);
	}
	
	@Override
	public void initBean() throws MospException {
		// ?????????????????????????????????
		super.initBean();
		// ????????????????????????
		humanSearch = (HumanSearchBeanInterface)createBean(HumanSearchBeanInterface.class);
		totalTimeEmployeeRefer = (TotalTimeEmployeeTransactionReferenceBeanInterface)createBean(
				TotalTimeEmployeeTransactionReferenceBeanInterface.class);
		timeMaster = (TimeMasterBeanInterface)createBean(TimeMasterBeanInterface.class);
	}
	
	@Override
	public Set<String> getCutoffPersonalIdSet(String cutoffCode, int targetYear, int targetMonth) throws MospException {
		// ??????ID??????????????????
		Set<String> idSet = new HashSet<String>();
		// ??????????????????????????????????????????
		CutoffDtoInterface cutoffDto = getCutoff(cutoffCode, targetYear, targetMonth);
		// ??????????????????
		if (mospParams.hasErrorMessage()) {
			return idSet;
		}
		// ???????????????
		int cutoffDate = cutoffDto.getCutoffDate();
		// ???????????????????????????????????????????????????????????????????????????
		Date cutoffTermTargetDate = TimeUtility.getCutoffTermTargetDate(cutoffDate, targetYear, targetMonth);
		Date firstDate = TimeUtility.getCutoffFirstDate(cutoffDate, targetYear, targetMonth);
		Date lastDate = TimeUtility.getCutoffLastDate(cutoffDate, targetYear, targetMonth);
		// ???????????????ID???????????????(?????????????????????????????????)
		Map<String, HumanDtoInterface> activateMap = getActivatePersonalIdMap(cutoffTermTargetDate, firstDate,
				lastDate);
		// ????????????????????????
		for (HumanDtoInterface humanDto : activateMap.values()) {
			// ???????????????????????????????????????
			ApplicationEntity entity = timeMaster.getApplicationEntity(humanDto, cutoffTermTargetDate);
			// ?????????????????????
			if (entity.getCutoffCode().equals(cutoffCode)) {
				// ???????????????ID????????????
				idSet.add(humanDto.getPersonalId());
			}
		}
		// ??????ID???????????????
		if (idSet.isEmpty()) {
			// ?????????????????????????????????
			mospParams.addErrorMessage(PlatformMessageConst.MSG_NO_ITEM, mospParams.getName("Employee"));
		}
		return idSet;
	}
	
	@Override
	public List<HumanDtoInterface> getCutoffHumanDto(String cutoffCode, int targetYear, int targetMonth)
			throws MospException {
		// ???????????????????????????
		List<HumanDtoInterface> humanList = new ArrayList<HumanDtoInterface>();
		
		// ??????????????????????????????????????????
		CutoffDtoInterface cutoffDto = getCutoff(cutoffCode, targetYear, targetMonth);
		// ??????????????????
		if (mospParams.hasErrorMessage()) {
			return humanList;
		}
		// ???????????????
		int cutoffDate = cutoffDto.getCutoffDate();
		// ???????????????????????????????????????????????????????????????????????????
		Date cutoffTermTargetDate = TimeUtility.getCutoffTermTargetDate(cutoffDate, targetYear, targetMonth);
		Date firstDate = TimeUtility.getCutoffFirstDate(cutoffDate, targetYear, targetMonth);
		Date lastDate = TimeUtility.getCutoffLastDate(cutoffDate, targetYear, targetMonth);
		// ???????????????ID???????????????(?????????????????????????????????)
		Map<String, HumanDtoInterface> activateMap = getActivatePersonalIdMap(cutoffTermTargetDate, firstDate,
				lastDate);
		// ????????????????????????
		for (HumanDtoInterface humanDto : activateMap.values()) {
			// ???????????????????????????????????????
			ApplicationEntity entity = timeMaster.getApplicationEntity(humanDto, cutoffTermTargetDate);
			// ?????????????????????
			if (entity.getCutoffCode().equals(cutoffCode)) {
				// ???????????????????????????
				humanList.add(humanDto);
			}
		}
		// ???????????????????????????
		if (humanList.isEmpty()) {
			// ?????????????????????????????????
			mospParams.addErrorMessage(PlatformMessageConst.MSG_NO_ITEM, mospParams.getName("Employee"));
		}
		return humanList;
	}
	
	@Override
	public CutoffDtoInterface getCutoff(String cutoffCode, int targetYear, int targetMonth) throws MospException {
		// ????????????????????????????????????
		Date yearMonthTargetDate = MonthUtility.getYearMonthTargetDate(targetYear, targetMonth, mospParams);
		// ????????????????????????????????????????????????????????? 
		CutoffDtoInterface cutoffDto = timeMaster.getCutoff(cutoffCode, yearMonthTargetDate);
		// ??????
		cutoffRefer.chkExistCutoff(cutoffDto, yearMonthTargetDate);
		return cutoffDto;
	}
	
	@Override
	public CutoffDtoInterface getCutoff(String cutoffCode, Date targetDate) throws MospException {
		// ????????????????????????????????????????????????????????? 
		CutoffDtoInterface cutoffDto = timeMaster.getCutoff(cutoffCode, targetDate);
		// ??????
		cutoffRefer.chkExistCutoff(cutoffDto, targetDate);
		return cutoffDto;
	}
	
	@Override
	public CutoffDtoInterface getCutoffForPersonalId(String personalId, int targetYear, int targetMonth)
			throws MospException {
		// ????????????ID??????????????????(???????????????????????????)??????????????????????????????????????????
		setCutoffSettings(personalId, MonthUtility.getYearMonthTargetDate(targetYear, targetMonth, mospParams));
		// ??????????????????
		if (mospParams.hasErrorMessage()) {
			return null;
		}
		return cutoffDto;
	}
	
	@Override
	public CutoffDtoInterface getCutoffForPersonalId(String personalId, Date targetDate) throws MospException {
		// ????????????ID??????????????????(???????????????????????????)??????????????????????????????????????????
		setCutoffSettings(personalId, targetDate);
		// ??????????????????
		if (mospParams.hasErrorMessage()) {
			return null;
		}
		return cutoffDto;
	}
	
	@Override
	public CutoffDtoInterface getCutoffForApplication(ApplicationDtoInterface dto, Date targetDate)
			throws MospException {
		// ???????????????????????????????????????????????????
		getTimeSetting(dto, targetDate);
		// ??????????????????
		if (mospParams.hasErrorMessage()) {
			return null;
		}
		// ?????????????????????????????????????????????
		cutoffDto = cutoffRefer.getCutoffInfo(timeSettingDto.getCutoffCode(), targetDate);
		// ??????????????????
		cutoffRefer.chkExistCutoff(cutoffDto, targetDate);
		return cutoffDto;
	}
	
	@Override
	public Date getCutoffCalculationDate(String cutoffCode, int targetYear, int targetMonth) throws MospException {
		// ????????????????????????????????????????????????????????? 
		CutoffDtoInterface cutoffDto = getCutoff(cutoffCode, targetYear, targetMonth);
		// ??????????????????
		if (mospParams.hasErrorMessage()) {
			return null;
		}
		// ????????????
		int cutoffDate = cutoffDto.getCutoffDate();
		// ????????????????????????????????????????????????
		return TimeUtility.getCutoffCalculationDate(cutoffDate, targetYear, targetMonth);
	}
	
	@Override
	public Date getCutoffTermTargetDate(String cutoffCode, int targetYear, int targetMonth) throws MospException {
		// ????????????????????????????????????????????????????????? 
		CutoffDtoInterface cutoffDto = getCutoff(cutoffCode, targetYear, targetMonth);
		// ??????????????????
		if (mospParams.hasErrorMessage()) {
			return null;
		}
		// ????????????
		int cutoffDate = cutoffDto.getCutoffDate();
		// ????????????????????????????????????????????????
		return TimeUtility.getCutoffTermTargetDate(cutoffDate, targetYear, targetMonth);
	}
	
	@Override
	public boolean checkTighten(String personalId, Date targetDate, String targetName) throws MospException {
		// ????????????ID?????????????????????????????????????????????????????????????????????
		Date cutoffDate = getCutoffMonth(personalId, targetDate);
		// ??????????????????
		if (mospParams.hasErrorMessage()) {
			return false;
		}
		// ????????????
		int year = DateUtility.getYear(cutoffDate);
		int month = DateUtility.getMonth(cutoffDate);
		// ???????????????
		boolean isNotTighten = isNotTighten(personalId, year, month);
		// ?????????????????????
		if (isNotTighten == false) {
			// ?????????????????????
			addMonthlyTreatmentErrorMessage(year, month, targetName);
		}
		return isNotTighten;
	}
	
	@Override
	public boolean checkTighten(String personalId, int targetYear, int targetMonth) throws MospException {
		// ???????????????
		boolean isNotTighten = isNotTighten(personalId, targetYear, targetMonth);
		// ?????????????????????
		if (isNotTighten == false) {
			// ?????????????????????
			TimeMessageUtility.addErrorTheMonthIsTighten(mospParams, targetYear, targetMonth);
		}
		return isNotTighten;
	}
	
	@Override
	public boolean isNotTighten(String personalId, Date targetDate) throws MospException {
		// ????????????ID????????????????????????????????????????????????????????????
		if (hasCutoffSettings(personalId, targetDate) == false) {
			return false;
		}
		// ???????????????????????????????????????????????????????????????????????????
		Date cutoffMonth = TimeUtility.getCutoffMonth(cutoffDto.getCutoffDate(), targetDate);
		// ???????????????
		return isNotTighten(personalId, DateUtility.getYear(cutoffMonth), DateUtility.getMonth(cutoffMonth));
	}
	
	@Override
	public boolean isNotTighten(String personalId, int targetYear, int targetMonth) throws MospException {
		// ????????????????????????????????????????????????
		Integer state = totalTimeEmployeeRefer.getCutoffState(personalId, targetYear, targetMonth);
		// ???????????????
		if (state == null || state == TimeConst.CODE_CUTOFF_STATE_NOT_TIGHT) {
			return true;
		}
		// ?????????????????????
		return false;
	}
	
	@Override
	public Date getCutoffFirstDate(String cutoffCode, int targetYear, int targetMonth) throws MospException {
		// ??????????????????
		CutoffDtoInterface cutoffDto = getCutoff(cutoffCode, targetYear, targetMonth);
		return TimeUtility.getCutoffFirstDate(cutoffDto.getCutoffDate(), targetYear, targetMonth);
	}
	
	@Override
	public Date getCutoffLastDate(String cutoffCode, int targetYear, int targetMonth) throws MospException {
		// ??????????????????
		CutoffDtoInterface cutoffDto = getCutoff(cutoffCode, targetYear, targetMonth);
		return TimeUtility.getCutoffLastDate(cutoffDto.getCutoffDate(), targetYear, targetMonth);
	}
	
	@Override
	public Date getCutoffMonth(String personalId, Date targetDate) throws MospException {
		// ??????????????????
		CutoffDtoInterface cutoffDto = getCutoffForPersonalId(personalId, targetDate);
		// ????????????????????????????????????
		if (cutoffDto == null) {
			// ???????????????????????????????????????
			return MonthUtility.getTargetYearMonth(targetDate, mospParams);
		}
		// ??????????????????????????????????????????
		return TimeUtility.getCutoffMonth(cutoffDto.getCutoffDate(), targetDate);
	}
	
	/**
	 * ???????????????ID???????????????????????????<br>
	 * @param targetDate ????????????
	 * @param staDate ???????????????
	 * @param endDate ???????????????
	 * @return ???????????????ID?????????
	 * @throws MospException ??????????????????????????????????????????
	 */
	protected Map<String, HumanDtoInterface> getActivatePersonalIdMap(Date targetDate, Date staDate, Date endDate)
			throws MospException {
		// ???????????????
		humanSearch.setTargetDate(targetDate);
		humanSearch.setStartDate(staDate);
		humanSearch.setEndDate(endDate);
		humanSearch.setStateType(PlatformConst.EMPLOYEE_STATE_PRESENCE);
		// ??????ID???????????????
		return humanSearch.getHumanDtoMap();
	}
	
	@Override
	public TimeSettingDtoInterface getTimeSetting(String personalId, Date targetDate) throws MospException {
		// ????????????ID????????????????????????????????????????????????
		setTimeSettings(personalId, targetDate);
		return timeSettingDto;
	}
	
	@Override
	public TimeSettingDtoInterface getTimeSetting(ApplicationDtoInterface dto, Date targetDate) throws MospException {
		// ???????????????????????????
		applicationDto = dto;
		// ????????????????????????
		applicationRefer.chkExistApplication(applicationDto, targetDate);
		// ??????????????????
		if (mospParams.hasErrorMessage()) {
			return null;
		}
		// ???????????????????????????????????????????????????
		timeSettingDto = timeSettingRefer.getTimeSettingInfo(applicationDto.getWorkSettingCode(), targetDate);
		// ????????????????????????
		timeSettingRefer.chkExistTimeSetting(timeSettingDto, targetDate);
		return timeSettingDto;
	}
	
	@Override
	public TimeSettingDtoInterface getTimeSettingNoMessage(String personalId, Date targetDate) throws MospException {
		// ??????????????????????????????
		if (hasApplicationSettings(personalId, targetDate) == false) {
			return null;
		}
		// ??????????????????
		if (hasTimeSettings(personalId, targetDate) == false) {
			return null;
		}
		// ??????????????????
		return timeSettingDto;
	}
	
	@Override
	public int getNoApproval(String cutoffCode, Date targetDate) throws MospException {
		// ??????????????????
		CutoffDtoInterface cutoffDto = getCutoff(cutoffCode, targetDate);
		if (mospParams.hasErrorMessage()) {
			return 0;
		}
		// ?????????????????????
		return cutoffDto.getNoApproval();
	}
	
	@Override
	public int getNoApproval(String cutoffCode, int targetYear, int targetMonth) throws MospException {
		// ??????????????????
		CutoffDtoInterface cutoffDto = getCutoff(cutoffCode, targetYear, targetMonth);
		if (mospParams.hasErrorMessage()) {
			return 0;
		}
		// ?????????????????????
		return cutoffDto.getNoApproval();
	}
	
}
