package com.taotao.manage.controller;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.taotao.common.bean.EasyUIResult;
import com.taotao.manage.pojo.Item;
import com.taotao.manage.service.ItemService;

@Controller
@RequestMapping("item")
public class ItemController {

	@Autowired
	private ItemService itemService;

	private static final Logger LOGGER = LoggerFactory.getLogger(ItemController.class);

	/**
	 * 新增商品
	 * 
	 * @param item
	 * @param desc
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<Void> saveItem(Item item, @RequestParam("desc") String desc,
			@RequestParam("itemParams") String itemParams) {
		try {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("新增商品，item={}, desc={}", item, desc);
			}
			if (StringUtils.isEmpty(item.getTitle())) { // TODO 未完成，待优化
				// 参数有误，400
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
			}
			// 新增商品
			Boolean bool = this.itemService.saveItem(item, desc, itemParams);
			if (bool) {
				if (LOGGER.isDebugEnabled()) {
					LOGGER.info("新增商品成功，itemId={}", item.getId());
				}
				// 201
				return ResponseEntity.status(HttpStatus.CREATED).build();
			} else {
				if (LOGGER.isDebugEnabled()) {
					LOGGER.info("新增商品失败，item={}", item);
				}
				// 500
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
			}
		} catch (Exception e) {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.error("新增商品出错，item={}" + item, e);
			}
		}
		// 500
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
	}

	/**
	 * 查询商品列表
	 * 
	 * @param page
	 * @param rows
	 * @return
	 */
	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<EasyUIResult> queryItemList(@RequestParam(value = "page", defaultValue = "1") Integer page,
			@RequestParam(value = "rows", defaultValue = "30") Integer rows) {
		try {
			EasyUIResult easyUIResult = this.itemService.queryItemList(page, rows);
			return ResponseEntity.ok(easyUIResult);
		} catch (Exception e) {
			LOGGER.error("查询商品列表出错！ page = " + page + "， rows= " + rows, e);
		}
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
	}

	/**
	 * 修改商品
	 * 
	 * @param item
	 * @param desc
	 * @return
	 */
	@RequestMapping(method = RequestMethod.PUT)
	public ResponseEntity<Void> updateItem(Item item, @RequestParam("desc") String desc,
			@RequestParam("itemParams") String itemParams) {
		try {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("编辑商品，item={}, desc={}", item, desc);
			}
			if (StringUtils.isEmpty(item.getTitle())) { // TODO 未完成，待优化
				// 参数有误，400
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
			}
			// 编辑商品
			Boolean bool = this.itemService.updateItem(item, desc, itemParams);
			if (bool) {
				if (LOGGER.isDebugEnabled()) {
					LOGGER.info("编辑商品成功，itemId={}", item.getId());
				}
				// 204
				return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
			} else {
				if (LOGGER.isDebugEnabled()) {
					LOGGER.info("编辑商品失败，item={}", item);
				}
				// 500
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
			}
		} catch (Exception e) {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.error("编辑商品出错，item={}" + item, e);
			}
		}
		// 500
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
	}
}
