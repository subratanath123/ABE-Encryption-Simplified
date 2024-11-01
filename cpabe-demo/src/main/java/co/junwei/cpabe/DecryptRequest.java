package co.junwei.cpabe;

import co.junwei.bswabe.BswabePrv;

import java.io.Serializable;

public class DecryptRequest implements Serializable {

    private String content;
    private String userKey;
    private String policy;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUserKey() {
        return userKey;
    }

    public void setUserKey(String userKey) {
        this.userKey = userKey;
    }

    public String getPolicy() {
        return policy;
    }

    public void setPolicy(String policy) {
        this.policy = policy;
    }
}
