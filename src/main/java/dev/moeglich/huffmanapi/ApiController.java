package dev.moeglich.huffmanapi;

import java.util.Base64;

import org.apache.commons.codec.DecoderException;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import dev.moeglich.huffmanapi.bitreader.StringDecodeReader;
import dev.moeglich.huffmanapi.bits.Bits;

@RestController
public class ApiController {
	@PostMapping("/encode")
	public String encode(String input) {
		return Base64.getEncoder().encodeToString(Encode.encode(input).encode());
	}

	@PostMapping("/decode")
	public String decode(String input) {
		try {
			return Bits.decode(Base64.getDecoder().decode(input)).applyBitReader(new StringDecodeReader());
		} catch (DecoderException e) {
			return "Error";
		}
	}
}
