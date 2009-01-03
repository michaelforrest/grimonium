package test;

import grimonium.NoteParser;
import grimonium.NoteParser.BadNoteFormatException;
import junit.framework.TestCase;

public class NoteBoneTest extends TestCase {

	public void testConvertStringToNoteNumber() {

		assertEquals(0, convert("C0"));
		assertEquals(12, convert("C1"));
		assertEquals(14, convert("D1"));
		assertEquals(13, convert("C#1"));
		assertEquals(0, convert("c0"));
	}
	public void testThrowsErrorForInvalidFormat(){
		try {
			NoteParser.convertStringToNoteNumber("foo");
			fail("should throw exception for bad number format");
		} catch (BadNoteFormatException e) {
			//expected
		}
	}

	private int convert(String string) {
		try {
			return NoteParser.convertStringToNoteNumber(string);
		} catch (BadNoteFormatException e) {
			fail(e.getMessage());
			return -1;
		}
	}

}
