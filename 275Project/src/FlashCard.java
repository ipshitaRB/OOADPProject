/**
 * 
 */
//package com.ooadp.project;

import java.io.Serializable;

/**
 * 
 * POJO class for each flashcard. Each object will have a word , the definition
 * and its difficulty (lowest being 1). The object can be serialized and is
 * saved in FlashCard.ser
 * 
 * @author Ipshita Roy Burman, Hongyu Cui
 *
 */
public class FlashCard implements Serializable,Comparable<FlashCard> {

	private String word;
	private String definition;
	private int difficulty;

	/**
	 * @param word
	 * @param definition
	 * 
	 */
	public FlashCard(String word, String definition) {
		super();
		this.word = word;
		this.definition = definition;
		// minimum difficulty
		this.difficulty = 1;
	}

	/**
	 * @return the word
	 */
	public String getWord() {
		return word;
	}

	/**
	 * @param word
	 *            the word to set
	 */
	public void setWord(String word) {
		this.word = word;
	}

	/**
	 * @return the definition
	 */
	public String getDefinition() {
		return definition;
	}

	/**
	 * @param definition
	 *            the definition to set
	 */
	public void setDefinition(String definition) {
		this.definition = definition;
	}

	/**
	 * @return the difficulty
	 */
	public int getDifficulty() {
		return difficulty;
	}

	/**
	 * @param difficulty
	 *            the difficulty to set
	 */
	public void increaseDifficulty() {
		this.difficulty++;
	}
	
	/**
	 * @param difficulty
	 *            the difficulty to set
	 */
	public void decreaseDifficulty() {
		this.difficulty--;
	}

	/* (non-Javadoc)
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	public int compareTo(FlashCard o) {
		 
		if(this.difficulty>o.getDifficulty())
	        return 1;
	    else if(this.difficulty<o.getDifficulty())
	        return -1;
	    else
	        return 0;
	}
	/**
	 * resets word difficulty to 1
	 */
	public void resetDifficutly() {
		this.difficulty = 1;
	}
	
	

}
