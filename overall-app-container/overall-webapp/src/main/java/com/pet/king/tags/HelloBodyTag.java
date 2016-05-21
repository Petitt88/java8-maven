package com.pet.king.tags;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.JspFragment;
import javax.servlet.jsp.tagext.SimpleTagSupport;

public class HelloBodyTag extends SimpleTagSupport {

	@Override
	public void doTag() throws JspException {
		JspWriter out = getJspContext().getOut();
		try {
			out.println("<span>Hello, ");
			JspFragment body = getJspBody();
			if (body != null) {
				// Print the body content as it is
				body.invoke(out);
			} else {
				out.println("everybody");
			}
			out.println(", goddammit!</span>");
		} catch (java.io.IOException ex) {
			throw new JspException("Error in HelloTag ", ex);
		}
	}
}