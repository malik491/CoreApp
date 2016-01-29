/**
 * 
 */
package edu.depaul.se491.beans;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlRootElement;

import edu.depaul.se491.enums.MeasurementUnit;

/**
 * @author Malik
 */

@XmlRootElement
public class RecipeItemBean implements Serializable {
	private static final long serialVersionUID = 1L;

	private InventoryItemBean inventoryItem;
	private int quantity;
	private MeasurementUnit measurementUnit;
	
	public RecipeItemBean() {
		
	}

	public InventoryItemBean getInventoryItem() {
		return inventoryItem;
	}

	public void setInventoryItem(InventoryItemBean inventoryItem) {
		this.inventoryItem = inventoryItem;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public MeasurementUnit getMeasurementUnit() {
		return measurementUnit;
	}

	public void setMeasurementUnit(MeasurementUnit measurementUnit) {
		this.measurementUnit = measurementUnit;
	}
	
	
}
