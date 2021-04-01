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
package jp.mosp.time.dao.settings;

import java.util.Date;
import java.util.List;
import java.util.Map;

import jp.mosp.framework.base.BaseDaoInterface;
import jp.mosp.framework.base.MospException;
import jp.mosp.time.dto.settings.DifferenceRequestDtoInterface;

/**
 * 時差出勤申請DAOインターフェース
 */
public interface DifferenceRequestDaoInterface extends BaseDaoInterface {
	
	/**
	 * 個人IDと申請日から時差出勤情報を取得する。<br>
	 * 条件と合致する情報が存在しない場合は、nullを返す。<br>
	 * ワークフローの状態が取下げであるものは除く。<br>
	 * @param personalId 個人ID
	 * @param requestDate 申請日
	 * @return 時差出勤情報
	 * @throws MospException SQLの作成に失敗した場合、或いはSQL例外が発生した場合
	 */
	DifferenceRequestDtoInterface findForKeyOnWorkflow(String personalId, Date requestDate) throws MospException;
	
	/**
	 * ワークフロー番号から時差出勤情報を取得する。<br>
	 * 条件と合致する情報が存在しない場合は、nullを返す。<br>
	 * @param workflow ワークフロー番号
	 * @return 時差出勤DTO
	 * @throws MospException SQLの作成に失敗した場合、或いはSQL例外が発生した場合
	 */
	DifferenceRequestDtoInterface findForWorkflow(long workflow) throws MospException;
	
	/**
	 * 個人IDと開始日と終了日から時差出勤申請リストを取得する。<br>
	 * @param personalId 個人ID
	 * @param startDate 開始日
	 * @param endDate 終了日
	 * @return 時差出勤申請リストDTO
	 * @throws MospException SQLの作成に失敗した場合、或いはSQL例外が発生した場合
	 */
	List<DifferenceRequestDtoInterface> findForList(String personalId, Date startDate, Date endDate)
			throws MospException;
	
	/**
	 * 個人IDと対象期間から時差出勤申請情報リストを取得する。<br>
	 * @param personalId 個人ID
	 * @param firstDate  対象期間初日
	 * @param lastDate   対象期間最終日
	 * @return 時差出勤申請情報リスト
	 * @throws MospException SQLの作成に失敗した場合、或いはSQL例外が発生した場合
	 */
	List<DifferenceRequestDtoInterface> findForTerm(String personalId, Date firstDate, Date lastDate)
			throws MospException;
	
	/**
	 * 承認段階、承認状況から時差出勤申請リストを取得する。<br>
	 * @param personalId 個人ID
	 * @param workflowStage 承認段階
	 * @param workflowStatus 承認状況
	 * @param routeCode ルートコード
	 * @return 時差出勤申請リスト
	 * @throws MospException SQLの作成に失敗した場合、或いはSQL例外が発生した場合
	 */
	List<DifferenceRequestDtoInterface> findForWorkflowStatus(String personalId, int workflowStage,
			String workflowStatus, String routeCode) throws MospException;
	
	/**
	 * 条件による検索。
	 * <p>
	 * 検索条件から時差出勤リストを取得する。
	 * </p>
	 * @param param 検索条件
	 * @return 時差出勤リスト
	 * @throws MospException SQLの作成に失敗した場合、或いはSQL例外が発生した場合
	 */
	List<DifferenceRequestDtoInterface> findForSearch(Map<String, Object> param) throws MospException;
	
	/**
	 * 検索条件取得。
	 * @return 設時差出勤検索条件マップ
	 */
	Map<String, Object> getParamsMap();
}
