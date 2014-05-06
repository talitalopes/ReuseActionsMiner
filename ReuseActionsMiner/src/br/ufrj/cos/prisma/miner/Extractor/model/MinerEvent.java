package br.ufrj.cos.prisma.miner.Extractor.model;

import java.util.Date;

import miner.Event;
import miner.MinerFactory;

public class MinerEvent {

	Event event;
	
	public MinerEvent(MinerActivity activity, Date date) {
		this.event = MinerFactory.eINSTANCE.createEvent();
		this.event.setActivity(activity.getActivity());
		this.event.setDate(date);
		this.event.setLifecycleStatus("COMPLETE");
	}
	
	public Event getEvent() {
		return this.event;
	}
}
