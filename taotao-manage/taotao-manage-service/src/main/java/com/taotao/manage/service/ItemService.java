package com.taotao.manage.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.taotao.manage.pojo.Item;
import com.taotao.manage.pojo.ItemDesc;

@Service
public class ItemService extends BaseService<Item> {

	@Autowired
	private ItemDescService itemDescService;

	public Boolean saveItem(Item item, String desc) {
		// 设置初始值
		item.setStatus(1);
		item.setId(null); // 处于安全考虑，强制设置id为null，使数据库中id自增
		Integer count1 = super.save(item);

		ItemDesc itemDesc = new ItemDesc();
		itemDesc.setItemId(item.getId());
		itemDesc.setItemDesc(desc);
		Integer count2 = this.itemDescService.save(itemDesc);
		return count1 == 1 && count2 == 1;
	}

}
