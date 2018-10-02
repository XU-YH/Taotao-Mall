package com.taotao.manage.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.github.abel533.entity.Example;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.taotao.common.bean.EasyUIResult;
import com.taotao.common.service.ApiService;
import com.taotao.manage.mapper.ItemMapper;
import com.taotao.manage.pojo.Item;
import com.taotao.manage.pojo.ItemDesc;
import com.taotao.manage.pojo.ItemParamItem;

@Service
public class ItemService extends BaseService<Item> {

	@Autowired
	private ItemDescService itemDescService;

	@Autowired
	private ItemParamItemService itemParamItemService;

	@Autowired
	private ItemMapper itemMapper;

	@Autowired
	private ApiService apiService;

	@Value("${TAOTAO_WEB_URL}")
	private String TAOTAO_WEB_URL;

	public Boolean saveItem(Item item, String desc, String itemParams) {
		// 设置初始值
		item.setStatus(1);
		item.setId(null); // 处于安全考虑，强制设置id为null，使数据库中id自增
		Integer count1 = super.save(item);

		ItemDesc itemDesc = new ItemDesc();
		itemDesc.setItemId(item.getId());
		itemDesc.setItemDesc(desc);
		Integer count2 = this.itemDescService.save(itemDesc);

		// 保存商品规格参数
		ItemParamItem itemParamItem = new ItemParamItem();
		itemParamItem.setItemId(item.getId());
		itemParamItem.setParamData(itemParams);
		Integer count3 = this.itemParamItemService.save(itemParamItem);

		return count1 == 1 && count2 == 1 && count3 == 1;
	}

	// 查询分页数据
	public EasyUIResult queryItemList(Integer page, Integer rows) {
		// 设置分页参数
		PageHelper.startPage(page, rows);
		Example example = new Example(Item.class);
		// 按创建时间倒序排序
		example.setOrderByClause("created DESC");

		List<Item> items = this.itemMapper.selectByExample(example);

		PageInfo<Item> pageInfo = new PageInfo<Item>(items);

		return new EasyUIResult(pageInfo.getTotal(), pageInfo.getList());
	}

	// 更新商品
	public Boolean updateItem(Item item, String desc, String itemParams) {
		item.setStatus(null);// 强制设置商品状态不能修改
		Integer count1 = super.updateSelective(item);

		ItemDesc itemDesc = new ItemDesc();
		itemDesc.setItemDesc(desc);
		itemDesc.setItemId(item.getId());
		Integer count2 = this.itemDescService.updateSelective(itemDesc);

		// 更新规格参数
		Integer count3 = this.itemParamItemService.updateItemParamItemById(item.getId(), itemParams);

		// 通知其他系统该商品已经更新
		try {
			String url = TAOTAO_WEB_URL + "/item/cache/" + item.getId() + ".html";
			this.apiService.doPost(url);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return count1 == 1 && count2 == 1 && count3 == 1;
	}
}
