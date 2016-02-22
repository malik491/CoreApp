package edu.depaul.se491.validators;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import edu.depaul.se491.beans.AddressBean;
import edu.depaul.se491.beans.OrderBean;
import edu.depaul.se491.beans.OrderItemBean;
import edu.depaul.se491.beans.PaymentBean;
import edu.depaul.se491.enums.OrderStatus;
import edu.depaul.se491.enums.OrderType;
import edu.depaul.se491.utils.ParamLengths;
import edu.depaul.se491.utils.ParamValues;

public class OrderValidatorTest {
	private OrderValidator validator;

	@Before
	public void setUp() throws Exception {
		validator = new OrderValidator();
	}

	@Test
	public void testValidate() {
		OrderBean bean = new OrderBean();
		
		// null
		assertFalse(validator.validate(null, true));
		
		
		// not null
		assertFalse(validator.validate(bean, true));
		
		PaymentBean payment = new PaymentBean();
		AddressBean address = new AddressBean();
		OrderItemBean[] orderItems = new OrderItemBean[ParamValues.Order.MAX_ORDER_ITEMS];
		
		bean.setStatus(OrderStatus.SUBMITTED);
		bean.setType(OrderType.PICKUP);
		bean.setConfirmation("1234567");
		bean.setPayment(payment);
		bean.setOrderItems(orderItems);
		//valid bean
		assertTrue(validator.validate(bean, true));
		assertFalse(validator.validate(bean, false));
		
		//confirmation < MIN_CONFIRMATION
		bean.setStatus(OrderStatus.SUBMITTED);
		bean.setType(OrderType.PICKUP);
		bean.setConfirmation("");
		bean.setPayment(payment);
		bean.setOrderItems(orderItems);
		assertTrue(validator.validate(bean, true));
		assertFalse(validator.validate(bean, false));
		
		//confirmation > MAX_CONFIRMATION
		String moreThanMaxLen = getLongString(ParamLengths.Order.MAX_CONFIRMATION + 1);
		bean.setStatus(OrderStatus.SUBMITTED);
		bean.setType(OrderType.PICKUP);
		bean.setConfirmation(moreThanMaxLen);
		bean.setPayment(payment);
		bean.setOrderItems(orderItems);
		assertTrue(validator.validate(bean, true));
		assertFalse(validator.validate(bean, false));
		
		//payment == null
		bean.setStatus(OrderStatus.SUBMITTED);
		bean.setType(OrderType.PICKUP);
		bean.setConfirmation("1233456");
		bean.setPayment(null);
		bean.setOrderItems(orderItems);
		assertFalse(validator.validate(bean, true));
		assertFalse(validator.validate(bean, false));
		
		//orderitems == null
		bean.setStatus(OrderStatus.SUBMITTED);
		bean.setType(OrderType.PICKUP);
		bean.setConfirmation("1233456");
		bean.setPayment(payment);
		bean.setOrderItems(null);
		assertFalse(validator.validate(bean, true));
		assertFalse(validator.validate(bean, false));
		
		//OrderStatus == null
		bean.setStatus(null);
		bean.setType(OrderType.PICKUP);
		bean.setConfirmation("1233456");
		bean.setPayment(payment);
		bean.setOrderItems(orderItems);
		assertFalse(validator.validate(bean, true));
		assertFalse(validator.validate(bean, false));
		
		//OrderType == null
		bean.setStatus(OrderStatus.SUBMITTED);
		bean.setType(null);
		bean.setConfirmation("1233456");
		bean.setPayment(payment);
		bean.setOrderItems(orderItems);
		assertFalse(validator.validate(bean, true));
		assertFalse(validator.validate(bean, false));
		
		//address == null
		bean.setStatus(OrderStatus.SUBMITTED);
		bean.setType(OrderType.DELIVERY);
		bean.setConfirmation("1234567");
		bean.setPayment(payment);
		bean.setOrderItems(orderItems);
		bean.setAddress(null);
		assertFalse(validator.validate(bean, true));
		assertFalse(validator.validate(bean, false));
		
		//OrderType == DELIVERY; address != null
		bean.setStatus(OrderStatus.SUBMITTED);
		bean.setType(OrderType.DELIVERY);
		bean.setConfirmation("1234567");
		bean.setPayment(payment);
		bean.setOrderItems(orderItems);
		bean.setAddress(address);
		assertTrue(validator.validate(bean, true));
		assertFalse(validator.validate(bean, false));
		
		//an existing order
		bean.setId(1L);
		
		//valid existing order
		bean.setStatus(OrderStatus.SUBMITTED);
		bean.setType(OrderType.PICKUP);
		bean.setConfirmation("1234567");
		bean.setPayment(payment);
		bean.setOrderItems(orderItems);
		bean.setAddress(null);
		assertFalse(validator.validate(bean, true));
		assertTrue(validator.validate(bean, false));
		
		//confirmation < MIN_CONFIRMATION
		bean.setStatus(OrderStatus.SUBMITTED);
		bean.setType(OrderType.PICKUP);
		bean.setConfirmation("");
		bean.setPayment(payment);
		bean.setOrderItems(orderItems);
		bean.setAddress(null);
		assertFalse(validator.validate(bean, true));
		assertFalse(validator.validate(bean, false));
		
		//confirmation > MAX_CONFIRMATION
		moreThanMaxLen = getLongString(ParamLengths.Order.MAX_CONFIRMATION + 1);
		bean.setStatus(OrderStatus.SUBMITTED);
		bean.setType(OrderType.PICKUP);
		bean.setConfirmation(moreThanMaxLen);
		bean.setPayment(payment);
		bean.setOrderItems(orderItems);
		bean.setAddress(null);
		assertFalse(validator.validate(bean, true));
		assertFalse(validator.validate(bean, false));
		
		
		//payment == null 
		bean.setStatus(OrderStatus.SUBMITTED);
		bean.setType(OrderType.PICKUP);
		bean.setConfirmation("1234567");
		bean.setPayment(null);
		bean.setOrderItems(orderItems);
		bean.setAddress(null);
		assertFalse(validator.validate(bean, true));
		assertFalse(validator.validate(bean, false));
		
		//orderItems == null 
		bean.setStatus(OrderStatus.SUBMITTED);
		bean.setType(OrderType.PICKUP);
		bean.setConfirmation("1234567");
		bean.setPayment(payment);
		bean.setOrderItems(null);
		bean.setAddress(null);
		assertFalse(validator.validate(bean, true));
		assertFalse(validator.validate(bean, false));
		
		//OrderStatus == null 
		bean.setStatus(null);
		bean.setType(OrderType.PICKUP);
		bean.setConfirmation("1234567");
		bean.setPayment(payment);
		bean.setOrderItems(orderItems);
		bean.setAddress(null);
		assertFalse(validator.validate(bean, true));
		assertFalse(validator.validate(bean, false));
		
		
		//OrderType == DELIVERY; address != null 
		bean.setStatus(OrderStatus.SUBMITTED);
		bean.setType(OrderType.DELIVERY);
		bean.setConfirmation("1234567");
		bean.setPayment(payment);
		bean.setOrderItems(orderItems);
		bean.setAddress(address);
		assertFalse(validator.validate(bean, true));
		assertTrue(validator.validate(bean, false));
	}

	@Test
	public void testValidateId() {
		boolean isNewOrder;
		
		// null
		Long id = null;
		isNewOrder = false;
		assertFalse(validator.validateId(id, isNewOrder));
		
		isNewOrder = true;
		assertFalse(validator.validateId(id, isNewOrder));
		
		// zero (the L to make 0 long instead of int)
		id = 0L; 
		isNewOrder = true;
		assertTrue(validator.validateId(id, isNewOrder));
		
		isNewOrder = false;
		assertFalse(validator.validateId(id, isNewOrder));
		
		// > 0
		id = Long.MAX_VALUE;
		isNewOrder = false;
		assertTrue(validator.validateId(id, isNewOrder));
		
		isNewOrder = true;
		assertFalse(validator.validateId(id, isNewOrder));
		
		// < 0
		id = Long.MIN_VALUE;
		isNewOrder = true;
		assertFalse(validator.validateId(id, isNewOrder));
		
		isNewOrder = false;
		assertFalse(validator.validateId(id, isNewOrder));
	}

	@Test
	public void testValidateConfirmation() {
		
		//MIN_CONFIRMATION < Confirmation <  MAX_CONFIRMATION
		assertTrue(validator.validateConfirmation("1234567890"));

		//Confirmation <  MIN_CONFIRMATION
		assertFalse(validator.validateConfirmation(""));
		
		//Confirmation > MAX_CONFIRMATION
		String moreThanMaxLen = getLongString(ParamLengths.Order.MAX_CONFIRMATION + 1);
		assertFalse(validator.validateConfirmation(moreThanMaxLen));
	}
	
	private String getLongString(int maxLength) {
		StringBuilder sb = new StringBuilder();
		for (int i=0; i < maxLength; i++)
			sb.append("x");
		
		return sb.toString();
	}

}
