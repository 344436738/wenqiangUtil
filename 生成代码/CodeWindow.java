package com.leimingtech.gencode.codegenerate.window;

import com.leimingtech.gencode.codegenerate.database.LmcmsReadTable;
import com.leimingtech.gencode.codegenerate.generate.CodeGenerate;
import com.leimingtech.gencode.codegenerate.pojo.CreateFileProperty;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


/**
 * 类:CodeWindow 功能:根据参数生成单表的增删改查
 */
public class CodeWindow extends JFrame {

	private static final long serialVersionUID = -5324160085184088010L;

	private static String entityPackage = "test";//包名（小写）
	private static String entityName = "TestEntity";//实体名（首字母大写）
	private static String tableName = "t00_company";	//表名
	private static String ftlDescription = "分公司";//功能描述
	private static int fieldRowNum = 1;//默认一行显示1个字段
	private static String primaryKeyPolicy = "uuid";//主键生成策略
	private static String sequenceCode = "";//主键序列号
	
	String planets[] = { "uuid","identity","sequence"}; 

	public CodeWindow() {
		JPanel jp = new JPanel();
		this.setContentPane(jp);
		jp.setLayout(new GridLayout(15, 3));

		JLabel infolbl = new JLabel("提示:");
		final JLabel showlbl = new JLabel();
		JLabel packagebl = new JLabel("包名（小写）：");
		final JTextField packagefld = new JTextField();
		JLabel entitylbl = new JLabel("实体类名（首字母大写）：");
		final JTextField entityfld = new JTextField();
		JLabel tablejbl = new JLabel("表名：");
		final JTextField tablefld = new JTextField(20);
		
		JLabel tablekeyjbl = new JLabel("主键生成策略：");
		final JComboBox tablekeyfld = new JComboBox(planets);
		JLabel sequence_lb = new JLabel("主键SEQUENCE：(oracle序列名)");
		final JTextField sequence_fld = new JTextField(20);
		
		JLabel titlelbl = new JLabel("功能描述：");
		final JTextField titlefld = new JTextField();
		
		JLabel fieldRowNumlbl = new JLabel("行字段数目：");
		final JTextField fieldRowNumfld = new JTextField();
		fieldRowNumfld.setText(fieldRowNum+"");
		
		ButtonGroup bg = new ButtonGroup();
		final JRadioButton jsp = new JRadioButton("Table风格(form)");
		jsp.setSelected(true);
		final JRadioButton jsp_row = new JRadioButton("树状风格1(form)");
		final JRadioButton tree = new JRadioButton("树状风格2(form)");
		bg.add(jsp);
		bg.add(jsp_row);
		bg.add(tree);
		
		
		//默认选中所有
		final JCheckBox actionButton = new JCheckBox("Action");
		actionButton.setSelected(true);
		final JCheckBox jspButton = new JCheckBox("html");
		jspButton.setSelected(true);
		final JCheckBox serviceIButton = new JCheckBox("ServiceI");
		serviceIButton.setSelected(true);
		final JCheckBox serviceImplButton = new JCheckBox("ServiceImpl");
		serviceImplButton.setSelected(true);
		final JCheckBox pageButton = new JCheckBox("Page");
		pageButton.setSelected(true);
		final JCheckBox entityButton = new JCheckBox("Entity");
		entityButton.setSelected(true);

		jp.add(infolbl);
		jp.add(showlbl);
		jp.add(packagebl);
		jp.add(packagefld);
		jp.add(entitylbl);
		jp.add(entityfld);
		jp.add(tablejbl);
		jp.add(tablefld);
		
		jp.add(tablekeyjbl);
		jp.add(tablekeyfld);
		
		jp.add(sequence_lb);
		jp.add(sequence_fld);
		
		jp.add(titlelbl);
		jp.add(titlefld);
		jp.add(fieldRowNumlbl);
		jp.add(fieldRowNumfld);
		
		
		//复选框
		jp.add(actionButton);
		jp.add(jspButton);
		jp.add(serviceIButton);
		jp.add(serviceImplButton);
		jp.add(pageButton);
		jp.add(entityButton);

		//radio button
		jp.add(jsp);
		jp.add(jsp_row);
		jp.add(tree);
		
		final JLabel blanklbl = new JLabel();
		jp.add(blanklbl);
		
		JButton confirmbtn = new JButton("生成");
		confirmbtn.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				if (!"".equals(packagefld.getText())) {
					entityPackage = packagefld.getText();
				}else{
					showlbl.setForeground(Color.red);
					showlbl.setText("包名不能为空！");
					return;
				}
				if (!"".equals(entityfld.getText())) {
					entityName = entityfld.getText();
				}else{
					showlbl.setForeground(Color.red);
					showlbl.setText("实体类名不能为空！");
					return;
				}
				if (!"".equals(titlefld.getText())) {
					ftlDescription = titlefld.getText();
				}else{
					showlbl.setForeground(Color.red);
					showlbl.setText("描述不能为空！");
					return;
				}
				if (!"".equals(tablefld.getText())) {
					tableName = tablefld.getText();
				}else{
					showlbl.setForeground(Color.red);
					showlbl.setText("表名不能为空！");
					return;
				}
//				if (!"".equals(fieldRowNumfld.getText())) {
//					fieldRowNum = Integer.parseInt(fieldRowNumfld.getText().trim());
//				}
				
				
				//主键生成策略+序列号
				primaryKeyPolicy = (String)tablekeyfld.getSelectedItem();//获取所选的内容 
				
				if (primaryKeyPolicy.equals("sequence")) {
					if (!"".equals(sequence_fld.getText())) {
						sequenceCode = sequence_fld.getText();//获取所选的内容 
					}else{
						showlbl.setForeground(Color.red);
						showlbl.setText("主键生成策略为sequence时，序列号不能为空！");
						return;
					}
				}
				
				
				CreateFileProperty createFileProperty = new CreateFileProperty();
				//获取JSP模板风格
				//-------------------------------------------------------------
				if(jsp.isSelected()){
					createFileProperty.setJspMode("01");
				}
				if(jsp_row.isSelected()){
					createFileProperty.setJspMode("02");
				}
				if(tree.isSelected()){
					createFileProperty.setJspMode("04");
				}
				//-------------------------------------------------------------
				
				//生成文件选择
				//-------------------------------------------------------------
				if(actionButton.isSelected()){
					createFileProperty.setActionFlag(true);
				}
				if(jspButton.isSelected()){
					createFileProperty.setJspFlag(true);
				}
				if(serviceIButton.isSelected()){
					createFileProperty.setServiceIFlag(true);	
				}
				if(serviceImplButton.isSelected()){
					createFileProperty.setServiceImplFlag(true);
				}
//				if(pageButton.isSelected()){
//					createFileProperty.setPageFlag(true);
//				}
				if(entityButton.isSelected()){
					createFileProperty.setEntityFlag(true);
				}
				//-------------------------------------------------------------
				try {
					//[1].判断表是否存在
					boolean tableexist = new LmcmsReadTable().checkTableExist(tableName);
					if(tableexist){
						//[2].调用代码生成器
						new CodeGenerate(entityPackage,entityName,tableName,ftlDescription,createFileProperty,fieldRowNum,primaryKeyPolicy,sequenceCode).generateToFile();
						showlbl.setForeground(Color.red);
						showlbl.setText("成功生成增删改查->功能："+ftlDescription);
					}else{
						showlbl.setForeground(Color.red);
						showlbl.setText("表["+tableName+"] 在数据库中，不存在");
					}
					
				} catch (Exception e1) {
					showlbl.setForeground(Color.red);
					showlbl.setText(e1.getMessage());
				}
				
			}
		});
		JButton extbtn = new JButton("退出");
		extbtn.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				dispose();
				System.exit(0);
			}
		});

		jp.add(confirmbtn);
		jp.add(extbtn);

		this.setTitle("LM代码生成器[单表模型]");
		this.setVisible(true);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setSize(new Dimension(600, 400));
		this.setResizable(false);
		this.setLocationRelativeTo(getOwner());
	}

	public static void main(String[] args) {
		try {
			new CodeWindow().pack();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}

	}
}