package com.lanshan.core.util;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.util.AttributeSource;
import org.wltea.analyzer.lucene.IKAnalyzer;

import antlr.TokenStream;

public class AnalyzerUtil {
	public static String[] phrase(String text) {
    	// TODO Auto-generated method stub
    	 //long b = System.currentTimeMillis();
    	 
    	 Analyzer anal=new IKAnalyzer();       
    	 StringReader reader=new StringReader(text);  	    
	     //分词  
	     TokenStream ts=(TokenStream) anal.tokenStream("", reader);  
	     CharTermAttribute term=((AttributeSource) ts).getAttribute(CharTermAttribute.class);
	     List<String> list = new ArrayList();
	        //遍历分词数据 
	     try{
		     while(((org.apache.lucene.analysis.TokenStream) ts).incrementToken()){
		         list.add(term.toString());
		     }
	     }catch(Exception e){
	    	 e.printStackTrace();
	     }finally{
	    	 anal.close(); 
	     }
	     int size=list.size();  
	     String[] array = (String[])list.toArray(new String[size]);	   
	     return array;
    }

}
