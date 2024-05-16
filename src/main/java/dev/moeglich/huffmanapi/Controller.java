package dev.moeglich.huffmanapi;

import org.apache.commons.codec.DecoderException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import dev.moeglich.huffmanlib.Huffman;

@RestController
public class Controller {
	@PostMapping("/encode")
	public String encode(String input) {
		return Huffman.encode_to_base64(input);
	}

	@PostMapping("/decode")
	public String decode(String input) {
		try {
			return Huffman.decode_from_base64(input);
		} catch (DecoderException e) {
			return "Error";
		}
	}
}
