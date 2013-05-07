package com.yunfeng.protocol;

import java.io.ByteArrayOutputStream;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

public final class DefaultCompressor implements ICompressor {
	public final int MAX_SIZE_FOR_COMPRESSION = 1000000;
	private final int compressionBufferSize = 256;

	public byte[] compress(byte[] data) throws Exception {
		if (data.length > MAX_SIZE_FOR_COMPRESSION) {
			return data;
		}

		Deflater compressor = new Deflater();

		compressor.setInput(data);
		compressor.finish();

		ByteArrayOutputStream bos = new ByteArrayOutputStream(data.length);

		byte[] buf = new byte[compressionBufferSize];
		while (!compressor.finished()) {
			int count = compressor.deflate(buf);
			bos.write(buf, 0, count);
		}

		bos.close();

		return bos.toByteArray();
	}

	public byte[] uncompress(byte[] zipData) throws Exception {
		Inflater unzipper = new Inflater();
		unzipper.setInput(zipData);

		ByteArrayOutputStream bos = new ByteArrayOutputStream(zipData.length);

		byte[] buf = new byte[compressionBufferSize];

		while (!unzipper.finished()) {
			int count = unzipper.inflate(buf);
			bos.write(buf, 0, count);
		}

		bos.close();

		return bos.toByteArray();
	}
}