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
 */
public final class Flow {

	private Pipe<?> current;
	private final Pipe<?> first;

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private Flow(final Sender<?> head) {

		this.current = new Pipe(head);
		this.first = current;
	}

	private Flow(final Pipe<?> head) {
		this.current = head;
		this.first = current;
	}

	public static Flow start(final Sender<?> head) {
		final Flow flow = new Flow(head);
		return flow;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Flow append(final Object next) {
		return append(new Pipe(next));

	}

	@SuppressWarnings("rawtypes")
	private Flow append(final Pipe pipe) {
		if (current.getProcessor() instanceof Sender) {
			current.addPost(pipe);
			current = pipe;
			return this;
		}
		throw new IllegalStateException("Cannot append to flow: " + current.getProcessor() + " is not a Sender<?>.");
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Flow branch(final Object next) {
		if (current.getProcessor() instanceof Tee) {
			final Pipe<?> pipe = new Pipe(next);
			current.addPost(pipe);
			return new Flow(pipe);
		}
		throw new IllegalStateException("Cannot branch: " + current.getProcessor() + " is not a Tee<?>.");
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static Flow unite(final Object next, final Flow... flows) {
		final Pipe pipe = new Pipe(next);
		for (Flow flow : flows) {
			flow.append(pipe);
		}
		return new Flow(pipe);
	}

	public void close() {
		first.closeStream(first);
	}

	public void flush() {
		first.flushStream(first);
	}
}
