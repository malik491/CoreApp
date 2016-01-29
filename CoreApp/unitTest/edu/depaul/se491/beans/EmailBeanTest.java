package edu.depaul.se491.beans;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class EmailBeanTest {
	private EmailBean bean;
	
	@Before
	public void setUp() throws Exception {
		bean = new EmailBean();
	}

	@Test
	public void testEmailBean() {
		assertNotNull(bean);
		assertNull(bean.getFrom());
		assertNull(bean.getTo());
		assertNull(bean.getMessage());
	}

	@Test
	public void testGetTo() {
		assertNull(bean.getTo());
		
		String to = "to@email.com";
		bean.setTo(to);
		
		assertEquals(to, bean.getTo());
		assertNotEquals("xyz", bean.getTo());
	}

	@Test
	public void testSetTo() {
		assertNull(bean.getTo());
		
		String to = "to@email.com";

		bean.setTo(to);
		assertEquals(to, bean.getTo());
		
		bean.setTo(null);
		assertNull(bean.getTo());
	}

	@Test
	public void testGetFrom() {
		assertNull(bean.getFrom());
		
		String from = "from@email.com";
		bean.setFrom(from);
		
		assertEquals(from, bean.getFrom());
		assertNotEquals("abc", bean.getFrom());
	}

	@Test
	public void testSetFrom() {
		assertNull(bean.getFrom());
		
		String from = "from@email.com";

		bean.setFrom(from);
		assertEquals(from, bean.getFrom());
		
		bean.setFrom(null);
		assertNull(bean.getFrom());
	}

	@Test
	public void testGetMessage() {
		assertNull(bean.getMessage());
		
		String msg = "this is the email blah";
		bean.setMessage(msg);
		
		assertEquals(msg, bean.getMessage());
		assertNotEquals("123", bean.getMessage());
	}

	@Test
	public void testSetMessage() {
		assertNull(bean.getMessage());
		
		String msg = "some message blah";

		bean.setMessage(msg);
		assertEquals(msg, bean.getMessage());
		
		bean.setMessage(null);
		assertNull(bean.getMessage());
	}

}
