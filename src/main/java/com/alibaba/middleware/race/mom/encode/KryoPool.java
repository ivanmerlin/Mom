package com.alibaba.middleware.race.mom.encode;


import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufInputStream;
import io.netty.buffer.ByteBufOutputStream;

import java.io.IOException;


public class KryoPool {
	
	private static final byte[] LENGTH_PLACEHOLDER = new byte[4];
	private Object lastEncodeMessage;
	private ByteBuf lastDecodeMessage;
	private KyroFactory kyroFactory;
	public KryoPool() {
		kyroFactory = new KyroFactory();
	}
	
	public void encode(final ByteBuf out, final Object message) throws IOException {
		ByteBufOutputStream bout = new ByteBufOutputStream(out);
		bout.write(LENGTH_PLACEHOLDER);
		KryoSerialization kryoSerialization = new KryoSerialization(kyroFactory);
		kryoSerialization.serialize(bout, message);
	}
	
	public Object decode(final ByteBuf in) throws IOException {
		KryoSerialization kryoSerialization = new KryoSerialization(kyroFactory);
		return kryoSerialization.deserialize(new ByteBufInputStream(in));
	}
}
