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

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

/**
 * @author Markus Geipel
 * @param <P>
 */
final class Pipe<P> implements LifeCycle<P> {

	private final List<Pipe<?>> posts = new LinkedList<Pipe<?>>();

	private final P processor;

	private final Collection<Pipe<?>> pres = new ArrayList<Pipe<?>>();
	private final Collection<Pipe<?>> openPres = new HashSet<Pipe<?>>();
	private final Collection<Pipe<?>> unflushedPres = new HashSet<Pipe<?>>();

	public Pipe(final P processor) {
		this.processor = processor;
	}

	@Override
	public P getProcessor() {
		return processor;
	}

	protected void addPre(final Pipe<?> pre) {
		pres.add(pre);
		openPres.add(pre);
		unflushedPres.add(pre);
	}

	@Override
	public void closeStream(final LifeCycle<?> pipe) {
		openPres.remove(pipe);
		if (openPres.isEmpty()) {
			if (processor instanceof CloseStreamListener) {
				final CloseStreamListener listener = (CloseStreamListener) processor;
				listener.onCloseStream();
			}
			onCloseStream();
		}
	}

	@Override
	public void flushStream(final LifeCycle<?> pipe) {
		unflushedPres.remove(pipe);
		if (unflushedPres.isEmpty()) {
			if (processor instanceof FlushStreamListener) {
				final FlushStreamListener listener = (FlushStreamListener) processor;
				listener.onFlushStream();
			}
			onFlushStream();
			unflushedPres.addAll(pres);
		}
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	protected  void addPost(final Pipe<?> post) {
		posts.add(post);
		post.addPre(this);
		if (getProcessor() instanceof Sender) {
			final Sender sender = (Sender) getProcessor();
			sender.setReceiver(post.getProcessor());
		}
	}

	protected void onCloseStream() {
		for (LifeCycle<?> post : posts) {
			post.closeStream(this);
		}
	}

	protected void onFlushStream() {
		for (LifeCycle<?> post : posts) {
			post.flushStream(this);
		}
	}

}
