package co.junwei.cpabe;

import it.unisa.dia.gas.jpbc.Element;

class Policy {
    private byte[] cypherBuffer;
    private Element m;

    public Policy() {
    }

    public Policy(byte[] cypherBuffer, Element m) {
        this.cypherBuffer = cypherBuffer;
        this.m = m;
    }

    public byte[] getCypherBuffer() {
        return cypherBuffer;
    }

    public void setCypherBuffer(byte[] cypherBuffer) {
        this.cypherBuffer = cypherBuffer;
    }

    public Element getM() {
        return m;
    }

    public void setM(Element m) {
        this.m = m;
    }
}
