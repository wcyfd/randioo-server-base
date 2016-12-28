package com.randioo.randioo_server_base.utils.sensitive;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public class SensitiveWordDictionary {
	private static SensitiveWordFilter sensitiveWordFilter = new SensitiveWordFilter();

	private static void readFile(String path, WordFilter wordFilter) {
		try (BufferedReader reader = new BufferedReader(new FileReader(path))) {
			String line = null;
			while ((line = reader.readLine()) != null) {
				List<String> words = wordFilter.getWord(line);
				for (String word : words)
					sensitiveWordFilter.addSensitiveWordToHashMap(word);
			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}

	public static boolean containsSensitiveWord(String... words) {
		boolean contains = false;
		for (String word : words) {
			contains = sensitiveWordFilter.isContaintSensitiveWord(word, SensitiveWordFilter.MIN_MATCH_TYPE);
			if (contains)
				break;
		}
		return contains;
	}

	public static void getAllWord(){
		System.out.println(sensitiveWordFilter.getSensitiveWordMap());
	}
	
	public static void readAll(String path){
		System.out.println("sensitive word load");
		readFile(path, new WordFilter() {
			@Override
			public List<String> getWord(String line) {
				List<String> list = new LinkedList<>();
				list.add(line.split("\\|")[0]);
				return list;
			}
		});
		System.out.println("sensitive word load complete");
//		getAllWord();
	}
public static void main(String[] args) {
	SensitiveWordDictionary.readAll("./xml/sensitive.txt");
	boolean contains = SensitiveWordDictionary.containsSensitiveWord("wenjiabao");
	System.out.println(contains);
}
	
}
