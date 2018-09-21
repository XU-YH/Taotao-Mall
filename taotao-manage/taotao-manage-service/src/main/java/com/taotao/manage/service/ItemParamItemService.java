package com.taotao.manage.service;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.abel533.entity.Example;
import com.taotao.manage.mapper.ItemParamItemMapper;
import com.taotao.manage.pojo.ItemParamItem;

@Service
public class ItemParamItemService extends BaseService<ItemParamItem> {

	@Autowired
	private ItemParamItemMapper itemParamItemMapper;

	// 更新商品规格参数
	public Integer updateItemParamItemById(Long itemId, String itemParams) {
		ItemParamItem record = new ItemParamItem();
		record.setParamData(itemParams);
		record.setUpdated(new Date());

		Example example = new Example(ItemParamItem.class);
		example.createCriteria().andEqualTo("itemId", itemId);

		return this.itemParamItemMapper.updateByExampleSelective(record, example);
	}

}
