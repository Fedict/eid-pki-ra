package be.fedict.eid.pkira.blm.model.contracthandler.services;

import java.security.Key;

import javax.xml.crypto.AlgorithmMethod;
import javax.xml.crypto.KeySelector;
import javax.xml.crypto.KeySelectorException;
import javax.xml.crypto.KeySelectorResult;
import javax.xml.crypto.XMLCryptoContext;
import javax.xml.crypto.dsig.keyinfo.KeyInfo;

final class SingletonKeySelector extends KeySelector {

	private final Key key;

	public SingletonKeySelector(Key certificateKey) {
		this.key = certificateKey;
	}

	@Override
	public KeySelectorResult select(KeyInfo keyInfo, Purpose purpose, AlgorithmMethod method, XMLCryptoContext context)
			throws KeySelectorException {
		return new KeySelectorResult() {

			@Override
			public Key getKey() {
				return key;
			}
		};
	}
}
