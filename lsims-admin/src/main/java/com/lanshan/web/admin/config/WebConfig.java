package com.lanshan.web.admin.config;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.lanshan.core.wrapper.WrapperResultHandler;

@Configuration
public class WebConfig implements WebMvcConfigurer {

	@Override
    public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
		WrapperResultHandler convert = new WrapperResultHandler();
		List<MediaType> supportedMediaTypes = new ArrayList<MediaType>();
        MediaType text_plain = new MediaType(MediaType.TEXT_PLAIN, Charset.forName("UTF-8"));
        MediaType text_html = new MediaType(MediaType.TEXT_HTML, Charset.forName("UTF-8"));
        MediaType x_www_form_urlencoded_utf8 = new MediaType(MediaType.APPLICATION_FORM_URLENCODED, Charset.forName("UTF-8"));
        supportedMediaTypes.add(text_html);
        supportedMediaTypes.add(text_plain);
        supportedMediaTypes.add(MediaType.APPLICATION_JSON_UTF8);
        supportedMediaTypes.add(x_www_form_urlencoded_utf8);


		convert.setSupportedMediaTypes(supportedMediaTypes);
		converters.clear();
		converters.add(convert);
	}
}
