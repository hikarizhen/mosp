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
<%@page import="jp.mosp.framework.utils.MospUtility"%>
<%@ page
language = "java"
pageEncoding = "UTF-8"
buffer = "256kb"
autoFlush = "false"
errorPage = "/jsp/common/error.jsp"
%><%@ page
import = "jp.mosp.framework.base.MospParams"
import = "jp.mosp.framework.constant.MospConst"
import = "jp.mosp.framework.utils.HtmlUtility"
import = "jp.mosp.framework.utils.RoleUtility"
import = "jp.mosp.platform.constant.PlatformConst"
import = "jp.mosp.platform.human.base.PlatformHumanVo"
import = "jp.mosp.platform.human.constant.PlatformHumanConst"
import = "jp.mosp.framework.property.ViewConfigProperty"
import = "jp.mosp.framework.xml.ViewProperty"
import = "jp.mosp.platform.human.action.HumanHistoryCardAction"
import = "jp.mosp.platform.human.action.HumanHistoryListAction"
import = "jp.mosp.platform.human.action.HumanArrayCardAction"
import = "jp.mosp.platform.human.action.HumanNormalCardAction"
import = "jp.mosp.platform.human.action.HumanBinaryHistoryCardAction"
import = "jp.mosp.platform.human.action.HumanBinaryHistoryListAction"
import = "jp.mosp.platform.human.action.HumanBinaryArrayCardAction"
import = "jp.mosp.platform.human.action.HumanBinaryNormalCardAction"
import = "jp.mosp.platform.human.action.HumanInfoAction"
import="jp.mosp.framework.xml.ViewTableProperty"
import = "jp.mosp.platform.human.action.HumanBinaryOutputFileAction"
%><%
// MosP??????????????????
MospParams params = (MospParams)request.getAttribute(MospConst.ATT_MOSP_PARAMS);
// MosP?????????????????????????????????????????????????????????????????????
String division = (String)params.getGeneralParam(PlatformHumanConst.PRM_HUMAN_DIVISION);
String view = (String)params.getGeneralParam(PlatformHumanConst.PRM_HUMAN_VIEW);
// ??????????????????????????????
if(division == null){
	// ????????????
	return;
}
// ????????????????????????????????????
boolean isReferenceDivision = RoleUtility.getReferenceDivisionsList(params).contains(division);
// ????????????????????????????????????
ViewConfigProperty viewConfig = params.getProperties().getViewConfigProperties().get(division);
String divisionType = viewConfig.getType();
// ????????????????????????????????????
ViewProperty viewProperty = viewConfig.getView(view);
// ??????????????????????????????????????????
String[] viewTableKeys = viewProperty.getViewTableKeys();
String[] viewTitles = viewProperty.getViewTableTitles();
// ??????????????????VO?????????
PlatformHumanVo vo = (PlatformHumanVo)params.getVo();
%>

<div class="List">
<%
// ??????????????????????????????(?????????)
for(int i=0; i<viewTableKeys.length; i++){	
	// ?????????????????????????????????
	if (view.equals(HumanInfoAction.KEY_VIEW_HUMAN_INFO) ) {
		// ???????????????
		if(divisionType.equals(PlatformHumanConst.PRM_HUMAN_DIVISION_TYPE_NORMAL)){
%>
		<table class="OverTable">
			<tr>
				<th colspan="3" class="ListTableTh" id="thDivision"><span class="TitleTh">
				<%= params.getName(viewTitles[i]) %></span>
					<span class="TableButtonSpan">
						<button type="button" class="Name2Button"
							onclick="submitTransfer(event, null, null, new Array('<%=PlatformConst.PRM_TRANSFERRED_TYPE%>','<%= division %>','<%=PlatformConst.PRM_TRANSFERRED_ACTION%>', '<%=HumanNormalCardAction.class.getName()%>'), '<%=HumanInfoAction.CMD_TRANSFER%>');">
							<% // ???????????????????????????%>
							<%= isReferenceDivision ? params.getName("Reference") : params.getName("Edit")%>
						</button>
					</span>
				</th>
			</tr>
		</table>
<%
			// 1????????????????????????????????????????????????????????????
			if(vo.getNormalMapInfo(division).isEmpty()){
				return;
			}
		}
		// ???????????????????????????
		if(divisionType.equals(PlatformHumanConst.PRM_HUMAN_DIVISION_TYPE_BINARY_NORMAL)){
			// ??????VO????????????
			String[] fileType = vo.getAryBinaryFileTypeMap(division);
			String[] fileName = vo.getAryBinaryFileNameMap(division);
			String[] fileRemark = vo.getAryBinaryFileRemarkMap(division);
%>
			<table class="OverTable">
				<tr>
					<th colspan="3" class="ListTableTh" id="thDivision"><span class="TitleTh">
					<%= params.getName(viewTitles[i]) %></span>
						<span class="TableButtonSpan">
							<button type="button" class="Name2Button"
								onclick="submitTransfer(event, null, null, new Array('<%=PlatformConst.PRM_TRANSFERRED_TYPE %>','<%= division %>','<%=PlatformConst.PRM_TRANSFERRED_ACTION%>', '<%=HumanBinaryNormalCardAction.class.getName()%>'), '<%=HumanInfoAction.CMD_TRANSFER%>');">
								<% // ???????????????????????????%>
								<%= isReferenceDivision ? params.getName("Reference") : params.getName("Edit")%>
							</button>
						</span>
					</th>
				</tr>
			</table>
<%
			// ??????????????????????????????
			if(fileName[0]==null || fileName.length ==0){
				return;
			}
%>		
			<table class="UnderTable">
			<tr>
				<td class="TitleTd" id="tdBinaryFileNameTitle"><%=params.getName("File","Name") %></td>
				<td class="InputTd" id="tdBinaryFileNameBody"><%= HtmlUtility.escapeHTML(fileName[0]) %></td>
			</tr>
			<tr>
				<td class="TitleTd" id="tdBinaryRemarksTitle" ><%=params.getName("Remarks") %></td>
				<td class="InputTd" id="tdFileRemarkBody"><%= HtmlUtility.escapeHTML(fileRemark[0]) %></td>
<% 
			// ???????????????????????????
			if(fileType[0].equals(PlatformHumanConst.CODE_HUMAN_BINARY_FILE_TYPE_FILE)){
%>
				<td class="TitleTd"><%=params.getName("Insert","Finish") %>????????????<%=params.getName("File") %></td>
				<td>	
					<button type="button" class="Name4Button" id="btnOutput" onclick="submitFile(event, null, new Array('<%=PlatformConst.PRM_TRANSFERRED_TYPE%>','<%= division %>'), '<%= HumanBinaryOutputFileAction.CMD_NORAML_INFO_FILE %>');"><%= params.getName("Output") %></button>
				</td>
<%				
			}
%>
			</tr>
		</table>
<%
			// ??????????????????
			return;
		}
		// ???????????????
		if(divisionType.equals(PlatformHumanConst.PRM_HUMAN_DIVISION_TYPE_HISTORY)){
			// ??????????????????????????????????????????
			ViewTableProperty viewTableProperty = viewConfig.getViewTable(viewTableKeys[i]);
			// ???????????????
			params.addGeneralParam(PlatformHumanConst.PRM_HUMAN_DATE_NAME,viewTableProperty.getDateName());
%>
		<table class="OverTable">
			<tr>
				<th colspan="3" class="ListTableTh" id="thDivision"><span class="TitleTh"><%= params.getName(viewTitles[i]) %></span>
					<span class="TableButtonSpan">
						<% // ???????????????????????????%>
						<% if (!isReferenceDivision) { %>
							<button type="button" class="Name4Button" onclick="submitTransfer(event, null, null, new Array('<%=PlatformConst.PRM_TRANSFERRED_TYPE%>','<%= division %>','<%=PlatformConst.PRM_TRANSFERRED_ACTION%>', '<%=HumanHistoryCardAction.class.getName()%>'), '<%= HumanInfoAction.CMD_TRANSFER %>');">
								<%= params.getName("History","Add")%>
							</button>
						<% } %>
						<%
						// ????????????????????????????????????????????????????????????
						if(vo.getHistoryMapInfo(division).isEmpty() ==false){
						%>&nbsp;
						<button type="button" class="Name4Button" onclick="submitTransfer(event, null, null, new Array('<%=PlatformConst.PRM_TRANSFERRED_TYPE%>','<%= division %>','<%= PlatformConst.PRM_TRANSFERRED_ACTION %>','<%= HumanHistoryListAction.class.getName() %>'), '<%= HumanInfoAction.CMD_TRANSFER %>');">
							<%= params.getName("History","List") %>
						</button>
						<%
						}
						%>
					</span>	
				</th>
			</tr>
		</table>
<%
			//1????????????????????????????????????????????????????????????
			if(vo.getHistoryMapInfo(division).isEmpty()){
				return;
			}
		}
		// ???????????????????????????
		if(divisionType.equals(PlatformHumanConst.PRM_HUMAN_DIVISION_TYPE_BINARY_HISTORY)){
			// ??????VO????????????
			String[] activeDate = vo.getAryBinaryActiveDateMap(division);
			String[] fileType = vo.getAryBinaryFileTypeMap(division);
			String[] fileName = vo.getAryBinaryFileNameMap(division);
			String[] fileRemark = vo.getAryBinaryFileRemarkMap(division);
	%>
		<table class="OverTable">
			<tr>
				<th colspan="3" class="ListTableTh" id="thDivision"><span class="TitleTh"><%= params.getName(viewTitles[i]) %></span>
					<span class="TableButtonSpan">
						<% // ???????????????????????????%>
						<% if (!isReferenceDivision) { %>
						<button type="button" class="Name4Button" onclick="submitTransfer(event, null, null, new Array('<%=PlatformConst.PRM_TRANSFERRED_TYPE%>','<%= division %>','<%=PlatformConst.PRM_TRANSFERRED_ACTION%>', '<%=HumanBinaryHistoryCardAction.class.getName()%>'), '<%= HumanInfoAction.CMD_TRANSFER %>');">
							<%= params.getName("History","Add")%>
						</button>
						<% } %>
						<%
						// ????????????????????????????????????????????????????????????
						if(activeDate[0]!=null){
						%>&nbsp;
						<button type="button" class="Name4Button" onclick="submitTransfer(event, null, null, new Array('<%=PlatformConst.PRM_TRANSFERRED_TYPE%>','<%= division %>','<%= PlatformConst.PRM_TRANSFERRED_ACTION %>','<%= HumanBinaryHistoryListAction.class.getName() %>'), '<%= HumanInfoAction.CMD_TRANSFER %>');">
							<%= params.getName("History","List") %>
						</button>
						<%
						}
						%>
					</span>	
				</th>
			</tr>
		</table>
<%
			// ??????????????????????????????
			if(activeDate[0]==null || activeDate.length ==0){
				return;
			}
%>
		<table class="UnderTable" id="under<%=division%>Table">
			<tr>
				<td class="TitleTd" id="tdBinaryActivateDateTitle"><%=params.getName("ActivateDate") %></td>
				<td class="InputTd" id="tdBinaryActivateDateBody"><%= HtmlUtility.escapeHTML(activeDate[0]) %></td>
				<td class="TitleTd" id="tdBinaryFileNameTitle" ><%=params.getName("File","Name") %></td>
				<td class="InputTd" id="tdBinaryFileNameBody"<% if(fileType[0].equals(PlatformHumanConst.CODE_HUMAN_BINARY_FILE_TYPE_FILE)){%>colspan="3"<%} %> ><%= HtmlUtility.escapeHTML(fileName[0]) %></td>
			</tr>
			<tr>
<% 
			// ???????????????????????????
			if(fileType[0].equals(PlatformHumanConst.CODE_HUMAN_BINARY_FILE_TYPE_FILE)){
%>
				<td class="TitleTd" id="tdBinaryFileOutputTitle"><%=params.getName("File","Output") %></td>
				<td class="ListSelectTd">	
					<button type="button" class="Name4Button" id="btnBinaryFileOutput" onclick="submitFile(event, null, new Array('<%=PlatformConst.PRM_TRANSFERRED_TYPE%>','<%= division %>','<%=PlatformConst.PRM_TRANSFERRED_ACTIVATE_DATE %>','<%=activeDate[0]%>'), '<%= HumanBinaryOutputFileAction.CMD_HISTORY_INFO_FILE %>');"><%= params.getName("Output") %></button>
				</td>
<%				
			}
%>
				<td class="TitleTd" id="tdBinaryFileRemarkTitle"><%=params.getName("FileRemark") %></td>
				<td class="InputTd" id="tdBinaryFileRemarkBody" colspan="3"><%= HtmlUtility.escapeHTML(fileRemark[0]) %></td>
			</tr>
			
		</table>
	<%
			// ??????????????????
			return;
		}
		// ???????????????
		if(divisionType.equals(PlatformHumanConst.PRM_HUMAN_DIVISION_TYPE_ARRAY)){
			// ??????????????????????????????????????????
			ViewTableProperty viewTableProperty = viewConfig.getViewTable(viewTableKeys[i]);
			// ???????????????
			params.addGeneralParam(PlatformHumanConst.PRM_HUMAN_DATE_NAME,viewTableProperty.getDateName());
%>
		<table class="OverTable">
			<tr>
				<th colspan="3" class="ListTableTh" id="thDivision"><span class="TitleTh"><%= params.getName(viewTitles[i]) %></span>
			  		<span class="TableButtonSpan">
						<% // ???????????????????????????%>
						<% if (!isReferenceDivision) { %>
				     	    <button type="button" class="Name2Button"
								onclick="submitTransfer(event, null, null, new Array('<%=PlatformConst.PRM_TRANSFERRED_TYPE%>','<%= division %>','<%= PlatformConst.PRM_TRANSFERRED_ACTION %>', '<%= HumanArrayCardAction.class.getName() %>'), '<%= HumanInfoAction.CMD_TRANSFER %>');">
								<%= params.getName("Add") %>
							</button>
						<% } %>
					</span>
				</th>
			</tr>
		</table>
<%
			//1???????????????????????????????????????????????????
			if(vo.getArrayMapInfo(division).isEmpty()){
				return;
			}
		}
		// ???????????????????????????
		if(divisionType.equals(PlatformHumanConst.PRM_HUMAN_DIVISION_TYPE_BINARY_ARRAY)){
			// ??????????????????????????????????????????
			ViewTableProperty viewTableProperty = viewConfig.getViewTable(viewTableKeys[i]);
			// ???????????????
			params.addGeneralParam(PlatformHumanConst.PRM_HUMAN_DATE_NAME,viewTableProperty.getDateName());
			// ??????VO????????????
			String[] rowId = vo.getAryBinaryRowIdMap(division);
			String[] activeDate = vo.getAryBinaryActiveDateMap(division);
			String[] fileType = vo.getAryBinaryFileTypeMap(division);
			String[] fileName = vo.getAryBinaryFileNameMap(division);
			String[] fileRemark = vo.getAryBinaryFileRemarkMap(division);
%>
		<table class="OverTable">
			<tr>
				<th colspan="3" class="ListTableTh" id="thDivision"><span class="TitleTh"><%= params.getName(viewTitles[i]) %></span>
			  		<span class="TableButtonSpan">
						<% // ???????????????????????????%>
						<% if (!isReferenceDivision) { %>
				     	    <button type="button" class="Name2Button" id="btnBinaryFileOutput"
								onclick="submitTransfer(event, null, null, new Array('<%=PlatformConst.PRM_TRANSFERRED_TYPE%>','<%= division %>','<%= PlatformConst.PRM_TRANSFERRED_ACTION %>', '<%= HumanBinaryArrayCardAction.class.getName() %>'), '<%= HumanInfoAction.CMD_TRANSFER %>');">
								<%= params.getName("Add") %>
							</button>
						<% } %>
					</span>
				</th>
			</tr>
		</table>
<%
			// ?????????????????????
			if(rowId.length == 0){
				return;
			}
%>
		<table class="UnderTable">
			<tr>
				<td class="TitleTd" id="tdBinaryButtonTitle"></td>	
				<td class="TitleTd" id="tdBinaryActivateDateTitle"><%=params.getName("ActivateDate") %></td>
				<td class="TitleTd" id="tdBinaryFileNameTitle"><%=params.getName("File","Name") %></td>
				<td class="TitleTd" id="tdBinaryFileOutputTitle"><%=params.getName("File","Output") %></td>
				<td class="TitleTd" id="tdBinaryFileRemarkTitle"><%=params.getName("FileRemark") %></td>
				
			</tr>
<%	
		// ??????????????????
		for (int idx = 0; idx < activeDate.length; idx++) {
%>
			<tr>
				<td>
					<button type="button" id="tdBinaryButtonBody" class="Name2Button"
						onclick="submitTransfer(event, null, null, new Array('<%= PlatformConst.PRM_TRANSFERRED_TYPE %>', '<%= division %>','<%= PlatformConst.PRM_TRANSFERRED_INDEX %>', '<%= rowId[idx] %>','<%= PlatformConst.PRM_TRANSFERRED_ACTION %>', '<%= HumanBinaryArrayCardAction.CMD_EDIT_SELECT %>'), '<%= HumanInfoAction.CMD_TRANSFER %>');">
						<%= params.getName("Select") %>
					</button>
				</td>
				<td class="InputTd" id="tdBinaryActivateDateBody"><%= HtmlUtility.escapeHTML(activeDate[idx]) %></td>
				<td class="InputTd" id="tdBinaryFileNameBody"><%= HtmlUtility.escapeHTML(fileName[idx]) %></td>
<% 
			// ???????????????????????????
			if(fileType[idx].equals(PlatformHumanConst.CODE_HUMAN_BINARY_FILE_TYPE_FILE)){
%>
				<td>	
					<button type="button" id="btnBinaryFileOutput" class="Name4Button" id="btnOutput" onclick="submitFile(event, null, new Array('<%=PlatformConst.PRM_TRANSFERRED_TYPE%>','<%= division %>','<%= PlatformConst.PRM_TRANSFERRED_INDEX %>', '<%= rowId[idx] %>'), '<%= HumanBinaryOutputFileAction.CMD_ARRAY_INFO_FILEE %>');"><%= params.getName("Output") %></button>
				</td>
<%				
			}
%>
				<td class="InputTd" id="tdBinaryFileRemarkBody"><%= HtmlUtility.escapeHTML(fileRemark[idx]) %></td>
			</tr>
<%
		}
%>
		</table>
	<%
			return;
		}
	}
	// ?????????????????????
	if (view.equals(HumanNormalCardAction.KEY_VIEW_NORMAL_CARD) && divisionType.equals(PlatformHumanConst.PRM_HUMAN_DIVISION_TYPE_NORMAL)) {
%>
		<table class="OverTable">
			<tr>
				<th colspan="3" class="ListTableTh" id="thDivision"><span class="TitleTh"><%= params.getName(viewTitles[i]) %></span>
				</th>
			</tr>
		</table>
<%
	}
	// ???????????????????????????
	if(view.equals(HumanHistoryCardAction.KEY_VIEW_HUMAN_HISTORY_CARD) && divisionType.equals(PlatformHumanConst.PRM_HUMAN_DIVISION_TYPE_HISTORY)){
		// ??????????????????????????????????????????
		ViewTableProperty viewTableProperty = viewConfig.getViewTable(viewTableKeys[i]);
		// ???????????????
		params.addGeneralParam(PlatformHumanConst.PRM_HUMAN_DATE_NAME,viewTableProperty.getDateName());
		// ????????????(????????????/????????????)
		// ?????????????????????
		String[] aryTitle = MospUtility.split(viewTitles[i], MospConst.APP_PROPERTY_SEPARATOR);
		// ???????????????
		String title = aryTitle[0];
		// ????????????(????????????/????????????)??????[HumanHistoryCard.jsp?????????]
		String imegeDivision = (String)params.getGeneralParam(HumanHistoryCardAction.KEY_VIEW_HUMAN_HISTORY_CARD);
		// ?????????????????????
		if(imegeDivision != null && imegeDivision.equals(HumanHistoryCardAction.CMD_ADD_SELECT)){
			// ????????????????????????????????????????????????
			if(aryTitle.length>1 &&!aryTitle[1].isEmpty() && aryTitle[1] != null){
				// ???????????????
				title = aryTitle[1];
			}
		}
%>
		<table class="OverTable">
			<tr>
				<th colspan="3" class="ListTableTh" id="thDivision"><span class="TitleTh"><%= params.getName(title) %></span>
				</th>
			</tr>
		</table>
<%
	}
	// ???????????????????????????
	if(view.equals(HumanHistoryListAction.KEY_VIEW_HISTORY_LIST) && divisionType.equals(PlatformHumanConst.PRM_HUMAN_DIVISION_TYPE_HISTORY)){
		// ????????????????????????
		String activeDate = (String)params.getGeneralParam(PlatformConst.PRM_TRANSFERRED_ACTIVATE_DATE);
		params.addGeneralParam(PlatformConst.PRM_TRANSFERRED_ACTIVATE_DATE,activeDate);
		// ??????????????????????????????????????????
		ViewTableProperty viewTableProperty = viewConfig.getViewTable(viewTableKeys[i]);
%>
		<table class="OverTable">
			<tr>
				<th colspan="4" class="ListTableTh" id=<%= activeDate %>>
				<span class="TitleTh"><%= HtmlUtility.escapeHTML(activeDate) %><%= params.getName("Wave") %></span>
					<span class="TableButtonSpan">
					<button type="button" id="btnHumenInfo" class="Name4Button" onclick="submitTransfer(event, null, null, new Array('<%= PlatformConst.PRM_TRANSFERRED_ACTIVATE_DATE %>',  '<%= activeDate %>','<%= PlatformConst.PRM_TRANSFERRED_TYPE %>',  '<%= division %>','<%= PlatformConst.PRM_TRANSFERRED_ACTION %>',  '<%= HumanHistoryCardAction.CMD_EDIT_SELECT %>'), '<%= HumanHistoryListAction.CMD_TRANSFER %>');">
					<% // ???????????????????????????%>
					<%= isReferenceDivision ? params.getName("History","Reference") : params.getName("History","Edit")%>
					</button>&nbsp;
					<% // ???????????????????????????%>
					<% if (!isReferenceDivision) { %>
						<button type="button" id="btnHumenInfo" name="btnDelete" class="Name4Button" onclick="submitTransfer(event,null,checkExtra,new Array('<%= PlatformConst.PRM_TRANSFERRED_ACTIVATE_DATE %>', '<%= activeDate %>','<%= PlatformConst.PRM_TRANSFERRED_TYPE %>','<%= division %>'),'<%= HumanHistoryListAction.CMD_DELETE %>');"><%= params.getName("History","Delete") %></button>
					<% } %>
					</span>
				</th>
			</tr>
		</table>
<%
	}
	// ?????????????????????
	if(view.equals(HumanArrayCardAction.KEY_VIEW_ARRAY_CARD) && divisionType.equals(PlatformHumanConst.PRM_HUMAN_DIVISION_TYPE_ARRAY)){
		// ??????????????????????????????????????????
		ViewTableProperty viewTableProperty = viewConfig.getViewTable(viewTableKeys[i]);
		// ???????????????
		params.addGeneralParam(PlatformHumanConst.PRM_HUMAN_DATE_NAME,viewTableProperty.getDateName());
		// ????????????(??????/??????)
		// ?????????????????????
		String[] aryTitle = MospUtility.split(viewTitles[i], MospConst.APP_PROPERTY_SEPARATOR);
		// ???????????????
		String title = aryTitle[0];
		// ????????????(??????/??????)??????[HumanArrayCard.jsp?????????]
		String imegeDivision = (String)params.getGeneralParam(HumanArrayCardAction.KEY_VIEW_ARRAY_CARD);
		// ?????????????????????
		if(imegeDivision != null && imegeDivision.equals(HumanArrayCardAction.CMD_ADD_SELECT)){
			// ????????????????????????????????????????????????
			if(aryTitle.length>1 &&!aryTitle[1].isEmpty() && aryTitle[1] != null){
				// ???????????????
				title = aryTitle[1];
			}
		}
%>
		<table class="OverTable">
			<tr>
				<th colspan="3" class="ListTableTh" id="thDivision"><span class="TitleTh"><%= params.getName(title) %></span>
				</th>
			</tr>
		</table>
<%
	}
	// ????????????????????????
	params.addGeneralParam(PlatformHumanConst.PRM_HUMAN_VIEW_TABLE, viewTableKeys[i]);
%>
	<jsp:include page="<%= PlatformHumanConst.PATH_HUMAN_VIEW_TABLE_JSP %>" flush="false" />
<%
}
%>
</div>

