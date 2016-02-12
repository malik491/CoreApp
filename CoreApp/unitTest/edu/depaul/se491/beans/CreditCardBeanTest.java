package edu.depaul.se491.beans;
 
import static org.junit.Assert.*;
 
import org.junit.Before;
import org.junit.Test;
 
public class CreditCardBeanTest {
	CreditCardBean ccb;
	
    @Before
    public void setUp() throws Exception {
    	ccb = new CreditCardBean();
    }
 
    @Test
    public void testCreditCardBean() {
         
        // testing default constructor.
        assertTrue(ccb instanceof CreditCardBean);  
        assertEquals(null,ccb.getCcHolderName());
        assertEquals(null,ccb.getCcNumber());
        assertNotEquals(null,ccb.getExpMonth());
        assertNotEquals(null,ccb.getExpYear());
 
    }
 
    @Test
    public void testCreditCardBeanStringStringIntInt() {
         
        // testing special constructor
        ccb = new CreditCardBean("123456789012","John Doe",12,2020);
         
        assertTrue(ccb instanceof CreditCardBean);
         
        assertEquals("John Doe",ccb.getCcHolderName());
        assertEquals("123456789012",ccb.getCcNumber());
        assertEquals(12,ccb.getExpMonth());
        assertEquals(2020,ccb.getExpYear());
         
    }
 
    @Test
    public void testGetCcNumber() {
         
        ccb.setCcNumber("123456789012");
        String numberStr = ccb.getCcNumber();
        assertEquals("123456789012", numberStr);
         
    }
 
    @Test
    public void testSetCcNumber() {
         
        ccb.setCcNumber("123456789012");
        assertEquals("123456789012",ccb.getCcNumber());
        ccb.setCcNumber("098765432109");
        assertEquals("098765432109",ccb.getCcNumber());
 
    }
 
    @Test
    public void testGetExpMonth() {
         
        ccb.setExpMonth(12);
        int expMonth = ccb.getExpMonth();
        assertEquals(12,expMonth);
         
    }
 
    @Test
    public void testSetExpMonth() {

    	ccb.setExpMonth(12);
        assertEquals(12,ccb.getExpMonth());
        ccb.setExpMonth(6);
        assertEquals(6,ccb.getExpMonth());
        ccb.setExpMonth(-1);
        assertEquals(-1,ccb.getExpMonth());
 
    }
 
    @Test
    public void testGetExpYear() {
 
        ccb.setExpYear(2020);
        int expYear = ccb.getExpYear();
        assertEquals(2020,expYear);
 
    }
 
    @Test
    public void testSetExpYear() {
 
        ccb.setExpYear(2020);
        assertEquals(2020,ccb.getExpYear());
        ccb.setExpYear(2000);
        assertEquals(2000,ccb.getExpYear());
        ccb.setExpYear(-123);
        assertEquals(-123,ccb.getExpYear());
         
         
    }
 
    @Test
    public void testGetCcHolderName() {
 
    	ccb.setCcHolderName("John Doe");
        String name = ccb.getCcHolderName();
        assertEquals("John Doe", name);
 
    }
 
    @Test
    public void testSetCcHolderName() {
         
        ccb.setCcHolderName("John Doe");
        assertEquals("John Doe", ccb.getCcHolderName());
        ccb.setCcHolderName("Clint Eastwood");
        assertEquals("Clint Eastwood", ccb.getCcHolderName());
        ccb.setCcHolderName("Pepe Perez");
        assertEquals("Pepe Perez", ccb.getCcHolderName());
 
    }
 
}