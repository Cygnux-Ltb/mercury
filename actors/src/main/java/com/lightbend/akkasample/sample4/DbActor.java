/*
 * Copyright (C) 2016 Lightbend Inc. <http://www.lightbend.com>
 */
package com.lightbend.akkasample.sample4;

import java.util.Optional;

import akka.actor.AbstractActor;
import akka.actor.Props;

public class DbActor extends AbstractActor {

	private final SynchronousDatabaseConnection connection = new SynchronousDatabaseConnection();

	private void getProduct(long id) {
		final Optional<Product> product = connection.findProduct(id);
		sender().tell(new ProductResult(product), self());
	}

	public static Props props() {
		return Props.create(DbActor.class);
	}

	// protocol
	public static class GetProduct {
		public final long id;

		public GetProduct(long id) {
			this.id = id;
		}
	}

	public static class ProductResult {
		public final Optional<Product> product;

		public ProductResult(Optional<Product> product) {
			this.product = product;
		}
	}

	@Override
	public Receive createReceive() {
		return receiveBuilder().match(GetProduct.class, query -> getProduct(query.id)).build();
	}

}
