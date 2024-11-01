
package co.junwei.cpabe;

import java.io.Serializable;

public class EncryptRequest implements Serializable {

    private String content;
    private String policy;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getPolicy() {
        return policy;
    }

    public void setPolicy(String policy) {
        this.policy = policy;
    }
}
