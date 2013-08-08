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

package org.culturegraph.mf.pentachoron.frame;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;


/**
 * @author Markus Geipel
 *
 * @param <N>
 */
public class DefaultTee<N> implements Tee<N>{
	private final List<N> receivers = new LinkedList<N>();
	private final List<N> protectedReceiversView = Collections.unmodifiableList(receivers);

	@Override
	public final <R extends N>  void setReceiver(final R receiver) {
		receivers.add(receiver);
	}
	
	protected final List<N> getReceivers() {
		return protectedReceiversView;
	}
}
