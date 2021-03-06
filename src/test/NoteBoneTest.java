package test;

import grimonium.NoteParser;
import grimonium.NoteParser.BadNoteFormatException;
import junit.framework.TestCase;

public class NoteBoneTest extends TestCase {

	public void testConvertStringToNoteNumber() {
		assertEquals(60, convert("C3"));
		assertEquals(0, convert("C-2"));
		assertEquals(12, convert("C-1"));
		assertEquals(14, convert("D-1"));
		assertEquals(13, convert("C#-1"));
		assertEquals(24, convert("c0"));

	}
	public void testThrowsErrorForInvalidFormat(){
		try {
			NoteParser.getNote("foo");
			fail("should throw exception for bad number format");
		} catch (BadNoteFormatException e) {
			//expected
		}
	}

	private int convert(String string) {
		try {
			return NoteParser.getNote(string);
		} catch (BadNoteFormatException e) {
			fail(e.getMessage());
			return -1;
		}
	}

}
