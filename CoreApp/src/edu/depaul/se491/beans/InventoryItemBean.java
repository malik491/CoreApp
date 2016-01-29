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
public class InventoryItemBean implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private long id;
	private int quantity;
	private MeasurementUnit measurementUnit;
	
	public InventoryItemBean() {
		
	}
	
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
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
