package com.wyc.common.context;


import com.auth0.jwt.algorithms.Algorithm;
import com.wyc.common.domain.Client;

import java.io.UnsupportedEncodingException;

public abstract class UserContext {

	private static ThreadLocal<Client> clients = new ThreadLocal<>();

	public static void set(Client client) {
		clients.set(client);
	}

	public static Client get(){
		return clients.get();
	}

	public static void clear() {
		clients.set(null);
		clients.remove();
	}

	public static Algorithm getDefaultAlgorithm() {
		try {
			return Algorithm.HMAC256("adfa1479akpqo0Il1Xm");
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}
	}

	public static final String HEADER_TOKEN = "x-token";

}
