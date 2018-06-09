package com.jsyunsi.demo01;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import sun.font.Decoration.Label;

public class MyFrame extends JFrame implements ActionListener{

	JLabel lblscore = new JLabel("0", JLabel.CENTER);

	int rows = 6;
	int cols = 6;
	GridLayout gl = new GridLayout(rows, cols);
	JPanel panCenter = new JPanel();
	JButton[][] buts = new JButton[rows][cols];
	int[][] buttonsLocation = new int[rows][cols];
	ImageIcon[] imgs = new ImageIcon[16];

	JPanel panSouth = new JPanel();
	JButton btnRestart = new JButton("重新开始");
	JButton btnExit = new JButton("退出");
	
	boolean alreadySelect=false;

	Random rand = new Random();

	void randomButtonLocations() {
		int num, x, y;
		for (int i = 0; i < rows * cols / 2; i++) {
			num = rand.nextInt(16) + 1;
			for (int alike = 0; alike < 2; alike++) {
				do {
					x = rand.nextInt(rows);
					y = rand.nextInt(cols);
				} while (buttonsLocation[x][y] != 0);
				buttonsLocation[x][y] = num;
			}
		}
	}

	public MyFrame(String title) {
		this.add(lblscore, BorderLayout.NORTH);
		panCenter.setLayout(gl);
		for (int i = 0; i < imgs.length; i++) {
			imgs[i] = new ImageIcon("imgs/" + (i + 1) + ".png");
		}
		randomButtonLocations();
		for (int i = 0; i < buts.length; i++) {
			for (int j = 0; j < buts[i].length; j++) {
				buts[i][j] = new JButton();
				buts[i][j].setIcon(imgs[buttonsLocation[i][j] - 1]);
				buts[i][j].addActionListener(this);
				panCenter.add(buts[i][j]);
			}
		}
		this.add(panCenter, BorderLayout.CENTER);

		panSouth.add(btnRestart);
		btnRestart.addActionListener(this);
		panSouth.add(btnExit);
		btnExit.addActionListener(this);
		this.add(panSouth, BorderLayout.SOUTH);

		this.setTitle(title);
		this.setSize(450, 530);
		this.setLocationRelativeTo(null);
		this.setVisible(true);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setResizable(false);
	}

	public static void main(String[] args) {
		MyFrame mf = new MyFrame("连连看");
	}

	int x1,y1,x2,y2,num1,num2;
	public void actionPerformed(ActionEvent e) {
		if(alreadySelect==false){
			for(int i=0;i<rows;i++){
				for(int j=0;j<cols;j++){
					if(e.getSource()==buts[i][j]){
						x1=i;
						y1=j;
						num1=buttonsLocation[i][j];
						alreadySelect=true;
					}
				}
			}
		}
		else{
			for(int i=0;i<rows;i++){
				for(int j=0;j<cols;j++){
					if(e.getSource()==buts[i][j]){
						x2=i;
						y2=j;
						num2=buttonsLocation[i][j];
						compareAndDelete();
					}
				}
			}
		}
		if(e.getSource()==btnRestart){//重新开始
			this.dispose();
			new MyFrame("连连看");
		}
		if(e.getSource()==btnExit){//退出
			System.exit(0);
		}
	}
	
	public boolean point0(int a1,int b1,int a2,int b2){//0折点连接
		if((Math.abs(a1-a2)==1&&b1==b2)||(Math.abs(b1-b2)==1&&a1==a2)){//两点相邻
			return true;
		}
		if(a1==a2&&b1!=b2){//两点在同一行
			boolean zeroFlag=true;
			if (b1<b2){
				for(int i=b1+1;i<b2;i++){
					if(buttonsLocation[a1][i]!=0){
						zeroFlag=false;
						break;
					}
				}
			}
			if (b1>b2){
				for(int i=b2+1;i<b1;i++){
					if(buttonsLocation[a1][i]!=0){
						zeroFlag=false;
						break;
					}
				}
			}
			return zeroFlag;
		}
		if(b1==b2&&a1!=a2){//两点在同一列
			boolean zeroFlag=true;
			if (a1<a2){
				for(int i=a1+1;i<a2;i++){
					if(buttonsLocation[i][b1]!=0){
						zeroFlag=false;
						break;
					}
				}
			}
			if (a1>a2){
				for(int i=a2+1;i<a1;i++){
					if(buttonsLocation[i][b1]!=0){
						zeroFlag=false;
						break;
					}
				}
			}
			return zeroFlag;
		}
		return false;
	}
	
	public boolean point1(int a1,int b1,int a2,int b2){//1折点连接
		if((point0(a1,b1,a1,b2)&&point0(a1,b2,a2,b2)&&buttonsLocation[a1][b2]==0)||(point0(a1,b1,a2,b1)&&point0(a2,b1,a2,b2)&&buttonsLocation[a2][b1]==0)){
			return true;
		}
		return false;
	}
	
	public boolean point2(int a1,int b1,int a2,int b2){//2折点连接
		if(a1==0||(point0(a1,b1,0,b1)&&buttonsLocation[0][b1]==0)){//跨越上边界连接
			if((point0(a2,b2,0,b2)&&buttonsLocation[0][b2]==0)||a2==0){
				return true;
			}
		}
		if(b1==0||(point0(a1,b1,a1,0)&&buttonsLocation[a1][0]==0)){//跨越左边界连接
			if((point0(a2,b2,a2,0)&&buttonsLocation[a2][0]==0)||b2==0){
				return true;
			}
		}
		if(a1==rows-1||(point0(a1,b1,rows-1,b1)&&buttonsLocation[rows-1][b1]==0)){//跨越下边界连接
			if((point0(a2,b2,rows-1,b2)&&buttonsLocation[rows-1][b2]==0)||a2==rows-1){
				return true;
			}
		}
		if(b1==cols-1||(point0(a1,b1,a1,cols-1)&&buttonsLocation[a1][cols-1]==0)){//跨越右边界连接
			if((point0(a2,b2,a2,cols-1)&&buttonsLocation[a2][cols-1]==0||b2==cols-1)){
				return true;
			}
		}
		for(int i=a1+1;i>=0&&i<rows&&buttonsLocation[i][b1]==0;i++){//向下找点
			if(point1(i,b1,a2,b2)){
				return true;
			}
		}
		for(int i=a1-1;i>=0&&i<rows&&buttonsLocation[i][b1]==0;i--){//向上找点
			if(point1(i,b1,a2,b2)){
				return true;
			}
		}
		for(int i=b1+1;i>=0&&i<cols&&buttonsLocation[a1][i]==0;i++){//向右找点
			if(point1(a1,i,a2,b2)){
				return true;
			}
		}
		for(int i=b1-1;i>=0&&i<cols&&buttonsLocation[a1][i]==0;i--){//向左找点
			if(point1(a1,i,a2,b2)){
				return true;
			}
		}
		return false;
	}
	
	public void addScore(int score){
		lblscore.setText((Integer.parseInt(lblscore.getText())+score)+"");
	}
	
	public boolean connectCheck(){
		if(x1==x2&&y1==y2){
			return false;
		}
		if(point0(x1,y1,x2,y2)){
			addScore(10);//0折点连接加10分
			return true;
		}
		if(point1(x1,y1,x2,y2)){
			addScore(20);//一折点加20分
			return true;
		}
		if(point2(x1,y1,x2,y2)){
			addScore(40);//2折点加40分
			return true;
		}
		return false;
	}
	
	public void compareAndDelete(){
		if(num1==num2&&connectCheck()){
			buts[x1][y1].setVisible(false);
			buts[x2][y2].setVisible(false);
			buttonsLocation[x1][y1]=0;
			buttonsLocation[x2][y2]=0;
			alreadySelect=false;
		}
		else{
			x1=x2;
			y1=y2;
			num1=num2;
		}
	}

}
