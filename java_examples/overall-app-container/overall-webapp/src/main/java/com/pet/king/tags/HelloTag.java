package com.pet.king.tags;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.SimpleTagSupport;
import java.io.IOException;

public class HelloTag extends SimpleTagSupport {

	private String name;

	// The container calls back this setter to process attribute "name"
	public void setName(String name) {
		this.name = name;
	}

	@Override
	public void doTag() throws JspException, IOException {
		JspWriter out = getJspContext().getOut();
		try {
			if (name != null) {
				out.println("<span>Hello, " + name + "!</span>");
			} else {
				out.println("<span>Hello, everybody!</span>");
			}
		} catch (java.io.IOException ex) {
			throw new JspException("Error in HelloAttributeTag ", ex);
		}
	}
}
