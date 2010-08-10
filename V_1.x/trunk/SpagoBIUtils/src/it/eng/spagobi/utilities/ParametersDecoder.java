/**
 * Copyright (c) 2005, Engineering Ingegneria Informatica s.p.a.
All rights reserved.

Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:

    * Redistributions of source code must retain the above copyright notice, this list of 
      conditions and the following disclaimer.
      
    * Redistributions in binary form must reproduce the above copyright notice, this list of 
      conditions and the following disclaimer in the documentation and/or other materials 
      provided with the distribution.
      
    * Neither the name of the Engineering Ingegneria Informatica s.p.a. nor the names of its contributors may
      be used to endorse or promote products derived from this software without specific
      prior written permission.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND 
CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, 
INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF 
MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE 
DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR 
CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT
NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT,
STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) 
ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, 
EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE

 */
package it.eng.spagobi.utilities;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Gioia
 *
 */
public class ParametersDecoder {

	private String openBlockMarker;
	private String closeBlockMarker;
	
	public static final String DEFAULT_OPEN_BLOCK_MARKER = "{";
	public static final String DEFAULT_CLOSE_BLOCK_MARKER = "}";
	
	
	/////////////////////////////////////////////////////////////
	//	CONSTRUCTORS
	/////////////////////////////////////////////////////////////
	
	public ParametersDecoder() {
		this(DEFAULT_OPEN_BLOCK_MARKER, DEFAULT_CLOSE_BLOCK_MARKER);
	}
	
	public ParametersDecoder(String openBlockMarker, String closeBlockMarker) {
		this.openBlockMarker = openBlockMarker;
		this.closeBlockMarker = closeBlockMarker;
	}
	
	
	/////////////////////////////////////////////////////////////
	//	ACCESS METHODS
	/////////////////////////////////////////////////////////////
	
	public String getCloseBlockMarker() {
		return closeBlockMarker;
	}

	public void setCloseBlockMarker(String closeBlockMarker) {
		this.closeBlockMarker = closeBlockMarker;
	}

	public String getOpenBlockMarker() {
		return openBlockMarker;
	}

	public void setOpenBlockMarker(String openBlockMarker) {
		this.openBlockMarker = openBlockMarker;
	}
	
	
	/////////////////////////////////////////////////////////////
	//	PUBLIC METHODS
	/////////////////////////////////////////////////////////////
	
	public boolean isMultiValues(String value) {
		return (value.trim().startsWith(openBlockMarker));
	}
	
	public List decode(String value) {
		List values = null;
		
		if(value == null) return null;
		
		if(isMultiValues(value)) {
			values = new ArrayList();
			String separator = getSeparator(value);
			String innerBlock = getInnerBlock(value);
			String[] chunks = innerBlock.split(separator);
			for(int i = 0; i < chunks.length; i++) {
				values.add(chunks[i]);
			}
		} else {
			values = new ArrayList();
			values.add(value);
		}
		
		return values;
	}
	
	/////////////////////////////////////////////////////////////
	//	UTILITY METHODS
	/////////////////////////////////////////////////////////////
	
	private String getSeparator(String value) {
		String separator = null;
		
		int outerBlockOpeningIndex = value.trim().indexOf(openBlockMarker);
		int innerBlockOpeningIndex = value.trim().indexOf(openBlockMarker, outerBlockOpeningIndex + 1);	
		separator = value.substring(outerBlockOpeningIndex + 1, innerBlockOpeningIndex).trim();
		
		return separator;
	}
	
	private String getInnerBlock(String value) {
		String innerBlock = null;
		
		int outerBlockOpeningIndex = value.trim().indexOf(openBlockMarker);
		int innerBlockOpeningIndex = value.trim().indexOf(openBlockMarker, outerBlockOpeningIndex + 1);
		int innerBlockClosingIndex = value.trim().indexOf(closeBlockMarker, innerBlockOpeningIndex + 1);	
		innerBlock = value.substring(innerBlockOpeningIndex + 1, innerBlockClosingIndex).trim();
		
		return innerBlock;
	}
	

	/////////////////////////////////////////////////////////////
	//	MAIN METHOD
	/////////////////////////////////////////////////////////////
	
	/**
	 * Just for test purpose ;-)
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
