package ds222rr_assign1;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Scanner;

public class Exercise4 {
	public static void main(String[] args) throws FileNotFoundException  {
		Scanner keyboard = new Scanner(System.in);
		System.out.println("1. Caesar Cipher\n2. Monoalphabetic Cipher\n3. Transposition Cipher");
		System.out.print("Choose method: ");
		int method = keyboard.nextInt();

		System.out.println("1. Encryption\n2. Decryption");
		System.out.print("Choose option: ");
		int option = keyboard.nextInt();
		
		System.out.print("Provide input file: ");
		String inputFile = keyboard.next();
		System.out.print("Provide output file: ");
		String outputFile = keyboard.next();
		
		File input = new File(inputFile);
		File output = new File(outputFile);
		
		if (method == 1) {
			int key = 0;
			// Key verification
			do  {
				System.out.print("Enter key (1-26): ");
				if (keyboard.hasNextInt()) {
					key = keyboard.nextInt();
					if (key > 0 && key <= 26) {
						caesarCipher(input, output, option, key);
					}
					else {
						System.out.println("Invalid input!");
					}
				}
				else {
					System.out.println("Invalid input!");
					keyboard.next();
				}
			} while (key <= 0 || key > 26);
		}

		else if (method == 2) {
			System.out.println("Use only characters in ASCII (32-126)!");
			boolean flag = false;
			// Key verification
			do {
				flag = true;
				System.out.print("Enter alphabetic key: ");
				String key = keyboard.next();
				for (int i = 0; i < key.length(); i++) {
					int ascii = (char) (key.charAt(i));
					if (ascii < 32 || ascii > 126) {
						flag = false;
						break;
					}
				}
				if (flag == true) {
					monoalphabeticCipher(input, output, option, key);
				}
				else {
					System.out.println("Invalid input!");
				}
			} while (flag == false);
		}
		
		else if (method == 3) {
			System.out.println("Key must contain all numbers up to key size minus one!");
			System.out.println("For example: Key with a length of 4 must contain numbers 0, 1, 2, 3. (but in any order)");
			boolean flag = false;
			// Key verification
			do {
				System.out.print("Enter numeric (0-9) key: ");
				String key = keyboard.next();
				for (int i = 0; i < key.length(); i++) {
					flag = false;
					for (int j = 0; j < key.length(); j++) {
						if (Character.isDigit(key.charAt(j))) {
							int number = Integer.parseInt(Character.toString(key.charAt(j)));
							if (i == number) {
								flag = true;
							}
						}
					}
					if (flag == false) {
						break;
					}
				}
				if (flag == true) {
					transpositionCipher(input, output, option, key);
				}
				else {
					System.out.println("Invalid input!");
				}
			} while (flag == false);
		}
		keyboard.close();
		
	}
	
	public static void caesarCipher(File in, File out, int opt, int k) throws FileNotFoundException {
		StringBuilder sb = new StringBuilder();
		String str = readFile(in);	// read text
		for (int i = 0; i < str.length(); i++) {
			if (Character.isLetter(str.charAt(i))) {
				char c = Character.toLowerCase(str.charAt(i));
				if (opt == 1) {			// encrypt text
					int ascii = ((int) (c)) + k;
					if (ascii > 122) {
						ascii -= 26;
					}
					c = (char) (ascii);
					sb.append(Character.toUpperCase(c));
				}
				else if (opt == 2) {	// decrypt text
					int ascii = ((int) (c)) - k;
					if (ascii < 97) {
						ascii += 26;
					}
					c = (char) (ascii);
					sb.append(Character.toLowerCase(c));
				}
			}
			else {
				sb.append(str.charAt(i));
			}
		}
		sb.append("\n");
		String temp = sb.toString();
		writeFile(out, temp);	// write text
	}
	
	public static void monoalphabeticCipher(File in, File out, int opt, String k) throws FileNotFoundException {
		char [] standart = new char[95];	// Key for plaintext
		char [] secure = new char[95];		// Key for ciphertext
		monoalphabeticCreateKeys(k, standart, secure);
		StringBuilder sb = new StringBuilder();
		String str = readFile(in);			// read text
		for (int i = 0; i < str.length(); i++) {
			if (opt == 1 ) {			// encrypt text 
				char c = str.charAt(i);
				for (int j = 0; j < standart.length; j++) {
					if (c == standart[j]) {
						c = secure[j];
						break;
					}
				}
				sb.append(c);
			}
			else if (opt == 2) {	// decrypt text
				char c = str.charAt(i);
				for (int j = 0; j < standart.length; j++) {
					if (c == secure[j]) {
						c = standart[j];
						break;
					}
				}
				sb.append(c);
			}	
		}
		sb.append("\n");
		String temp = sb.toString();
		writeFile(out, temp);	// write  text
	}
	
	public static void transpositionCipher(File in, File out, int opt, String k) throws FileNotFoundException {			
		String str = readFile(in);		// Read text
		String key = k;	// Encryption-Decryption key
		StringBuilder sb = new StringBuilder();
		
		if (opt == 1) {				// encrypt text 
			int keyNumber = 0;
			int intKey = 0;
				// Iterative statement to determine the next row to be processed
				for (int j = 0; j < key.length(); j++) {
					for ( int l = 0; l < key.length(); l++) {
						intKey = Integer.parseInt(Character.toString(key.charAt(l)));
						// In case the next row has been determined
						if (intKey == keyNumber) {
							intKey = l;
							break;
						}
					}
					// Block will contain all characters of the current block being processed
					char [] block = new char[str.length()/key.length()+1];
					int index = 0;
					for (int i = intKey; i < str.length(); i += key.length()) {
						block[index++] = str.charAt(i);
					}
					for (int m = 0; m < block.length; m++ ) {
						sb.append(block[m]);
					}
					keyNumber++;
				}

		}
		else if (opt == 2) {	// decrypt text
			for (int i = 0; i < str.length()/key.length(); i++) {
				for (int j = 0; j < key.length(); j++) {
					int intKey = Integer.parseInt(Character.toString(key.charAt(j)));
					char a = str.charAt((str.length()/key.length())*(intKey)+i);
					sb.append(a);
				}
			}  
		}
		String temp = sb.toString();	
		writeFile(out, temp);	// write encrypted text
	}
	
	// Key-creation-method for the monoalphabetic-algorithm
	public static void monoalphabeticCreateKeys(String k, char[] sta, char[] sec) {
		// Create Standart-key	
		for (int i = 0; i < 95; i++) {
			sta[i] = (char) (i+32);
		}
		// Create Secure-Key
		int next = 0;
		for (int i = 0; i < k.length(); i++) {
			// Flag to determine whether a character has already been appended to the secure-key
			boolean flag = false;
			for (int j = 0; j < sec.length; j++) {
				if (k.charAt(i) == sec[j]) {
					// Flag = true: character does already exist in the secure-key
					flag = true;
					break;
				}
			}
			if (flag == false) {
				sec[next++] = k.charAt(i);
			}
		}
		// Append remaining letters
		for (int i = 126; i >= 32; i--) {
			// Flag to determine whether a character has already been appended to the secure-key
			boolean flag = false;	
			for (int j = 0; j < sec.length; j++) {
				if (((char) i) == sec[j]) {
					// Flag = true: character does already exist in the secure-key
					flag = true;
					break;
				}
			}
			if (flag == false) {
				sec[next++] = ((char) i);
			}
		}
	}
	
	//Method for reading a textfile
	public static String readFile(File f) throws FileNotFoundException {
		StringBuilder sb = new StringBuilder();
		Scanner sc = new Scanner(f);
		while (sc.hasNextLine()) {
			String temp = sc.nextLine();
			sb.append(temp + "\n");
		}
		sc.close();
		return sb.toString();
	}
	
	//Method for writing a textfile
	public static void writeFile(File f, String str) throws FileNotFoundException {
		PrintWriter printer = new PrintWriter(f);
		Scanner sc = new Scanner(str);
		while (sc.hasNextLine()) {
			String tmp = sc.nextLine();
			printer.print(tmp + "\r\n");
		}
		sc.close();
		printer.close();
	}
}