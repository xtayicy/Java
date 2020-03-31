package harry.io.test;

import java.io.IOException;
import java.io.PushbackInputStream;

/**
 * 
 * @author Harry
 *
 */
public class PushbackInputStreamTest {
	public static void main(String[] args) throws IOException {
		readAndPrint();
	}

	private static void readAndPrint() throws IOException {
		PushbackInputStream pushbackInputStream = new PushbackInputStream(System.in, 3);
		int c,c1,c2;
		while((c = pushbackInputStream.read()) != 'q'){
			switch(c){
			case '.':
				System.out.print((char)c);
				if((c1 = pushbackInputStream.read()) == '0'){
					if((c2 = pushbackInputStream.read()) == '0'){
						System.out.print("**");
					}else{
						pushbackInputStream.unread(c2);
						pushbackInputStream.unread(c1);
					}
				}else{
					pushbackInputStream.unread(c1);
				}
				
				break;
				
			default:
				System.out.print((char)c);
				
				break;
			}
		}
		
		pushbackInputStream.close();
	}
}
