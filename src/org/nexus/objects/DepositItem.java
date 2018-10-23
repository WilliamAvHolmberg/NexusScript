package org.nexus.objects;

import java.util.List;

public class DepositItem extends RequiredItem{
	
	private List<String> items;
	private DepositType type;
	public enum DepositType{
		DEPOSIT_ALL, DEPOSIT_ALL_EXCEPT
	}
	public DepositItem(DepositType type, List<String> items) {
		setType(type);
		setItems(items);
	}
	public List<String> getItems() {
		return items;
	}
	public void setItems(List<String> items) {
		this.items = items;
	}
	public DepositType getType() {
		return type;
	}
	public void setType(DepositType type) {
		this.type = type;
	}
}
