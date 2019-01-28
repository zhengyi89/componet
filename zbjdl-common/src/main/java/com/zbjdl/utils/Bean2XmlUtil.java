package com.zbjdl.utils;

import java.util.List;
import java.util.Map;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.thoughtworks.xstream.XStream;

public class Bean2XmlUtil {
	public static final <T> String bean2Xml(T t, Map<String, Class<?>> alias) {
		XStream xstream = new XStream();
		if(alias != null && alias.size() > 0){
			for (Map.Entry<String, Class<?>> entry : alias.entrySet()) {
				xstream.alias(entry.getKey(), entry.getValue());
			}
		}
		
		return xstream.toXML(t);
	}
	
	public static final <T> T xml2Bean(String xml, Map<String, Class<?>> alias){
		XStream xstream = new XStream();
		if(alias != null && alias.size() > 0){
			for (Map.Entry<String, Class<?>> entry : alias.entrySet()) {
				xstream.alias(entry.getKey(), entry.getValue());
			}
		}
		
		return (T) xstream.fromXML(xml);
	}
	
	public static void main(String[] args) {
		Item item = new Bean2XmlUtil().new Item();
		item.setItem(99);
		Item item2 = new Bean2XmlUtil().new Item();
		item2.setItem(88);
		List<Item> list = Lists.newArrayList();
		list.add(item);
		list.add(item2);
		
		Model model = new Bean2XmlUtil().new Model();
		model.setId(99999l);
		model.setItems(list);
		
		Map<String, Class<?>> alias = Maps.newHashMap();
		alias.put("root", Model.class);
		alias.put("data", Item.class);
		System.out.println(Bean2XmlUtil.bean2Xml(model, alias));
	}
	
	class Model{
		private Long id;
		private String name;
		
		List<Item> items;
		
		public Long getId() {
			return id;
		}
		public void setId(Long id) {
			this.id = id;
		}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public List<Item> getItems() {
			return items;
		}
		public void setItems(List<Item> items) {
			this.items = items;
		}
		
	}
	
	class Item{
		private Integer item;

		public Integer getItem() {
			return item;
		}

		public void setItem(Integer item) {
			this.item = item;
		}
	}
}
