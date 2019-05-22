import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;

import java.util.Scanner;

import java.util.concurrent.ThreadLocalRandom;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class MainProject {

			static File wordsFile=new File("/root/Dictionary/Test/wordsList.txt");
			static File namesFile=new File("/root/Dictionary/Test/nameList.txt");
			static File wordDefinitionFile=new File("/root/Dictionary/Test/dictionary.txt");
			
			static String word;
			static String CurrentWordDefinition;
			static String CurrentUserName;
			static String answer;
			static String line;
			
			static boolean check=true;
			
			static String websiteUrl = "http://services.aonaware.com/DictService/Default.aspx?action=define&dict=wn&query=";
			
			static Scanner consoleWordInput= new Scanner(System.in);
			static String[] wordslist={"brick","bridge","bright","broken","brother","brown","brush","bucket","history","hole","hollow","hook","hope","horn","horse",
			"hospital","town","trade","train","transport","tray","tree","trick","trouble","trousers","grab","strap","biology","grey","basket","brain"
					
			};
			static FileWriter fileWriter;
			static Scanner fileReader;
			static int  once=1;
			static int score=0;
			static int option;
			static int numberOfGuesses;
			static char userEntered;
			
			
			//main method	
			public static void main(String args[])throws Exception{
				while(once==1)
				{
				welcomeConsole();
				once++;
				}
				askOptions();
				
				switch (option)
				{
				case 1: wordMeaning();
				while(Continue())
				{
					wordMeaning();
				}
				break;
				case 2: Quiz();
				break;
				case 3: WordGame();
				break;
				case 4: System.exit(0);
				break;
				default : main(null);
				break;
				
				}
				
				main(args);
				
				
			}
			
			//to check weather User wants to continue using program
			public static boolean Continue(){
				System.out.println("Do you want to continue?\nEnter y to continue, anything else to Discontinue :");
				consoleWordInput=new Scanner(System.in);
				char cont = consoleWordInput.next().charAt(0);
				if(cont=='y'||cont=='Y')
				{
				return true;
				}
				return false;
				}
			
			//Welcoming the user and writing or reading name from namelist
		public static void welcomeConsole() throws IOException{
			
			System.out.println("Welcome");	
			System.out.println("What's your name?");
			CurrentUserName = consoleWordInput.nextLine();
			
			if(!namesFile.exists()){
			    namesFile.createNewFile();
			}
			
			fileReader = new Scanner(namesFile);	
			boolean check=true;        
			
			while (fileReader.hasNextLine()){
				line=fileReader.nextLine();
				if(line.equals(CurrentUserName)){
					System.out.println("Wellcome back "+CurrentUserName);
					check=false;
				}
			}
			
			
			
		if (check==true)
			            {
			            	fileWriter = new FileWriter(namesFile,true);
			            	fileWriter.write(CurrentUserName+"\n");
				            System.out.println("Wellcome for the first time "+CurrentUserName);
				            fileWriter.flush();
				            fileWriter.close();
			            }
			        
					}
			

		//for the menu
		public static void askOptions() throws Exception{
		
			System.out.println("\n\nThese are the options you have got here");
			System.out.println("1------------Find Meaning to a word");
			System.out.println("2------------Play Quiz");
			System.out.println("3------------Play Word game");
			System.out.println("4------------Exit the program");
			System.out.print("Enter your Option");
			option=consoleWordInput.nextInt();
			consoleWordInput = new Scanner(System.in);
		
		}


		//Start of dictionary main function
		public static void wordMeaning()throws Exception{
			
			System.out.print("Enter word : ");
			word=consoleWordInput.nextLine();
		if(wordExists(word, wordsFile)){
			GetDefinationOffline(word);
			}
			if(!wordExists(word, wordsFile)){	
			CurrentWordDefinition = getDefinitionInternet(word);
			
				if(CurrentWordDefinition.equals("@")){
					System.out.println("No Definion Found for the word : " + word+"\nEnter a valid word!");
					wordMeaning();
					}
			System.out.println(CurrentWordDefinition);
			writeWord(word);
			
			}			
			}
		
		
		
		//search offlines for word
		public static void GetDefinationOffline(String word) throws Exception{
			System.out.println("Word Found");
			fileReader = new Scanner(wordDefinitionFile);        
			while(fileReader.hasNextLine())
			   {
		         String currentLine=fileReader.nextLine();
			            
		         if(currentLine.equalsIgnoreCase("@@@start@@@"+word))
		       {	
		        	 currentLine=fileReader.nextLine();
			       	do{
			           	currentLine=fileReader.nextLine();
			           	System.out.println(currentLine);
			       	}
			       	while(!currentLine.equals("@@@end@@@"));
		       }
			   }
		wordMeaning();	
		}


		//takes the word or phrase input
		public static void writeWord(String word)throws Exception{
			
			if(!wordsFile.exists())
			{
				wordsFile.createNewFile();
			}
			
		    if(!wordExists(word, wordsFile))
		    {
			WriteDictionary(CurrentWordDefinition);
			fileWriter = new FileWriter(wordsFile,true);
			fileWriter.write(word+"\n");
			fileWriter.flush();
			fileWriter.close();
			}
		}
		
		
		
		//writes the meaning of the word to dictionary
		public static void WriteDictionary(String defination) throws Exception{
			if(!wordDefinitionFile.exists())
			{
				wordDefinitionFile.createNewFile();
			}
			fileWriter = new FileWriter(wordDefinitionFile,true);
			
			fileWriter.write("@@@start@@@"+word+"\n"+CurrentWordDefinition+"\n@@@end@@@\n");
			
			fileWriter.flush();
			fileWriter.close();
		}

		//checks every time if the word is present in the wordlist file
		public static boolean wordExists(String word, File file) throws Exception{
			fileReader=new Scanner(file);
			while (fileReader.hasNext())
			{
				String currentWord = fileReader.next();
				if (currentWord.equalsIgnoreCase(word))
				{
				return true;
				}
		    }
			return false;
		}


		//search online for the definition
		public static String getDefinitionInternet(String word) throws Exception{
			
			Document htmlPage = Jsoup.connect(websiteUrl+word).timeout(50000).get();
			String orignal = htmlPage.text();
			String text="No definitions found for";
			if(contains(orignal,text))
			{
				return "@";
			}
			
			String mod = orignal.replace("Definition Lookup Search for Using dictionary Any CIA World Factbook 2002 Easton's 1897 Bible Dictionary Elements database 20001107 Hitchcock's Bible Names Dictionary (late 1800's) Jargon File (4.3.1, 29 Jun 2001) The Collaborative International Dictionary of English v.0.44 THE DEVIL'S DICTIONARY ((C)1911 Released April 15 1993) The Free On-line Dictionary of Computing (27 SEP 03) U.S. Gazetteer (1990) Virtual Entity of Relevant Acronyms (Version 1.9, June 2002) WordNet (r) 2.0   1 definition found for "," ").replace(word+" : From WordNet (r) 2.0: "+word," ");
			
			return mod;
		} 
		public static boolean contains( String orignal, String string ) {
			  orignal = orignal == null ? "" : orignal;
			  string = string == null ? "" : string;

			  return orignal.toLowerCase().contains( string.toLowerCase() );
			}


/*Start Of Quiz Game*/		
		
		public static int Questions(){
			
			
			score=0;
			System.out.println("Wellcome "+CurrentUserName+" Your Current Score is "+score);
			System.out.println("Each Question contains 5 marks");
			System.out.println("There is also negative marking(-3 for each wrong answer)\n SO BE CAREFULL WHILE ANSWERING");
			System.out.println("Answer The folowing Questions BY choosing a, b, c or d ");
			
			
			System.out.println("1---Synonym of Acquaint ");
			System.out.println("a: Withhold");
			System.out.println("b: Conceal");
			System.out.println("c: Familiarise");
			System.out.println("d: Risky");
			userEntered=consoleWordInput.next().charAt(0);
			if (userEntered=='c'||userEntered=='C')
				score=score+5;
			else 
				score=score-3;
			
			System.out.println();
			System.out.println("2---Synonym of AGGRAVATE");
			System.out.println("a:Decline");
			System.out.println("b:Acquire");
			System.out.println("c:Excited");
			System.out.println("d:Irritate");
			userEntered=consoleWordInput.next().charAt(0);
			if (userEntered=='d'||userEntered=='D')
				score=score+5;
			else 
				score=score-3;
			
			System.out.println();
			System.out.println("3---Synonym of ABSTAIN ");
			System.out.println("a:Refrain");
			System.out.println("b:Ingest");
			System.out.println("c:Take in");
			System.out.println("d:Consume");
			userEntered=consoleWordInput.next().charAt(0);
			if (userEntered=='a'||userEntered=='A')
				score=score+5;
			else 
				score=score-3;
			
			System.out.println();
			System.out.println("4---Synonym of Sporadic ");
			System.out.println("a:methodical");
			System.out.println("b:continuous");
			System.out.println("c:occasional");
			System.out.println("d:constant");
			userEntered=consoleWordInput.next().charAt(0);
			if (userEntered=='c'||userEntered=='C')
				score=score+5;
			else 
				score=score-3;
			
			System.out.println();
			System.out.println("5---Synonym of Melange");
			System.out.println("a:mixture of medley");
			System.out.println("b:optical illusion");
			System.out.println("c:desert");
			System.out.println("d:household");
			userEntered=consoleWordInput.next().charAt(0);
			if (userEntered=='a'||userEntered=='A')
				score=score+5;
			else 
				score=score-3;
			
			System.out.println();
			System.out.println("6---Synonym of ANTIPATHY");
			System.out.println("a:Charity");
			System.out.println("b:Dislke");
			System.out.println("c:Approval");
			System.out.println("d:Reprove");
			userEntered=consoleWordInput.next().charAt(0);
			if (userEntered=='b'||userEntered=='B')
				score=score+5;
			else 
				score=score-3;
			
			System.out.println();
			System.out.println("7---Synonym of ABASH ");
			System.out.println("a:Pull out");
			System.out.println("b:Dislike");
			System.out.println("c:Embarass");
			System.out.println("d:Hearten");
			userEntered=consoleWordInput.next().charAt(0);
			if (userEntered=='c'||userEntered=='C')
				score=score+5;
			else 
				score=score-3;
			
			System.out.println();
			System.out.println("8---Synonym of BRITTLE");
			System.out.println("a:Partner");
			System.out.println("b:Like iron");
			System.out.println("c:Tough");
			System.out.println("d:Fragile");
			userEntered=consoleWordInput.next().charAt(0);
			if (userEntered=='d'||userEntered=='D')
				score=score+5;
			else 
				score=score-3;
			
			System.out.println();
			System.out.println("9---Synonym of BUSTLE");
			System.out.println("a:Flurry");
			System.out.println("b:Calm");
			System.out.println("c:Charm");
			System.out.println("d:Enjoyful");
			userEntered=consoleWordInput.next().charAt(0);
			if (userEntered=='a'||userEntered=='A')
				score=score+5;
			else 
				score=score-3;
			
			System.out.println();
			System.out.println("10---Synonym of CAJOLE");
			System.out.println("a:Like");
			System.out.println("b:Tempt");
			System.out.println("c:Warn");
			System.out.println("d:Assure");
			userEntered=consoleWordInput.next().charAt(0);
			if (userEntered=='b'||userEntered=='B')
				score=score+5;
			else 
				score=score-3;
			
			return(score);
			
		}
			//Quiz main function
		public static void Quiz() throws Exception{
			score=Questions();
			System.out.println("Your Score is :"+score);
			Grade(score);	
		}
		//grading system
		public static void Grade(double marks) throws Exception{
			double percentage;
			if (marks<=15){
				percentage=(marks/50)*100;
				System.out.println("Very poor, Improve Your Vocabulary \n Percentage="+percentage+"%");
				main(null);
				}
			if (marks<=25){
				percentage=(marks/50)*100;
				System.out.println("Poor, Work on your words\n Percentage="+percentage+"%");
				main(null);
			}
			if (marks<=35){
				percentage=(marks/50)*100;
				System.out.println("Better Than 60% of the world\n Percentage="+percentage+"%");
				main(null);	
			}
			if (marks<=45){
				percentage=(marks/50)*100;
				System.out.println("Good, You can Get more.\n Percentage="+percentage+"%");
				main(null);
			}
			if (marks<=50){
				percentage=(marks/50)*100;
				System.out.println("Excellent You are Genius.\n Percentage="+percentage+"%");
				main(null);
			}
		}
			
		
		//End Of Quiz Game	
			
		
		//Start Of Word Game 	
		public static void worslist() throws FileNotFoundException{
			
			}
		    public static void WordGame() throws Exception {
		      	do{
		      		startGame();
		      	}
		      	while(check);
		      	main(null);
		      }
		    
		    //word game main function
		public static void startGame() throws Exception {
		        numberOfGuesses = 0;
		        
		        String original = selectRandomWord();
		        String shuffled = getShuffledWord(original);
		        
		        boolean gameon = true;
		        while(gameon) {
		        	if (numberOfGuesses<5){
		        		 numberOfGuesses++;
		        		 System.out.println("Shuffled word is: "+shuffled);
		        		 System.out.println("Please type in the original word: ");
				    		answer=consoleWordInput.nextLine();
		        		 
		        		 if(original.equalsIgnoreCase(answer)) 
		        		 {
		        			 System.out.println("Congratulations! You found the word in "+numberOfGuesses+" guesses");
		        			 gameon = false;
		        		 }
		        		 else 
		        		 {
		        			 System.out.println("Sorry, Wrong answer");
		        		 }
		            	}
		        	else if(numberOfGuesses==5)
		        	{
		        		System.out.println("Your Guesses are Exhausted");
		        		numberOfGuesses++;
		        		main(null);
		        	}
		        	}       
		    }
		  
		    
		    
		    //selecting of random word from array
		    public static String selectRandomWord() 
		    {
		        int rPos = ThreadLocalRandom.current().nextInt(0, wordslist.length);
		        return wordslist[rPos];
		    }				
		    
		    
		    
		    
		    
		    public static String getShuffledWord(String original)
		    {   
		    	String shuffledWord = original;
		        int wordSize = original.length();
		        int shuffleCount = 10;
		       
		        for(int i=0;i<shuffleCount;i++)
		        {
		            int position1 = ThreadLocalRandom.current().nextInt(0, wordSize);
		            int position2 = ThreadLocalRandom.current().nextInt(0, wordSize);
		            
		            shuffledWord = swapCharacters(shuffledWord,position1,position2);
		        }
		        return shuffledWord;
		    }
		    
		    
		    
		    private static String swapCharacters(String shuffledWord, int position1, int position2) 
		    {
		        char[] charArray = shuffledWord.toCharArray();
		        
		        char temp = charArray[position1];
		        charArray[position1] = charArray[position2];
		        charArray[position2] = temp;
		       
		        return new String(charArray);
		    }
		    /*End Of Word Game*/	
		    		 
}


