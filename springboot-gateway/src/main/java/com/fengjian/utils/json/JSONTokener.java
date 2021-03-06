package com.fengjian.utils.json;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

public class JSONTokener {
	private int index;
	private Reader reader;
	private char lastChar;
	private boolean useLastChar;

	public JSONTokener(Reader reader) {
		this.reader = (Reader) (reader.markSupported() ? reader : new BufferedReader(reader));
		this.useLastChar = false;
		this.index = 0;
	}

	public JSONTokener(String s) {
		this((Reader) (new StringReader(s)));
	}

	public void back() throws JSONException {
		if (!this.useLastChar && this.index > 0) {
			--this.index;
			this.useLastChar = true;
		} else {
			throw new JSONException("Stepping back two steps is not supported");
		}
	}

	public static int dehexchar(char c) {
		return c >= 48 && c <= 57 ? c - 48 : (c >= 65 && c <= 70 ? c - 55 : (c >= 97 && c <= 102 ? c - 87 : -1));
	}

	public boolean more() throws JSONException {
		char nextChar = this.next();
		if (nextChar == 0) {
			return false;
		} else {
			this.back();
			return true;
		}
	}

	public char next() throws JSONException {
		if (this.useLastChar) {
			this.useLastChar = false;
			if (this.lastChar != 0) {
				++this.index;
			}

			return this.lastChar;
		} else {
			int c;
			try {
				c = this.reader.read();
			} catch (IOException arg2) {
				throw new JSONException(arg2);
			}

			if (c <= 0) {
				this.lastChar = 0;
				return ' ';
			} else {
				++this.index;
				this.lastChar = (char) c;
				return this.lastChar;
			}
		}
	}

	public char next(char c) throws JSONException {
		char n = this.next();
		if (n != c) {
			throw this.syntaxError("Expected \'" + c + "\' and instead saw \'" + n + "\'");
		} else {
			return n;
		}
	}

	public String next(int n) throws JSONException {
		if (n == 0) {
			return "";
		} else {
			char[] buffer = new char[n];
			int pos = 0;
			if (this.useLastChar) {
				this.useLastChar = false;
				buffer[0] = this.lastChar;
				pos = 1;
			}

			int exc;
			try {
				while (pos < n && (exc = this.reader.read(buffer, pos, n - pos)) != -1) {
					pos += exc;
				}
			} catch (IOException arg4) {
				throw new JSONException(arg4);
			}

			this.index += pos;
			if (pos < n) {
				throw this.syntaxError("Substring bounds error");
			} else {
				this.lastChar = buffer[n - 1];
				return new String(buffer);
			}
		}
	}

	public char nextClean() throws JSONException {
		char c;
		do {
			c = this.next();
		} while (c != 0 && c <= 32);

		return c;
	}

	public String nextString(char quote) throws JSONException {
		StringBuffer sb = new StringBuffer();

		while (true) {
			char c = this.next();
			switch (c) {
				case ' ' :
				case '\n' :
				case '\r' :
					throw this.syntaxError("Unterminated string");
				case '\\' :
					c = this.next();
					switch (c) {
						case 'b' :
							sb.append('\b');
							continue;
						case 'c' :
						case 'd' :
						case 'e' :
						case 'g' :
						case 'h' :
						case 'i' :
						case 'j' :
						case 'k' :
						case 'l' :
						case 'm' :
						case 'o' :
						case 'p' :
						case 'q' :
						case 's' :
						case 'v' :
						case 'w' :
						default :
							sb.append(c);
							continue;
						case 'f' :
							sb.append('\f');
							continue;
						case 'n' :
							sb.append('\n');
							continue;
						case 'r' :
							sb.append('\r');
							continue;
						case 't' :
							sb.append('\t');
							continue;
						case 'u' :
							sb.append((char) Integer.parseInt(this.next((int) 4), 16));
							continue;
						case 'x' :
							sb.append((char) Integer.parseInt(this.next((int) 2), 16));
							continue;
					}
				default :
					if (c == quote) {
						return sb.toString();
					}

					sb.append(c);
			}
		}
	}

	public String nextTo(char d) throws JSONException {
		StringBuffer sb = new StringBuffer();

		while (true) {
			char c = this.next();
			if (c == d || c == 0 || c == 10 || c == 13) {
				if (c != 0) {
					this.back();
				}

				return sb.toString().trim();
			}

			sb.append(c);
		}
	}

	public String nextTo(String delimiters) throws JSONException {
		StringBuffer sb = new StringBuffer();

		while (true) {
			char c = this.next();
			if (delimiters.indexOf(c) >= 0 || c == 0 || c == 10 || c == 13) {
				if (c != 0) {
					this.back();
				}

				return sb.toString().trim();
			}

			sb.append(c);
		}
	}

	public Object nextValue() throws JSONException {
		char c = this.nextClean();
		switch (c) {
			case '\"' :
			case '\'' :
				return this.nextString(c);
			case '(' :
			case '[' :
				this.back();
				return new JSONArray(this);
			case '{' :
				this.back();
				return new JSONObject(this);
			default :
				StringBuffer sb;
				for (sb = new StringBuffer(); c >= 32 && ",:]}/\\\"[{;=#".indexOf(c) < 0; c = this.next()) {
					sb.append(c);
				}

				this.back();
				String s = sb.toString().trim();
				if (s.equals("")) {
					throw this.syntaxError("Missing value");
				} else {
					return JSONObject.stringToValue(s);
				}
		}
	}

	public char skipTo(char to) throws JSONException {
		char c;
		try {
			int exc = this.index;
			this.reader.mark(Integer.MAX_VALUE);

			do {
				c = this.next();
				if (c == 0) {
					this.reader.reset();
					this.index = exc;
					return c;
				}
			} while (c != to);
		} catch (IOException arg3) {
			throw new JSONException(arg3);
		}

		this.back();
		return c;
	}

	public JSONException syntaxError(String message) {
		return new JSONException(message + this.toString());
	}

	public String toString() {
		return " at character " + this.index;
	}
}