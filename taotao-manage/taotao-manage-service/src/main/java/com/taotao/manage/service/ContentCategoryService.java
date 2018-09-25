package com.taotao.manage.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.taotao.manage.pojo.ContentCategory;

@Service
public class ContentCategoryService extends BaseService<ContentCategory> {

	public void saveContentCategory(ContentCategory contentCategory) {
		contentCategory.setId(null);
		contentCategory.setIsParent(false);
		contentCategory.setSortOrder(1);
		contentCategory.setStatus(1);
		super.save(contentCategory);

		// 判断该节点的父节点的idParent是否为true，不是，则修改为true
		ContentCategory parent = super.queryById(contentCategory.getParentId());
		if (!parent.getIsParent()) {
			parent.setIsParent(true);
			super.update(parent);
		}
	}

	public void deleteAll(ContentCategory contentCategory) {
		ArrayList<Object> ids = new ArrayList<Object>();
		ids.add(contentCategory.getId());

		// 递归查找该节点下的所有子节点id
		this.findAllSubNode(ids, contentCategory.getId());

		super.deleteByIds(ids, ContentCategory.class, "id");

		ContentCategory record = new ContentCategory();
		record.setParentId(contentCategory.getParentId());
		List<ContentCategory> list = super.queryListByWhere(record);
		if (null == list || list.isEmpty()) {
			ContentCategory parent = new ContentCategory();
			parent.setId(contentCategory.getParentId());
			parent.setIsParent(false);
			super.updateSelective(parent);
		}

	}

	private void findAllSubNode(ArrayList<Object> ids, Long pid) {
		ContentCategory record = new ContentCategory();
		record.setParentId(pid);
		List<ContentCategory> list = super.queryListByWhere(record);
		for (ContentCategory contentCategory : list) {
			ids.add(contentCategory.getId());
			//
			if (contentCategory.getIsParent()) {
				findAllSubNode(ids, contentCategory.getId());
			}
		}
	}
}
