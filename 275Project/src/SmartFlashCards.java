/**
 * COEN 275 Final Project
 * Bo Xiao, Ipshita Roy Burman, and Hongyu Cui
 * 
 * SmartFlashCards
 * 
 */
//package com.ooadp.project;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.*;
import java.util.ArrayList;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import com.sun.speech.freetts.Voice;
import com.sun.speech.freetts.VoiceManager;


/**
 * GUI for the SmartFlashCards application using Swing
 * 
 * @author Bo Xiao
 *
 */
class SmartFlashCards {
	JFrame jfrm;								// Main window
	JPanel buttonP;								// Contains btnAddWord and btnDiffWords
	JPanel wordDisp; 							// Contains current word
	JPanel prompt; 								// Contains questions for the user
	JPanel addWordButtons; 						// Contains the two buttons in Add Word dialog box
	JPanel titlePanel; 							// Contains the title
	JPanel promptButtons; 						// Contains the buttons in prompt
	JList jlstDiffWords; 						// Difficult word list
	JList jlsAllWords; 							// All word list
	JDialog dlgAddWord; 						// Add Word dialog box
	JDialog dlgDiffWords; 						// Difficult words dialog box
	JDialog dlgAllWords; 						// All words dialog box
	JButton btnAddWord; 						// Add word button
	JButton btnDiffWords; 						// Difficult words button
	JButton btnAllWords; 						// All words button
	JButton btnReset; 							// Reset
	JButton btnStart; 							// Start button
	JButton btnYes; 							// Yes button
	JButton btnNo; 								// No button
	JButton btnNext; 							// Next button
	JButton btnAdd; 							// Add button
	JButton btnBack; 							// Back button
	JButton btnPronunciation; 					// button to play audio pronunciation
	JTextField jtfNewWord; 						// New word textfield
	JTextArea jtaNewWordDef;					// New word definition
	JScrollPane scrollPane; 					// New word definition scroll pane


	String newWord; 							// Storing the new word extracted from jtfNewWord
	String newWordDef; 							// Storing new word definition extracted from jtaNewWordDef

	JLabel title; 								// title above current word
	JLabel remaining; 							// remaining words title
	JLabel welcome; 							// Welcome message
	JLabel preWord; 							// Field name before word
	JLabel word; 								// The current word
	JLabel preDefinition; 						// Field name before definition
	JLabel diffWordsInst; 						// title above difficult words list
	JTextArea diffWordsDef; 					// definition of the difficult word selected
	JTextArea definition; 						// Definition of the current word
	JScrollPane definitionSP; 					// ScrollPane for the definition
	JScrollPane diffWordsSP; 					// Difficult words scrollpane
	JScrollPane allWordsSPw; 					// All words scrollpane
	JLabel question; 							// Question the user if he/she knows the current word
	JLabel questionNext; 						// Question the user if he/she wants to see the next word
	String currentWord = ""; 					// Storing the current word string
	String currentDef = ""; 					// Storing the current word definition string
	FlashCard currentFC;
	static final String VOICENAME = "kevin16";
	Voice voice;
	VoiceManager vm;

	/**
	 * Constructor of the class Creates the GUI
	 */
	SmartFlashCards() {
		vm  = VoiceManager.getInstance();
		voice = vm.getVoice(VOICENAME);
		voice.allocate();
		// Initialize the FlashCard collection
		FlashCardHelper.initialize();
		currentFC = FlashCardHelper.getNextFlashCard();
		if (currentFC != null) {
			currentWord = currentFC.getWord();
			currentDef = currentFC.getDefinition();
		}
		// System.out.println(currentDef);

		// Initialize the labels
		title = new JLabel("Word of the day");
		preWord = new JLabel("Word:");
		word = new JLabel(currentWord);
		preDefinition = new JLabel("Definition:");

		definition = new JTextArea(currentDef);
		definition.setSize(400, 250);
		definition.setLineWrap(true);
		definition.setEditable(false);
		definition.setOpaque(false);
		definitionSP = new JScrollPane(definition);
		definitionSP.setBorder(BorderFactory.createEmptyBorder());

		question = new JLabel();
		questionNext = new JLabel();

		Font buttonFont = new Font("Arial", Font.PLAIN, 15);
		Font textLabel = new Font("Arial", Font.PLAIN, 15);
		Font titleFont = new Font("Arial", Font.BOLD, 25);
		Font preFont = new Font("Arial", Font.BOLD, 20);
		Font wordFont = new Font("Arial", Font.PLAIN, 20);
		Font welcomeFont = new Font("Arial", Font.ITALIC | Font.BOLD, 25);

		// Set main frame (use GridBagLayout)
		jfrm = new JFrame("Smart Flashcards");
		jfrm.getContentPane().setLayout(new GridBagLayout());
		GridBagConstraints gbcFrame = new GridBagConstraints();
		jfrm.setSize(800, 600);
		jfrm.setResizable(false);
		jfrm.setLocationRelativeTo(null);
		jfrm.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// Set container for buttons (use FlowLayout)
		buttonP = new JPanel(new FlowLayout(FlowLayout.LEFT));
		buttonP.setBackground(Color.LIGHT_GRAY);
		gbcFrame.weightx = 1;
		gbcFrame.weighty = 0;
		gbcFrame.fill = GridBagConstraints.HORIZONTAL;
		gbcFrame.gridx = 0;
		gbcFrame.gridy = 0;
		jfrm.add(buttonP, gbcFrame);

		// Set add word button
		btnAddWord = new JButton("Add New Word");
		btnAddWord.setPreferredSize(new Dimension(150, 40));
		btnAddWord.setFocusPainted(false);
		btnAddWord.setFont(buttonFont);
		buttonP.add(btnAddWord);

		// Set difficult word button
		btnDiffWords = new JButton("Difficult Words");
		btnDiffWords.setPreferredSize(new Dimension(150, 40));
		btnDiffWords.setFocusPainted(false);
		btnDiffWords.setFont(buttonFont);
		buttonP.add(btnDiffWords);

		// Set all word button
		btnAllWords = new JButton("All Words");
		btnAllWords.setPreferredSize(new Dimension(150, 40));
		btnAllWords.setFocusPainted(false);
		btnAllWords.setFont(buttonFont);
		buttonP.add(btnAllWords);

		// Set reset button
		btnReset = new JButton("Reset");
		btnReset.setPreferredSize(new Dimension(150, 40));
		btnReset.setFocusPainted(false);
		btnReset.setFont(buttonFont);
		buttonP.add(btnReset);

		// Set remaining word label
		String wordRemaining = "Words remaining: "
				+ FlashCardHelper.getNumberofRemainingWords() + "/"
				+ FlashCardHelper.totalNumberOfCards();
		remaining = new JLabel(wordRemaining);
		remaining.setFont(buttonFont);
		buttonP.add(remaining);

		/*************************************************************
		 * Set container for words (use GridBagLayout)
		 */
		wordDisp = new JPanel(new GridBagLayout());
		GridBagConstraints gbcWord = new GridBagConstraints();
		wordDisp.setBorder(BorderFactory.createEtchedBorder());
		// wordDisp.setBackground(Color.RED);
		gbcFrame.weightx = 1;
		gbcFrame.weighty = 0.7;
		gbcFrame.fill = GridBagConstraints.BOTH;
		gbcFrame.gridx = 0;
		gbcFrame.gridy = 1;
		gbcFrame.insets = new Insets(25, 50, 0, 50);
		jfrm.add(wordDisp, gbcFrame);

		/*
		 * titlePanel = new JPanel(new FlowLayout());
		 * titlePanel.setBackground(Color.YELLOW); gbcWord.weighty = 0;
		 * gbcWord.weightx = 1; gbcWord.gridx = 0; gbcWord.gridy = 0;
		 * gbcWord.gridwidth = 2; gbcWord.insets = new Insets(0,0,0,0);
		 * gbcWord.anchor = GridBagConstraints.NORTH; gbcWord.fill =
		 * GridBagConstraints.HORIZONTAL; wordDisp.add(titlePanel, gbcWord);
		 * title.setFont(titleFont); titlePanel.add(title);
		 */

		gbcWord.gridwidth = 1;
		gbcWord.gridx = 0;
		gbcWord.gridy = 0;
		gbcWord.weightx = 0.4;
		gbcWord.weighty = 1;
		gbcWord.fill = GridBagConstraints.NONE;
		gbcWord.anchor = GridBagConstraints.NORTHEAST;
		gbcWord.insets = new Insets(20, 0, 0, 10);
		preWord.setFont(preFont);
		wordDisp.add(preWord, gbcWord);
		// preWord.setVisible(false);

		gbcWord.weightx = 0.6;
		gbcWord.gridx = 1;
		gbcWord.gridy = 0;
		gbcWord.anchor = GridBagConstraints.NORTHWEST;
		gbcWord.insets = new Insets(20, 10, 0, 0);
		word.setFont(wordFont);
		wordDisp.add(word, gbcWord);

		gbcWord.weightx = 0.2;
		gbcWord.weighty = 200;
		gbcWord.gridx = 0;
		gbcWord.gridy = 1;
		gbcWord.anchor = GridBagConstraints.NORTHEAST;
		gbcWord.insets = new Insets(20, 0, 0, 10);
		preDefinition.setFont(preFont);
		wordDisp.add(preDefinition, gbcWord);
		btnPronunciation = new JButton("Pronunciation");
		btnPronunciation.setPreferredSize(new Dimension(150, 40));
		btnPronunciation.setFocusPainted(false);
		btnPronunciation.setFont(buttonFont);
		preDefinition.setVisible(false);

		gbcWord.weightx = 0.2;
		gbcWord.weighty = 200;
		gbcWord.gridx = 0;
		gbcWord.gridy = 1;
		gbcWord.anchor = GridBagConstraints.CENTER;
		gbcWord.insets = new Insets(20, 0, 0, 10);
		wordDisp.add(btnPronunciation,gbcWord);
		gbcWord.weightx = 0.6;
		gbcWord.gridx = 1;
		gbcWord.gridy = 1;
		gbcWord.anchor = GridBagConstraints.NORTHWEST;
		gbcWord.insets = new Insets(20, 10, 0, 0);
		definition.setFont(wordFont);
		wordDisp.add(definitionSP, gbcWord);
		// Hide current definition
		definitionSP.setVisible(false);

		btnNext = new JButton("Next Word");
		btnNext.setPreferredSize(new Dimension(150, 40));
		btnNext.setFocusPainted(false);
		btnNext.setFont(buttonFont);
		btnNext.setVisible(false);

		gbcWord.weightx = 1;
		gbcWord.weighty = 1;
		gbcWord.gridx = 0;
		gbcWord.gridy = 2;
		gbcWord.gridwidth = 2;
		gbcWord.anchor = GridBagConstraints.NORTH;
		titlePanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		// titlePanel.setBackground(Color.YELLOW);
		gbcWord.fill = GridBagConstraints.HORIZONTAL;
		wordDisp.add(titlePanel, gbcWord);
		titlePanel.add(btnNext);

		/*************************************************************
		 * Set container for prompt (use GridBagLayout)
		 */
		prompt = new JPanel(new GridBagLayout());
		GridBagConstraints gbcPrompt = new GridBagConstraints();
		prompt.setBorder(BorderFactory.createEtchedBorder());
		gbcFrame.weightx = 1;
		gbcFrame.weighty = 0.3;
		gbcFrame.fill = GridBagConstraints.BOTH;
		gbcFrame.gridx = 0;
		gbcFrame.gridy = 2;
		gbcFrame.insets = new Insets(25, 70, 10, 70);
		jfrm.add(prompt, gbcFrame);

		// Set buttons for prompt
		btnStart = new JButton("Start");
		btnStart.setPreferredSize(new Dimension(150, 40));
		btnStart.setFocusPainted(false);
		btnStart.setFont(buttonFont);

		btnYes = new JButton("Yes");
		btnYes.setPreferredSize(new Dimension(150, 40));
		btnYes.setFocusPainted(false);
		btnYes.setFont(buttonFont);

		btnNo = new JButton("No");
		btnNo.setPreferredSize(new Dimension(150, 40));
		btnNo.setFocusPainted(false);
		btnNo.setFont(buttonFont);





		question = new JLabel("Do you know the meaning of this word?");
		question.setFont(welcomeFont);
		// welcome.setForeground(Color.BLUE);
		gbcPrompt.gridx = 0;
		gbcPrompt.gridy = 0;
		gbcPrompt.weighty = 1;
		prompt.add(question, gbcPrompt);

		promptButtons = new JPanel(new FlowLayout());
		gbcPrompt.gridx = 0;
		gbcPrompt.gridy = 1;
		prompt.add(promptButtons, gbcPrompt);
		promptButtons.add(btnYes);
		promptButtons.add(btnNo);

		/*************************************************************
		 * Set Add Word dialog (GridBag)
		 */
		dlgAddWord = new JDialog(jfrm, "Add New Word", false);
		dlgAddWord.setSize(400, 250);
		dlgAddWord.getContentPane().setLayout(new GridBagLayout());
		GridBagConstraints gbcAW = new GridBagConstraints();
		dlgAddWord.setResizable(false);
		dlgAddWord.setLocationRelativeTo(jfrm);

		// Set buttons for Add Word Dialog
		btnAdd = new JButton("Add");
		btnAdd.setPreferredSize(new Dimension(100, 40));
		btnAdd.setFocusPainted(false);
		btnAdd.setFont(buttonFont);

		btnBack = new JButton("Back");
		btnBack.setPreferredSize(new Dimension(100, 40));
		btnBack.setFocusPainted(false);
		btnBack.setFont(buttonFont);

		// Set Text fields in Add Word dialog
		JLabel jlNewWord = new JLabel("New Word");
		JLabel jlNewWordDef = new JLabel("Definition");
		jtfNewWord = new JTextField(20);
		jlNewWord.setFont(textLabel);
		jtfNewWord.setBorder(BorderFactory.createEtchedBorder());

		jtaNewWordDef = new JTextArea(6, 20);
		jlNewWordDef.setFont(textLabel);
		jtaNewWordDef.setLineWrap(true);
		scrollPane = new JScrollPane(jtaNewWordDef);

		gbcAW.weightx = 0.5;
		gbcAW.anchor = GridBagConstraints.NORTHEAST;
		gbcAW.gridx = 0;
		gbcAW.gridy = 0;
		gbcAW.insets = new Insets(0, 0, 0, 0);
		dlgAddWord.add(jlNewWord, gbcAW);
		gbcAW.weightx = 0.5;
		gbcAW.anchor = GridBagConstraints.NORTHWEST;
		gbcAW.gridx = 1;
		gbcAW.gridy = 0;
		gbcAW.insets = new Insets(0, 20, 0, 0);
		dlgAddWord.add(jtfNewWord, gbcAW);
		gbcAW.anchor = GridBagConstraints.NORTHEAST;
		gbcAW.gridx = 0;
		gbcAW.gridy = 1;
		gbcAW.insets = new Insets(10, 0, 0, 0);
		dlgAddWord.add(jlNewWordDef, gbcAW);
		gbcAW.anchor = GridBagConstraints.NORTHWEST;
		gbcAW.gridx = 1;
		gbcAW.gridy = 1;
		gbcAW.insets = new Insets(10, 20, 10, 0);
		dlgAddWord.add(scrollPane, gbcAW);

		// Set addWordButtons (FlowLayout)
		addWordButtons = new JPanel(new FlowLayout());
		// addWordButtons.setBackground(Color.LIGHT_GRAY);
		gbcAW.weightx = 0;
		gbcAW.weighty = 0;
		gbcAW.insets = new Insets(0, 0, 0, 0);
		gbcAW.anchor = GridBagConstraints.PAGE_END;
		gbcAW.fill = GridBagConstraints.HORIZONTAL;
		gbcAW.gridwidth = 2;
		gbcAW.gridx = 0;
		gbcAW.gridy = 2;
		dlgAddWord.add(addWordButtons, gbcAW);

		addWordButtons.add(btnAdd);
		addWordButtons.add(btnBack);
		


		/*************************************************************
		 * Button event listeners
		 */

		// 'Add Word' button listener (show the Add New Word dialog box)
		btnAddWord.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				dlgAddWord.setVisible(true);
			}
		});

		// 'Difficult Words' button listener (show the Difficult words list
		// dialog box)
		btnDiffWords.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				/*************************************************************
				 * Set Difficult Words dialog
				 */
				dlgDiffWords = new JDialog(jfrm, "Difficult Words List", false);
				dlgDiffWords.setSize(430, 300);
				dlgDiffWords.getContentPane().setLayout(
						new FlowLayout(FlowLayout.LEFT));
				// GridBagConstraints gbcDW = new GridBagConstraints();
				dlgDiffWords.setLocationRelativeTo(jfrm);
				dlgDiffWords.setResizable(false);
				ArrayList<FlashCard> fcl = FlashCardHelper.getDifficultWords();

				DefaultListModel<String> lm = new DefaultListModel<>();
				for (FlashCard fc : fcl) {
					lm.addElement(fc.getWord());
				}
				jlstDiffWords = new JList<String>(lm);
				jlstDiffWords
				.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
				jlstDiffWords.setFont(wordFont);
				diffWordsSP = new JScrollPane(jlstDiffWords);
				diffWordsSP.setPreferredSize(new Dimension(150, 200));

				diffWordsInst = new JLabel(
						"Click each word to see its definition");
				diffWordsInst.setFont(wordFont);

				JPanel jp = new JPanel(new FlowLayout(FlowLayout.LEFT));
				jp.add(diffWordsSP);

				diffWordsDef = new JTextArea();
				diffWordsDef.setFont(wordFont);
				diffWordsDef.setPreferredSize(new Dimension(250, 200));
				diffWordsDef.setLineWrap(true);
				jp.add(diffWordsDef);

				dlgDiffWords.add(diffWordsInst);
				dlgDiffWords.add(jp);

				dlgDiffWords.setVisible(true);

				// Difficult words list selection listener
				jlstDiffWords
				.addListSelectionListener(new ListSelectionListener() {

					@Override
					public void valueChanged(ListSelectionEvent e) {
						
						diffWordsDef.setText(FlashCardHelper
								.getFlashCard(
										(String) jlstDiffWords
										.getSelectedValue())
										.getDefinition());
					}
				});
			}
		});

		// 'All Words' button listener
		btnAllWords.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				
				/*************************************************************
				 * Set All Words dialog
				 */
				dlgAllWords = new JDialog(jfrm, "All Words", false);
				dlgAllWords.setSize(200, 400);
				dlgAllWords.getContentPane().setLayout(new FlowLayout());
				dlgAllWords.setLocationRelativeTo(jfrm);
				dlgAllWords.setResizable(false);
				ArrayList<FlashCard> fclw = FlashCardHelper.getAllFlashCards();

				DefaultListModel<String> lmw = new DefaultListModel<>();
				for (FlashCard fc : fclw) {
					lmw.addElement(fc.getWord());
				}
				jlsAllWords = new JList<String>(lmw);
				jlsAllWords
				.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
				jlsAllWords.setFont(wordFont);
				allWordsSPw = new JScrollPane(jlsAllWords);
				allWordsSPw.setPreferredSize(new Dimension(180, 350));
				dlgAllWords.add(allWordsSPw);
				dlgAllWords.setVisible(true);
			}
		});

		// 'Reset' button listener
		btnReset.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				btnYes.setEnabled(true);
				btnNo.setEnabled(true);
				FlashCardHelper.resetFlashCards();
				FlashCard fc = FlashCardHelper.getNextFlashCard();
				currentWord = fc.getWord();
				currentDef = fc.getDefinition();
				wordDisp.revalidate();
				word.setText(currentWord);
				definition.setText(currentDef);
				definitionSP.setVisible(false);
				remaining.setText("Words remaining: "
						+ FlashCardHelper.getNumberofRemainingWords() + "/"
						+ FlashCardHelper.totalNumberOfCards());

				wordDisp.repaint();
			}
		});


		// 'Yes' button listener (user remembers the word)
		btnYes.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				FlashCardHelper.userRemembersWord(currentWord);
				FlashCard fc = FlashCardHelper.getNextFlashCard();
				if (fc != null) {

					currentWord = fc.getWord();
					currentDef = fc.getDefinition();
					wordDisp.revalidate();
					wordDisp.repaint();
					word.setText(currentWord);
					definition.setText(currentDef);
					definitionSP.setVisible(false);
				} else {
					wordDisp.revalidate();
					wordDisp.repaint();
					word.setText("No more words remaining");
					definition.setText(currentDef);
					definitionSP.setVisible(false);
					btnYes.setEnabled(false);
					btnNo.setEnabled(false);
				}

				//
				remaining.setText("Words remaining: "
						+ FlashCardHelper.getNumberofRemainingWords() + "/"
						+ FlashCardHelper.totalNumberOfCards());
				// btnYes.setEnabled(false);
				// btnNo.setEnabled(false);



			}
		});

		// 'No' button listener (user does not remember the word)
		btnNo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				btnNext.setVisible(true);
				preDefinition.setVisible(true);
				wordDisp.revalidate();
				wordDisp.repaint();
				remaining.setText("Words remaining: "
						+ FlashCardHelper.getNumberofRemainingWords() + "/"
						+ FlashCardHelper.totalNumberOfCards());
				// System.out.println(currentWord);
				// System.out.println(currentDef);
				definition.setText(currentDef);
				definitionSP.setVisible(true);
				FlashCardHelper.userDoesNotRememberWord(currentWord);
				btnYes.setEnabled(false);
				btnNo.setEnabled(false);

			}
		});

		// 'Audio' button listener (user does not remember the word)
		btnPronunciation.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {

				try {
					voice.speak(word.getText());
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});

		// 'Start' button listener
		btnStart.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				// Optional
			}
		});

		// 'Next Word' button listener
		btnNext.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				btnNext.setVisible(false);
				preDefinition.setVisible(false);
				FlashCard fc = FlashCardHelper.getNextFlashCard();
				if (fc != null) {

					currentWord = fc.getWord();
					currentDef = fc.getDefinition();
					wordDisp.revalidate();
					wordDisp.repaint();
					word.setText(currentWord);
					definition.setText(currentDef);
					definitionSP.setVisible(false);
					btnYes.setEnabled(true);
					btnNo.setEnabled(true);

				} else {
					wordDisp.revalidate();
					wordDisp.repaint();
					word.setText("No more words remaining");
					definition.setText(currentDef);
					definitionSP.setVisible(false);
					btnYes.setEnabled(false);
					btnNo.setEnabled(false);
				}
				remaining.setText("Words remaining: "
						+ FlashCardHelper.getNumberofRemainingWords() + "/"
						+ FlashCardHelper.totalNumberOfCards());

			}
		});

		// 'Add' button listener
		btnAdd.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				if (jtfNewWord.getText().equals("")
						|| jtaNewWordDef.getText().equals("")
						|| jtfNewWord.getText().matches("[a-zA-Z ]*\\d+.*")) {
					JOptionPane.showMessageDialog(jfrm,
							"Word/Definition invalid");
				} else {
					newWord = jtfNewWord.getText();
					newWordDef = jtaNewWordDef.getText();

					FlashCardHelper.addWord(newWord, newWordDef);
					remaining.setText("Words remaining: "
							+ FlashCardHelper.getNumberofRemainingWords() + "/"
							+ FlashCardHelper.totalNumberOfCards());
					dlgAddWord.dispose();
				}
			}
		});

		// 'Back' button listener
		btnBack.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				dlgAddWord.dispose();
			}
		});

		// Close FlashCard collection before exit
		jfrm.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				FlashCardHelper.beforeAppExit();
			}
		});

		jfrm.setVisible(true);
	}

	/** main method */
	public static void main(String args[]) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				new SmartFlashCards();
			}
		});
	}
}