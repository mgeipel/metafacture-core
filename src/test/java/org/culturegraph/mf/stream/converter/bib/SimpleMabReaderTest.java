/*
 *  Copyright 2013 Deutsche Nationalbibliothek
 *
 *  Licensed under the Apache License, Version 2.0 the "License";
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.culturegraph.mf.stream.converter.bib;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Collections;
import java.util.List;

import org.culturegraph.mf.exceptions.FormatException;
import org.culturegraph.mf.stream.DataFilePath;
import org.culturegraph.mf.stream.converter.LineReader;
import org.culturegraph.mf.stream.converter.bib.MabDecoder;
import org.culturegraph.mf.stream.sink.Counter;
import org.culturegraph.mf.stream.sink.StreamValidator;
import org.culturegraph.mf.stream.source.ResourceOpener;
import org.culturegraph.mf.types.Event;
import org.junit.Assert;
import org.junit.Test;


/**
 * Tests {@link MabDecoder}. So far only verifies that the correct number of records and fields is read.
 * @author Markus Michael Geipel, Christoph Böhme
 * @see MabDecoder
 */
public final class SimpleMabReaderTest {
	
	private static final int NUM_RECORDS = 10;
	private static final int NUM_LITERALS = 520;
	

	@Test
	public void testRead(){
		final ResourceOpener opener = new ResourceOpener();
		final Counter countStreamReceiver = opener.setReceiver(new LineReader())
				.setReceiver(new MabDecoder())
				.setReceiver(new Counter());
		
		opener.process(DataFilePath.TITLE_MAB);
		opener.closeStream();
		
		Assert.assertEquals("Number of read records is incorrect", NUM_RECORDS, countStreamReceiver.getNumRecords());
		Assert.assertEquals("Number of read literals is incorrect", NUM_LITERALS, countStreamReceiver.getNumLiterals());
	}
	
	@Test
	public void testGetId() throws IOException {
		final InputStream inputStream = Thread.currentThread()
				.getContextClassLoader().getResourceAsStream(DataFilePath.TITLE_MAB);
		
		final BufferedReader breader = new BufferedReader(new InputStreamReader(inputStream));
		
		String line = breader.readLine();
		while (line != null) {
			if(!line.isEmpty()){
				Assert.assertNotNull(MabDecoder.extractIdFromRecord(line));
			}
			line = breader.readLine();
		}
		
		breader.close();
	}
	
	@Test
	public void testSkipEmptyStrings() {
		final MabDecoder decoder = new MabDecoder();
		
		final List<Event> expected = Collections.emptyList();
		final StreamValidator validator = new StreamValidator(expected);
		
		decoder.setReceiver(validator);
		
		try {
			decoder.process(" ");
			decoder.closeStream();
		} catch (FormatException e) {
			Assert.fail(e.toString());
		}
	}
	
}
