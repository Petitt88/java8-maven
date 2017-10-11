package org.pet.businesslogic;

import javax.annotation.Resource;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.transaction.UserTransaction;

/**
 * Session Bean implementation class MyEjb
 */
@Stateless
@TransactionManagement(TransactionManagementType.BEAN)
// @TransactionAttribute(TransactionAttributeType.REQUIRED)
@LocalBean
public class MyEjb {

	@Resource
	UserTransaction transaction;

	public String getMessage() {
		try {
			transaction.begin();

			transaction.commit();

			return "message from bean - transaction succeeded";
		} catch (Exception e) {
			return "error during transation.";
		}

	}

}
