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
language = "java"
pageEncoding = "UTF-8"
buffer = "256kb"
autoFlush = "false"
errorPage = "/jsp/common/error.jsp"
%><%@ page
import = "jp.mosp.framework.constant.MospConst"
import = "jp.mosp.framework.base.MospParams"
import = "jp.mosp.framework.utils.HtmlUtility"
import = "jp.mosp.platform.constant.PlatformConst"
import = "jp.mosp.platform.system.base.PlatformSystemVo"
import = "jp.mosp.platform.system.vo.EmploymentMasterVo"
import = "jp.mosp.platform.system.action.EmploymentMasterAction"
import = "jp.mosp.platform.system.vo.SectionMasterVo"
import = "jp.mosp.platform.system.action.SectionMasterAction"
import = "jp.mosp.platform.system.vo.WorkPlaceMasterVo"
import = "jp.mosp.platform.system.action.WorkPlaceMasterAction"
import = "jp.mosp.platform.system.vo.PositionMasterVo"
import = "jp.mosp.platform.system.action.PositionMasterAction"
import = "jp.mosp.platform.system.vo.NamingMasterVo"
import = "jp.mosp.platform.system.action.NamingMasterAction"
import = "jp.mosp.platform.system.vo.AccountMasterVo"
import = "jp.mosp.platform.system.action.AccountMasterAction"
import = "jp.mosp.platform.message.vo.MessageCardVo"
import = "jp.mosp.platform.message.action.MessageCardAction"
import = "jp.mosp.platform.file.vo.ExportCardVo"
import = "jp.mosp.platform.file.action.ExportCardAction"
import = "jp.mosp.platform.file.vo.ImportCardVo"
import = "jp.mosp.platform.file.action.ImportCardAction"
import = "jp.mosp.platform.workflow.vo.SubApproverSettingVo"
import = "jp.mosp.platform.workflow.action.SubApproverSettingAction"
import = "jp.mosp.platform.workflow.vo.UnitCardVo"
import = "jp.mosp.platform.workflow.action.UnitCardAction"
import = "jp.mosp.platform.workflow.vo.RouteCardVo"
import = "jp.mosp.platform.workflow.action.RouteCardAction"
import = "jp.mosp.platform.workflow.vo.RouteApplicationCardVo"
import = "jp.mosp.platform.workflow.action.RouteApplicationCardAction"
%><%
// MosP??????????????????VO?????????
MospParams params = (MospParams)request.getAttribute(MospConst.ATT_MOSP_PARAMS);
PlatformSystemVo vo = (PlatformSystemVo)params.getVo();
// ?????????????????????????????????(????????????????????????)
String headerTitle = params.getName("Edit");
// ???????????????????????????(???????????????????????????)
String editModeTitle = params.getName("History") + params.getName("Edit");
// ?????????????????????????????????
String insertModeCommand = "";
String addModeCommand = "";
String editModeCommand = "";
String replicationModeCommand = "";
// ?????????????????????
String keyCode = "";
// VO??????
if (vo instanceof EmploymentMasterVo) {
	// ?????????????????????
	insertModeCommand = EmploymentMasterAction.CMD_INSERT_MODE;
	addModeCommand = EmploymentMasterAction.CMD_ADD_MODE;
	editModeCommand = EmploymentMasterAction.CMD_EDIT_MODE;
	keyCode = ((EmploymentMasterVo)vo).getTxtEditEmploymentCode();
} else if (vo instanceof SectionMasterVo) {
	// ???????????????
	insertModeCommand = SectionMasterAction.CMD_INSERT_MODE;
	addModeCommand = SectionMasterAction.CMD_ADD_MODE;
	editModeCommand = SectionMasterAction.CMD_EDIT_MODE;
	keyCode = ((SectionMasterVo)vo).getTxtEditSectionCode();
} else if (vo instanceof WorkPlaceMasterVo) {
	// ??????????????????
	insertModeCommand = WorkPlaceMasterAction.CMD_INSERT_MODE;
	addModeCommand = WorkPlaceMasterAction.CMD_ADD_MODE;
	editModeCommand = WorkPlaceMasterAction.CMD_EDIT_MODE;
	keyCode = ((WorkPlaceMasterVo)vo).getTxtEditWorkPlaceCode();
} else if (vo instanceof PositionMasterVo) {
	// ???????????????
	insertModeCommand = PositionMasterAction.CMD_INSERT_MODE;
	addModeCommand = PositionMasterAction.CMD_ADD_MODE;
	editModeCommand = PositionMasterAction.CMD_EDIT_MODE;
	keyCode = ((PositionMasterVo)vo).getTxtEditPositionCode();
} else if (vo instanceof NamingMasterVo) {
	// ?????????????????????
	insertModeCommand = NamingMasterAction.CMD_INSERT_MODE;
	addModeCommand = NamingMasterAction.CMD_ADD_MODE;
	editModeCommand = NamingMasterAction.CMD_EDIT_MODE;
	keyCode = ((NamingMasterVo)vo).getTxtEditNamingItemCode();
} else if (vo instanceof AccountMasterVo) {
	// ???????????????
	insertModeCommand = AccountMasterAction.CMD_INSERT_MODE;
	addModeCommand = AccountMasterAction.CMD_ADD_MODE;
	editModeCommand = AccountMasterAction.CMD_EDIT_MODE;
	keyCode = ((AccountMasterVo)vo).getTxtEditUserId();
} else if (vo instanceof MessageCardVo) {
	// ???????????????
	insertModeCommand = MessageCardAction.CMD_INSERT_MODE;
	replicationModeCommand = MessageCardAction.CMD_REPLICATION_MODE;
	editModeTitle = params.getName("Edit");
} else if (vo instanceof ImportCardVo) {
	// ??????????????????
	insertModeCommand = ImportCardAction.CMD_INSERT_MODE;
	editModeTitle = params.getName("Edit");
} else if (vo instanceof ExportCardVo) {
	// ???????????????
	insertModeCommand = ExportCardAction.CMD_INSERT_MODE;
	editModeTitle = params.getName("Edit");
} else if (vo instanceof SubApproverSettingVo) {
	// ????????????
	insertModeCommand = SubApproverSettingAction.CMD_INSERT_MODE;
	editModeTitle = params.getName("Edit");
} else if (vo instanceof UnitCardVo) {
	// ??????????????????
	insertModeCommand = UnitCardAction.CMD_INSERT_MODE;
	addModeCommand = UnitCardAction.CMD_ADD_MODE;
	editModeCommand = UnitCardAction.CMD_SELECT_SHOW;
	keyCode = ((UnitCardVo)vo).getTxtUnitCode();
} else if (vo instanceof RouteCardVo) {
	// ???????????????
	insertModeCommand = RouteCardAction.CMD_INSERT_MODE;
	addModeCommand = RouteCardAction.CMD_ADD_MODE;
	editModeCommand = RouteCardAction.CMD_SELECT_SHOW;
	keyCode = ((RouteCardVo)vo).getTxtRouteCode();
} else if (vo instanceof RouteApplicationCardVo) {
	// ???????????????
	insertModeCommand = RouteApplicationCardAction.CMD_INSERT_MODE;
	addModeCommand = RouteApplicationCardAction.CMD_ADD_MODE;
	editModeCommand = RouteApplicationCardAction.CMD_SELECT_SHOW;
	keyCode = ((RouteApplicationCardVo)vo).getTxtApplicationCode();
} 

//?????????????????????
if (vo.getModeCardEdit().equals(PlatformConst.MODE_CARD_EDIT_INSERT)) {
%>

<span class="TitleTh"><%= headerTitle + params.getName("FrontWithCornerParentheses") + params.getName("New") + params.getName("Insert") + params.getName("BackWithCornerParentheses") %></span>
<%
} else if (vo.getModeCardEdit().equals(PlatformConst.MODE_CARD_EDIT_ADD)) {
%>
<span class="TitleTh"><%= headerTitle + params.getName("FrontWithCornerParentheses") + params.getName("History") + params.getName("Add") + params.getName("BackWithCornerParentheses") %></span>
<a onclick="submitTransfer(event, 'divEdit', null, null, '<%= insertModeCommand %>');"><%= params.getName("FrontWithCornerParentheses") + params.getName("New") + params.getName("Insert") + params.getName("BackWithCornerParentheses") %></a>
<%
} else if (vo.getModeCardEdit().equals(PlatformConst.MODE_CARD_EDIT_EDIT)) {
%>
<span class="TitleTh"><%= headerTitle + params.getName("FrontWithCornerParentheses") + editModeTitle + params.getName("BackWithCornerParentheses") %></span>
<a onclick="submitTransfer(event, 'divEdit', null, null, '<%= insertModeCommand %>');"><%= params.getName("FrontWithCornerParentheses") + params.getName("New") + params.getName("Insert") + params.getName("BackWithCornerParentheses") %></a>
<%
	if (addModeCommand.isEmpty() == false) {
%>
<a onclick="submitTransfer(event, null, null, null, '<%= addModeCommand %>');"><%= params.getName("FrontWithCornerParentheses") + params.getName("History") + params.getName("Add") + params.getName("BackWithCornerParentheses") %></a>
<%
	}
	if (replicationModeCommand.isEmpty() == false) {
%>
<a onclick="submitTransfer(event, null, confirmReplicationMode, null, '<%= replicationModeCommand %>')"><%= params.getName("FrontWithCornerParentheses") + params.getName("Replication") + params.getName("BackWithCornerParentheses") %></a>
<%
	}
	if (editModeCommand.isEmpty()) {
		return;
	}
%>
<div class="TableLabelSpan">
<%
if (vo.getLblBackActivateDate() != null && vo.getLblBackActivateDate().isEmpty() == false) {
%>
	<a class="ActivateDateRollLink"
			onclick="submitTransfer(event, 'divEdit', null, new Array('<%= PlatformConst.PRM_TRANSFERRED_ACTIVATE_DATE %>', '<%= HtmlUtility.escapeHTML(vo.getLblBackActivateDate()) %>', '<%= PlatformConst.PRM_TRANSFERRED_CODE %>' , '<%= HtmlUtility.escapeHTML(keyCode) %>'), '<%= editModeCommand %>');">
		&lt;&lt;&nbsp;
	</a>
<%
}
%>
	<span class=""><%= params.getName("ActivateDate") %></span>
<%
if (vo.getLblNextActivateDate() == null || vo.getLblNextActivateDate().isEmpty()) {
%>
		&nbsp;<%= params.getName("Latest") %>
<%
} else {
%>
	<a class="ActivateDateRollLink"
			onclick="submitTransfer(event, 'divEdit', null, new Array('<%= PlatformConst.PRM_TRANSFERRED_ACTIVATE_DATE %>', '<%= HtmlUtility.escapeHTML(vo.getLblNextActivateDate()) %>', '<%= PlatformConst.PRM_TRANSFERRED_CODE %>' , '<%= HtmlUtility.escapeHTML(keyCode) %>'), '<%= editModeCommand %>');">
		&nbsp;&gt;&gt;
	</a>
<%
}
%>
	&nbsp;<%= params.getName("History") %>&nbsp;<%= params.getName("All") + vo.getCountHistory() + params.getName("Count")%>
</div>
<%
}
%>
