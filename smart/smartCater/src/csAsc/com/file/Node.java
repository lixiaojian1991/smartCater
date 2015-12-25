package csAsc.com.file;

public class Node {
	 // 节点属性   
    private String xattribute;   
       
    // 节点PATH   
    private String xpath;   
       
    // 节点值   
    private String value;   
       
    public Node(String xattribute, String xpath, String value)   
    {   
        this.xattribute = xattribute;   
        this.xpath = xpath;   
        this.value = value;   
    }   
       
    public String getXpath()   
    {   
        return xpath;   
    }   
       
    public void setXpath(String xpath)   
    {   
        this.xpath = xpath;   
    }   
       
    public String getValue()   
    {   
        return value;   
    }   
       
    public void setValue(String value)   
    {   
        this.value = value;   
    }   
       
    public String getXattribute()   
    {   
        return xattribute;   
    }   
       
    public void setXattribute(String xattribute)   
    {   
        this.xattribute = xattribute;   
    }   
}
