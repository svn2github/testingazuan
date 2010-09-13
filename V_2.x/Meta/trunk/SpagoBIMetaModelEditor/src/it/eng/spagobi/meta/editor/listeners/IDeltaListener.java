package it.eng.spagobi.meta.editor.listeners;

public interface IDeltaListener {
	public void add(DeltaEvent event);
	public void remove(DeltaEvent event);
}
