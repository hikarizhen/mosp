<%--
MosP - Mind Open Source Project    http://www.mosp.jp/
Copyright (C) MIND Co., Ltd.       http://www.e-mind.co.jp/

This program is free software: you can redistribute it and/or
modify it under the terms of the GNU Affero General Public License
as published by the Free Software Foundation, either version 3
of the License, or (at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU Affero General Public License for more details.

You should have received a copy of the GNU Affero General Public License
along with this program.  If not, see <http://www.gnu.org/licenses/>.
--%>
<%@ page
language     = "java"
pageEncoding = "UTF-8"
buffer       = "256kb"
autoFlush    = "false"
errorPage    = "/jsp/common/error.jsp"
%><%@ page
import = "jp.mosp.framework.base.MospParams"
import = "jp.mosp.framework.constant.MospConst"
import = "jp.mosp.framework.utils.HtmlUtility"
import = "jp.mosp.platform.constant.PlatformConst"
import = "jp.mosp.platform.utils.PlatformNamingUtility"
import = "jp.mosp.time.settings.action.ApplicationCardAction"
import = "jp.mosp.time.settings.action.CutoffMasterAction"
import = "jp.mosp.time.settings.action.HolidayMasterAction"
import = "jp.mosp.time.settings.action.PaidHolidayCardAction"
import = "jp.mosp.time.settings.action.ScheduleCardAction"
import = "jp.mosp.time.settings.action.TimeSettingCardAction"
import = "jp.mosp.time.settings.action.WorkTypeCardAction"
import = "jp.mosp.time.settings.action.WorkTypePatternCardAction"
import = "jp.mosp.time.settings.base.TimeSettingVo"
import = "jp.mosp.time.settings.vo.ApplicationCardVo"
import = "jp.mosp.time.settings.vo.CutoffMasterVo"
import = "jp.mosp.time.settings.vo.HolidayMasterVo"
import = "jp.mosp.time.settings.vo.PaidHolidayCardVo"
import = "jp.mosp.time.settings.vo.ScheduleCardVo"
import = "jp.mosp.time.settings.vo.TimeSettingCardVo"
import = "jp.mosp.time.settings.vo.WorkTypeCardVo"
import = "jp.mosp.time.settings.vo.WorkTypePatternCardVo"
%><%
// MosP??????????????????VO?????????
MospParams params = (MospParams)request.getAttribute(MospConst.ATT_MOSP_PARAMS);
TimeSettingVo vo = (TimeSettingVo)params.getVo();
// ?????????????????????????????????(??????????????????????????????)
String headerTitle = PlatformNamingUtility.basisInfo(params);
//????????????????????????????????????
String newInsert = PlatformNamingUtility.cornerParentheses(params, PlatformNamingUtility.newInsert(params));
String addHistory = PlatformNamingUtility.cornerParentheses(params, PlatformNamingUtility.addHistory(params));
String edtiHistory = PlatformNamingUtility.cornerParentheses(params, PlatformNamingUtility.edtiHistory(params));
String replication = PlatformNamingUtility.cornerParentheses(params, PlatformNamingUtility.replication(params));
// ?????????????????????????????????
String insertModeCommand = "";
String addModeCommand = "";
String editModeCommand = "";
String replicationModeCommand = "";
// ?????????????????????
String keyCode = "";
// VO??????
if (vo instanceof ApplicationCardVo) {
	// ??????????????????
	insertModeCommand = ApplicationCardAction.CMD_INSERT_MODE;
	addModeCommand = ApplicationCardAction.CMD_ADD_MODE;
	editModeCommand = ApplicationCardAction.CMD_SELECT_SHOW;
	keyCode = ((ApplicationCardVo)vo).getTxtEditApplicationCode();
} else if (vo instanceof CutoffMasterVo) {
	// ????????????
	headerTitle = PlatformNamingUtility.edit(params);
	insertModeCommand = CutoffMasterAction.CMD_INSERT_MODE;
	addModeCommand = CutoffMasterAction.CMD_ADD_MODE;
	editModeCommand = CutoffMasterAction.CMD_EDIT_MODE;
	keyCode = ((CutoffMasterVo)vo).getTxtEditCutoffCode();
} else if (vo instanceof HolidayMasterVo) {
	// ??????????????????
	headerTitle = PlatformNamingUtility.edit(params);
	insertModeCommand = HolidayMasterAction.CMD_INSERT_MODE;
	addModeCommand = HolidayMasterAction.CMD_ADD_MODE;
	editModeCommand = HolidayMasterAction.CMD_EDIT_MODE;
	keyCode = ((HolidayMasterVo)vo).getTxtEditHolidayCode();
} else if (vo instanceof PaidHolidayCardVo) {
	// ??????????????????
	insertModeCommand = PaidHolidayCardAction.CMD_INSERT_MODE;
	addModeCommand = PaidHolidayCardAction.CMD_ADD_MODE;
	editModeCommand = PaidHolidayCardAction.CMD_SELECT_SHOW;
	replicationModeCommand = PaidHolidayCardAction.CMD_REPLICATION_MODE;
	keyCode = ((PaidHolidayCardVo)vo).getTxtPaidHolidayCode();
} else if (vo instanceof ScheduleCardVo) {
	// ??????????????????
	insertModeCommand = ScheduleCardAction.CMD_INSERT_MODE;
	addModeCommand = ScheduleCardAction.CMD_ADD_MODE;
	editModeCommand = ScheduleCardAction.CMD_SELECT_SHOW;
	replicationModeCommand = ScheduleCardAction.CMD_REPLICATION_MODE;
	keyCode = ((ScheduleCardVo)vo).getTxtScheduleCode();
} else if (vo instanceof TimeSettingCardVo) {
	// ????????????
	insertModeCommand = TimeSettingCardAction.CMD_INSERT_MODE;
	addModeCommand = TimeSettingCardAction.CMD_ADD_MODE;
	editModeCommand = TimeSettingCardAction.CMD_SELECT_SHOW;
	replicationModeCommand = TimeSettingCardAction.CMD_REPLICATION_MODE;
	keyCode = ((TimeSettingCardVo)vo).getTxtSettingCode();
} else if (vo instanceof WorkTypeCardVo) {
	// ????????????
	insertModeCommand = WorkTypeCardAction.CMD_INSERT_MODE;
	addModeCommand = WorkTypeCardAction.CMD_ADD_MODE;
	editModeCommand = WorkTypeCardAction.CMD_SELECT_SHOW;
	keyCode = ((WorkTypeCardVo)vo).getTxtWorkTypeCode();
} else if (vo instanceof WorkTypePatternCardVo) {
	// ????????????????????????
	insertModeCommand = WorkTypePatternCardAction.CMD_INSERT_MODE;
	addModeCommand = WorkTypePatternCardAction.CMD_ADD_MODE;
	editModeCommand = WorkTypePatternCardAction.CMD_SELECT_SHOW;
	keyCode = ((WorkTypePatternCardVo)vo).getTxtPatternCode();
}
//??????????????????????????????
if (vo.getModeCardEdit().equals(PlatformConst.MODE_CARD_EDIT_INSERT)) {
%>
<span class="TitleTh"><%= headerTitle %><%= newInsert %></span>
<%
}
// ??????????????????????????????
if (vo.getModeCardEdit().equals(PlatformConst.MODE_CARD_EDIT_ADD)) {
%>
<span class="TitleTh"><%= headerTitle %><%= addHistory %></span>
<a onclick="submitTransfer(event, 'divEdit', null, null, '<%= insertModeCommand %>');"><%= newInsert %></a>
<%
}
//??????????????????????????????
if (vo.getModeCardEdit().equals(PlatformConst.MODE_CARD_EDIT_EDIT)) {
%>
<span class="TitleTh"><%= headerTitle %><%= edtiHistory %></span>
<a onclick="submitTransfer(event, 'divEdit', null, null, '<%= insertModeCommand %>');"><%= newInsert %></a>
<a onclick="submitTransfer(event, null, null, null, '<%= addModeCommand %>');"><%= addHistory %></a>
<% if (replicationModeCommand.isEmpty() == false) { %>
<a onclick="submitTransfer(event, null, confirmReplicationMode, null, '<%= replicationModeCommand %>')"><%= replication %></a>
<%
}
%>
<div class="TableLabelSpan">
<%
// ????????????????????????????????????????????????
if (vo.getLblBackActivateDate() != null && vo.getLblBackActivateDate().isEmpty() == false) {
%>
	<a class="ActivateDateRollLink"
		onclick="submitTransfer(event, 'divEdit', null, new Array('<%= PlatformConst.PRM_TRANSFERRED_ACTIVATE_DATE %>', '<%= HtmlUtility.escapeHTML(vo.getLblBackActivateDate()) %>', '<%= PlatformConst.PRM_TRANSFERRED_CODE %>' , '<%= HtmlUtility.escapeHTML(keyCode) %>'), '<%= editModeCommand %>');">
		&lt;&lt;&nbsp;
	</a>
<%
}
%>
	<span class=""><%= PlatformNamingUtility.activateDate(params) %></span>
<%
// ???????????????????????????????????????????????????
if (vo.getLblNextActivateDate() == null || vo.getLblNextActivateDate().isEmpty()) {
%>
	&nbsp;<%= PlatformNamingUtility.latest(params) %>
<%
} else {
// ????????????????????????????????????????????????
%>
	<a class="ActivateDateRollLink"
		onclick="submitTransfer(event, 'divEdit', null, new Array('<%= PlatformConst.PRM_TRANSFERRED_ACTIVATE_DATE %>', '<%= HtmlUtility.escapeHTML(vo.getLblNextActivateDate()) %>', '<%= PlatformConst.PRM_TRANSFERRED_CODE %>' , '<%= HtmlUtility.escapeHTML(keyCode) %>'), '<%= editModeCommand %>');">
		&nbsp;&gt;&gt;
	</a>
<%
}
%>
	&nbsp;<%= PlatformNamingUtility.history(params) %>&nbsp;<%= PlatformNamingUtility.allCount(params, vo.getCountHistory()) %>
</div>
<%
}
%>
