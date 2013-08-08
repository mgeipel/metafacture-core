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


/**
 * @author Markus Geipel
 *
 * @param <N>
 */
public class DefaultSender<N> implements Sender<N>{
	private N receiver;

	@Override
	public final <R extends N>  void setReceiver(final R receiver) {
				
		if(this.receiver==null){
			this.receiver = receiver;
		}else{
			throw new IllegalStateException("Already part of a flow. Receiver=" + receiver);
		}
	}
	
	protected final N getReceiver() {
		return receiver;
	}
}
