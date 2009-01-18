package grimonium.maps;

import grimonium.NoteParser;
import grimonium.NoteParser.BadNoteFormatException;
import processing.xml.XMLElement;

public class BoneTempoKeyRanges extends MapBase {

	private Integer low;
	private Integer high;
	private int bonecount;
	private float tempo;
	private BoneTempoKeys[] ranges;
	private String boneRoot;
	private int channel;

	public BoneTempoKeyRanges(XMLElement element) {
		try {
			low = NoteParser.getNote(element.getStringAttribute("low", "1"));
			high = NoteParser.getNote(element.getStringAttribute("high", "100"));
		} catch (BadNoteFormatException e) {
			System.out.println(e.getMessage());
		}
		channel = element.getIntAttribute("channel", 16) - 1;
		boneRoot = element.getStringAttribute("bone");
		tempo = element.getFloatAttribute("tempo", 1);
		bonecount = element.getIntAttribute("bonecount",1);
		addRanges();
	}

	private void addRanges() {
		ranges = new BoneTempoKeys[bonecount];
		float step = (((float)high)-((float)low))/((float)bonecount);
		for (int i = 0; i < bonecount; i++) {
			int rangeLow = low + (int)(i*step);
			System.out.println("added range low=" + rangeLow + " step was " + step);
			ranges[i] = new BoneTempoKeys(rangeLow, rangeLow + (int)step, boneRoot+i, tempo, channel);
		}
	}
	@Override
	public void activate() {
		super.activate();
		for (BoneTempoKeys range : ranges) {
			range.activate();
		}
	}
	@Override
	public void deactivate() {
		super.deactivate();
		for (BoneTempoKeys range : ranges) {
			range.deactivate();
		}
	}

}
