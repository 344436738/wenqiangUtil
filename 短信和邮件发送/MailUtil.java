package com.leimingtech.core.util;

import com.leimingtech.base.entity.temp.PfConfigEntity;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * 邮件工具类
 * 
 * @author :linjm linjianmao@gmail.com
 * @version :2014-5-13下午04:44:37
 **/
public class MailUtil {
	
	/**
	 * 邮件发送线程
	 * 
	 * @author 谢进伟
	 * @version :2015-07-28 11:17:34
	 * 
	 */
	private static class MailSendThread extends Thread {
		
		volatile boolean isStop;
		private String tomail;
		private String frommail;
		private String title;
		private String content;
		
		public MailSendThread (String tomail , String frommail , String title , String content ){
			super();
			this.isStop = false;
			this.tomail = tomail;
			this.frommail = frommail;
			this.title = title;
			this.content = content;
		}
		
		@Override
		public void run() {
			if(!this.isStop) {
				boolean isSuccess = send(tomail , frommail , title , content);
				if(isSuccess) {
					this.isStop = true;
					this.interrupt();
				}
			}
		}
		
	}
	
	/**
	 * 发邮件方法 速度慢
	 * 
	 * @param tomail
	 * @param frommail
	 * @param title
	 * @param content
	 * @return true or false
	 */
	public static boolean send(String tomail , String frommail , String title , String content) {
		try {
			JavaMailSenderImpl javaMail = getJavaMail();
			MimeMessage message = javaMail.createMimeMessage();
			MimeMessageHelper messageHelp;
			messageHelp = new MimeMessageHelper(message , true , "UTF-8");
			messageHelp.setFrom(frommail == null ? ResourceUtil.getConfigByName("mailUsername") : frommail);
			messageHelp.setTo(tomail);
			messageHelp.setSubject(title);
			// messageHelp
			// String body = "";// content 可以是文字，HTML等
			messageHelp.setText(content , true);
			javaMail.send(message);
			return true;
		} catch (MessagingException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	/**
	 * 单个邮件发送
	 * 
	 * @param tomails
	 * @param frommail
	 * @param title
	 * @param content
	 */
	public static boolean sendMail(final String tomail , final String frommail , final String title , final String content) {
		try {
			MailSendThread thread = new MailSendThread(tomail , frommail , title , content);
			thread.start();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	/**
	 * 多个邮件发送
	 * 
	 * @param tomails
	 * @param frommail
	 * @param title
	 * @param content
	 */
	public static void sendMails(final List<String> tomails , final String frommail , final String title , final String content) {
		for(int i = 0 ; i < tomails.size() ; i++) {
			MailSendThread thread = new MailSendThread(tomails.get(i) , frommail , title , content);
			thread.start();
		}
	}
	
	private static JavaMailSenderImpl getJavaMail() {
		Map<String , String> mCache = PfConfigEntity.pfconfigCache;
		JavaMailSenderImpl javaMail = new JavaMailSenderImpl();
		javaMail.setHost(mCache.get("qq_smtpServer"));// stmp服务
		javaMail.setPassword(mCache.get("qq_mailPassword"));// 邮箱密码
		javaMail.setUsername(mCache.get("qq_mailUsername"));// 邮箱
		Properties prop = new Properties();//
		prop.setProperty("mail.smtp.auth" , mCache.get("qq_mailisCheck"));// 邮箱校验
		// prop.put("mail.smtp.host",
		// ResourceUtil.getConfigByName("stmpServer"));
		javaMail.setJavaMailProperties(prop);
		return javaMail;
	}
}
