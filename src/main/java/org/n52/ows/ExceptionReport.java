/**
 * Copyright (C) 2012 52°North Initiative for Geospatial Open Source Software GmbH
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.n52.ows;

import java.io.InputStream;

import org.n52.util.CommonUtilities;

/**
 * Class which represents an OWS Common 1.1.0 ExceptionReport.
 * 
 * @author matthes rieke
 *
 */
public class ExceptionReport extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final String TEMPLATE_FILE = "template_ows_exception_report.xml";
	private static final String CODE_KEY = "${code}";
	private static final String TEXT_KEY = "${text}";
	private static String template;
	private String code;
	private String text;
	
	static {
		InputStream stream = ExceptionReport.class.getResourceAsStream(TEMPLATE_FILE);
		template = CommonUtilities.readResource(stream);
	}
	
	
	public ExceptionReport(String code, String text) {
		super(code.concat(": ").concat(text));
		this.code = code;
		this.text = text;
	}
	
	public String getCode() {
		return code;
	}

	public String getText() {
		return text;
	}

	@Override
	public String toString() {
		return template.replace(CODE_KEY, code).replace(TEXT_KEY, text);
	}
	
}
