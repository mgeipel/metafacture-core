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

package org.culturegraph.mf.pentachoron.sandbox;

import org.culturegraph.mf.pentachoron.frame.CloseStreamListener;
import org.culturegraph.mf.pentachoron.frame.DefaultSender;
import org.culturegraph.mf.pentachoron.frame.DefaultTee;
import org.culturegraph.mf.pentachoron.frame.Flow;
import org.culturegraph.mf.pentachoron.frame.ObjectReceiver;

/**
 * @author geipel
 *
 */
public final class Test {

	private Test() {
		// no instances
	}
	
	/**
	 * @param args
	 */
	public static void main(final String[] args) {

		final Add head = new Add("Hello ");
		
		final Flow flow = Flow.start(head).append(new Add("World")).append(new ObjectTee<String>());
		
		final Flow branch1 = flow.branch(new Add("!")).append(new Add("!"));
		final Flow branch2 = flow.branch(new Add("?"));
		
		Flow.unite(new ObjectPrinter(), branch1, branch2);
		 
		head.process("");
		flow.close();

	}
	
	/**
	 *
	 */
	protected static final class Add extends DefaultSender<ObjectReceiver<String>> implements ObjectReceiver<String>{
		
		private final String string;
		
		public Add(final String string) {
			this.string = string;
		}
		
		@Override
		public void process(final String obj) {
			getReceiver().process(obj + string);
		}
		
		@Override
		public String toString() {
			return "Append " + string;
		}
	}
	
	/**
	 *
	 */
	protected static final class ObjectTee<T> extends DefaultTee<ObjectReceiver<T>> implements ObjectReceiver<T>{
		@Override
		public void process(final T obj) {
			for (ObjectReceiver<T> receiver : getReceivers()) {
				receiver.process(obj);
			}
		}
	}
	
	
	/**
	 *
	 */
	protected static final class ObjectPrinter implements ObjectReceiver<String>, CloseStreamListener{
		@Override
		public void process(final String obj) {
			System.out.println(obj);
		}

		@Override
		public void onCloseStream() {
			System.out.println("closing");
			
		}
	}

}
