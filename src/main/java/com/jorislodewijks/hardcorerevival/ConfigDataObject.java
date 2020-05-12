package com.jorislodewijks.hardcorerevival;

import java.util.List;

import com.jorislodewijks.hardcorerevival.ritual.Ritual;
import com.jorislodewijks.hardcorerevival.ritual.source.SourceBookGenerator.SourceBookData;

public class ConfigDataObject {

	private List<Ritual> rituals;
	private SourceBookData sourceBookData;

	public ConfigDataObject(List<Ritual> rituals, SourceBookData sourceBookData) {
		this.rituals = rituals;
		this.sourceBookData = sourceBookData;
	}
	
	// Should we have this?
	public void setRituals(List<Ritual> rituals) {
		this.rituals = rituals;
	}

	public List<Ritual> getRituals() {
		return this.rituals;
	}
	
	public SourceBookData getSourceBookData() {
		return this.sourceBookData;
	}

}