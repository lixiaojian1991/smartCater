package scater.common;

import java.io.IOException;
import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;

public class SendMessage {

	
	/**
	 * 实现短信发送功能，小灵通除外
	 * @param tel  电话号码
	 * @param text  短信内容
	 * @return String 发送短信的状态 ，>0表示发送成功
	 * @throws HttpException   请求sms服务器发生的异常
	 * @throws IOException    io出错发生的异常，主要出现在进行字符编码的时候
	 */
	public static String SendMessage(String tel,String text) throws HttpException, IOException{
		HttpClient client = new HttpClient();
		PostMethod post = new PostMethod("http://utf8.sms.webchinese.cn");
		post.addRequestHeader("Content-Type",
				"application/x-www-form-urlencoded;charset=utf8");
		NameValuePair[] data = { new NameValuePair("Uid", "hehuihui123"),
				new NameValuePair("Key", "d6dbba3803aab62e32eb"),
				new NameValuePair("smsMob", tel),
				new NameValuePair("smsText", text) };
		post.setRequestBody(data);

		client.executeMethod(post);
		Header[] headers = post.getResponseHeaders();
		int statusCode = post.getStatusCode();
		System.out.println("statusCode:" + statusCode);
		for (Header h : headers) {  //打印返回的头部信息
			System.out.println(h.toString());
		}
		String result = new String(post.getResponseBodyAsString().getBytes(
				"utf8"));  //返回值
		post.releaseConnection();
		System.out.println(result); // 打印短信发送状态，>0为短信发送数量，即发送成功的意思
		return result;
	}
	

	/*短信发送后返回值	说　明
	-1	没有该用户账户
	-2	接口密钥不正确 [查看密钥]不是账户登陆密码
	-21	MD5接口密钥加密不正确
	-3	短信数量不足
	-11	该用户被禁用
	-14	短信内容出现非法字符
	-4	手机号格式不正确
	-41	手机号码为空
	-42	短信内容为空
	-51	短信签名格式不正确接口签名格式为：【签名内容】
	-6	IP限制
	大于0	短信发送数量*/

}
