package com.jdjt.mangrovepay.utils;

import com.android.pc.ioc.view.PullToRefreshManager;
import com.jdjt.mangrovepay.contanst.UrlParams;

public class PageUtil {
	public  int currPageNo = 1;
	public int totalCount;
	public int pageSize = UrlParams.PAGESIZE;
	public boolean isEndPageTurn = false;
	
	
	public PageUtil(int totalCount, int pageSize){
		this.totalCount = totalCount;
		this.pageSize = pageSize;
	}
	
	public PageUtil () {
		
	}
	
	/**
	 * 上拉 ,向下翻页
	 */
	public  void isEndPullPage(){
		
		currPageNo++;
		
		//判断总条数/每页显示条数,是否有余数
		if(totalCount % pageSize == 0){
			if(currPageNo > totalCount/pageSize){

				currPageNo = totalCount/pageSize;
				isEndRefreshPage();
				isEndPageTurn = true;//结束下翻页
			}else{
			    isEndPageTurn = false;//正在翻页
			}
		}else{
			if(currPageNo > (totalCount/pageSize+1)){
				currPageNo = totalCount/pageSize+1;

				isEndRefreshPage();
				isEndPageTurn = true;//结束下翻页
			}else{

				isEndPageTurn = false;//正在翻页
			}
		}
		
	}
	
	/**
	 * 下拉 ,向上翻页
	 */
	public  void isEndDownPage(){
		isEndRefreshPage();
//		currPageNo--;
//
//		if(currPageNo < 1){
//			currPageNo = 1;
//
//			PullToRefreshManager.getInstance().onHeaderRefreshComplete();
//			PullToRefreshManager.getInstance().onFooterRefreshComplete();
//			isEndPageTurn = true;//结束上翻页
//		}else{
//			isEndPageTurn = false;//上翻页正在进行
//		}
	}
	
	/**
	 * 结束翻页
	 */
	public  void isEndRefreshPage(){
		//上拉
		PullToRefreshManager.getInstance().onHeaderRefreshComplete();
		PullToRefreshManager.getInstance().headerUnable();
		PullToRefreshManager.getInstance().headerEnable();
		//下拉
		PullToRefreshManager.getInstance().onFooterRefreshComplete();
		PullToRefreshManager.getInstance().footerUnable();
		PullToRefreshManager.getInstance().footerEnable();
	}

	public int getCurrPageNo() {
		return currPageNo;
	}

	public void setCurrPageNo(int currPageNo) {
		this.currPageNo = currPageNo;
	}

	public int getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}
	
	
	

}
