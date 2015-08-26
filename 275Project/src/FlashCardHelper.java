/**
 * 
 */
//package com.ooadp.project;

import java.io.BufferedReader;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;

/**
 * 
 * This class containts all the static methods which are used by the UI
 * component of the app to inflate the Frame components and interact with the
 * user. The static methods implement the underlying algorithm to show random
 * flash cards and repeat flash cards when they are not remembered by the user.
 * 
 * @author Ipshita Roy Burman, Hongyu Cui
 *
 */
abstract public class FlashCardHelper {

	/**
	 * The treemap is sorted in the order of the keys in alphabetical order and
	 * the word of FlashCard is used as the key to keep the words unique To sort
	 * the treemap in order of difficulty use sortByValue method
	 */
	static TreeMap<String, FlashCard> flashcardMap = new TreeMap<String, FlashCard>();

	static ArrayList<FlashCard> flashCardList = new ArrayList<FlashCard>();
	
//	static ArrayList<FlashCard> topDifficultWord = new ArrayList<FlashCard>();
	
//	static TreeMap<Integer, ArrayList<FlashCard>> sortbyDifficulty = new TreeMap<Integer, ArrayList<FlashCard>>();

	static String serialFileName = "FlashCards.ser";

	static MediatorBetweenUIandHelper objBetweenUIandHelper = MediatorBetweenUIandHelper
			.getInstance();
	static final int TOP_PERCENTAGE = 10;

	/* =============== public Methods ==================== */

	/**
	 * Serialize the objects from flashCardMap and save them in FlashCard.ser
	 * Clears the arraylist flashCardList and flashCardMap This method is called
	 * before the JFrame exits
	 * 
	 */
	public static void beforeAppExit() {

		saveFlashCards();
		clearCollections();

	} // saveWords()

	/**
	 * Deserializes and restores FlashCard objects into flashCardMap. inflates
	 * the arraylist . adds an object according to the difficulty.For example if
	 * difficulty is 5 the word is added five times. if difficulty < 1 then it
	 * is not added.
	 * 
	 * Method needs to be called when the Jframe window is created
	 */
	public static void initialize() {
		restoreflashCards();
		inflateFlashCardList();
	}
	
	
	/**
	 * restart app without closing, reset all words to initial state (difficulty = 1)
	 */
	public static void reStart(){
		
		flashCardList.clear();
		inflateFlashCardList();		
		
	}

	
	/**
	 * gets flashcard object from map using @param word as key and increases its
	 * difficutly updates the collections
	 * 
	 * @param word
	 * 
	 *            To be called when user does not remember word definition
	 */
	public static void userDoesNotRememberWord(String word) {

		increaseDifficultyAndUpdateCollections(word);
	}

	/**
	 * gets flashcard object from map using @param word as key and decreases its
	 * difficutly updates the collections
	 * 
	 * @param word
	 *            This method is called everytime the user remembers the word
	 * 
	 * @param word
	 */
	public static void userRemembersWord(String word) {

		decreaseDifficultyAndUpdateCollections(word);
	}

	/**
	 * @param word
	 * @param definition
	 * @return true if the word is added else false if the word exists if word
	 *         already exists the user might want to see the word(use
	 *         getFlashCard()) and definition and replace it...
	 */
	public static boolean addWord(String word, String definition) {

		if (!flashcardMap.containsKey(word)) {
			FlashCard objCard = new FlashCard(word, definition);
			flashcardMap.put(objCard.getWord(), objCard);
			flashCardList.add(objCard);
			return true;
		} else
			return false;
	}

	/**
	 * @param word
	 * @return flashcard object. returns null if the word does not
	 *         exist.Appropriate message needs to be shown here.
	 */
	public static FlashCard getFlashCard(String word) {

		if (flashcardMap.containsKey(word))
			return flashcardMap.get(word);
		else
			return null;
	}

	/**
	 * gets size of arraylist . pick a random number from the size range. return
	 * that flashcard object. remove it from the array list. if the arraylist is
	 * empty then return null . appropriate message needs to be shown at the UI
	 * 
	 * @return FlashCard
	 */
	public static FlashCard getNextFlashCard() {
		int size = flashCardList.size();
		int randomIndex = 0;
		FlashCard objFlashCard = null;
		if (size < 1) {
			objBetweenUIandHelper.noFlashCardsRemaining();
			return null;
		} else if (size == 1) {
			objFlashCard = flashCardList.get(0);
			flashCardList.remove(0);
			return objFlashCard;
		} else {
			// note a single Random object is reused here
			Random randomGenerator = new Random();

			randomIndex = randomGenerator.nextInt(size);
			objFlashCard = flashCardList.get(randomIndex);
			flashCardList.remove(randomIndex);
			return objFlashCard;

		}

	}

	/**
	 * @return
	 */
	public static ArrayList<FlashCard> getDifficultWords() {
		// TODO return top 10 % of the words. get size . get 10% of size .
		// TODO its already sorted so figure a way to get top 10.Use a different
		// data structure if needed.
		// but it has to come from the treemap because it has unique words and
		// it is sorted according to difficulty
		// use the sortByValue method @see printSortedValues()
		
		ArrayList<FlashCard> topDifficultWord = new ArrayList<FlashCard>();
		
		TreeMap<String, FlashCard> sortedMap = sortByValue(flashcardMap);
		
		int numOfWord = flashcardMap.size() * TOP_PERCENTAGE / 100;
		int count = 0;
		FlashCard card;
		
		Collection<FlashCard> c = sortedMap.values();
		Iterator<FlashCard> itr = c.iterator();
		
		while(itr.hasNext() && count < numOfWord){
			count ++;
			card = itr.next();
			topDifficultWord.add(card);			
		}
		//System.out.println(topDifficultWord.size());
		/*for (FlashCard fc : topDifficultWord) {
			
			System.out.println(fc.getWord() + " " + fc.getDifficulty());
			
		}*/
		return topDifficultWord;
	}
	
	/**

	* @return

	*/

	public static ArrayList<FlashCard> getAllFlashCards(){

		FlashCard word;

		ArrayList<FlashCard> allWords = new ArrayList<>();


		Collection<FlashCard> c = flashcardMap.values();


		Iterator<FlashCard> itr = c.iterator();

		while (itr.hasNext()) {

			word = itr.next();

			allWords.add(word);
		}
	
		return allWords;
	}
	
	/**
	* @return
	*/
	public static int getNumberOfRemainingCards(){
		int number = 0;
		number = flashCardList.size();
		return number;
	}
	
	/**
	 * @return the number of words which have a difficulty greater than zero
	 */
	public static int getNumberofRemainingWords(){
		int numberOfRemainingWords = 0;
		/*------------Inflating flashCardList using Map----------*/

		/*
		 * get Collection of values contained in TreeMap using Collection
		 * values() method of TreeMap class
		 */
		Collection<FlashCard> c = flashcardMap.values();

		// obtain an Iterator for Collection
		Iterator<FlashCard> itr = c.iterator();

		FlashCard objFlashCard = null;
		// iterate through TreeMap values iterator
		while (itr.hasNext()) {
			objFlashCard = itr.next();
			if (objFlashCard.getDifficulty()>0) {
				numberOfRemainingWords++;
			}

		}
		return numberOfRemainingWords;
	}
	
	/**

	* @return

	*/

	public static int totalNumberOfCards(){

		int number = 0;

		number = flashcardMap.size();

		return number;

	}

	
	/**
	 * Resets all word difficulty to 1 including added ones
	 */
	public static void resetFlashCards() {
		/*------------Inflating flashCardList using Map----------*/

		/*
		 * get Collection of values contained in TreeMap using Collection
		 * values() method of TreeMap class
		 */
		Collection<FlashCard> c = flashcardMap.values();

		// obtain an Iterator for Collection
		Iterator<FlashCard> itr = c.iterator();

		FlashCard objFlashCard = null;
		flashCardList.clear();
		// iterate through TreeMap values iterator
		while (itr.hasNext()) {
			objFlashCard = itr.next();
			objFlashCard.resetDifficutly();
			flashcardMap.put(objFlashCard.getWord(), objFlashCard);
			flashCardList.add(objFlashCard);

		}

	}
	
	/* =============== private Methods ==================== */

	/**
	 * @param word
	 */
	private static void increaseDifficultyAndUpdateCollections(String word) {
		FlashCard objFlashCard = flashcardMap.get(word);
		if (null != objFlashCard) {

			objFlashCard.increaseDifficulty();
			flashcardMap.put(word, objFlashCard);
			//add the word that the user could not remember
			flashCardList.add(objFlashCard);
			//add it again as penalty 
			flashCardList.add(objFlashCard);
		} else {
			System.out
					.println("increaseDifficultyAndUpdateCollections(String word) faile. FlashCardObject Null for word "
							+ word);
		}

	}

	/**
	 * @param word
	 */
	private static void decreaseDifficultyAndUpdateCollections(String word) {
		FlashCard objFlashCard = flashcardMap.get(word);
		objFlashCard.decreaseDifficulty();
		flashcardMap.put(word, objFlashCard);
		

	}

	/**
	 * 
	 */
	private static void clearCollections() {

		/*-- Clearing TreeMap and ArrayList --*/
		flashCardList.clear();
		flashcardMap.clear();

	}

	/**
	 * 
	 */
	private static void saveFlashCards() {
		try {

			File flashCardFile = new File(serialFileName);
			if (!flashCardFile.exists()) {
				flashCardFile.createNewFile();
			}
			FileOutputStream fs = new FileOutputStream(flashCardFile);
			ObjectOutputStream os = new ObjectOutputStream(fs);

			/*
			 * get Collection of values contained in TreeMap using Collection
			 * values() method of TreeMap class
			 */
			Collection<FlashCard> c = flashcardMap.values();

			// obtain an Iterator for Collection
			Iterator<FlashCard> itr = c.iterator();

			// iterate through TreeMap values iterator
			while (itr.hasNext()) {
				// System.out.println(itr.next());
				os.writeObject(itr.next());
			}

			os.close();
			fs.close();
		} catch (FileNotFoundException e) {
			System.out.println("file not found while saving flash cards");
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("IO exception while saving FlashCard objects");
			e.printStackTrace();
		} catch (Exception e) {
			System.out.println("Exception : check stack trace");
			e.printStackTrace();
		} // catch

	}

	/**
	 * 
	 */
	private static void inflateFlashCardList() {
		/*------------Inflating flashCardList using Map----------*/

		/*
		 * get Collection of values contained in TreeMap using Collection
		 * values() method of TreeMap class
		 */
		Collection<FlashCard> c = flashcardMap.values();

		// obtain an Iterator for Collection
		Iterator<FlashCard> itr = c.iterator();

		FlashCard objFlashCard = null;
		int i, difficulty;
		// iterate through TreeMap values iterator
		while (itr.hasNext()) {
			objFlashCard = itr.next();
			difficulty = objFlashCard.getDifficulty();
			for (i = 1; i <= difficulty; i++) {
				flashCardList.add(objFlashCard);
			}

		}

	}

	/**
	 * 
	 */
	private static void restoreflashCards() {
		/*------------Deserializing and restoring FlashCard objects into flashCardMap----------*/
		FileInputStream fileInputStream;
		try {
			
			File flashCardFile = new File(serialFileName);
			if (!flashCardFile.exists()) {
				objBetweenUIandHelper.noFlashCardsAdded();
				flashCardFile.createNewFile();
				initializeVocabulary(); 
			}
			fileInputStream = new FileInputStream(flashCardFile);

			ObjectInputStream objectInputStream = new ObjectInputStream(
					fileInputStream);

			FlashCard objFlashCard = null;
			while ((objFlashCard = (FlashCard) objectInputStream.readObject()) != null) {

				flashcardMap.put(objFlashCard.getWord(), objFlashCard);
			}

			objectInputStream.close();
			fileInputStream.close();

		} catch (FileNotFoundException e) {
			System.out.println("file not found while restoring flash cards");
			e.printStackTrace();
		} catch (EOFException e) {
			
		} catch (IOException e) {
		
			System.out
					.println("IO exception while restoring FlashCard objects");
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			System.out
					.println("ClassNotFoundException while restoring FlashCard objects");
			e.printStackTrace();
		} catch (Exception e) {
			System.out.println("exception while restoring FlashCard objects");
		} finally {

		}

	}
	
	/**
	 * 
	 */
	private static void initializeVocabulary(){
		try {
			String file = "Vocabulary.csv";
			
			String dataRow;
			
			BufferedReader csvFile = new BufferedReader(new FileReader(file));
			
			while ((dataRow = csvFile.readLine()) != null){
				
				String[] dataArray;				
				String delimiter = "\t";
				dataArray = dataRow.split(delimiter); // separate word and definition with tab in each line 
				addWord(dataArray[0], dataArray[1]); // add word and definition pair
				
			}
			
			csvFile.close();
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e){
			e.printStackTrace();
		} catch (Exception e){
			System.out.println("Exception while reading csv file: " + e);
		}
	}
	
	
	/**
	 * 
	 */
	public static void printSortedValues() {

		TreeMap<String, FlashCard> sortedMap = sortByValue(flashcardMap);
		
		Collection<FlashCard> c = sortedMap.values();

		// obtain an Iterator for Collection
		Iterator<FlashCard> itr = c.iterator();

		FlashCard objFlashCard = null;
		int difficulty;
		// iterate through TreeMap values iterator
		while (itr.hasNext()) {
			objFlashCard = itr.next();
			difficulty = objFlashCard.getDifficulty();
			System.out.println("Word : " + objFlashCard.getWord()
					+ "\n Difficulty : " + difficulty);

		}
	}

	private static TreeMap<String, FlashCard> sortByValue(
			TreeMap<String, FlashCard> unsortedMap) {
		TreeMap<String, FlashCard> sortedMap = new TreeMap<String, FlashCard>(
				new DifficultyComparator(unsortedMap));
		sortedMap.putAll(unsortedMap);
		return sortedMap;
	}

}

class DifficultyComparator implements Comparator<String> {

	Map<String, FlashCard> map;

	public DifficultyComparator(TreeMap<String, FlashCard> map) {
		this.map = map;
	}

	public int compare(String keyA, String keyB) {
		FlashCard objA = (FlashCard) map.get(keyA);
		int difficultyA = objA.getDifficulty();
		FlashCard objB = (FlashCard) map.get(keyB);
		int difficultyB = objB.getDifficulty();
		if (difficultyA > difficultyB)
			return -1;
		else
			return 1;

	}
}
