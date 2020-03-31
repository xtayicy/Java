package harry.io.test;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.io.StreamTokenizer;

/**
 * 
 * @author Harry
 *
 */
public class StreamTokenizerTest {
	public static void main(String[] args) {
		parseFile("java.docx");
	}
	
	private static void parseFile(String fileName){
		int wordCount = 0;
		int numberCount = 0;
		try(FileReader reader = new FileReader(fileName);){
			StreamTokenizer streamTokenizer = new StreamTokenizer(reader);
			streamTokenizer.slashSlashComments(true);
			streamTokenizer.slashStarComments(true);
			while(streamTokenizer.nextToken() != StreamTokenizer.TT_EOF){
				if(streamTokenizer.ttype == StreamTokenizer.TT_WORD){
					wordCount++;
				}else if(streamTokenizer.ttype == StreamTokenizer.TT_NUMBER){
					numberCount++;
				}
			}
		}catch (FileNotFoundException e) {
			System.out.println("File not found: " + fileName);
			return;
		}catch (IOException e) {
			System.out.println("Error parsing file.");
		}
		
		System.out.println("Number of words: " + wordCount);
		System.out.println("Number of numerals: " + numberCount);
	}
}
