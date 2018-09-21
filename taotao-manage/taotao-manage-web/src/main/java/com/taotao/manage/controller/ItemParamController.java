package com.taotao.manage.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.taotao.common.bean.EasyUIResult;
import com.taotao.manage.pojo.ItemParam;
import com.taotao.manage.service.ItemParamService;

@Controller
@RequestMapping("item/param")
public class ItemParamController {

	@Autowired
	private ItemParamService itemParamService;

	private static final Logger LOGGER = LoggerFactory.getLogger(ItemController.class);
	
	/**
	 * 根据商品类目id查询规格参数模板
	 * 
	 * @param itemCatId
	 * @return
	 */
	@RequestMapping(value = "{itemCatId}", method = RequestMethod.GET)
	public ResponseEntity<ItemParam> queryByItemCatId(@PathVariable("itemCatId") Long itemCatId) {
		try {
			ItemParam itemParam = new ItemParam();
			itemParam.setItemCatId(itemCatId);
			ItemParam itemParam2 = this.itemParamService.queryOne(itemParam);
			if (null == itemParam2) {
				// 404
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
			}
			return ResponseEntity.ok(itemParam2);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
	}

	/**
	 * 新增规格参数模板
	 * 
	 * @param itemCatId
	 * @param paramData
	 * @return
	 */
	@RequestMapping(value = "{itemCatId}", method = RequestMethod.POST)
	public ResponseEntity<Void> saveItemParam(@PathVariable("itemCatId") Long itemCatId,
			@RequestParam("paramData") String paramData) {
		try {
			ItemParam itemParam = new ItemParam();
			itemParam.setId(null);
			itemParam.setItemCatId(itemCatId);
			itemParam.setParamData(paramData);
			this.itemParamService.save(itemParam);
			return ResponseEntity.status(HttpStatus.CREATED).build();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();

	}
	
	/**
	 * 查询商品规格参数模板列表
	 * 
	 * @param page
	 * @param rows
	 * @return
	 */
	@RequestMapping(value="list", method = RequestMethod.GET)
	public ResponseEntity<EasyUIResult> queryItemList(@RequestParam(value = "page", defaultValue = "1") Integer page,
			@RequestParam(value = "rows", defaultValue = "30") Integer rows) {
		try {
			EasyUIResult easyUIResult = this.itemParamService.queryItemParamList(page, rows);
			return ResponseEntity.ok(easyUIResult);
		} catch (Exception e) {
			LOGGER.error("查询商品规格参数模板出错！ page = " + page + "， rows= " + rows, e);
		}
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
	}

}