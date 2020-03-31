package harry.io.test;

import java.io.Externalizable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

/**
 * 
 * @author Harry
 *
 */
public class ExternalizableTest {
	public static void main(String[] args) {
		try{
			Customer customer = new Customer(1,"1234-5678-9876");
			System.out.println("Before saving object: ");
			System.out.println("ID:" + customer.getId() + " CC:" + customer.getCreditCard());
			ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(new File("customer.dat")));
			out.writeObject(customer);
			out.close();
			ObjectInputStream in = new ObjectInputStream(new FileInputStream(new File("customer.dat")));
			customer = (Customer) in.readObject();
			System.out.println("After retrieving object: ");
			System.out.println("ID:" + customer.getId() + " CC:" + customer.getCreditCard());
			in.close();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}

class Customer implements Externalizable{
	private int id;
	private String creditCard;
	private static Cipher cipher;
	private static SecretKeySpec skeySpec;
	
	public Customer() {
		id = 0;
		creditCard = "";
	}

	static{
		try{
			CreateCipher();
		}catch(Exception e){
			e.printStackTrace();
			System.exit(0);
		}
	}
	
	public int getId() {
		return id;
	}

	private static void CreateCipher() throws Exception {
		KeyGenerator kgen = KeyGenerator.getInstance("AES");
		kgen.init(128);
		SecretKey secretKey = kgen.generateKey();
		byte[] raw = secretKey.getEncoded();
		skeySpec = new SecretKeySpec(raw, "AES");
		cipher = Cipher.getInstance("AES");
	}

	public String getCreditCard() {
		return creditCard;
	}

	public Customer(int id, String creditCard) {
		this.id = id;
		this.creditCard = creditCard;
	}

	@Override
	public void writeExternal(ObjectOutput out){
		try {
			out.write(id);
			encrypt();
			out.writeUTF(creditCard);
			System.out.println("After encryption: ");
			System.out.println("ID: " + id + " CC:" + creditCard);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void encrypt() throws Exception{
		cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
		byte[] buff = cipher.doFinal(creditCard.getBytes());
		creditCard = parseByte2HexStr(buff);
	}

	@Override
	public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
		try {
			id = in.read();
			String str = in.readUTF();
			decrypt(str);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void decrypt(String str) throws Exception {
		cipher.init(Cipher.DECRYPT_MODE, skeySpec);
		byte[] buff = cipher.doFinal(parseHexStr2Byte(str));
		creditCard = new String(buff);
	}
	
	private static String parseByte2HexStr(byte buf[]) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < buf.length; i++) {
            String hex = Integer.toHexString(buf[i] & 0xFF);
            if (hex.length() == 1) {
                    hex = '0' + hex;
            }
            
            sb.append(hex.toUpperCase());
        }
        
        return sb.toString();
	}
	
	private static byte[] parseHexStr2Byte(String hexStr) {
        if (hexStr.length() < 1)
                return null;
        
        byte[] result = new byte[hexStr.length()/2];
        for (int i = 0;i< hexStr.length()/2; i++) {
            int high = Integer.parseInt(hexStr.substring(i*2, i*2+1), 16);
            int low = Integer.parseInt(hexStr.substring(i*2+1, i*2+2), 16);
            result[i] = (byte) (high * 16 + low);
        }
        
        return result;
	}
}