/*
 * Copyright 2013 the original author or authors.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package nivance.jpa.cassandra.prepare.schema;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import nivance.jpa.cassandra.prepare.support.CassandraExceptionTranslator;
import nivance.jpa.cassandra.prepare.support.exception.CassandraUncategorizedException;

import com.datastax.driver.core.exceptions.DriverException;
import com.google.common.util.concurrent.ForwardingListenableFuture.SimpleForwardingListenableFuture;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.Uninterruptibles;

/**
 * CassandraFuture
 * 
 * Used to map unchecked exceptions and support additional methods
 * 
 * @author Alex Shvid
 * 
 * @param <T>
 */

public class CassandraFuture<T> extends SimpleForwardingListenableFuture<T> {

	private CassandraExceptionTranslator exceptionTranslator;

	public CassandraFuture(ListenableFuture<T> delegate, CassandraExceptionTranslator exceptionTranslator) {
		super(delegate);
		this.exceptionTranslator = exceptionTranslator;
	}

	protected RuntimeException mapException(ExecutionException e) {
		if (e.getCause() instanceof DriverException) {
			return translateIfPossible(((DriverException) e.getCause()).copy());
		} else {
			return new CassandraUncategorizedException("unknown exception", e.getCause());
		}
	}

	@Override
	public T get() throws InterruptedException, ExecutionException {
		try {
			return super.get();
		} catch (ExecutionException e) {
			throw mapException(e);
		}
	}

	@Override
	public T get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
		try {
			return super.get(timeout, unit);
		} catch (ExecutionException e) {
			throw mapException(e);
		} catch (TimeoutException e) {
			super.cancel(true);
			throw e;
		}
	}

	public T getUnchecked() throws InterruptedException {
		try {
			return super.get();
		} catch (ExecutionException e) {
			throw mapException(e);
		}
	}

	public T getUnchecked(long timeout, TimeUnit unit) throws InterruptedException, TimeoutException {
		try {
			return super.get(timeout, unit);
		} catch (ExecutionException e) {
			throw mapException(e);
		} catch (TimeoutException e) {
			super.cancel(true);
			throw e;
		}
	}

	public T getUninterruptibly() {
		try {
			return Uninterruptibles.getUninterruptibly(this);
		} catch (ExecutionException e) {
			throw mapException(e);
		}
	}

	public T getUninterruptibly(long timeout, TimeUnit unit) throws TimeoutException {
		try {
			return Uninterruptibles.getUninterruptibly(this, timeout, unit);
		} catch (ExecutionException e) {
			throw mapException(e);
		} catch (TimeoutException e) {
			super.cancel(true);
			throw e;
		}
	}

	/**
	 * Attempt to translate a Runtime Exception to a Spring Data Exception
	 * 
	 * @param ex
	 * @return
	 */
	protected RuntimeException translateIfPossible(RuntimeException ex) {
		RuntimeException resolved = exceptionTranslator.translateExceptionIfPossible(ex);
		return resolved == null ? ex : resolved;
	}

}
