package com.taotao.manage.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.taotao.manage.pojo.Item;
import com.taotao.manage.service.ItemService;

@Controller
@RequestMapping("item")
public class ItemController {

	@Autowired
	private ItemService itemService;

	private static final Logger LOGGER = LoggerFactory.getLogger(ItemController.class);

	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<Void> saveItem(Item item, @RequestParam("desc") String desc) {
		try {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("新增商品，item={}, desc={}", item, desc);
			}
			// 新增商品
			Boolean bool = this.itemService.saveItem(item, desc);
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
}
