package org.pet.businesslogic;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;

/**
 * Session Bean implementation class MyEjb
 */
@Stateless
@TransactionManagement(TransactionManagementType.BEAN)
@LocalBean
public class MyEjb {

	public String getMessage() {
		return "message from bean";
	}

}
