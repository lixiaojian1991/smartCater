package csAsc.EIO.MsgEngine;

import java.io.FileInputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.NodeList;

public class CEioConfig
{
	private Document  doc;//定义document对象
	
	public  CEioConfig(String filepath)
	{//构造函数，加载指定的xml文件，初台化参数
		//调用 DocumentBuilderFactory.newInstance() 方法得到创建 DOM 解析器的工厂  
        DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance(); 
        //调用工厂对象的 newDocumentBuilder方法得到 DOM 解析器对象  
        DocumentBuilder builder;
		try
		{
			builder = builderFactory.newDocumentBuilder();
			 //通过FileInputStream的方式获取Document对象  
	        doc=builder.parse(new FileInputStream(filepath));
		} catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  	
	}
	
	/**
     * 功能：根据消息包名称，返回相应的MessagePack节点中子结点的Json格式，当没有找到相应的节点时返回null
     * @param  msgPack 消息包名称
     */
	public String getXml2Json(String msgPack)
	{
		//获取MessagePack元素
        NodeList tListMessagePack = getMessagePacks();
		if (tListMessagePack==null) return null;
		//获取名称为msgPack的MessagePack节点的子节点
		NodeList tMessagePackChildNodes=getMessagePackChildNodes(tListMessagePack,msgPack);
		if (tMessagePackChildNodes == null)
		{
			return null;
		}
		StringBuffer tStringBuffer=new StringBuffer();
		 tStringBuffer.append("{");
		for (int i = 0; i < tMessagePackChildNodes.getLength(); i++)
		{// 循环查找满足条件节点
			//获取MessagePack中子节点的名称
			String tNodeName = tMessagePackChildNodes.item(i).getNodeName();
			if(tNodeName.equals("MsgType"))
			{  
				// 取得MsgType节点包含的属性
				NamedNodeMap tMsgTypeAtt =tMessagePackChildNodes.item(i).getAttributes();
				// 读取MsgType中属性name的值
				String tMsgTypeName = tMsgTypeAtt.getNamedItem("name")
						.getNodeValue();
				tStringBuffer.append("\"").append(tMsgTypeName).append("\"");
				tStringBuffer.append(":{");
				//获取MsgType的子节点
				NodeList tMsgTypeChildNodes=tMessagePackChildNodes.item(i).getChildNodes();
				for(int j=0;j<tMsgTypeChildNodes.getLength();j++)
				{
					if(tMsgTypeChildNodes.item(j).getNodeName().equals("VeMessage"))
					{			
						// 取得VeMessage节点包含的属性
						NamedNodeMap tVeMessageeAtt =tMsgTypeChildNodes.item(j).getAttributes();
						// 读取VeMessage中属性name的值
						String tVeMessageName = tVeMessageeAtt.getNamedItem("name")
								.getNodeValue();
						tStringBuffer.append("\"").append(tVeMessageName).append("\"");
						tStringBuffer.append(":{");
						//获取VeMessage的所有子节点
						NodeList tVeMessageChildNodes=tMsgTypeChildNodes.item(j).getChildNodes();
						for(int k=0;k<tVeMessageChildNodes.getLength();k++)
						{
							if(tVeMessageChildNodes.item(k).getNodeName().equals("Request"))
							{
								// 取得Request节点包含的属性
								NamedNodeMap tRequestAtt =tVeMessageChildNodes.item(k).getAttributes();
								// 读取Request属性name的值
								String tRequestName = tRequestAtt.getNamedItem("name")
										.getNodeValue();
								tStringBuffer.append("\"ReqFunc\":\"").append(tRequestName).append("\",");
							}
							if(tVeMessageChildNodes.item(k).getNodeName().equals("Response"))
							{
								// 取得Response节点包含的属性
								NamedNodeMap tResponseAtt =tVeMessageChildNodes.item(k).getAttributes();
								// 读取Response属性name的值
								String tResponseName = tResponseAtt.getNamedItem("name")
										.getNodeValue();
								tStringBuffer.append("\"ResFunc\":\"").append(tResponseName).append("\",");
							}
							if(tVeMessageChildNodes.item(k).getNodeName().equals("Processor"))
							{
								// 取得Processor节点包含的属性
								NamedNodeMap tResponseAtt =tVeMessageChildNodes.item(k).getAttributes();
								// 读取Processor属性name的值
								String tResponseName = tResponseAtt.getNamedItem("name")
										.getNodeValue();
								tStringBuffer.append("\"MsgHandle\":\"").append(tResponseName).append("\",");
							}
						}
						tStringBuffer.replace(tStringBuffer.length()-1, tStringBuffer.length(), "},");
						
					}
				}
				tStringBuffer.insert(tStringBuffer.length()-1, "}");
				
			}
			
		}
		tStringBuffer.replace(tStringBuffer.length()-1, tStringBuffer.length(), "}");
		
		return tStringBuffer.toString();
	}
	/**
     * 功能：根据参数名称，查到相应的参数值，并返回所找到的参数值，没有找到返回null
     * @param  paramName 参数名称
     */
	public String getInitParameter(String paramName)
	{
		NodeList serverslist = doc.getElementsByTagName("init-param");
		if (serverslist != null) {
			for (int i = 0; i < serverslist.getLength(); i++) 
			{  
				//取得tParamName的值
				String tParamName = doc
						.getElementsByTagName("param-name").item(i)
						.getFirstChild().getNodeValue();
				//如果与请求的内容相同，取paramValue值返回
				if(tParamName.equals(paramName))
				{
					String tParamValue= doc
							.getElementsByTagName("param-value").item(i)
							.getFirstChild().getNodeValue();
					return tParamValue;
				}
				
			}
		}
		return null;
	}
	/**
     * 功能：根据“消息包名称”、“消息种类”及“消息名称”返回指定的客户端消息请求器名称即Request的值，没有找到返回null
     * @param msgPack 消息包名称
     * @param msgTypeName 消息种类
     * @param VeMessageName 消息名称
     */
	public String getMsgReq(String msgPack,String msgTypeName,String VeMessageName)
	{
		//获取MessagePack元素
        NodeList tListMessagePack = getMessagePacks();
		if (tListMessagePack==null) return null;
		//获取名称为msgPack的MessagePack节点的子节点
		NodeList tMessagePackChildNodes=getMessagePackChildNodes(tListMessagePack,msgPack);
		if(tMessagePackChildNodes==null) return null;
		//获取名称为msgTypeName的MsgType节点中的所有子节点
		NodeList tMsgTypeChildNodes=getMsgTypeChildNodes(tMessagePackChildNodes, msgTypeName);
		if(tMsgTypeChildNodes==null)
			return null;
		for(int i=0;i<tMsgTypeChildNodes.getLength();i++)
		{
			//获取MsgType中子节点的名称
			String tNodeName=tMsgTypeChildNodes.item(i).getNodeName();
			if(tNodeName.equals("VeMessage"))
			{
				// 取得此节点包含的属性
				NamedNodeMap tMsgNameAtt =tMsgTypeChildNodes.item(i).getAttributes();
				// 读取指定属性名称的属性的值
				String tVeMessageName = tMsgNameAtt.getNamedItem("name")
						.getNodeValue();
				if(tVeMessageName.equals(VeMessageName))
				{//在VeMessage节点中找到属性name值为VeMessageName的元素，读取Request值，并返回
					//获取VeMessage包含的所有子节点
					NodeList tVeMessageChildNodes=tMsgTypeChildNodes.item(i).getChildNodes();
					if(tVeMessageChildNodes==null) break;
					for(int j=0; j<tVeMessageChildNodes.getLength();j++)
					{
						if(tVeMessageChildNodes.item(j).getNodeName().equals("Request"))
						{
							// 取得Request包含的属性
							NamedNodeMap tRequestAtt =tVeMessageChildNodes.item(j).getAttributes();
							return tRequestAtt.getNamedItem("name").getNodeValue();
						}
					}
					
				}
			}			
		}
		return null;
	}
	
	/**
     * 功能：根据“消息包名称”、“消息种类”及“消息名称”返回指定的客户端消息响应器名称,即Response的值,没有找到返回null
     * @param msgPack 消息包名称
     * @param msgTypeName 消息种类
     * @param VeMessageName 消息名称
     */
	public String getMsgRes(String msgPack,String msgTypeName,String VeMessageName)
	{//
		//获取MessagePack元素
        NodeList tListMessagePack = getMessagePacks();
		if (tListMessagePack==null) return null;
		//获取名称为msgPack的MessagePack节点的子节点
		NodeList tMessagePackChildNodes=getMessagePackChildNodes(tListMessagePack,msgPack);
		if(tMessagePackChildNodes==null) return null;
		//获取名称为msgTypeName的MsgType节点中的所有子节点
		NodeList tMsgTypeChildNodes=getMsgTypeChildNodes(tMessagePackChildNodes, msgTypeName);
		if(tMsgTypeChildNodes==null)
			return null;
		for(int i=0;i<tMsgTypeChildNodes.getLength();i++)
		{
			//获取MsgType中子节点的名称
			String tNodeName=tMsgTypeChildNodes.item(i).getNodeName();
			if(tNodeName.equals("VeMessage"))
			{
				// 取得此节点包含的属性
				NamedNodeMap tMsgNameAtt =tMsgTypeChildNodes.item(i).getAttributes();
				// 读取指定属性名称的属性的值
				String tVeMessageName = tMsgNameAtt.getNamedItem("name")
						.getNodeValue();
				if(tVeMessageName.equals(VeMessageName))
				{//在VeMessage节点中找到属性name值为VeMessageName的元素，读取Response值，并返回
					//获取VeMessage包含的所有子节点
					NodeList tVeMessageChildNodes=tMsgTypeChildNodes.item(i).getChildNodes();
					if(tVeMessageChildNodes==null) break;
					for(int j=0; j<tVeMessageChildNodes.getLength();j++)
					{
						if(tVeMessageChildNodes.item(j).getNodeName().equals("Response"))
						{
							// 取得Response包含的属性
							NamedNodeMap tRequestAtt =tVeMessageChildNodes.item(j).getAttributes();
							return tRequestAtt.getNamedItem("name").getNodeValue();
						}
					}
					
				}
			}			
		}
		return null;
		
	}
	
	/**
     * 功能：根据“消息包名称”、“消息种类”及“消息名称”返回指定的消息处理器名称，即Processor的值,没有找到返回null
     * @param msgPack 消息包名称
     * @param msgTypeName 消息种类
     * @param VeMessageName 消息名称
     */
	public String getMsghandle(String msgPack,String msgTypeName,String VeMessageName)
	{//
		//获取MessagePack元素
        NodeList tListMessagePack = getMessagePacks();
		if (tListMessagePack==null) return null;
		//获取名称为msgPack的MessagePack节点的子节点
		NodeList tMessagePackChildNodes=getMessagePackChildNodes(tListMessagePack,msgPack);
		if(tMessagePackChildNodes==null) return null;
		//获取名称为msgTypeName的MsgType节点中的所有子节点
		NodeList tMsgTypeChildNodes=getMsgTypeChildNodes(tMessagePackChildNodes, msgTypeName);
		if(tMsgTypeChildNodes==null)
			return null;
		for(int i=0;i<tMsgTypeChildNodes.getLength();i++)
		{
			//获取MsgType中子节点的名称
			String tNodeName=tMsgTypeChildNodes.item(i).getNodeName();
			if(tNodeName.equals("VeMessage"))
			{
				// 取得此节点包含的属性
				NamedNodeMap tMsgNameAtt =tMsgTypeChildNodes.item(i).getAttributes();
				// 读取指定属性名称的属性的值
				String tVeMessageName = tMsgNameAtt.getNamedItem("name")
						.getNodeValue();
				if(tVeMessageName.equals(VeMessageName))
				{//在VeMessage节点中找到属性name值为VeMessageName的元素，读取Processor值，并返回
					//获取VeMessage包含的所有子节点
					NodeList tVeMessageChildNodes=tMsgTypeChildNodes.item(i).getChildNodes();
					if(tVeMessageChildNodes==null) break;
					for(int j=0; j<tVeMessageChildNodes.getLength();j++)
					{
						if(tVeMessageChildNodes.item(j).getNodeName().equals("Processor"))
						{
							// 取得Processor包含的属性
							NamedNodeMap tRequestAtt =tVeMessageChildNodes.item(j).getAttributes();
							return tRequestAtt.getNamedItem("name").getNodeValue();
						}
					}
					
				}
			}			
		}
		return null;
	}
	
	/**
     * 功能：根据“消息包名称”、“消息种类”及“消息名称”读取METype的值,没有找到返回null
     * @param msgPack 消息包名称
     * @param msgTypeName 消息种类
     * @param VeMessageName 消息名称
     */
	public String getMsgMEType(String msgPack,String msgTypeName,String VeMessageName)
	{//	 	
		//获取MessagePack元素
        NodeList tListMessagePack = getMessagePacks();
		if (tListMessagePack==null) return null;
		//获取名称为msgPack的MessagePack节点的子节点
		NodeList tMessagePackChildNodes=getMessagePackChildNodes(tListMessagePack,msgPack);
		if(tMessagePackChildNodes==null) return null;
		//获取名称为msgTypeName的MsgType节点中的所有子节点
		NodeList tMsgTypeChildNodes=getMsgTypeChildNodes(tMessagePackChildNodes, msgTypeName);
		
		if(tMsgTypeChildNodes==null)
			return null;
		for(int i=0;i<tMsgTypeChildNodes.getLength();i++)
		{
			//获取MsgType中子节点的名称
			String tNodeName=tMsgTypeChildNodes.item(i).getNodeName();
			if(tNodeName.equals("VeMessage"))
			{
				// 取得此节点包含的属性
				NamedNodeMap tMsgNameAtt =tMsgTypeChildNodes.item(i).getAttributes();
				// 读取指定属性名称的属性的值
				String tVeMessageName = tMsgNameAtt.getNamedItem("name")
						.getNodeValue();
				if(tVeMessageName.equals(VeMessageName))
				{//在VeMessage节点中找到属性name值为VeMessageName的元素，读取Processor值，并返回
					//获取VeMessage包含的所有子节点
					NodeList tVeMessageChildNodes=tMsgTypeChildNodes.item(i).getChildNodes();
					if(tVeMessageChildNodes==null) break;
					for(int j=0; j<tVeMessageChildNodes.getLength();j++)
					{
						if(tVeMessageChildNodes.item(j).getNodeName().equals("METype"))
						{
							// 取得METype包含的属性
							NamedNodeMap tRequestAtt =tVeMessageChildNodes.item(j).getAttributes();
							return tRequestAtt.getNamedItem("name").getNodeValue();
						}
					}
					
				}
			}			
		}
		return null;
	}
	
	/**
     * 功能：根据“消息包名称”、“消息种类”及“消息名称”读取消息类型	
     * @param msgPack 消息包名称
     * @param msgTypeName 消息种类
     * @param VeMessageName 消息名称
     */
	public String getVeMessageType(String msgPack,String msgTypeName,String VeMessageName)
	{//
		//获取MessagePack元素
        NodeList tListMessagePack = getMessagePacks();
		if (tListMessagePack==null) return null;
		//获取名称为msgPack的MessagePack节点的子节点
		NodeList tMessagePackChildNodes=getMessagePackChildNodes(tListMessagePack,msgPack);
		if(tMessagePackChildNodes==null) return null;
		//获取名称为msgTypeName的MsgType节点中的所有子节点
		NodeList tMsgTypeChildNodes=getMsgTypeChildNodes(tMessagePackChildNodes, msgTypeName);
		if(tMsgTypeChildNodes==null)
			return null;
		for(int i=0;i<tMsgTypeChildNodes.getLength();i++)
		{
			//获取MsgType中子节点的名称
			String tNodeName=tMsgTypeChildNodes.item(i).getNodeName();
			if(tNodeName.equals("VeMessage"))
			{
				// 取得VeMessage节点包含的属性
				NamedNodeMap tMsgNameAtt =tMsgTypeChildNodes.item(i).getAttributes();
				// 读取指定属性名称的属性的值
				String tVeMessageName = tMsgNameAtt.getNamedItem("name")
						.getNodeValue();
				if(tVeMessageName.equals(VeMessageName))
				{//在VeMessage节点中找到属性name值为VeMessageName的元素，读取其type值，并返回
					return tMsgNameAtt.getNamedItem("type")
							.getNodeValue();
				}
			}			
		}//end for
		return null;
	}
	/**
     * 功能：获取VEMessageDesc下的所有MessagePack子节，并以NodeList返回
     */
	private NodeList getMessagePacks()
	{//获取VEMessageDesc下的所有MessagePack子节，并以NodeList返回
		 //获取MessagePack元素
        NodeList tListMessagePack = doc.getElementsByTagName("MessagePack");
        if (tListMessagePack != null)
        {
        	return tListMessagePack;
		}
		return null;
		
	}
	/**
     * 功能：根据tListMessagePack和msgPack，返回名称为msgPack的MessagePack节点所包含的所有子节点
     * @param tListMessagePack 是一个NodeList,是VEMessageDesc下的所有MessagePack子节组成的NodeList
     * @param msgPack 消息包名称
     */
	private NodeList getMessagePackChildNodes(NodeList tListMessagePack,String msgPack)
	{//功能根据tListMessagePack和messagePackName，返回名称为messagePackName的MessagePack节点所包含的所有子节点
		//方式：根据messagePackName,找到相应元素，并返回子节点组成的NodeList
		if (tListMessagePack == null)
			return null;
		for (int i = 0; i < tListMessagePack.getLength(); i++)
		{
			// 取得此节点包含的属性
			NamedNodeMap tMessagePackAtt = tListMessagePack.item(i)
					.getAttributes();
			// 读取指定属性名称的属性的值
			String tMessagePackName = tMessagePackAtt.getNamedItem("name")
					.getNodeValue();
			// 找到指定的包
			if (tMessagePackName.equals(msgPack))
			{	
				// 获致此节点包含的所有子节点
				NodeList tMessagePackChildNodes = tListMessagePack.item(i)
						.getChildNodes();
				return tMessagePackChildNodes;
			}
			// System.out.println(tMessagePackName);
		}

		return null;
		
	}
	/**
     * 功能：根据tMessagePackChildNodes和msgTypeName，返回MsgType节点的包含的所有子节点
     * @param tMessagePackChildNodes 是一个NodeList,是由MessagePack节点所包含的所有子节点组成的NodeList
     * @param msgTypeName 消息种类
     */
	private NodeList getMsgTypeChildNodes(NodeList tMessagePackChildNodes, String msgTypeName)
	{//方式：根据msgTypeName,找到相应元素，并以其子节点组成的NodeList返回
        if (tMessagePackChildNodes == null) 
        {	
        	return null;
		}
		for(int i=0;i<tMessagePackChildNodes.getLength();i++)
    	{
			//获取MessagePack中子节点的名称
			String tNodeName = tMessagePackChildNodes.item(i).getNodeName();
			if(tNodeName.equals("MsgType"))
			{
				//取得每一个MsgType节点包含的属性值
				NamedNodeMap  tMsgTypeAtt=tMessagePackChildNodes.item(i).getAttributes();
				if (tMsgTypeAtt==null) break;
				//读取指定属性名称的属性的值
				String tMsgTypeName=tMsgTypeAtt.getNamedItem("name").getNodeValue();
				
				if(tMsgTypeName.equals(msgTypeName))
				{//找到名称等于msgTypeName的MsgType
					NodeList tMsgTypeChildNodes=tMessagePackChildNodes.item(i).getChildNodes();
					//以tMessagePackChildNodes作为参数，构建CMsgType对象，并返回
					return tMsgTypeChildNodes;
				}
				
			}
    	}
		return null;
		
	}
	
}
