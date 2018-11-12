package com.yingguo.controller.master;

public class LinRouHua {
	public static void main(String[] args) {
		printTopRen(10);
	}
	
	  /*
     * 打印正人字状
     */
    public static void printTopRen(int n){
        for(int i = 1; i <= n; i ++){
            for(int j = i; j < n; j ++){
                System.out.print("   ");
            }
            //左半部分
            for(int j = 1; j <= i; j++){
                if(j == 1)
                    System.out.print(" * ");
                else
                    System.out.print("   ");
            }
            //右半部分
            for(int j = 1; j < i; j++){
                if(j == i - 1)
                    System.out.print(" * ");
                else 
                    System.out.print("   ");
            }
            System.out.println();
        }
    }


}
