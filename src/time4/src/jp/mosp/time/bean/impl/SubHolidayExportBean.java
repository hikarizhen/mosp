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
import java.util.List;

import jp.mosp.framework.base.MospException;
import jp.mosp.framework.base.MospParams;
import jp.mosp.framework.constant.MospConst;
import jp.mosp.framework.utils.DateUtility;
import jp.mosp.framework.utils.MospUtility;
import jp.mosp.orangesignal.OrangeSignalUtility;
import jp.mosp.platform.base.PlatformBean;
import jp.mosp.platform.bean.human.HumanSearchBeanInterface;
import jp.mosp.platform.bean.system.SectionReferenceBeanInterface;
import jp.mosp.platform.bean.workflow.WorkflowIntegrateBeanInterface;
import jp.mosp.platform.constant.PlatformFileConst;
import jp.mosp.platform.constant.PlatformMessageConst;
import jp.mosp.platform.dao.file.ExportDaoInterface;
import jp.mosp.platform.dao.file.ExportFieldDaoInterface;
import jp.mosp.platform.dao.human.impl.PfmHumanDao;
import jp.mosp.platform.dto.file.ExportDtoInterface;
import jp.mosp.platform.dto.file.ExportFieldDtoInterface;
import jp.mosp.platform.dto.human.HumanDtoInterface;
import jp.mosp.time.bean.ApplicationReferenceBeanInterface;
import jp.mosp.time.bean.CutoffUtilBeanInterface;
import jp.mosp.time.bean.SubHolidayExportBeanInterface;
import jp.mosp.time.bean.SubHolidayReferenceBeanInterface;
import jp.mosp.time.bean.TimeSettingReferenceBeanInterface;
import jp.mosp.time.constant.TimeConst;
import jp.mosp.time.constant.TimeFileConst;
import jp.mosp.time.dao.settings.SubHolidayRequestDaoInterface;
import jp.mosp.time.dao.settings.impl.TmdSubHolidayDao;
import jp.mosp.time.dto.settings.ApplicationDtoInterface;
import jp.mosp.time.dto.settings.CutoffDtoInterface;
import jp.mosp.time.dto.settings.SubHolidayDtoInterface;
import jp.mosp.time.dto.settings.SubHolidayRequestDtoInterface;
import jp.mosp.time.dto.settings.TimeSettingDtoInterface;
import jp.mosp.time.utils.TimeUtility;

/**
 * ?????????????????????????????????????????????
 */
public class SubHolidayExportBean extends PlatformBean implements SubHolidayExportBeanInterface {
	
	/**
	 * ???????????????????????????DAO???<br>
	 */
	protected ExportDaoInterface				exportDao;
	
	/**
	 * ???????????????????????????????????????DAO???<br>
	 */
	protected ExportFieldDaoInterface			exportFieldDao;
	
	/**
	 * ?????????????????????????????????<br>
	 */
	protected SubHolidayReferenceBeanInterface	subHolidayReference;
	
	/**
	 * ????????????DAO???<br>
	 */
	protected SubHolidayRequestDaoInterface		subHolidayRequestDao;
	
	/**
	 * ????????????????????????????????????<br>
	 */
	protected WorkflowIntegrateBeanInterface	workflowIntegrate;
	
	/**
	 * ????????????????????????????????????<br>
	 */
	protected ApplicationReferenceBeanInterface	applicationReference;
	
	/**
	 * ??????????????????????????????<br>
	 */
	protected TimeSettingReferenceBeanInterface	timeSettingReference;
	
	/**
	 * ??????????????????????????????<br>
	 */
	protected CutoffUtilBeanInterface			cutoffUtil;
	
	/**
	 * ?????????????????????????????????<br>
	 */
	protected HumanSearchBeanInterface			humanSearch;
	
	/**
	 * ?????????????????????????????????<br>
	 */
	protected SectionReferenceBeanInterface		sectionReference;
	
	/**
	 * ?????????????????????????????????<br>
	 */
	protected List<HumanDtoInterface>			humanList;
	
	/**
	 * ?????????????????????????????????????????????
	 */
	private int									ckbNeedLowerSection	= 0;
	
	
	/**
	 * {@link PlatformBean#PlatformBean()}??????????????????<br>
	 */
	public SubHolidayExportBean() {
		super();
	}
	
	/**
	 * {@link PlatformBean#PlatformBean(MospParams, Connection)}??????????????????<br>
	 * @param mospParams MosP????????????
	 * @param connection DB??????????????????
	 */
	public SubHolidayExportBean(MospParams mospParams, Connection connection) {
		super(mospParams, connection);
	}
	
	@Override
	public void initBean() throws MospException {
		// DAO?????????
		exportDao = (ExportDaoInterface)createDao(ExportDaoInterface.class);
		exportFieldDao = (ExportFieldDaoInterface)createDao(ExportFieldDaoInterface.class);
		subHolidayReference = (SubHolidayReferenceBeanInterface)createBean(SubHolidayReferenceBeanInterface.class);
		subHolidayRequestDao = (SubHolidayRequestDaoInterface)createDao(SubHolidayRequestDaoInterface.class);
		workflowIntegrate = (WorkflowIntegrateBeanInterface)createBean(WorkflowIntegrateBeanInterface.class);
		cutoffUtil = (CutoffUtilBeanInterface)createBean(CutoffUtilBeanInterface.class);
		humanSearch = (HumanSearchBeanInterface)createBean(HumanSearchBeanInterface.class);
		applicationReference = (ApplicationReferenceBeanInterface)createBean(ApplicationReferenceBeanInterface.class);
		timeSettingReference = (TimeSettingReferenceBeanInterface)createBean(TimeSettingReferenceBeanInterface.class);
		sectionReference = (SectionReferenceBeanInterface)createBean(SectionReferenceBeanInterface.class);
	}
	
	@Override
	public void export(String exportCode, int startYear, int startMonth, int endYear, int endMonth, String cutoffCode,
			String workPlaceCode, String employmentContractCode, String sectionCode, int ckbNeedLowerSection,
			String positionCode) throws MospException {
		// ???????????????????????????????????????????????????
		this.ckbNeedLowerSection = ckbNeedLowerSection;
		// ??????????????????????????????
		ExportDtoInterface dto = exportDao.findForKey(exportCode);
		if (dto == null) {
			// ????????????????????????????????????????????????????????????
			addNoExportDataMessage();
			return;
		}
		// ?????????????????????
		CutoffDtoInterface cutoffDto = cutoffUtil.getCutoff(cutoffCode, startYear, startMonth);
		if (mospParams.hasErrorMessage()) {
			return;
		}
		// ???????????????????????????????????????
		Date startDate = TimeUtility.getCutoffFirstDate(cutoffDto.getCutoffDate(), startYear, startMonth);
		Date endDate = TimeUtility.getCutoffLastDate(cutoffDto.getCutoffDate(), endYear, endMonth);
		// CSV???????????????????????????
		List<String[]> list = getCsvDataList(dto, startDate, endDate, cutoffCode, workPlaceCode, employmentContractCode,
				sectionCode, positionCode);
		// CSV????????????????????????
		if (list.isEmpty()) {
			// ????????????????????????????????????????????????????????????
			addNoExportDataMessage();
			return;
		}
		// CSV?????????????????????MosP?????????????????????
		mospParams.setFile(OrangeSignalUtility.getOrangeSignalParams(list));
		// ????????????????????????MosP?????????????????????
		setFileName(dto, startDate, endDate);
	}
	
	/**
	 * CSV????????????????????????????????????<br>
	 * @param dto ??????DTO
	 * @param startDate ?????????
	 * @param endDate ?????????
	 * @param cutoffCode ???????????????
	 * @param workPlaceCode ??????????????????
	 * @param employmentContractCode ?????????????????????
	 * @param sectionCode ???????????????
	 * @param positionCode ???????????????
	 * @return CSV??????????????????
	 * @throws MospException ???????????????????????????????????????SQL???????????????????????????
	 */
	protected List<String[]> getCsvDataList(ExportDtoInterface dto, Date startDate, Date endDate, String cutoffCode,
			String workPlaceCode, String employmentContractCode, String sectionCode, String positionCode)
			throws MospException {
		// CSV????????????????????????
		List<String[]> list = new ArrayList<String[]>();
		// ?????????????????????????????????????????????
		List<ExportFieldDtoInterface> fieldList = exportFieldDao.findForList(dto.getExportCode());
		// ??????????????????
		searchHumanData(list, fieldList, startDate, endDate, cutoffCode, workPlaceCode, employmentContractCode,
				sectionCode, positionCode);
		// ??????????????????
		addSubHolidayData(list, fieldList, startDate, endDate);
		// ?????????????????????
		if (dto.getHeader() != PlatformFileConst.HEADER_TYPE_NONE) {
			addHeader(list, fieldList, endDate);
		}
		return list;
	}
	
	/**
	 * CSV????????????????????????????????????????????????<br>
	 * @param csvDataList CSV??????????????????
	 * @param fieldList ????????????????????????????????????????????????
	 * @param targetDate ?????????
	 * @throws MospException ???????????????????????????????????????SQL???????????????????????????
	 */
	protected void addHeader(List<String[]> csvDataList, List<ExportFieldDtoInterface> fieldList, Date targetDate)
			throws MospException {
		// ???????????????
		String[] header = new String[fieldList.size()];
		// ???????????????????????????????????????
		for (int i = 0; i < header.length; i++) {
			header[i] = getCodeName(fieldList.get(i).getFieldName(), TimeFileConst.CODE_EXPORT_TYPE_TMD_SUB_HOLIDAY);
		}
		// ????????????CSV??????????????????
		csvDataList.add(0, header);
	}
	
	/**
	 * ??????????????????????????????????????????????????????<br>
	 * @param csvDataList CSV??????????????????
	 * @param fieldList ????????????????????????????????????????????????
	 * @param startDate ?????????
	 * @param endDate ?????????
	 * @param cutoffCode ???????????????
	 * @param workPlaceCode ??????????????????
	 * @param employmentContractCode ?????????????????????
	 * @param sectionCode ???????????????
	 * @param positionCode ???????????????
	 * @throws MospException ???????????????????????????????????????SQL???????????????????????????
	 */
	protected void searchHumanData(List<String[]> csvDataList, List<ExportFieldDtoInterface> fieldList, Date startDate,
			Date endDate, String cutoffCode, String workPlaceCode, String employmentContractCode, String sectionCode,
			String positionCode) throws MospException {
		// ??????????????????????????????
		humanSearch.setTargetDate(endDate);
		humanSearch.setWorkPlaceCode(workPlaceCode);
		humanSearch.setEmploymentContractCode(employmentContractCode);
		humanSearch.setSectionCode(sectionCode);
		humanSearch.setPositionCode(positionCode);
		// ??????????????????(??????)
		humanSearch.setStateType("");
		// ??????????????????(??????????????????) ???????????????????????????????????????????????????
		if (ckbNeedLowerSection == 1) {
			humanSearch.setNeedLowerSection(true);
		} else {
			humanSearch.setNeedLowerSection(false);
		}
		// ??????????????????(????????????)
		humanSearch.setNeedConcurrent(true);
		// ??????????????????(????????????)
		humanSearch.setOperationType(MospConst.OPERATION_TYPE_REFER);
		// ??????????????????
		List<HumanDtoInterface> allHumanList = humanSearch.search();
		if (cutoffCode.isEmpty()) {
			humanList = allHumanList;
			return;
		}
		humanList = new ArrayList<HumanDtoInterface>();
		for (HumanDtoInterface humanDto : allHumanList) {
			ApplicationDtoInterface applicationDto = applicationReference.findForPerson(humanDto.getPersonalId(),
					endDate);
			if (applicationDto == null) {
				continue;
			}
			TimeSettingDtoInterface timeSettingDto = timeSettingReference
				.getTimeSettingInfo(applicationDto.getWorkSettingCode(), endDate);
			if (timeSettingDto == null) {
				continue;
			}
			if (!cutoffCode.equals(timeSettingDto.getCutoffCode())) {
				continue;
			}
			humanList.add(humanDto);
		}
	}
	
	/**
	 * CSV???????????????????????????????????????????????????<br>
	 * @param csvDataList CSV??????????????????
	 * @param fieldList ????????????????????????????????????????????????
	 * @param startDate ?????????
	 * @param endDate ?????????
	 * @throws MospException ???????????????????????????????????????SQL???????????????????????????
	 */
	protected void addSubHolidayData(List<String[]> csvDataList, List<ExportFieldDtoInterface> fieldList,
			Date startDate, Date endDate) throws MospException {
		// ???????????????????????????????????????
		Integer employeeCodeIndex = null;
		Integer fullNameIndex = null;
		Integer sectionNameIndex = null;
		Integer sectionDisplayIndex = null;
		Integer workDateIndex = null;
		Integer subHolidayTypeIndex = null;
		Integer requestDate1Index = null;
		Integer requestDate2Index = null;
		// ??????????????????????????????
		for (ExportFieldDtoInterface dto : fieldList) {
			int index = dto.getFieldOrder() - 1;
			if (PfmHumanDao.COL_EMPLOYEE_CODE.equals(dto.getFieldName())) {
				// ???????????????
				employeeCodeIndex = index;
			} else if (TimeFileConst.FIELD_FULL_NAME.equals(dto.getFieldName())) {
				// ??????
				fullNameIndex = index;
			} else if (PlatformFileConst.FIELD_SECTION_NAME.equals(dto.getFieldName())) {
				// ????????????
				sectionNameIndex = index;
			} else if (PlatformFileConst.FIELD_SECTION_DISPLAY.equals(dto.getFieldName())) {
				// ??????????????????
				sectionDisplayIndex = index;
			} else if (TmdSubHolidayDao.COL_WORK_DATE.equals(dto.getFieldName())) {
				// ?????????
				workDateIndex = index;
			} else if (TmdSubHolidayDao.COL_SUB_HOLIDAY_TYPE.equals(dto.getFieldName())) {
				// ????????????
				subHolidayTypeIndex = index;
			} else if (TimeFileConst.FIELD_REQUEST_DATE1.equals(dto.getFieldName())) {
				// ?????????1
				requestDate1Index = index;
			} else if (TimeFileConst.FIELD_REQUEST_DATE2.equals(dto.getFieldName())) {
				// ?????????2
				requestDate2Index = index;
			}
		}
		// CSV???????????????????????????????????????
		for (HumanDtoInterface humanDto : humanList) {
			// ??????????????????????????????
			List<SubHolidayDtoInterface> list = subHolidayReference.getSubHolidayList(humanDto.getPersonalId(),
					startDate, endDate, TimeConst.HOLIDAY_TIMES_HALF);
			for (SubHolidayDtoInterface subHolidayDto : list) {
				Date[] requestDateArray = getRequestDateArray(subHolidayDto.getPersonalId(),
						subHolidayDto.getWorkDate(), subHolidayDto.getTimesWork(), subHolidayDto.getSubHolidayType());
				// CSV???????????????
				String[] csvData = new String[fieldList.size()];
				// ??????????????????
				if (employeeCodeIndex != null) {
					csvData[employeeCodeIndex.intValue()] = humanDto.getEmployeeCode();
				}
				if (fullNameIndex != null) {
					csvData[fullNameIndex.intValue()] = MospUtility.getHumansName(humanDto.getFirstName(),
							humanDto.getLastName());
				}
				if (sectionNameIndex != null) {
					csvData[sectionNameIndex.intValue()] = sectionReference.getSectionName(humanDto.getSectionCode(),
							subHolidayDto.getWorkDate());
				}
				if (sectionDisplayIndex != null) {
					csvData[sectionDisplayIndex.intValue()] = sectionReference
						.getSectionDisplay(humanDto.getSectionCode(), subHolidayDto.getWorkDate());
				}
				if (workDateIndex != null) {
					csvData[workDateIndex.intValue()] = getStringDate(subHolidayDto.getWorkDate());
				}
				if (subHolidayTypeIndex != null) {
					StringBuffer sb = new StringBuffer();
					int subHolidayType = subHolidayDto.getSubHolidayType();
					if (subHolidayType == TimeConst.CODE_LEGAL_SUBHOLIDAY_CODE) {
						// ??????
						sb.append(mospParams.getName("Legal"));
					} else if (subHolidayType == TimeConst.CODE_PRESCRIBED_SUBHOLIDAY_CODE) {
						// ??????
						sb.append(mospParams.getName("Prescribed"));
					} else if (subHolidayType == TimeConst.CODE_MIDNIGHT_SUBHOLIDAY_CODE) {
						// ??????
						sb.append(mospParams.getName("Midnight"));
					} else {
						continue;
					}
					sb.append(mospParams.getName("FrontParentheses"));
					double subHolidayDays = subHolidayDto.getSubHolidayDays();
					if (Double.compare(subHolidayDays, 1) == 0) {
						// ??????
						sb.append(mospParams.getName("AllTime"));
					} else if (Double.compare(subHolidayDays, TimeConst.HOLIDAY_TIMES_HALF) == 0) {
						// ??????
						sb.append(mospParams.getName("HalfTime"));
					} else {
						continue;
					}
					sb.append(mospParams.getName("BackParentheses"));
					csvData[subHolidayTypeIndex.intValue()] = sb.toString();
				}
				if (requestDate1Index != null) {
					csvData[requestDate1Index.intValue()] = getStringDate(requestDateArray[0]);
				}
				if (requestDate2Index != null) {
					csvData[requestDate2Index.intValue()] = getStringDate(requestDateArray[1]);
				}
				// CSV????????????CSV???????????????????????????
				csvDataList.add(csvData);
			}
		}
	}
	
	/**
	 * ????????????????????????MosP??????????????????????????????<br>
	 * @param dto ??????DTO
	 * @param startDate ?????????
	 * @param endDate ?????????
	 */
	protected void setFileName(ExportDtoInterface dto, Date startDate, Date endDate) {
		String hyphen = mospParams.getName("Hyphen");
		String exportCode = "";
		String fileExtension = "";
		if (dto != null) {
			if (dto.getExportCode() != null) {
				exportCode = dto.getExportCode();
			}
			if (PlatformFileConst.FILE_TYPE_CSV.equals(dto.getType())) {
				// CSV
				fileExtension = ".csv";
			}
		}
		// CSV?????????????????????
		StringBuffer sb = new StringBuffer();
		// ???????????????????????????
		sb.append(exportCode);
		// ????????????
		sb.append(hyphen);
		// ?????????
		sb.append(DateUtility.getStringYear(startDate));
		// ?????????
		sb.append(DateUtility.getStringMonth(startDate));
		// ?????????
		sb.append(DateUtility.getStringDay(startDate));
		// ????????????
		sb.append(hyphen);
		// ?????????
		sb.append(DateUtility.getStringYear(endDate));
		// ?????????
		sb.append(DateUtility.getStringMonth(endDate));
		// ?????????
		sb.append(DateUtility.getStringDay(endDate));
		// ?????????
		sb.append(fileExtension);
		// ???????????????????????????
		mospParams.setFileName(sb.toString());
	}
	
	/**
	 * ?????????????????????????????????<br>
	 * @param personalId ??????ID
	 * @param workDate ?????????
	 * @param timesWork ????????????
	 * @param subHolidayType ????????????
	 * @return ???????????????
	 * @throws MospException ???????????????????????????????????????SQL???????????????????????????
	 */
	protected Date[] getRequestDateArray(String personalId, Date workDate, int timesWork, int subHolidayType)
			throws MospException {
		Date[] requestDateArray = new Date[2];
		List<SubHolidayRequestDtoInterface> subHolidayRequestList = subHolidayRequestDao.findForList(personalId,
				workDate, timesWork, subHolidayType);
		for (SubHolidayRequestDtoInterface subHolidayRequestDto : subHolidayRequestList) {
			if (!workflowIntegrate.isCompleted(subHolidayRequestDto.getWorkflow())) {
				continue;
			}
			int holidayRange = subHolidayRequestDto.getHolidayRange();
			if (holidayRange == TimeConst.CODE_HOLIDAY_RANGE_ALL) {
				requestDateArray[0] = subHolidayRequestDto.getRequestDate();
				requestDateArray[1] = subHolidayRequestDto.getRequestDate();
				return requestDateArray;
			} else if (holidayRange == TimeConst.CODE_HOLIDAY_RANGE_AM
					|| holidayRange == TimeConst.CODE_HOLIDAY_RANGE_PM) {
				if (requestDateArray[0] == null) {
					requestDateArray[0] = subHolidayRequestDto.getRequestDate();
				} else {
					requestDateArray[1] = subHolidayRequestDto.getRequestDate();
				}
			}
		}
		return requestDateArray;
	}
	
	/**
	 * ???????????????????????????????????????????????????????????????????????????????????????<br>
	 */
	protected void addNoExportDataMessage() {
		String rep = mospParams.getName("Export", "Information");
		mospParams.addErrorMessage(PlatformMessageConst.MSG_NO_ITEM, rep);
	}
}
