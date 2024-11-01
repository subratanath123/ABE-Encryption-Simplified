package co.junwei.bswabe;

import java.io.Serializable;
import java.util.ArrayList;

import it.unisa.dia.gas.jpbc.Element;

public class BswabePrv implements Serializable {

	/*
	 * A private key
	 */
	Element d; /* G_2 */
	ArrayList<BswabePrvComp> comps; /* BswabePrvComp */


}