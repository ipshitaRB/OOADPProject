/**
 * 
 */
//package com.ooadp.project;

/**
 * This class has been created to relay messages from the helper class in case
 * of exceptions to the UI classes
 * 
 * @author Ipshita
 *
 */
public class MediatorBetweenUIandHelper {

	private static final MediatorBetweenUIandHelper INSTANCE = new MediatorBetweenUIandHelper();

	private MediatorBetweenUIandHelper() {

	}

	public static final MediatorBetweenUIandHelper getInstance() {
		return INSTANCE;
	}

	ExceptionListener objExceptionListener;

	public interface ExceptionListener {

		/**
		 * all flash cards have been used up
		 */
		public void noFlashCardsRemaining();

		/**
		 * no flash cards were ever added
		 */
		public void noFlashCardsAdded();

	}

	public void registerExceptionListener(ExceptionListener objExceptionListener) {
		this.objExceptionListener = objExceptionListener;
	}

	public void unregisterExceptionListener() {
		this.objExceptionListener = null;
	}

	/**
	 * to be called by Helper class
	 * 
	 */
	public void noFlashCardsRemaining() {
		if (objExceptionListener != null) {
			objExceptionListener.noFlashCardsRemaining();
		} else {
			System.out.println("ExceptionListener null");
		}
	}

	/**
	 * to be called by helper class
	 */
	public void noFlashCardsAdded() {
		if (objExceptionListener != null) {
			objExceptionListener.noFlashCardsAdded();
		} else {
			System.out.println("ExceptionListener null");
		}
	}

}
