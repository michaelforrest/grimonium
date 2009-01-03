package grimonium;

import java.util.Hashtable;

import processing.xml.XMLElement;

public class NoteParser {


	private static Hashtable<String, Integer> notes = new Hashtable<String, Integer>();
	static {
		notes.put("C",  0);
		notes.put("C#", 1);
		notes.put("D",  2);
		notes.put("D#", 3);
		notes.put("E",  4);
		notes.put("F",  5);
		notes.put("F#", 6);
		notes.put("G",  7);
		notes.put("G#", 8);
		notes.put("A",  9);
		notes.put("A#", 10);
		notes.put("B",  11);
	}
	public static Integer getNote(XMLElement xml) throws BadNoteFormatException{
		String noteName = xml.getStringAttribute("note");
		try {
			return Integer.parseInt(noteName);

		} catch (NumberFormatException e) {
			return convertStringToNoteNumber(noteName);
		}
	}
	/*
	 * C0 = 0
	 * C1 = 12
	 * C2 = 24 etc...
	 */
	public static Integer convertStringToNoteNumber(String string) throws BadNoteFormatException{
		try {
			Integer octave = Integer.parseInt( stringAt(string, string.length()-1));
			int base = 12 * octave;
			char[] buffer = new char[2];
			string.getChars(0, string.length()-1, buffer, 0);
			String id = new String(buffer).trim().toUpperCase();
			Integer offset =  notes.get( id) ;
			return base + offset;

		} catch (Exception e) {
			throw new BadNoteFormatException("Couldn't convert " +  string + " to a MIDI note number");
		}
	}
	private static String stringAt(String string, int index) {
		return String.valueOf(string.charAt(index));
	}
	public static class BadNoteFormatException extends Exception {
		public BadNoteFormatException(String string) {
			super(string);
		}
	}
}
