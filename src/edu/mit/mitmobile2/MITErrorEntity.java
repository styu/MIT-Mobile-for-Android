package edu.mit.mitmobile2;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;

import org.apache.http.Header;
import org.apache.http.HttpEntity;

public class MITErrorEntity implements HttpEntity{

	public final static String JSON_ERROR = "{\"result\":\"AUTH_ERROR\"}";

	@Override
	public void consumeContent() throws IOException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public InputStream getContent() throws IOException, IllegalStateException {
		// TODO Auto-generated method stub
		InputStream is = new ByteArrayInputStream(JSON_ERROR.getBytes());
		return is;
	}

	@Override
	public Header getContentEncoding() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getContentLength() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Header getContentType() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isChunked() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isRepeatable() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isStreaming() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void writeTo(OutputStream outstream) throws IOException {
		// TODO Auto-generated method stub
		
	}

}
