package bfx.assembly.util;

import java.io.OutputStream;
import java.io.PrintStream;

import com.google.common.base.Joiner;

public class Table {
	private PrintStream out;
	private Joiner joiner = Joiner.on('\t');
	public Table(OutputStream out) {
		this.out = new PrintStream(out);
	}

	public void printRow(Object... things) {
		out.println(joiner.join(things));
	}

	public void close() {
		out.close();
	}
}
