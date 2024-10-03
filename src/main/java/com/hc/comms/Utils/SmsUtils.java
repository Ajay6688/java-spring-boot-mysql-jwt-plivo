package com.hc.comms.Utils;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

import com.hc.comms.config.SmsProperties;
import com.plivo.api.Plivo;
import com.plivo.api.PlivoClient;
import com.plivo.api.models.phlo.Phlo;
import com.plivo.api.models.phlo.PhloUpdateResponse;
import io.micrometer.common.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
public class SmsUtils {

	@Autowired
	private SmsProperties smsProperties;

	private static final Logger logger = LoggerFactory.getLogger(SmsUtils.class);


	public  Map<String, String> parseUrlEncodedString(String urlEncodedString) throws UnsupportedEncodingException {
		Map<String, String> params = new HashMap<>();
		try {

			String[] pairs = urlEncodedString.split("&");
			for (String pair : pairs) {
				int idx = pair.indexOf("=");
				String key = URLDecoder.decode(pair.substring(0, idx), "UTF-8");
				String value = URLDecoder.decode(pair.substring(idx + 1), "UTF-8");
				params.put(key, value);
				logger.info(params.toString());
			}
		}catch (Exception e) {
			logger.error("[SendSmsUtils][parseUrlEncodedString]" + e.getMessage());
		}
		return params;
	}

    public PhloUpdateResponse sendPlivoMessage(String to , String message , String sourceNumber){

		PhloUpdateResponse response = null;
		String authId = null;
		String authToken  = null;
		String phloId = null;

		try{
			if(!StringUtils.isBlank(smsProperties.getAuthId())) {
				authId = smsProperties.getAuthId().trim();
			}

			if(!StringUtils.isBlank(smsProperties.getAuthToken())) {
				authToken = smsProperties.getAuthToken().trim();
			}

			if(!StringUtils.isBlank(smsProperties.getPhloId())) {
				phloId = smsProperties.getPhloId().trim();
			}

			PlivoClient client = new PlivoClient(authId, authToken);
			Plivo.init(authId, authToken);

			// Prepare the payload for the SMS
			Map<String, Object> payload = new HashMap<>();
			payload.put("from", sourceNumber);
			payload.put("to", to);
			payload.put("message", message);

			// Fetch the Phlo instance and run the updater with the payload
			Phlo phlo = Phlo.getter(phloId).client(client).get();
			response = Phlo.updater(phloId).payload(payload).run();
			return response;

		}catch (Exception e) {
			logger.error("[SmsApiUtils][sendMessages] " + e.getMessage());
			return response;
		}
	}

}
