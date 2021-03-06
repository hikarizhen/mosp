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
package jp.mosp.time.management.action;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import jp.mosp.framework.base.BaseDtoInterface;
import jp.mosp.framework.base.BaseVo;
import jp.mosp.framework.base.MospException;
import jp.mosp.framework.constant.MospConst;
import jp.mosp.framework.property.MospProperties;
import jp.mosp.framework.utils.DateUtility;
import jp.mosp.framework.utils.MospUtility;
import jp.mosp.framework.utils.TopicPathUtility;
import jp.mosp.platform.bean.human.HumanReferenceBeanInterface;
import jp.mosp.platform.comparator.base.EmployeeCodeComparator;
import jp.mosp.platform.constant.PlatformConst;
import jp.mosp.platform.constant.PlatformMessageConst;
import jp.mosp.platform.dto.human.HumanDtoInterface;
import jp.mosp.platform.utils.MonthUtility;
import jp.mosp.time.base.TimeAction;
import jp.mosp.time.bean.SubordinateSearchBeanInterface;
import jp.mosp.time.bean.TotalTimeCalcBeanInterface;
import jp.mosp.time.constant.TimeConst;
import jp.mosp.time.dto.settings.SubordinateListDtoInterface;
import jp.mosp.time.dto.settings.TotalTimeDataDtoInterface;
import jp.mosp.time.input.action.AttendanceListAction;
import jp.mosp.time.input.action.ScheduleReferenceAction;
import jp.mosp.time.management.vo.SubordinateListVo;
import jp.mosp.time.settings.base.TimeSettingAction;

/**
 * ??????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????<br>
 * <br>
 * ?????????????????????????????????<br>
 * <ul><li>
 * {@link #CMD_SHOW}
 * </li><li>
 * {@link #CMD_SEARCH}
 * </li><li>
 * {@link #CMD_RE_SHOW}
 * </li><li>
 * {@link #CMD_TRANSFER}
 * </li><li>
 * {@link #CMD_CALC}
 * </li><li>
 * {@link #CMD_SORT}
 * </li><li>
 * {@link #CMD_PAGE}
 * </li><li>
 * {@link #CMD_SET_ACTIVATION_DATE}
 * </li><li>
 * {@link #CMD_OUTPUT}
 * </li><li>
 * {@link #CMD_SCHEDULE}
 * </li><li>
 * {@link #CMD_PAID_HOLIDAY_USAGE}
 * </li><li>
 * {@link #CMD_SHOW_APPROVED}
 * </li></ul>
 */
public class SubordinateListAction extends TimeSettingAction {
	
	/**
	 * ?????????????????????<br>
	 * <br>
	 * ???????????????????????????????????????????????????????????????????????????????????????????????????????????????<br>
	 */
	public static final String	CMD_SHOW				= "TM2100";
	
	/**
	 * ?????????????????????<br>
	 * <br>
	 * ?????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????<br>
	 * ???????????????????????????????????????????????????????????????<br>
	 */
	public static final String	CMD_SEARCH				= "TM2102";
	
	/**
	 * ????????????????????????<br>
	 * <br>
	 * ?????????????????????????????????????????????????????????????????????????????????????????????????????????
	 * ???????????????????????????????????????????????????????????????????????????????????????<br>
	 */
	public static final String	CMD_RE_SHOW				= "TM2103";
	
	/**
	 * ???????????????????????????<br>
	 * <br>
	 * ???????????????????????????????????????????????????ID??????????????????MosP????????????????????????????????????????????????<br>
	 */
	public static final String	CMD_TRANSFER			= "TM2105";
	
	/**
	 * ???????????????????????????<br>
	 * <br>
	 * ??????????????????????????????????????????????????????????????????????????????????????????????????????????????????
	 * ?????????????????????????????????????????????????????????????????????????????????????????????????????????<br>
	 * ???????????????????????????????????????????????????????????????????????????????????????????????????<br>
	 */
	public static final String	CMD_CALC				= "TM2106";
	
	/**
	 * ????????????????????????<br>
	 * <br>
	 * ?????????????????????????????????????????????????????????????????????????????????????????????????????????<br>
	 * ??????????????????????????????????????????????????????????????????????????????????????????<br>
	 */
	public static final String	CMD_SORT				= "TM2108";
	
	/**
	 * ??????????????????????????????<br>
	 * <br>
	 * ?????????????????????????????????????????????100????????????????????????????????????????????????????????????????????????<br>
	 */
	public static final String	CMD_PAGE				= "TM2109";
	
	/**
	 * ?????????????????????????????????<br>
	 * <br>
	 * ?????????????????????????????????????????????????????????????????????????????????????????????????????????????????????
	 * ???????????????????????????????????????????????????????????????<br>
	 */
	public static final String	CMD_SET_ACTIVATION_DATE	= "TM2190";
	
	/**
	 * ??????????????????????????????<br>
	 * <br>
	 * ??????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????<br>
	 */
	public static final String	CMD_OUTPUT				= "TM2196";
	
	/**
	 * ??????????????????????????????<br>
	 * <br>
	 * ??????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????<br>
	 */
	public static final String	CMD_SCHEDULE			= "TM2197";
	
	/**
	 * ?????????????????????????????????????????????????????????<br>
	 * <br>
	 * ?????????????????????????????????????????????????????????????????????????????????????????????????????????<br>
	 * ???????????????????????????????????????????????????????????????????????????<br>
	 */
	public static final String	CMD_PAID_HOLIDAY_USAGE	= "TM2198";
	
	/**
	 * ??????????????????(???????????????????????????)???<br>
	 * <br>
	 * ???????????????????????????????????????????????????????????????????????????????????????????????????????????????<br>
	 * ??????????????????????????????????????????????????????<br>
	 */
	public static final String	CMD_SHOW_APPROVED		= "TM2410";
	
	
	/**
	 * {@link TimeAction#TimeAction()}??????????????????<br>
	 */
	public SubordinateListAction() {
		super();
		// ??????????????????????????????????????????
		topicPathCommand = CMD_RE_SHOW;
	}
	
	@Override
	protected BaseVo getSpecificVo() {
		return new SubordinateListVo();
	}
	
	@Override
	public void action() throws MospException {
		// ????????????????????????
		if (mospParams.getCommand().equals(CMD_SHOW)) {
			// ??????
			prepareVo(false, false);
			show();
		} else if (mospParams.getCommand().equals(CMD_SHOW_APPROVED)) {
			// ??????
			prepareVo(false, false);
			show();
		} else if (mospParams.getCommand().equals(CMD_SEARCH)) {
			// ??????
			prepareVo();
			search();
		} else if (mospParams.getCommand().equals(CMD_RE_SHOW)) {
			// ?????????
			prepareVo(true, false);
			reShowJudging();
		} else if (mospParams.getCommand().equals(CMD_TRANSFER)) {
			// ??????
			prepareVo(true, false);
			transfer();
		} else if (mospParams.getCommand().equals(CMD_CALC)) {
			// ????????????
			prepareVo();
			calc();
		} else if (mospParams.getCommand().equals(CMD_SORT)) {
			// ?????????
			prepareVo();
			sort();
		} else if (mospParams.getCommand().equals(CMD_PAGE)) {
			// ???????????????
			prepareVo();
			page();
		} else if (mospParams.getCommand().equals(CMD_SET_ACTIVATION_DATE)) {
			// ??????????????????
			prepareVo();
			setActivateDate();
		} else if (mospParams.getCommand().equals(CMD_OUTPUT)) {
			// ???????????????
			prepareVo();
			outputAttendanceBooks();
		} else if (mospParams.getCommand().equals(CMD_SCHEDULE)) {
			// ???????????????
			prepareVo();
			outputScheduleBooks();
		} else if (mospParams.getCommand().equals(CMD_PAID_HOLIDAY_USAGE)) {
			// ??????????????????????????????????????????
			prepareVo();
			outputPaidHolidayUsage();
		}
	}
	
	/**
	 * ???????????????????????????<br>
	 * @throws MospException ???????????????
	 */
	protected void reShowJudging() throws MospException {
		// APP_VIEW_TOTAL_VALUES???true?????????
		if (mospParams.getApplicationPropertyBool(TimeConst.APP_VIEW_TOTAL_VALUES)) {
			return;
		} else {
			// ?????????
			search();
		}
	}
	
	/**
	 * ??????????????????????????????<br>
	 * @throws MospException ???????????????
	 */
	protected void show() throws MospException {
		// VO??????
		SubordinateListVo vo = (SubordinateListVo)mospParams.getVo();
		// ????????????????????????
		vo.setShowCommand(mospParams.getCommand());
		// ????????????????????????
		if (vo.getShowCommand().equals(CMD_SHOW_APPROVED)) {
			// ?????????????????????????????????(???????????????????????????)
			TopicPathUtility.setTopicPathName(mospParams, vo.getClassName(), mospParams.getName("AttendanceHumanList"));
		}
		// ???????????????
		setDefaultValues();
		// ?????????????????????
		setPageInfo(CMD_PAGE, getListLength());
		// ?????????????????????
		setPulldown();
		// ?????????????????????
		vo.setComparatorName(EmployeeCodeComparator.class.getName());
	}
	
	/**
	 * ????????????????????????<br>
	 * @throws MospException ????????????????????????????????????????????????
	 */
	protected void search() throws MospException {
		// VO??????
		SubordinateListVo vo = (SubordinateListVo)mospParams.getVo();
		// ?????????????????????
		int targetYear = getInt(vo.getPltSearchRequestYear());
		int targetMonth = getInt(vo.getPltSearchRequestMonth());
		// ?????????????????????
		SubordinateSearchBeanInterface search = timeReference().subordinateSearch();
		// ????????????????????????
		if (vo.getModeActivateDate().equals(PlatformConst.MODE_ACTIVATE_DATE_CHANGING)) {
			// ??????????????????????????????(????????????????????????????????????)
			mospParams.addErrorMessage(PlatformMessageConst.MSG_EFFECTIVE_DAY);
			return;
		}
		// ??????????????????
		checkSearchCondition(vo.getTxtSearchEmployeeCode(), vo.getTxtSearchEmployeeName(), vo.getPltSearchWorkPlace(),
				vo.getPltSearchEmployment(), vo.getPltSearchSection(), vo.getPltSearchPosition(),
				vo.getPltSearchApproval(), vo.getPltSearchCalc(), vo.getPltSearchHumanType());
		if (mospParams.hasErrorMessage()) {
			return;
		}
		// VO?????????????????????????????????
		search.setTargetDate(getSearchDate());
		search.setEmployeeCode(vo.getTxtSearchEmployeeCode());
		search.setEmployeeName(vo.getTxtSearchEmployeeName());
		search.setWorkPlaceCode(vo.getPltSearchWorkPlace());
		search.setEmploymentContractCode(vo.getPltSearchEmployment());
		search.setSectionCode(vo.getPltSearchSection());
		search.setPositionCode(vo.getPltSearchPosition());
		search.setApproval(vo.getPltSearchApproval());
		search.setApprovalBeforeDay(vo.getCkbYesterday());
		search.setCalc(vo.getPltSearchCalc());
		search.setHumanType(vo.getPltSearchHumanType());
		search.setStartDate(MonthUtility.getYearMonthTermFirstDate(targetYear, targetMonth, mospParams));
		search.setEndDate(MonthUtility.getYearMonthTermLastDate(targetYear, targetMonth, mospParams));
		search.setTargetYear(targetYear);
		search.setTargetMonth(targetMonth);
		// ??????
		List<SubordinateListDtoInterface> list = search.getSubordinateList();
		// ???????????????????????????
		vo.setList(list);
		// ??????????????????????????????????????????????????????
		vo.setComparatorName(EmployeeCodeComparator.class.getName());
		vo.setAscending(false);
		// ?????????
		sort();
		// ???????????????????????????
		initCkbSelect();
		// ??????????????????
		if (list.isEmpty()) {
			// ???????????????????????????????????????
			addNoSearchResultMessage();
		}
	}
	
	/**
	 * ????????????ID??????????????????MosP???????????????????????????
	 * ??????Action??????????????????????????????????????????????????????????????????<br>
	 */
	protected void transfer() {
		// VO??????
		SubordinateListVo vo = (SubordinateListVo)mospParams.getVo();
		// ??????Action??????????????????
		String actionName = getTransferredAction();
		// MosP???????????????????????????ID?????????
		setTargetPersonalId(getSelectedPersonalId(getTransferredIndex()));
		// MosP????????????????????????????????????
		setTargetYear(getInt(vo.getPltSearchRequestYear()));
		setTargetMonth(getInt(vo.getPltSearchRequestMonth()));
		// ??????Action????????????????????????
		if (actionName.equals(AttendanceListAction.class.getName())) {
			// ????????????????????????
			if (vo.getShowCommand().equals(SubordinateListAction.CMD_SHOW_APPROVED)) {
				// ??????????????????(?????????????????????)?????????(??????????????????????????????)
				mospParams.setNextCommand(AttendanceListAction.CMD_SHOW_APPROVAL);
			} else {
				mospParams.addGeneralParam(TimeConst.PRM_ROLL_ARRAY, getArray());
				// ???????????????????????????(??????????????????????????????)
				mospParams.setNextCommand(AttendanceListAction.CMD_SELECT_SHOW);
			}
		} else if (actionName.equals(ScheduleReferenceAction.class.getName())) {
			// ???????????????????????????(??????????????????????????????)
			mospParams.setNextCommand(ScheduleReferenceAction.CMD_SELECT_SHOW);
		}
	}
	
	/**
	 * ??????????????????????????????<br>
	 * @throws MospException ????????????????????????????????????SQL???????????????????????????
	 */
	protected void calc() throws MospException {
		// VO??????
		SubordinateListVo vo = (SubordinateListVo)mospParams.getVo();
		HumanReferenceBeanInterface human = reference().human();
		// ?????????????????????
		SubordinateSearchBeanInterface search = timeReference().subordinateSearch();
		// ???????????????????????????
		TotalTimeCalcBeanInterface total = time().totalTimeCalc();
		// VO?????????????????????????????????
		search.setTargetDate(getSearchDate());
		// ??????????????????????????????????????????????????????
		List<SubordinateListDtoInterface> list = getSelectedListDto();
		// ?????????????????????????????????????????????
		for (SubordinateListDtoInterface dto : list) {
			// ??????ID???????????????????????????
			String personalId = dto.getPersonalId();
			int targetYear = dto.getTargetYear();
			int targetMonth = dto.getTargetMonth();
			// ????????????
			TotalTimeDataDtoInterface totalTimeDataDto = total.calc(personalId, targetYear, targetMonth, true);
			// ????????????????????????????????????????????????
			search.setTotalTimeData(dto, totalTimeDataDto);
			HumanDtoInterface humanDto = human.getHumanInfo(dto.getPersonalId(), getSearchDate());
			// ???????????????????????????
			search.setLimitStandard(dto, humanDto);
		}
		// ???????????????????????????
		vo.setList(list);
		// ??????????????????????????????????????????????????????
		vo.setComparatorName(EmployeeCodeComparator.class.getName());
		vo.setAscending(false);
		// ?????????
		sort();
		// ???????????????????????????
		initCkbSelect();
		// ??????????????????
		if (list.isEmpty()) {
			// ???????????????????????????????????????
			addNoSearchResultMessage();
		}
	}
	
	/**
	 * ????????????????????????????????????<br>
	 * @throws MospException ???????????????????????????????????????????????????????????????
	 */
	protected void sort() throws MospException {
		setVoList(sortList(getTransferredSortKey()));
	}
	
	/**
	 * ????????????????????????????????????
	 * @throws MospException ???????????????
	 */
	protected void page() throws MospException {
		setVoList(pageList());
	}
	
	/**
	 * ????????????????????????????????????<br>
	 * ?????????????????????????????????????????????????????????????????????????????????????????????<br>
	 * @throws MospException ?????????????????????????????????????????????
	 */
	protected void setActivateDate() throws MospException {
		// VO??????
		SubordinateListVo vo = (SubordinateListVo)mospParams.getVo();
		// ??????????????????
		checkSubordinateAvailable(getSearchDate());
		if (mospParams.hasErrorMessage()) {
			return;
		}
		// ????????????????????????????????????
		if (vo.getModeActivateDate().equals(PlatformConst.MODE_ACTIVATE_DATE_CHANGING)) {
			// ????????????????????????
			vo.setModeActivateDate(PlatformConst.MODE_ACTIVATE_DATE_FIXED);
			// ?????????????????????
			setPulldown();
		} else {
			// ????????????????????????
			vo.setModeActivateDate(PlatformConst.MODE_ACTIVATE_DATE_CHANGING);
			// ????????????????????????
			setInitPulldown();
		}
		// ??????????????????
		// ?????????????????????
		List<BaseDtoInterface> list = new ArrayList<BaseDtoInterface>();
		// VO????????????
		vo.setList(list);
		// ????????????????????????
		setVoList(list);
	}
	
	/**
	 * ???????????????????????????<br>
	 * @throws MospException ???????????????
	 */
	public void setDefaultValues() throws MospException {
		// VO??????
		SubordinateListVo vo = (SubordinateListVo)mospParams.getVo();
		// ??????????????????
		vo.setTxtSearchEmployeeCode("");
		vo.setTxtSearchEmployeeName("");
		vo.setPltSearchWorkPlace("");
		vo.setPltSearchEmployment("");
		vo.setPltSearchSection("");
		vo.setPltSearchPosition("");
		vo.setPltSearchApproval("");
		vo.setCkbYesterday(MospConst.CHECKBOX_OFF);
		vo.setPltSearchCalc("");
		// ????????????????????????(??????)
		vo.setPltSearchHumanType(String.valueOf(TimeConst.CODE_SUBORDINATE_SEARCH_TYPE_SUBORDINATE));
		// ????????????????????????????????????
		if (vo.getShowCommand().equals(CMD_SHOW_APPROVED)) {
			// ???????????????????????????(?????????????????????)
			vo.setPltSearchHumanType(String.valueOf(TimeConst.CODE_SUBORDINATE_SEARCH_TYPE_APPROVER));
		}
		// ????????????????????????
		vo.setModeActivateDate(PlatformConst.MODE_ACTIVATE_DATE_CHANGING);
		// ??????????????????(??????????????????)?????????????????????
		Date targetYearMonth = MonthUtility.getTargetYearMonth(getSystemDate(), mospParams);
		// ??????????????????????????????????????????(???????????????)
		vo.setAryPltRequestYear(getYearArray(DateUtility.getYear(targetYearMonth)));
		vo.setAryPltRequestMonth(getMonthArray());
		// ?????????????????????????????????????????????????????????(??????????????????)
		vo.setPltSearchRequestYear(DateUtility.getStringYear(targetYearMonth));
		vo.setPltSearchRequestMonth(DateUtility.getStringMonthM(targetYearMonth));
		// ??????????????????
		checkSubordinateAvailable(getSearchDate());
		if (mospParams.hasErrorMessage()) {
			return;
		}
		// ????????????????????????
		vo.setModeActivateDate(PlatformConst.MODE_ACTIVATE_DATE_FIXED);
		vo.setJsSearchConditionRequired(isSearchConditionRequired());
	}
	
	/**
	 * ?????????????????????
	 * @throws MospException ???????????????
	 */
	private void setPulldown() throws MospException {
		// VO??????
		SubordinateListVo vo = (SubordinateListVo)mospParams.getVo();
		// ??????????????????
		String operationType = MospConst.OPERATION_TYPE_REFER;
		// ????????????????????????
		if (vo.getShowCommand().equals(CMD_SHOW_APPROVED)) {
			// ????????????????????????????????????(?????????????????????????????????????????????)
			operationType = null;
		}
		// ?????????
		String[][] workPlace = reference().workPlace().getNameSelectArray(getSearchDate(), true, operationType);
		vo.setAryPltWorkPlace(workPlace);
		// ????????????
		String[][] aryEmployment = reference().employmentContract().getNameSelectArray(getSearchDate(), true,
				operationType);
		vo.setAryPltEmployment(aryEmployment);
		// ??????
		String[][] arySection = reference().section().getCodedSelectArray(getSearchDate(), true, operationType);
		vo.setAryPltSection(arySection);
		// ??????
		String[][] aryPosition = reference().position().getCodedSelectArray(getSearchDate(), true, operationType);
		vo.setAryPltPosition(aryPosition);
		MospProperties properties = mospParams.getProperties();
		// ?????????
		vo.setAryPltApproval(properties.getCodeArray(TimeConst.CODE_NOT_APPROVED, true));
		// ?????????
		vo.setAryPltCalc(properties.getCodeArray(TimeConst.CODE_CUTOFFSTATE, true));
		// ??????????????????
		vo.setAryPltHumanType(properties.getCodeArray(TimeConst.CODE_SUBORDINATE_SEARCH_TYPE, true));
		// ??????????????????????????????????????????(???????????????)
		vo.setAryPltRequestYear(getYearArray(MospUtility.getInt(vo.getPltSearchRequestYear())));
	}
	
	/**
	 * ??????????????????????????????
	 * @throws MospException ???????????????
	 */
	private void setInitPulldown() throws MospException {
		// VO??????
		SubordinateListVo vo = (SubordinateListVo)mospParams.getVo();
		// ????????????????????????
		// ?????????
		String[][] initPulldown = getInputActivateDatePulldown();
		vo.setAryPltWorkPlace(initPulldown);
		// ????????????
		vo.setAryPltEmployment(initPulldown);
		// ??????
		vo.setAryPltSection(initPulldown);
		// ??????
		vo.setAryPltPosition(initPulldown);
	}
	
	/**
	 * ?????????????????????????????????VO??????????????????<br>
	 * @param list ???????????????
	 * @throws MospException ???????????????
	 */
	protected void setVoList(List<? extends BaseDtoInterface> list) throws MospException {
		// VO??????
		SubordinateListVo vo = (SubordinateListVo)mospParams.getVo();
		// ????????????????????????
		String[] aryLblEmployeeCode = new String[list.size()];
		String[] aryLblEmployeeName = new String[list.size()];
		String[] aryLblSection = new String[list.size()];
		String[] aryLblWorkDate = new String[list.size()];
		String[] aryLblWorkTime = new String[list.size()];
		String[] aryLblRestTime = new String[list.size()];
		String[] aryLblPrivateTime = new String[list.size()];
		String[] aryLblLateTime = new String[list.size()];
		String[] aryLblLeaveEarlyTime = new String[list.size()];
		String[] aryLblLateLeaveEarlyTime = new String[list.size()];
		String[] aryLblOverTimeIn = new String[list.size()];
		String[] aryLblOverTimeOut = new String[list.size()];
		String[] aryLblWorkOnHolidayTime = new String[list.size()];
		String[] aryLblLateNightTime = new String[list.size()];
		String[] aryLblPaidHoliday = new String[list.size()];
		String[] aryLblAllHoliday = new String[list.size()];
		String[] aryLblAbsence = new String[list.size()];
		String[] aryLblApploval = new String[list.size()];
		String[] aryLblCalc = new String[list.size()];
		String[] aryLblCorrection = new String[list.size()];
		String[] aryOvertimeOutStyle = new String[list.size()];
		String[] claApploval = new String[list.size()];
		String[] claCalc = new String[list.size()];
		// ???????????????
		for (int i = 0; i < list.size(); i++) {
			// ??????????????????????????????
			SubordinateListDtoInterface dto = (SubordinateListDtoInterface)list.get(i);
			// ????????????????????????
			aryLblEmployeeCode[i] = dto.getEmployeeCode();
			aryLblEmployeeName[i] = getLastFirstName(dto.getLastName(), dto.getFirstName());
			aryLblSection[i] = reference().section().getSectionAbbr(dto.getSectionCode(), getSearchDate());
			aryLblWorkDate[i] = getNumberString(dto.getWorkDate(), 1);
			aryLblWorkTime[i] = toTimeDotFormatString(dto.getWorkTime());
			aryLblRestTime[i] = toTimeDotFormatString(dto.getRestTime());
			aryLblPrivateTime[i] = toTimeDotFormatString(dto.getPrivateTime());
			aryLblLateTime[i] = toTimeDotFormatString(dto.getLateTime());
			aryLblLeaveEarlyTime[i] = toTimeDotFormatString(dto.getLeaveEarlyTime());
			aryLblLateLeaveEarlyTime[i] = toTimeDotFormatString(dto.getLateLeaveEarlyTime());
			aryLblOverTimeIn[i] = toTimeDotFormatString(dto.getOverTimeIn());
			aryOvertimeOutStyle[i] = dto.getOvertimeOutStyle();
			aryLblOverTimeOut[i] = toTimeDotFormatString(dto.getOverTimeOut());
			aryLblWorkOnHolidayTime[i] = toTimeDotFormatString(dto.getWorkOnHolidayTime());
			aryLblLateNightTime[i] = toTimeDotFormatString(dto.getLateNightTime());
			aryLblPaidHoliday[i] = getNumberString(dto.getPaidHoliday(), 1);
			aryLblAllHoliday[i] = getNumberString(dto.getAllHoliday(), 1);
			aryLblAbsence[i] = getNumberString(dto.getAbsence(), 1);
			claApploval[i] = dto.getApprovalStateClass();
			aryLblApploval[i] = dto.getApproval();
			claCalc[i] = dto.getCutoffStateClass();
			aryLblCalc[i] = dto.getCalc();
			aryLblCorrection[i] = dto.getCorrection();
		}
		// ????????????VO?????????
		// ????????????????????????
		vo.setAryLblEmployeeCode(aryLblEmployeeCode);
		vo.setAryLblEmployeeName(aryLblEmployeeName);
		vo.setAryLblSection(aryLblSection);
		vo.setAryLblWorkDate(aryLblWorkDate);
		vo.setAryLblWorkTime(aryLblWorkTime);
		vo.setAryLblRestTime(aryLblRestTime);
		vo.setAryLblPrivateTime(aryLblPrivateTime);
		vo.setAryLblLateTime(aryLblLateTime);
		vo.setAryLblLeaveEarlyTime(aryLblLeaveEarlyTime);
		vo.setAryLblLateLeaveEarlyTime(aryLblLateLeaveEarlyTime);
		vo.setAryLblOverTimeIn(aryLblOverTimeIn);
		vo.setAryLblOverTimeOut(aryLblOverTimeOut);
		vo.setAryLblWorkOnHolidayTime(aryLblWorkOnHolidayTime);
		vo.setAryLblLateNightTime(aryLblLateNightTime);
		vo.setAryLblPaidHoliday(aryLblPaidHoliday);
		vo.setAryLblAllHoliday(aryLblAllHoliday);
		vo.setAryLblAbsence(aryLblAbsence);
		vo.setClaApploval(claApploval);
		vo.setAryLblApploval(aryLblApploval);
		vo.setAryOvertimeOutStyle(aryOvertimeOutStyle);
		vo.setClaCalc(claCalc);
		vo.setAryLblCalc(aryLblCalc);
		vo.setAryLblCorrection(aryLblCorrection);
	}
	
	/**
	 * ??????(?????????)?????????????????????????????????????????????????????????<br>
	 * @throws MospException ????????????????????????????????????
	 */
	protected void outputAttendanceBooks() throws MospException {
		// VO??????
		SubordinateListVo vo = (SubordinateListVo)mospParams.getVo();
		// ??????ID????????????
		String[] personalIds = getSelectedPersonalIds(vo.getCkbSelect());
		// ??????????????????(VO??????)
		int year = getInt(vo.getPltSearchRequestYear());
		int month = getInt(vo.getPltSearchRequestMonth());
		// ???????????????????????????
		timeReference().attendanceBook().makeAttendanceBooks(personalIds, year, month);
		// ???????????????
		if (mospParams.hasErrorMessage()) {
			// ?????????????????????
			addNoSearchResultMessage();
		}
	}
	
	/**
	 * ??????(?????????)?????????????????????????????????????????????????????????<br>
	 * @throws MospException ????????????????????????????????????
	 */
	protected void outputScheduleBooks() throws MospException {
		// VO??????
		SubordinateListVo vo = (SubordinateListVo)mospParams.getVo();
		// ??????ID????????????
		String[] personalIds = getSelectedPersonalIds(vo.getCkbSelect());
		// ??????????????????(VO??????)
		int year = getInt(vo.getPltSearchRequestYear());
		int month = getInt(vo.getPltSearchRequestMonth());
		// ???????????????????????????
		timeReference().scheduleBook().makeScheduleBooks(personalIds, year, month);
		// ???????????????
		if (mospParams.hasErrorMessage()) {
			// ?????????????????????
			addNoSearchResultMessage();
		}
	}
	
	/**
	 * ??????(????????????????????????????????????)?????????????????????????????????????????????????????????<br>
	 * @throws MospException ????????????????????????????????????
	 */
	protected void outputPaidHolidayUsage() throws MospException {
		// VO??????
		SubordinateListVo vo = (SubordinateListVo)mospParams.getVo();
		// ??????ID????????????
		String[] personalIds = getSelectedPersonalIds(vo.getCkbSelect());
		// ??????????????????(VO??????)
		int year = getInt(vo.getPltSearchRequestYear());
		int month = getInt(vo.getPltSearchRequestMonth());
		// ??????????????????????????????????????????????????????
		timeReference().paidHolidayUsageExport().export(personalIds, year, month);
	}
	
	/**
	 * VO???????????????????????????????????????<br>
	 * @return ???????????????
	 * @throws MospException ?????????????????????????????????
	 */
	protected Date getSearchDate() throws MospException {
		// VO??????
		SubordinateListVo vo = (SubordinateListVo)mospParams.getVo();
		// ???????????????????????????
		return MonthUtility.getYearMonthTargetDate(getInt(vo.getPltSearchRequestYear()),
				getInt(vo.getPltSearchRequestMonth()), mospParams);
	}
	
	/**
	 * ???????????????????????????????????????????????????????????????<br>
	 * <br>
	 * @return ???????????????????????????
	 */
	protected List<SubordinateListDtoInterface> getSelectedListDto() {
		// VO??????
		SubordinateListVo vo = (SubordinateListVo)mospParams.getVo();
		// ?????????????????????????????????????????????
		List<SubordinateListDtoInterface> selectedList = new ArrayList<SubordinateListDtoInterface>();
		// ???????????????????????????????????????????????????
		for (String idx : vo.getCkbSelect()) {
			// ??????????????????????????????????????????????????????
			selectedList.add((SubordinateListDtoInterface)getSelectedListDto(idx));
		}
		// ??????????????????????????????????????????????????????????????????
		return selectedList;
	}
	
}
