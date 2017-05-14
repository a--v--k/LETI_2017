package mobilebank.ru.mobilebank;

/**
 * Created by Vlastelina on 10.05.2017.
 */

public class Authorization
{
    private String sessionKey = null;
    private String userName = null;

    public void setSessionKey(String newKey)
    {
        sessionKey = newKey;
    }

    public String getSessionKey()
    {
        return sessionKey;
    }

    public void setUserName(String name)
    {
        userName = name;
    }

    public String getUserName()
    {
        return userName;
    }
}
