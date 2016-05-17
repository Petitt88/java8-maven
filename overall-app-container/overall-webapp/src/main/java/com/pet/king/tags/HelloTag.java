package com.pet.king.tags;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.SimpleTagSupport;

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
				out.println("<h1>Hello, " + name + "!</h1>");
			} else {
				out.println("<h1>Hello, everybody!</h1>");
			}
		} catch (java.io.IOException ex) {
			throw new JspException("Error in HelloAttributeTag ", ex);
		}
	}
}
